package cool.klass.data.store.reladomo;

import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.time.ZoneOffset;
import java.util.List;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import com.gs.fw.common.mithra.MithraDatedTransactionalObject;
import com.gs.fw.common.mithra.MithraManagerProvider;
import com.gs.fw.common.mithra.MithraObject;
import com.gs.fw.common.mithra.MithraTransactionalObject;
import com.gs.fw.common.mithra.attribute.Attribute;
import com.gs.fw.common.mithra.attribute.TimestampAttribute;
import com.gs.fw.common.mithra.finder.AbstractRelatedFinder;
import com.gs.fw.common.mithra.finder.Operation;
import com.gs.fw.common.mithra.finder.RelatedFinder;
import cool.klass.data.store.DataStore;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.multimap.list.ImmutableListMultimap;

// TODO: Refactor this whole thing to use generated getters/setters instead of Reladomo Attribute
public class ReladomoDataStore implements DataStore
{
    public static final Converter<String, String> LOWER_TO_UPPER = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.UPPER_CAMEL);

    @Override
    public void runInTransaction(Runnable runnable)
    {
        MithraManagerProvider.getMithraManager().executeTransactionalCommand(tx ->
        {
            runnable.run();
            return null;
        });
    }

    @Override
    public Object findByKey(Klass klass, ImmutableList<Object> keys)
    {
        RelatedFinder<?>                finder        = this.getRelatedFinder(klass);
        ImmutableList<DataTypeProperty> keyProperties = klass.getKeyProperties();
        if (keyProperties.size() != keys.size())
        {
            throw new AssertionError();
        }

        ImmutableList<Operation> operations = keyProperties.collectWithIndex((keyProperty, index) ->
                this.getOperation(finder, keyProperty, keys.get(index)));

        Operation operation = operations.reduce(Operation::and).get();
        return finder.findOne(operation);
    }

    private Operation getOperation(
            RelatedFinder<?> finder,
            DataTypeProperty keyProperty,
            Object key)
    {
        Attribute        attribute = finder.getAttributeByName(keyProperty.getName());
        OperationVisitor visitor   = new OperationVisitor(attribute, key);
        keyProperty.visit(visitor);
        return visitor.getResult();
    }

    @Override
    public Object instantiate(Klass klass, ImmutableList<Object> keys)
    {
        Object newInstance = this.instantiateNewInstance(klass);
        this.setKeys(klass, newInstance, keys);
        return newInstance;
    }

    public Object instantiateNewInstance(Klass klass)
    {
        try
        {
            // TODO: now timestamp
            Class<?> aClass = Class.forName(klass.getFullyQualifiedName());
            return aClass.newInstance();
        }
        catch (ReflectiveOperationException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void setKeys(Klass klass, Object newInstance, ImmutableList<Object> keys)
    {
        this.generateAndSetId(newInstance, klass);

        ImmutableList<DataTypeProperty> keyProperties = klass.getKeyProperties();
        if (keyProperties.size() != keys.size())
        {
            throw new AssertionError();
        }
        for (int i = 0; i < keyProperties.size(); i++)
        {
            DataTypeProperty keyProperty = keyProperties.get(i);
            Object           key         = keys.get(i);
            this.setDataTypeProperty(newInstance, keyProperty, key);
        }
    }

    private void generateAndSetId(Object persistentInstance, Klass klass)
    {
        ImmutableList<DataTypeProperty> idProperties = klass.getDataTypeProperties().select(DataTypeProperty::isID);
        if (idProperties.isEmpty())
        {
            return;
        }

        DataTypeProperty idProperty = idProperties.getOnly();

        try
        {
            String methodName             = "generateAndSet" + LOWER_TO_UPPER.convert(idProperty.getName());
            Method generateAndSetIdMethod = persistentInstance.getClass().getMethod(methodName);
            generateAndSetIdMethod.invoke(persistentInstance);
        }
        catch (ReflectiveOperationException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Object getDataTypeProperty(Object persistentInstance, DataTypeProperty dataTypeProperty)
    {
        RelatedFinder<?> finder    = this.getRelatedFinder((MithraObject) persistentInstance);
        Attribute        attribute = finder.getAttributeByName(dataTypeProperty.getName());
        if (attribute == null)
        {
            throw new AssertionError(
                    "Domain model and generated code are out of sync. Try rerunning a full clean build.");
        }

        if (dataTypeProperty.isOptional() && attribute.isAttributeNull(persistentInstance))
        {
            return null;
        }

        Object result = attribute.valueOf(persistentInstance);

        if (dataTypeProperty.getType() == PrimitiveType.LOCAL_DATE)
        {
            return ((java.sql.Date) result).toLocalDate();
        }

        if (dataTypeProperty.getType() == PrimitiveType.INSTANT)
        {
            return ((Timestamp) result).toLocalDateTime().toInstant(ZoneOffset.UTC);
        }

        boolean isTemporal = dataTypeProperty.isTemporal();
        if (isTemporal)
        {
            Timestamp infinity = ((TimestampAttribute<?>) attribute).getAsOfAttributeInfinity();
            if (infinity.equals(result))
            {
                return null;
            }
            // TODO: Consider handling here the case where validTo == systemTo + 1 day, but really means infinity
            // TODO: Alternately, just enable future dated rows to turn off this optimization
            return ((Timestamp) result).toInstant();
        }

        return result;
    }

    @Override
    public void setDataTypeProperty(Object persistentInstance, DataTypeProperty dataTypeProperty, Object newValue)
    {
        RelatedFinder<?> finder    = this.getRelatedFinder((MithraObject) persistentInstance);
        Attribute        attribute = finder.getAttributeByName(dataTypeProperty.getName());

        if (newValue == null)
        {
            attribute.setValueNull(persistentInstance);
        }
        else
        {
            // TODO: 🐜 Accept Instant. Write a test that mutates a date field
            attribute.setValue(persistentInstance, newValue);
        }
    }

    @Override
    public Object getToOne(Object persistentSourceInstance, AssociationEnd associationEnd)
    {
        RelatedFinder<?> finder = this.getRelatedFinder(associationEnd.getOwningKlass());
        AbstractRelatedFinder relationshipFinder = (AbstractRelatedFinder) finder
                .getRelationshipFinderByName(associationEnd.getName());

        return relationshipFinder.valueOf(persistentSourceInstance);
    }

    @Override
    public List<Object> getToMany(Object persistentSourceInstance, AssociationEnd associationEnd)
    {
        return (List<Object>) this.getToOne(persistentSourceInstance, associationEnd);
    }

    @Override
    public void setToOne(
            Object persistentSourceInstance,
            AssociationEnd associationEnd,
            Object persistentTargetInstance)
    {
        if (persistentTargetInstance == null)
        {
            throw new AssertionError();
        }

        // A Reladomo bug prevents just calling a method like setQuestion here. Instead we have to call foreign key setters like setQuestionId

        ImmutableList<DataTypeProperty> targetDataTypeProperties = associationEnd
                .getOwningKlass()
                .getDataTypeProperties();
        for (DataTypeProperty targetDataTypeProperty : targetDataTypeProperties)
        {
            ImmutableListMultimap<AssociationEnd, DataTypeProperty> keysMatchingThisForeignKey = targetDataTypeProperty.getKeysMatchingThisForeignKey();

            ImmutableList<DataTypeProperty> keysInRelatedObject = keysMatchingThisForeignKey.get(associationEnd);
            if (keysInRelatedObject.isEmpty())
            {
                continue;
            }

            DataTypeProperty foreignKey = targetDataTypeProperty;

            // TODO: If this assertion holds, then the data structure ought to be a map, not multimap
            DataTypeProperty keyInRelatedObject = keysInRelatedObject.getOnly();

            Object keyValue = this.getDataTypeProperty(persistentTargetInstance, keyInRelatedObject);

            this.setDataTypeProperty(persistentSourceInstance, foreignKey, keyValue);
        }
    }

    @Override
    public void insert(Object persistentInstance)
    {
        if (!(persistentInstance instanceof MithraTransactionalObject))
        {
            throw new AssertionError();
        }

        ((MithraTransactionalObject) persistentInstance).insert();
    }

    @Override
    public void deleteOrTerminate(Object persistentInstance)
    {
        if (persistentInstance instanceof MithraDatedTransactionalObject)
        {
            ((MithraDatedTransactionalObject) persistentInstance).terminate();
        }
        else if (persistentInstance instanceof MithraTransactionalObject)
        {
            ((MithraTransactionalObject) persistentInstance).delete();
        }
        else
        {
            throw new AssertionError();
        }
    }

    private RelatedFinder<?> getRelatedFinder(MithraObject mithraObject)
    {
        return mithraObject.zGetPortal().getFinder();
    }

    private RelatedFinder<?> getRelatedFinder(Klass klass)
    {
        try
        {
            String   finderName      = klass.getFullyQualifiedName() + "Finder";
            Class<?> finderClass     = Class.forName(finderName);
            Method   getFinderMethod = finderClass.getMethod("getFinderInstance");
            return (RelatedFinder<?>) getFinderMethod.invoke(null);
        }
        catch (ReflectiveOperationException | IllegalArgumentException | SecurityException e)
        {
            throw new RuntimeException(e);
        }
    }
}
