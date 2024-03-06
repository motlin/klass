package cool.klass.data.store.reladomo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
import com.gs.fw.common.mithra.util.DefaultInfinityTimestamp;
import cool.klass.data.store.DataStore;
import cool.klass.data.store.Transaction;
import cool.klass.data.store.TransactionalCommand;
import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.Enumeration;
import cool.klass.model.meta.domain.api.EnumerationLiteral;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.property.EnumerationProperty;
import cool.klass.model.meta.domain.api.property.Property;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import com.typesafe.config.ConfigRenderOptions;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.multimap.list.ImmutableListMultimap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Refactor this whole thing to use generated getters/setters instead of Reladomo Attribute
public class ReladomoDataStore implements DataStore
{
    public static final Converter<String, String> LOWER_TO_UPPER = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.UPPER_CAMEL);

    private static final Logger                    LOGGER      = LoggerFactory.getLogger(ReladomoDataStore.class);
    private static final Converter<String, String> UPPER_CAMEL = CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.UPPER_CAMEL);

    private final int retryCount;

    public ReladomoDataStore()
    {
        Config config         = ConfigFactory.load();
        Config reladomoConfig = config.getConfig("klass.data.reladomo");

        if (LOGGER.isDebugEnabled())
        {
            ConfigRenderOptions configRenderOptions = ConfigRenderOptions.defaults()
                    .setJson(false)
                    .setOriginComments(false);
            String render = reladomoConfig.root().render(configRenderOptions);
            LOGGER.debug("Reladomo configuration:\n{}", render);
        }

        this.retryCount = reladomoConfig.getInt("retryCount");
    }

    @Override
    public <Result> Result runInTransaction(@Nonnull TransactionalCommand<Result> transactionalCommand)
    {
        return MithraManagerProvider.getMithraManager().executeTransactionalCommand(tx ->
        {
            Transaction transactionAdapter = new TransactionAdapter(tx);
            return transactionalCommand.run(transactionAdapter);
        }, this.retryCount);
    }

    @Override
    public void runInTransaction(@Nonnull Runnable runnable)
    {
        MithraManagerProvider.getMithraManager().executeTransactionalCommand(tx ->
        {
            runnable.run();
            return null;
        }, this.retryCount);
    }

    @Override
    public Object findByKey(@Nonnull Klass klass, @Nonnull ImmutableList<Object> keys)
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
            @Nonnull RelatedFinder<?> finder,
            @Nonnull DataTypeProperty keyProperty,
            Object key)
    {
        Attribute        attribute = finder.getAttributeByName(keyProperty.getName());
        OperationVisitor visitor   = new OperationVisitor(attribute, key);
        keyProperty.visit(visitor);
        return visitor.getResult();
    }

    @Nonnull
    @Override
    public Object instantiate(@Nonnull Klass klass, @Nonnull ImmutableList<Object> keys)
    {
        keys.each(Objects::requireNonNull);

        Object newInstance = this.instantiateNewInstance(klass);
        this.setKeys(klass, newInstance, keys);
        return newInstance;
    }

    @Nonnull
    private Object instantiateNewInstance(@Nonnull Klass klass)
    {
        try
        {
            Class<?> aClass = Class.forName(klass.getFullyQualifiedName());
            Class<?>[] parameterTypes = klass.isSystemTemporal()
                    ? new Class<?>[]{Timestamp.class}
                    : new Class<?>[]{};
            Constructor<?> constructor = aClass.getConstructor(parameterTypes);
            Object[] constructorArgs = klass.isSystemTemporal()
                    ? new Object[]{DefaultInfinityTimestamp.getDefaultInfinity()}
                    : new Object[]{};
            return constructor.newInstance(constructorArgs);
        }
        catch (ReflectiveOperationException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Nonnull
    private Object instantiateNewInstance(@Nonnull Klass klass, @Nonnull Instant validTime)
    {
        try
        {
            Class<?>       aClass      = Class.forName(klass.getFullyQualifiedName());
            Constructor<?> constructor = aClass.getConstructor(Timestamp.class, Timestamp.class);
            Timestamp      timestamp   = Timestamp.valueOf(LocalDateTime.ofInstant(validTime, ZoneOffset.UTC));
            // TODO: One of these would be infinity, forgot which one
            return constructor.newInstance(timestamp, timestamp);
        }
        catch (ReflectiveOperationException e)
        {
            throw new RuntimeException(e);
        }
    }

    public void setKeys(@Nonnull Klass klass, @Nonnull Object newInstance, @Nonnull ImmutableList<Object> keys)
    {
        this.generateAndSetId(newInstance, klass);

        ImmutableList<DataTypeProperty> keyProperties = klass.getKeyProperties().reject(DataTypeProperty::isID);
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

    private void generateAndSetId(@Nonnull Object persistentInstance, @Nonnull Klass klass)
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

    @Nullable
    @Override
    public Object getDataTypeProperty(@Nonnull Object persistentInstance, @Nonnull DataTypeProperty dataTypeProperty)
    {
        if (dataTypeProperty.isDerived())
        {
            return this.getPropertyReflectively(persistentInstance, dataTypeProperty);
        }

        RelatedFinder<?> finder    = this.getRelatedFinder((MithraObject) persistentInstance);
        Attribute        attribute = finder.getAttributeByName(dataTypeProperty.getName());
        if (attribute == null)
        {
            throw new AssertionError(
                    "Domain model and generated code are out of sync. Try rerunning a full clean build.");
        }

        if (attribute.isAttributeNull(persistentInstance))
        {
            if (dataTypeProperty.isOptional())
            {
                return null;
            }

            throw new AssertionError(dataTypeProperty);
        }

        Object result = attribute.valueOf(persistentInstance);

        if (dataTypeProperty.getType() == PrimitiveType.LOCAL_DATE)
        {
            return ((java.sql.Date) result).toLocalDate();
        }

        if (dataTypeProperty.getType() == PrimitiveType.INSTANT)
        {
            return ((Timestamp) result).toInstant();
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

        if (dataTypeProperty instanceof EnumerationProperty)
        {
            String              prettyName          = (String) result;
            EnumerationProperty enumerationProperty = (EnumerationProperty) dataTypeProperty;
            Enumeration         enumeration         = enumerationProperty.getType();

            Optional<EnumerationLiteral> enumerationLiteral = enumeration.getEnumerationLiterals()
                    .detectOptional(each -> each.getPrettyName().equals(prettyName));

            return enumerationLiteral.orElseThrow(() -> new AssertionError(prettyName));
        }

        return result;
    }

    @Nullable
    private Object getDataTypePropertyLenient(
            @Nonnull Object persistentInstance,
            @Nonnull DataTypeProperty dataTypeProperty)
    {
        if (dataTypeProperty.isDerived())
        {
            return this.getPropertyReflectively(persistentInstance, dataTypeProperty);
        }

        RelatedFinder<?> finder    = this.getRelatedFinder((MithraObject) persistentInstance);
        Attribute        attribute = finder.getAttributeByName(dataTypeProperty.getName());
        if (attribute == null)
        {
            throw new AssertionError(
                    "Domain model and generated code are out of sync. Try rerunning a full clean build. Could not find: "
                            + dataTypeProperty);
        }

        if (attribute.isAttributeNull(persistentInstance))
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
            return ((Timestamp) result).toInstant();
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

        if (dataTypeProperty instanceof EnumerationProperty)
        {
            String              prettyName          = (String) result;
            EnumerationProperty enumerationProperty = (EnumerationProperty) dataTypeProperty;
            Enumeration         enumeration         = enumerationProperty.getType();

            Optional<EnumerationLiteral> enumerationLiteral = enumeration.getEnumerationLiterals()
                    .detectOptional(each -> each.getPrettyName().equals(prettyName));

            return enumerationLiteral.orElseThrow(() -> new AssertionError(prettyName));
        }

        return result;
    }

    public Object getPropertyReflectively(
            @Nonnull Object persistentInstance,
            @Nonnull Property property)
    {
        try
        {
            Classifier owningClassifier   = property.getOwningClassifier();
            String     fullyQualifiedName = owningClassifier.getFullyQualifiedName();
            Class<?>   aClass             = Class.forName(fullyQualifiedName);
            String     methodName         = this.getMethodName(property);
            Method     method             = aClass.getMethod(methodName);
            return method.invoke(persistentInstance);
        }
        catch (ReflectiveOperationException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Nonnull
    private String getMethodName(Property property)
    {
        String prefix = property.getType() == PrimitiveType.BOOLEAN ? "is" : "get";
        String suffix = UPPER_CAMEL.convert(property.getName());
        return prefix + suffix;
    }

    @Override
    public boolean setDataTypeProperty(
            @Nonnull Object persistentInstance,
            @Nonnull DataTypeProperty dataTypeProperty,
            @Nullable Object newValue)
    {
        if (dataTypeProperty.isDerived())
        {
            throw new AssertionError(dataTypeProperty);
        }

        Object oldValue = this.getDataTypePropertyLenient(persistentInstance, dataTypeProperty);
        if (Objects.equals(oldValue, newValue))
        {
            return false;
        }

        RelatedFinder<?> finder    = this.getRelatedFinder((MithraObject) persistentInstance);
        Attribute        attribute = finder.getAttributeByName(dataTypeProperty.getName());

        if (newValue == null)
        {
            attribute.setValueNull(persistentInstance);
        }
        else if (dataTypeProperty instanceof EnumerationProperty)
        {
            attribute.setValue(persistentInstance, ((EnumerationLiteral) newValue).getPrettyName());
        }
        else if (dataTypeProperty.getType() == PrimitiveType.LOCAL_DATE)
        {
            Timestamp timestamp = Timestamp.valueOf(((LocalDate) newValue).atStartOfDay());
            attribute.setValue(persistentInstance, timestamp);
        }
        else if (dataTypeProperty.getType() == PrimitiveType.INSTANT)
        {
            Timestamp timestamp = Timestamp.from((Instant) newValue);
            attribute.setValue(persistentInstance, timestamp);
        }
        else
        {
            attribute.setValue(persistentInstance, newValue);
        }

        return true;
    }

    @Override
    public Object getToOne(Object persistentSourceInstance, @Nonnull AssociationEnd associationEnd)
    {
        RelatedFinder<?> finder = this.getRelatedFinder(associationEnd.getOwningClassifier());
        AbstractRelatedFinder relationshipFinder = (AbstractRelatedFinder) finder
                .getRelationshipFinderByName(associationEnd.getName());

        return relationshipFinder.valueOf(persistentSourceInstance);
    }

    @Nonnull
    @Override
    public List<Object> getToMany(Object persistentSourceInstance, @Nonnull AssociationEnd associationEnd)
    {
        return (List<Object>) this.getToOne(persistentSourceInstance, associationEnd);
    }

    @Override
    public boolean setToOne(
            @Nonnull Object persistentSourceInstance,
            @Nonnull AssociationEnd associationEnd,
            @Nonnull Object persistentTargetInstance)
    {
        Objects.requireNonNull(persistentTargetInstance);

        boolean mutationOccurred = false;

        // A Reladomo bug prevents just calling a method like setQuestion here. Instead we have to call foreign key setters like setQuestionId

        ImmutableList<DataTypeProperty> targetDataTypeProperties = associationEnd
                .getOwningClassifier()
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

            mutationOccurred |= this.setDataTypeProperty(persistentSourceInstance, foreignKey, keyValue);
        }

        return mutationOccurred;
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
    public void deleteOrTerminate(@Nonnull Object persistentInstance)
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

    @Override
    public boolean isInstanceOf(@Nonnull Object persistentInstance, @Nonnull Classifier classifier)
    {
        try
        {
            Class<?> persistentInstanceClass = persistentInstance.getClass();
            Class<?> domainModelClass        = Class.forName(classifier.getPackageName() + "." + classifier.getName());
            return domainModelClass.isAssignableFrom(persistentInstanceClass);
        }
        catch (ClassNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }

    private RelatedFinder<?> getRelatedFinder(@Nonnull MithraObject mithraObject)
    {
        return mithraObject.zGetPortal().getFinder();
    }

    @Nonnull
    private RelatedFinder<?> getRelatedFinder(@Nonnull Klass klass)
    {
        try
        {
            String   finderName      = klass.getFullyQualifiedName() + "Finder";
            Class<?> finderClass     = Class.forName(finderName);
            Method   getFinderMethod = finderClass.getMethod("getFinderInstance");
            return (RelatedFinder<?>) getFinderMethod.invoke(null);
        }
        catch (@Nonnull ReflectiveOperationException | IllegalArgumentException | SecurityException e)
        {
            throw new RuntimeException(e);
        }
    }
}
