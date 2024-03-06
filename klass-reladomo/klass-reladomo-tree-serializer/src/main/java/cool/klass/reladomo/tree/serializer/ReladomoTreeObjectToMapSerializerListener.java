package cool.klass.reladomo.tree.serializer;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import com.gs.fw.common.mithra.MithraObject;
import com.gs.fw.common.mithra.finder.RelatedFinder;
import com.gs.fw.finder.DomainList;
import cool.klass.data.store.reladomo.ReladomoDataStore;
import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.EnumerationLiteral;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.property.ReferenceProperty;
import cool.klass.model.reladomo.tree.DataTypePropertyReladomoTreeNode;
import cool.klass.model.reladomo.tree.ReferencePropertyReladomoTreeNode;
import cool.klass.model.reladomo.tree.ReferenceReladomoTreeNode;
import cool.klass.model.reladomo.tree.ReladomoTreeNodeToManyAwareListener;
import cool.klass.model.reladomo.tree.RootReladomoTreeNode;
import cool.klass.model.reladomo.tree.SubClassReladomoTreeNode;
import cool.klass.model.reladomo.tree.SuperClassReladomoTreeNode;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.stack.MutableStack;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.mutable.MapAdapter;
import org.eclipse.collections.impl.stack.mutable.ArrayStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReladomoTreeObjectToMapSerializerListener
        implements ReladomoTreeNodeToManyAwareListener
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ReladomoTreeObjectToMapSerializerListener.class);

    private static final Converter<String, String> UPPER_TO_LOWER_CAMEL =
            CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_CAMEL);

    private final MutableStack<Object>           contextStack            = new ArrayStack<>();
    private final MutableStack<RelatedFinder<?>> finderStack             = new ArrayStack<>();
    private final MutableStack<Object>           persistentInstanceStack = new ArrayStack<>();
    private final MutableStack<Object>           resultNodeStack         = new ArrayStack<>();

    private final ReladomoDataStore dataStore;
    private final DomainList        domainList;
    private final Klass             klass;

    private final MutableList<Object> result = Lists.mutable.empty();

    public ReladomoTreeObjectToMapSerializerListener(
            ReladomoDataStore dataStore,
            DomainList domainList,
            Klass klass)
    {
        this.dataStore   = Objects.requireNonNull(dataStore);
        this.domainList  = Objects.requireNonNull(domainList);
        this.klass       = Objects.requireNonNull(klass);
    }

    public MutableList<Object> getResult()
    {
        return this.result;
    }

    @Override
    public void enterListIndex(int index)
    {
        MutableList<Object>        resultNode     = (MutableList<Object>) this.resultNodeStack.peek();
        MutableMap<String, Object> nextResultNode = MapAdapter.adapt(new LinkedHashMap<>());
        Object                     context        = this.contextStack.peek();
        if (context instanceof ReferenceProperty referenceProperty)
        {
            if (referenceProperty.getType().isAbstract())
            {
                nextResultNode.put("__typeName", referenceProperty.getType().getName());
            }
        }
        else if (context instanceof Classifier classifier)
        {
            if (classifier.isAbstract())
            {
                nextResultNode.put("__typeName", ((Classifier) context).getName());
            }
        }
        else
        {
            throw new AssertionError("Unknown context: " + context);
        }
        resultNode.add(nextResultNode);

        List<Object> persistentInstance     = (List<Object>) this.persistentInstanceStack.peek();
        Object       nextPersistentInstance = persistentInstance.get(index);

        this.contextStack.push(index);
        this.persistentInstanceStack.push(nextPersistentInstance);
        this.resultNodeStack.push(nextResultNode);
    }

    @Override
    public void exitListIndex(int index)
    {
        this.contextStack.pop();
        this.persistentInstanceStack.pop();
        this.resultNodeStack.pop();
    }

    @Override
    public Optional<Integer> enterRoot(RootReladomoTreeNode rootReladomoTreeNode)
    {
        if (this.klass != rootReladomoTreeNode.getOwningClassifier())
        {
            String detailMessage = "Expected " + this.klass + " but got " + rootReladomoTreeNode.getOwningClassifier();
            throw new AssertionError(detailMessage);
        }
        RelatedFinder<?> relatedFinder = this.dataStore.getRelatedFinder(rootReladomoTreeNode.getOwningClassifier());

        this.contextStack.push(rootReladomoTreeNode.getOwningClassifier());
        this.finderStack.push(relatedFinder);
        this.persistentInstanceStack.push(this.domainList);
        this.resultNodeStack.push(this.result);

        return Optional.of(this.domainList.size());
    }

    @Override
    public void exitRoot(RootReladomoTreeNode rootReladomoTreeNode)
    {
        this.contextStack.pop();
        this.finderStack.pop();
        this.persistentInstanceStack.pop();
        this.resultNodeStack.pop();
    }

    @Override
    public void enterDataTypeProperty(DataTypePropertyReladomoTreeNode dataTypePropertyReladomoTreeNode)
    {
        DataTypeProperty dataTypeProperty = dataTypePropertyReladomoTreeNode.getDataTypeProperty();
        this.contextStack.push(dataTypeProperty);

        Object persistentInstance = this.persistentInstanceStack.peek();
        if (persistentInstance == null)
        {
            return;
        }

        MutableMap<String, Object> resultNode = (MutableMap<String, Object>) this.resultNodeStack.peek();

        Object data = this.dataStore.getDataTypeProperty(persistentInstance, dataTypeProperty);
        Object value = data instanceof EnumerationLiteral enumerationLiteral
                ? enumerationLiteral.getName()
                : data;
        resultNode.put(dataTypeProperty.getName(), value);
    }

    @Override
    public void exitDataTypeProperty(DataTypePropertyReladomoTreeNode dataTypePropertyReladomoTreeNode)
    {
        this.contextStack.pop();
    }

    @Override
    public void enterSuperClass(SuperClassReladomoTreeNode superClassReladomoTreeNode)
    {
        Klass            owningClassifier   = superClassReladomoTreeNode.getOwningClassifier();
        Klass            superClass         = superClassReladomoTreeNode.getType();
        String           relationshipName   = UPPER_TO_LOWER_CAMEL.convert(superClass.getName()) + "SuperClass";
        RelatedFinder<?> relatedFinder      = this.finderStack.peek();
        RelatedFinder<?> nextFinder         = relatedFinder.getRelationshipFinderByName(relationshipName);
        Object           persistentInstance = this.persistentInstanceStack.peek();

        Object superClassPersistentInstance = this.dataStore.getSuperClass(persistentInstance, owningClassifier);

        this.contextStack.push(superClass);
        this.finderStack.push(nextFinder);
        this.persistentInstanceStack.push(superClassPersistentInstance);
    }

    @Override
    public void exitSuperClass(SuperClassReladomoTreeNode superClassReladomoTreeNode)
    {
        this.contextStack.pop();
        this.finderStack.pop();
        this.persistentInstanceStack.pop();
    }

    @Override
    public void enterSubClass(SubClassReladomoTreeNode subClassReladomoTreeNode)
    {
        Klass            owningClassifier   = subClassReladomoTreeNode.getOwningClassifier();
        Klass            subClass           = subClassReladomoTreeNode.getType();
        String           relationshipName   = UPPER_TO_LOWER_CAMEL.convert(subClass.getName()) + "SubClass";
        RelatedFinder<?> relatedFinder      = this.finderStack.peek();
        RelatedFinder<?> nextFinder         = relatedFinder.getRelationshipFinderByName(relationshipName);
        Object           persistentInstance = this.persistentInstanceStack.peek();

        Object subClassPersistentInstance = this.dataStore.getSubClassPersistentInstance(
                owningClassifier,
                subClass,
                (MithraObject) persistentInstance);

        if (subClassPersistentInstance != null)
        {
            MutableMap<String, Object> resultNode = (MutableMap<String, Object>) this.resultNodeStack.peek();
            resultNode.put("__typeName", subClass.getName());
        }

        this.contextStack.push(subClass);
        this.finderStack.push(nextFinder);
        this.persistentInstanceStack.push(subClassPersistentInstance);
    }

    @Override
    public void exitSubClass(SubClassReladomoTreeNode subClassReladomoTreeNode)
    {
        this.contextStack.pop();
        this.finderStack.pop();
        this.persistentInstanceStack.pop();
    }

    @Override
    public Optional<Integer> enterReferenceProperty(ReferencePropertyReladomoTreeNode referencePropertyReladomoTreeNode)
    {
        ReferenceProperty referenceProperty = referencePropertyReladomoTreeNode.getReferenceProperty();
        this.contextStack.push(referenceProperty);

        String           propertyName  = referenceProperty.getName();
        RelatedFinder<?> relatedFinder = this.finderStack.peek();
        RelatedFinder<?> nextFinder    = relatedFinder.getRelationshipFinderByName(propertyName);
        this.finderStack.push(nextFinder);
        Object persistentInstance = this.persistentInstanceStack.peek();

        if (referenceProperty.getMultiplicity().isToOne())
        {
            Object toOne = this.dataStore.getToOne(persistentInstance, referenceProperty);
            this.persistentInstanceStack.push(toOne);
            MutableMap<String, Object> resultNode     = (MutableMap<String, Object>) this.resultNodeStack.peek();
            MutableMap<String, Object> nextResultNode = MapAdapter.adapt(new LinkedHashMap<>());
            Classifier                 type           = referenceProperty.getType();
            if (type.isAbstract())
            {
                nextResultNode.put("__typeName", type.getName());
            }
            resultNode.put(referenceProperty.getName(), nextResultNode);
            this.resultNodeStack.push(nextResultNode);
            return Optional.empty();
        }

        if (referenceProperty.getMultiplicity().isToMany())
        {
            List<Object> toMany = this.dataStore.getToMany(persistentInstance, referenceProperty);
            this.persistentInstanceStack.push(toMany);
            MutableMap<String, Object> resultNode     = (MutableMap<String, Object>) this.resultNodeStack.peek();
            MutableList<Object>        nextResultNode = Lists.mutable.empty();
            resultNode.put(referenceProperty.getName(), nextResultNode);
            this.resultNodeStack.push(nextResultNode);
            return Optional.of(toMany.size());
        }

        throw new AssertionError("Unknown multiplicity: " + referenceProperty.getMultiplicity());
    }

    @Override
    public void exitReferenceProperty(ReferencePropertyReladomoTreeNode referencePropertyReladomoTreeNode)
    {
        this.contextStack.pop();
        this.finderStack.pop();
        this.persistentInstanceStack.pop();
        this.resultNodeStack.pop();
    }

    @Override
    public Optional<Integer> enterReference(ReferenceReladomoTreeNode referenceReladomoTreeNode)
    {
        LOGGER.info("referenceReladomoTreeNode = " + referenceReladomoTreeNode);
        return Optional.empty();
    }

    @Override
    public void exitReference(ReferenceReladomoTreeNode referenceReladomoTreeNode)
    {
        LOGGER.info("referenceReladomoTreeNode = " + referenceReladomoTreeNode);
    }
}
