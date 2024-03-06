package cool.klass.model.converter.bootstrap.writer;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.gs.fw.common.mithra.finder.RelatedFinder;
import cool.klass.data.store.DataStore;
import cool.klass.model.meta.domain.api.Association;
import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.DataType;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.Enumeration;
import cool.klass.model.meta.domain.api.EnumerationLiteral;
import cool.klass.model.meta.domain.api.Interface;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.NamedElement;
import cool.klass.model.meta.domain.api.PackageableElement;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.domain.api.criteria.Criteria;
import cool.klass.model.meta.domain.api.modifier.Modifier;
import cool.klass.model.meta.domain.api.order.OrderBy;
import cool.klass.model.meta.domain.api.order.OrderByMemberReferencePath;
import cool.klass.model.meta.domain.api.parameter.Parameter;
import cool.klass.model.meta.domain.api.projection.Projection;
import cool.klass.model.meta.domain.api.projection.ProjectionChild;
import cool.klass.model.meta.domain.api.projection.ProjectionDataTypeProperty;
import cool.klass.model.meta.domain.api.projection.ProjectionElement;
import cool.klass.model.meta.domain.api.projection.ProjectionProjectionReference;
import cool.klass.model.meta.domain.api.projection.ProjectionReferenceProperty;
import cool.klass.model.meta.domain.api.projection.ProjectionVisitor;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.property.EnumerationProperty;
import cool.klass.model.meta.domain.api.property.PrimitiveProperty;
import cool.klass.model.meta.domain.api.service.Service;
import cool.klass.model.meta.domain.api.service.ServiceGroup;
import cool.klass.model.meta.domain.api.service.url.Url;
import cool.klass.model.meta.domain.api.value.ExpressionValue;
import cool.klass.model.meta.domain.api.value.ThisMemberReferencePath;
import klass.model.meta.domain.AssociationEndFinder;
import klass.model.meta.domain.AssociationEndModifierFinder;
import klass.model.meta.domain.AssociationEndOrderBy;
import klass.model.meta.domain.AssociationFinder;
import klass.model.meta.domain.ClassifierInterfaceMapping;
import klass.model.meta.domain.ClassifierInterfaceMappingFinder;
import klass.model.meta.domain.ClassifierInterfaceMappingList;
import klass.model.meta.domain.ClassifierList;
import klass.model.meta.domain.ClassifierModifier;
import klass.model.meta.domain.ClassifierModifierFinder;
import klass.model.meta.domain.ClassifierModifierList;
import klass.model.meta.domain.DataTypePropertyList;
import klass.model.meta.domain.EnumerationFinder;
import klass.model.meta.domain.EnumerationList;
import klass.model.meta.domain.EnumerationLiteralFinder;
import klass.model.meta.domain.EnumerationLiteralList;
import klass.model.meta.domain.EnumerationParameter;
import klass.model.meta.domain.EnumerationPropertyFinder;
import klass.model.meta.domain.EnumerationPropertyList;
import klass.model.meta.domain.InterfaceFinder;
import klass.model.meta.domain.InterfaceList;
import klass.model.meta.domain.KlassFinder;
import klass.model.meta.domain.MaxLengthPropertyValidationFinder;
import klass.model.meta.domain.MaxLengthPropertyValidationList;
import klass.model.meta.domain.MaxPropertyValidationFinder;
import klass.model.meta.domain.MaxPropertyValidationList;
import klass.model.meta.domain.MemberReferencePathList;
import klass.model.meta.domain.MinLengthPropertyValidationFinder;
import klass.model.meta.domain.MinLengthPropertyValidationList;
import klass.model.meta.domain.MinPropertyValidationFinder;
import klass.model.meta.domain.MinPropertyValidationList;
import klass.model.meta.domain.NamedElementAbstract;
import klass.model.meta.domain.NamedProjection;
import klass.model.meta.domain.NamedProjectionFinder;
import klass.model.meta.domain.PackageableElementAbstract;
import klass.model.meta.domain.PrimitiveParameter;
import klass.model.meta.domain.PrimitivePropertyFinder;
import klass.model.meta.domain.PrimitivePropertyList;
import klass.model.meta.domain.ProjectionDataTypePropertyFinder;
import klass.model.meta.domain.ProjectionElementList;
import klass.model.meta.domain.ProjectionProjectionReferenceFinder;
import klass.model.meta.domain.ProjectionReferencePropertyFinder;
import klass.model.meta.domain.PropertyModifierFinder;
import klass.model.meta.domain.PropertyModifierList;
import klass.model.meta.domain.RootProjection;
import klass.model.meta.domain.RootProjectionFinder;
import klass.model.meta.domain.RootProjectionList;
import klass.model.meta.domain.ServiceFinder;
import klass.model.meta.domain.ServiceGroupFinder;
import klass.model.meta.domain.ThisMemberReferencePathFinder;
import klass.model.meta.domain.ThisMemberReferencePathList;
import klass.model.meta.domain.TypeMemberReferencePathList;
import klass.model.meta.domain.UrlFinder;
import klass.model.meta.domain.UrlParameter;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Maps;

public class KlassBootstrapWriter
{
    // TODO: Implement Purge on DataStore and break the dependency on Reladomo
    private static final ImmutableList<RelatedFinder<?>> BOOTSTRAP_FINDERS = Lists.immutable.with(
            EnumerationFinder.getFinderInstance(),
            EnumerationLiteralFinder.getFinderInstance(),
            InterfaceFinder.getFinderInstance(),
            KlassFinder.getFinderInstance(),
            AssociationFinder.getFinderInstance(),
            NamedProjectionFinder.getFinderInstance(),
            RootProjectionFinder.getFinderInstance(),
            ProjectionReferencePropertyFinder.getFinderInstance(),
            ProjectionProjectionReferenceFinder.getFinderInstance(),
            ProjectionDataTypePropertyFinder.getFinderInstance(),
            ServiceGroupFinder.getFinderInstance(),
            UrlFinder.getFinderInstance(),
            ServiceFinder.getFinderInstance(),
            PrimitivePropertyFinder.getFinderInstance(),
            EnumerationPropertyFinder.getFinderInstance(),
            PropertyModifierFinder.getFinderInstance(),
            ClassifierModifierFinder.getFinderInstance(),
            AssociationEndFinder.getFinderInstance(),
            AssociationEndModifierFinder.getFinderInstance(),
            ThisMemberReferencePathFinder.getFinderInstance(),
            MinLengthPropertyValidationFinder.getFinderInstance(),
            MaxLengthPropertyValidationFinder.getFinderInstance(),
            MinPropertyValidationFinder.getFinderInstance(),
            MaxPropertyValidationFinder.getFinderInstance(),
            ClassifierInterfaceMappingFinder.getFinderInstance());

    private final DomainModel domainModel;
    private final DataStore   dataStore;

    public KlassBootstrapWriter(DomainModel domainModel, DataStore dataStore)
    {
        this.domainModel = Objects.requireNonNull(domainModel);
        this.dataStore   = Objects.requireNonNull(dataStore);
    }

    public void bootstrapMetaModel()
    {
        this.dataStore.runInTransaction(this::bootstrapMetaModelInTransaction);
    }

    private void bootstrapMetaModelInTransaction()
    {
        // BOOTSTRAP_FINDERS.each(this::deleteAll);

        this.domainModel
                .getEnumerations()
                .collect(this::handleEnumeration, new EnumerationList())
                .insertAll();
        this.domainModel
                .getEnumerations()
                .flatCollect(Enumeration::getEnumerationLiterals)
                .collect(this::handleEnumerationLiteral, new EnumerationLiteralList())
                .insertAll();
        this.domainModel
                .getClassifiers()
                .collect(this::handleClassifier, new ClassifierList())
                .insertAll();
        this.domainModel
                .getInterfaces()
                .collect(this::handleInterface, new InterfaceList())
                .insertAll();
        this.domainModel
                .getClasses()
                .collect(this::handleClass, new klass.model.meta.domain.KlassList())
                .insertAll();
        this.domainModel
                .getClassifiers()
                .flatCollect(this::handleSuperInterface, new ClassifierInterfaceMappingList())
                .insertAll();
        this.domainModel
                .getClassifiers()
                .flatCollect(this::handleClassifierModifier, new ClassifierModifierList())
                .insertAll();
        this.domainModel
                .getClassifiers()
                .flatCollect(this::handleDataTypeProperty, new DataTypePropertyList())
                .insertAll();

        ImmutableList<DataTypeProperty> allDataTypeProperties = this.domainModel
                .getClassifiers()
                .flatCollect(Classifier::getDeclaredDataTypeProperties);
        allDataTypeProperties
                .flatCollect(this::handlePropertyModifier, new PropertyModifierList())
                .insertAll();
        allDataTypeProperties
                .collect(this::handleMinLengthPropertyValidation)
                .reject(Optional::isEmpty)
                .collect(Optional::get, new MinLengthPropertyValidationList())
                .insertAll();
        allDataTypeProperties
                .collect(this::handleMaxLengthPropertyValidation)
                .reject(Optional::isEmpty)
                .collect(Optional::get, new MaxLengthPropertyValidationList())
                .insertAll();
        allDataTypeProperties
                .collect(this::handleMinPropertyValidation)
                .reject(Optional::isEmpty)
                .collect(Optional::get, new MinPropertyValidationList())
                .insertAll();
        allDataTypeProperties
                .collect(this::handleMaxPropertyValidation)
                .reject(Optional::isEmpty)
                .collect(Optional::get, new MaxPropertyValidationList())
                .insertAll();
        allDataTypeProperties
                .select(PrimitiveProperty.class::isInstance)
                .collect(PrimitiveProperty.class::cast)
                .collect(this::handlePrimitiveProperty, new PrimitivePropertyList())
                .insertAll();
        allDataTypeProperties
                .select(EnumerationProperty.class::isInstance)
                .collect(EnumerationProperty.class::cast)
                .collect(this::handleEnumerationProperty, new EnumerationPropertyList())
                .insertAll();

        BootstrapExpressionValueVisitor1 expressionValueVisitor1 = new BootstrapExpressionValueVisitor1();
        var criteriaVisitor1 = new BootstrapExpressionValueCriteriaVisitor(expressionValueVisitor1);
        this.domainModel
                .getAssociations()
                .collect(Association::getCriteria)
                .each(criteria -> criteria.visit(criteriaVisitor1));
        this.domainModel
                .getAssociations()
                .flatCollect(Association::getAssociationEnds)
                .collect(AssociationEnd::getOrderBy)
                .reject(Optional::isEmpty)
                .collect(Optional::get)
                .flatCollect(OrderBy::getOrderByMemberReferencePaths)
                .collect(OrderByMemberReferencePath::getThisMemberReferencePath)
                .each(expressionValueVisitor1::visitThisMember);

        expressionValueVisitor1.getBootstrappedExpressionValues().insertAll();

        var expressionValuesByExpressionValue = expressionValueVisitor1.getExpressionValuesByExpressionValue().toImmutable();
        var expressionValueVisitor2           = new BootstrapExpressionValueVisitor2(expressionValuesByExpressionValue);
        var criteriaVisitor2                  = new BootstrapExpressionValueCriteriaVisitor(expressionValueVisitor2);

        this.domainModel
                .getAssociations()
                .collect(Association::getCriteria)
                .each(criteria -> criteria.visit(criteriaVisitor2));
        this.domainModel
                .getAssociations()
                .flatCollect(Association::getAssociationEnds)
                .collect(AssociationEnd::getOrderBy)
                .reject(Optional::isEmpty)
                .collect(Optional::get)
                .flatCollect(OrderBy::getOrderByMemberReferencePaths)
                .collect(OrderByMemberReferencePath::getThisMemberReferencePath)
                .each(expressionValueVisitor2::visitThisMember);

        new MemberReferencePathList(expressionValueVisitor2.getBootstrappedMemberReferencePaths()).insertAll();
        new ThisMemberReferencePathList(expressionValueVisitor2.getBootstrappedThisMemberReferencePaths()).insertAll();
        new TypeMemberReferencePathList(expressionValueVisitor2.getBootstrappedTypeMemberReferencePaths()).insertAll();

        var criteriaVisitor3 = new BootstrapCriteriaVisitor1();
        this.domainModel
                .getAssociations()
                .collect(Association::getCriteria)
                .each(criteria -> criteria.visit(criteriaVisitor3));
        criteriaVisitor3.getBootstrappedCriteria().insertAll();

        ImmutableMap<Criteria, klass.model.meta.domain.Criteria> criteriaByCriteria = criteriaVisitor3.getCriteriaByCriteria();
        var criteriaVisitor4 = new BootstrapCriteriaVisitor2(criteriaByCriteria, expressionValuesByExpressionValue);
        this.domainModel
                .getAssociations()
                .collect(Association::getCriteria)
                .each(criteria -> criteria.visit(criteriaVisitor4));
        criteriaVisitor4.getAllCriteria().insertAll();
        criteriaVisitor4.getEdgePointCriteria().insertAll();
        criteriaVisitor4.getOperatorCriteria().insertAll();
        criteriaVisitor4.getBinaryCriteria().insertAll();
        criteriaVisitor4.getAndCriteria().insertAll();
        criteriaVisitor4.getOrCriteria().insertAll();

        this.domainModel
                .getAssociations()
                .collectWith(this::handleAssociation, criteriaByCriteria, new klass.model.meta.domain.AssociationList())
                .insertAll();
        this.domainModel
                .getAssociations()
                .flatCollect(Association::getAssociationEnds)
                .collect(this::handleAssociationEnd, new klass.model.meta.domain.AssociationEndList())
                .insertAll();
        this.domainModel
                .getAssociations()
                .flatCollect(Association::getAssociationEnds)
                .flatCollect(
                        associationEnd -> associationEnd
                                .getModifiers()
                                .collectWith(this::handleAssociationEndModifier, associationEnd),
                        new klass.model.meta.domain.AssociationEndModifierList())
                .insertAll();

        this.domainModel
                .getAssociations()
                .flatCollect(Association::getAssociationEnds)
                .flatCollect(
                        associationEnd -> associationEnd
                                .getOrderBy()
                                .map(OrderBy::getOrderByMemberReferencePaths).orElseGet(Lists.immutable::empty)
                                .collect(memberReferencePath -> this.handleOrderByMemberReferencePath(memberReferencePath, associationEnd, expressionValuesByExpressionValue)),
                        new klass.model.meta.domain.AssociationEndOrderByList())
                .insertAll();

        MutableMap<Projection, klass.model.meta.domain.ProjectionElement> rootProjectionByProjection = Maps.mutable.empty();

        ProjectionElementList projectionElementList = new ProjectionElementList();
        for (Projection projection : this.domainModel.getProjections())
        {
            klass.model.meta.domain.ProjectionElement projectionElement = this.handleRootProjectionElement(projection);
            rootProjectionByProjection.put(projection, projectionElement);
            projectionElementList.add(projectionElement);
        }
        projectionElementList.insertAll();

        this.domainModel
                .getProjections()
                .collect(each -> this.handleRootProjection(
                        each,
                        rootProjectionByProjection.get(each)), new RootProjectionList())
                .insertAll();

        this.domainModel
                .getProjections()
                .collect(projection -> this.handleNamedProjection(projection, rootProjectionByProjection), new klass.model.meta.domain.NamedProjectionList())
                .insertAll();

        this.domainModel
                .getProjections()
                .each(projection -> this.handleProjectionChildren(
                        projection,
                        rootProjectionByProjection.get(projection)));

        this.domainModel
                .getServiceGroups()
                .collect(this::handleServiceGroup, new klass.model.meta.domain.ServiceGroupList())
                .insertAll();

        this.domainModel
                .getServiceGroups()
                .flatCollect(ServiceGroup::getUrls)
                .collect((Url url) -> this.handleUrl(url), new klass.model.meta.domain.UrlList())
                .insertAll();
    }

    /*
    private void deleteAll(@Nonnull RelatedFinder<?> finder)
    {
        Operation     operation               = finder.all();
        MithraList<?> mithraList              = finder.findMany(operation);
        var           transactionalDomainList = (TransactionalDomainList<?>) mithraList;
        transactionalDomainList.deleteAll();
    }
    */

    private klass.model.meta.domain.Enumeration handleEnumeration(@Nonnull Enumeration enumeration)
    {
        var bootstrappedEnumeration = new klass.model.meta.domain.Enumeration();
        KlassBootstrapWriter.handlePackageableElement(bootstrappedEnumeration, enumeration);
        return bootstrappedEnumeration;
    }

    private klass.model.meta.domain.EnumerationLiteral handleEnumerationLiteral(@Nonnull EnumerationLiteral enumerationLiteral)
    {
        var bootstrappedEnumerationLiteral = new klass.model.meta.domain.EnumerationLiteral();
        KlassBootstrapWriter.handleNamedElement(bootstrappedEnumerationLiteral, enumerationLiteral);
        enumerationLiteral.getDeclaredPrettyName().ifPresent(bootstrappedEnumerationLiteral::setPrettyName);
        bootstrappedEnumerationLiteral.setEnumerationName(enumerationLiteral.getType().getName());
        return bootstrappedEnumerationLiteral;
    }

    private klass.model.meta.domain.Classifier handleClassifier(@Nonnull Classifier classifier)
    {
        var bootstrappedClassifier = new klass.model.meta.domain.Classifier();
        bootstrappedClassifier.setPackageName(classifier.getPackageName());
        bootstrappedClassifier.setName(classifier.getName());
        bootstrappedClassifier.setOrdinal(classifier.getOrdinal());
        return bootstrappedClassifier;
    }

    private ImmutableList<ClassifierInterfaceMapping> handleSuperInterface(@Nonnull Classifier classifier)
    {
        return classifier
                .getInterfaces()
                .collect(superInterface -> getClassifierInterfaceMapping(classifier, superInterface));
    }

    @Nonnull
    private static ClassifierInterfaceMapping getClassifierInterfaceMapping(
            @Nonnull Classifier classifier,
            Interface superInterface)
    {
        ClassifierInterfaceMapping classifierInterfaceMapping = new ClassifierInterfaceMapping();
        classifierInterfaceMapping.setClassifierName(classifier.getName());
        classifierInterfaceMapping.setInterfaceName(superInterface.getName());
        return classifierInterfaceMapping;
    }

    private ImmutableList<ClassifierModifier> handleClassifierModifier(@Nonnull Classifier classifier)
    {
        return classifier
                .getModifiers()
                .collect(modifier -> this.getClassifierModifier(classifier, modifier));
    }

    private klass.model.meta.domain.ClassifierModifier getClassifierModifier(Classifier classifier, Modifier modifier)
    {
        var bootstrappedClassifierModifier = new klass.model.meta.domain.ClassifierModifier();
        bootstrappedClassifierModifier.setKeyword(modifier.getKeyword());
        bootstrappedClassifierModifier.setOrdinal(modifier.getOrdinal());
        bootstrappedClassifierModifier.setClassifierName(classifier.getName());
        return bootstrappedClassifierModifier;
    }

    private ImmutableList<klass.model.meta.domain.DataTypeProperty> handleDataTypeProperty(@Nonnull Classifier classifier)
    {
        return classifier
                .getDeclaredDataTypeProperties()
                .collect(this::getDataTypeProperty);
    }

    private klass.model.meta.domain.DataTypeProperty getDataTypeProperty(
            DataTypeProperty dataTypeProperty)
    {
        Classifier classifier = dataTypeProperty.getOwningClassifier();

        var bootstrappedDataTypeProperty = new klass.model.meta.domain.DataTypeProperty();
        bootstrappedDataTypeProperty.setName(dataTypeProperty.getName());
        bootstrappedDataTypeProperty.setOrdinal(dataTypeProperty.getOrdinal());
        bootstrappedDataTypeProperty.setClassifierName(classifier.getName());
        bootstrappedDataTypeProperty.setOptional(dataTypeProperty.isOptional());
        return bootstrappedDataTypeProperty;
    }

    private ImmutableList<klass.model.meta.domain.PropertyModifier> handlePropertyModifier(@Nonnull DataTypeProperty dataTypeProperty)
    {
        return dataTypeProperty
                .getModifiers()
                .collect(modifier -> this.getPropertyModifier(dataTypeProperty, modifier));
    }

    private klass.model.meta.domain.PropertyModifier getPropertyModifier(DataTypeProperty dataTypeProperty, Modifier modifier)
    {
        var bootstrappedPropertyModifier = new klass.model.meta.domain.PropertyModifier();
        bootstrappedPropertyModifier.setKeyword(modifier.getKeyword());
        bootstrappedPropertyModifier.setOrdinal(modifier.getOrdinal());
        bootstrappedPropertyModifier.setClassifierName(dataTypeProperty.getOwningClassifier().getName());
        bootstrappedPropertyModifier.setPropertyName(dataTypeProperty.getName());
        return bootstrappedPropertyModifier;
    }

    private Optional<klass.model.meta.domain.MinLengthPropertyValidation> handleMinLengthPropertyValidation(DataTypeProperty dataTypeProperty)
    {
        return dataTypeProperty.getMinLengthPropertyValidation().map(validation ->
        {
            Classifier classifier = dataTypeProperty.getOwningClassifier();

            var bootstrappedMinLengthPropertyValidation = new klass.model.meta.domain.MinLengthPropertyValidation();
            bootstrappedMinLengthPropertyValidation.setClassifierName(classifier.getName());
            bootstrappedMinLengthPropertyValidation.setPropertyName(dataTypeProperty.getName());
            bootstrappedMinLengthPropertyValidation.setNumber(validation.getNumber());

            return bootstrappedMinLengthPropertyValidation;
        });
    }

    private Optional<klass.model.meta.domain.MaxLengthPropertyValidation> handleMaxLengthPropertyValidation(DataTypeProperty dataTypeProperty)
    {
        return dataTypeProperty.getMaxLengthPropertyValidation().map(validation ->
        {
            Classifier classifier = dataTypeProperty.getOwningClassifier();

            var bootstrappedMaxLengthPropertyValidation = new klass.model.meta.domain.MaxLengthPropertyValidation();
            bootstrappedMaxLengthPropertyValidation.setClassifierName(classifier.getName());
            bootstrappedMaxLengthPropertyValidation.setPropertyName(dataTypeProperty.getName());
            bootstrappedMaxLengthPropertyValidation.setNumber(validation.getNumber());

            return bootstrappedMaxLengthPropertyValidation;
        });
    }

    private Optional<klass.model.meta.domain.MinPropertyValidation> handleMinPropertyValidation(DataTypeProperty dataTypeProperty)
    {
        return dataTypeProperty.getMinPropertyValidation().map(validation ->
        {
            Classifier classifier = dataTypeProperty.getOwningClassifier();

            var bootstrappedMinPropertyValidation = new klass.model.meta.domain.MinPropertyValidation();
            bootstrappedMinPropertyValidation.setClassifierName(classifier.getName());
            bootstrappedMinPropertyValidation.setPropertyName(dataTypeProperty.getName());
            bootstrappedMinPropertyValidation.setNumber(validation.getNumber());

            return bootstrappedMinPropertyValidation;
        });
    }

    private Optional<klass.model.meta.domain.MaxPropertyValidation> handleMaxPropertyValidation(DataTypeProperty dataTypeProperty)
    {
        return dataTypeProperty.getMaxPropertyValidation().map(validation ->
        {
            Classifier classifier = dataTypeProperty.getOwningClassifier();

            var bootstrappedMaxPropertyValidation = new klass.model.meta.domain.MaxPropertyValidation();
            bootstrappedMaxPropertyValidation.setClassifierName(classifier.getName());
            bootstrappedMaxPropertyValidation.setPropertyName(dataTypeProperty.getName());
            bootstrappedMaxPropertyValidation.setNumber(validation.getNumber());

            return bootstrappedMaxPropertyValidation;
        });
    }

    private klass.model.meta.domain.PrimitiveProperty handlePrimitiveProperty(PrimitiveProperty primitiveProperty)
    {
        Classifier classifier = primitiveProperty.getOwningClassifier();

        var bootstrappedPrimitiveProperty = new klass.model.meta.domain.PrimitiveProperty();
        bootstrappedPrimitiveProperty.setClassifierName(classifier.getName());
        bootstrappedPrimitiveProperty.setName(primitiveProperty.getName());
        bootstrappedPrimitiveProperty.setPrimitiveType(primitiveProperty.getType().getPrettyName());

        return bootstrappedPrimitiveProperty;
    }

    private klass.model.meta.domain.EnumerationProperty handleEnumerationProperty(EnumerationProperty enumerationProperty)
    {
        Classifier classifier = enumerationProperty.getOwningClassifier();

        var bootstrappedEnumerationProperty = new klass.model.meta.domain.EnumerationProperty();
        bootstrappedEnumerationProperty.setClassifierName(classifier.getName());
        bootstrappedEnumerationProperty.setName(enumerationProperty.getName());
        bootstrappedEnumerationProperty.setEnumerationName(enumerationProperty.getType().getName());

        return bootstrappedEnumerationProperty;
    }

    private klass.model.meta.domain.Interface handleInterface(@Nonnull Interface anInterface)
    {
        var bootstrappedInterface = new klass.model.meta.domain.Interface();
        bootstrappedInterface.setName(anInterface.getName());
        return bootstrappedInterface;
    }

    private klass.model.meta.domain.Klass handleClass(@Nonnull Klass klass)
    {
        var bootstrappedClass = new klass.model.meta.domain.Klass();
        bootstrappedClass.setName(klass.getName());
        // TODO: Report Reladomo bug. If any non-nullable properties are not set on a transient object, insert() ought to throw but doesn't
        bootstrappedClass.setAbstractClass(klass.isAbstract());

        klass.getSuperClass()
                .map(NamedElement::getName)
                .ifPresent(bootstrappedClass::setSuperClassName);
        return bootstrappedClass;
    }

    private klass.model.meta.domain.Association handleAssociation(
            @Nonnull Association association,
            @Nonnull ImmutableMap<Criteria, klass.model.meta.domain.Criteria> criteriaByCriteria)
    {
        var bootstrappedCriteria    = criteriaByCriteria.get(association.getCriteria());
        var bootstrappedAssociation = new klass.model.meta.domain.Association();
        KlassBootstrapWriter.handlePackageableElement(bootstrappedAssociation, association);
        bootstrappedAssociation.setCriteria(bootstrappedCriteria);
        return bootstrappedAssociation;
    }

    private klass.model.meta.domain.AssociationEnd handleAssociationEnd(@Nonnull AssociationEnd associationEnd)
    {
        String direction = getDirection(associationEnd);
        var bootstrappedAssociationEnd = new klass.model.meta.domain.AssociationEnd();
        KlassBootstrapWriter.handleNamedElement(bootstrappedAssociationEnd, associationEnd);
        bootstrappedAssociationEnd.setOwningClassName(associationEnd.getOwningClassifier().getName());
        bootstrappedAssociationEnd.setAssociationName(associationEnd.getOwningAssociation().getName());
        bootstrappedAssociationEnd.setDirection(direction);
        bootstrappedAssociationEnd.setMultiplicity(associationEnd.getMultiplicity().getPrettyName());
        bootstrappedAssociationEnd.setResultTypeName(associationEnd.getType().getName());
        return bootstrappedAssociationEnd;
    }

    private klass.model.meta.domain.AssociationEndModifier handleAssociationEndModifier(
            @Nonnull Modifier modifier, @Nonnull AssociationEnd associationEnd)
    {
        var bootstrappedAssociationEndModifier = new klass.model.meta.domain.AssociationEndModifier();
        bootstrappedAssociationEndModifier.setOwningClassName(associationEnd.getOwningClassifier().getName());
        bootstrappedAssociationEndModifier.setAssociationEndName(associationEnd.getName());
        bootstrappedAssociationEndModifier.setKeyword(modifier.getKeyword());
        bootstrappedAssociationEndModifier.setOrdinal(modifier.getOrdinal());
        return bootstrappedAssociationEndModifier;
    }

    @Nonnull
    private AssociationEndOrderBy handleOrderByMemberReferencePath(
            @Nonnull OrderByMemberReferencePath orderByMemberReferencePath,
            @Nonnull AssociationEnd associationEnd,
            @Nonnull ImmutableMap<ExpressionValue, klass.model.meta.domain.ExpressionValue> expressionValuesByExpressionValue)
    {
        ThisMemberReferencePath thisMemberReferencePath = orderByMemberReferencePath.getThisMemberReferencePath();

        var expressionValue = expressionValuesByExpressionValue.get(thisMemberReferencePath);

        var associationEndOrderBy = new AssociationEndOrderBy();
        associationEndOrderBy.setAssociationEndClassName(associationEnd
                .getOwningClassifier()
                .getName());
        associationEndOrderBy.setAssociationEndName(associationEnd.getName());
        associationEndOrderBy.setThisMemberReferencePathId(expressionValue.getId());
        associationEndOrderBy.setOrderByDirection(orderByMemberReferencePath
                .getOrderByDirectionDeclaration()
                .getOrderByDirection()
                .getPrettyName());
        return associationEndOrderBy;
    }

    private void bootstrapAssociationEndOrderBy(
            @Nonnull AssociationEnd associationEnd,
            @Nonnull OrderBy orderBy)
    {
        for (OrderByMemberReferencePath orderByMemberReferencePath : orderBy.getOrderByMemberReferencePaths())
        {
            ThisMemberReferencePath thisMemberReferencePath = orderByMemberReferencePath.getThisMemberReferencePath();

            klass.model.meta.domain.ThisMemberReferencePath bootstrappedThisMemberReferencePath =
                    this.bootstrapThisMemberReferencePath(thisMemberReferencePath);

            var associationEndOrderBy = new AssociationEndOrderBy();
            associationEndOrderBy.setAssociationEndClassName(associationEnd.getOwningClassifier().getName());
            associationEndOrderBy.setAssociationEndName(associationEnd.getName());
            associationEndOrderBy.setThisMemberReferencePathId(bootstrappedThisMemberReferencePath.getId());
            associationEndOrderBy.setOrderByDirection(orderByMemberReferencePath
                    .getOrderByDirectionDeclaration()
                    .getOrderByDirection()
                    .getPrettyName());
            associationEndOrderBy.insert();
        }
    }

    @Nonnull
    private static String getDirection(@Nonnull AssociationEnd associationEnd)
    {
        if (associationEnd == associationEnd.getOwningAssociation().getSourceAssociationEnd())
        {
            return "source";
        }
        if (associationEnd == associationEnd.getOwningAssociation().getTargetAssociationEnd())
        {
            return "target";
        }
        throw new AssertionError();
    }

    private klass.model.meta.domain.ProjectionElement handleRootProjectionElement(@Nonnull Projection projection)
    {
        var bootstrappedProjectionElement = new klass.model.meta.domain.ProjectionElement();
        bootstrappedProjectionElement.setName(projection.getName());
        bootstrappedProjectionElement.setOrdinal(projection.getOrdinal());
        return bootstrappedProjectionElement;
    }

    private klass.model.meta.domain.RootProjection handleRootProjection(
            @Nonnull Projection projection,
            @Nonnull    klass.model.meta.domain.ProjectionElement projectionElement)
    {
        var bootstrappedRootProjection = new RootProjection();
        bootstrappedRootProjection.setId(projectionElement.getId());
        bootstrappedRootProjection.setClassName(projection.getClassifier().getName());
        return bootstrappedRootProjection;
    }

    private void handleProjectionChildren(
            @Nonnull Projection projection,
            @Nonnull klass.model.meta.domain.ProjectionElement bootstrappedProjectionElement)
    {
        for (ProjectionChild projectionChild : projection.getChildren())
        {
            this.handleElementProjection(
                    projectionChild,
                    bootstrappedProjectionElement);
        }
    }

    private void handleElementProjection(
            @Nonnull ProjectionElement projectionElement,
            @Nonnull klass.model.meta.domain.ProjectionElement bootstrappedProjectionParent)
    {
        projectionElement.visit(new ProjectionVisitor()
        {
            @Override
            public void visitProjection(@Nonnull Projection projection)
            {
                throw new AssertionError();
            }

            @Override
            public void visitProjectionReferenceProperty(@Nonnull ProjectionReferenceProperty projectionReferenceProperty)
            {
                var bootstrappedProjectionElement = new klass.model.meta.domain.ProjectionElement();
                bootstrappedProjectionElement.setName(projectionReferenceProperty.getName());
                bootstrappedProjectionElement.setOrdinal(projectionReferenceProperty.getOrdinal());
                bootstrappedProjectionElement.setParentId(bootstrappedProjectionParent.getId());
                bootstrappedProjectionElement.insert();

                var bootstrappedProjectionWithAssociationEnd = new klass.model.meta.domain.ProjectionWithAssociationEnd();
                bootstrappedProjectionWithAssociationEnd.setId(bootstrappedProjectionElement.getId());
                bootstrappedProjectionWithAssociationEnd.setAssociationEndClass(projectionReferenceProperty
                        .getProperty()
                        .getOwningClassifier()
                        .getName());
                bootstrappedProjectionWithAssociationEnd.setAssociationEndName(projectionReferenceProperty
                        .getProperty()
                        .getName());
                bootstrappedProjectionWithAssociationEnd.insert();

                var bootstrappedProjectionReferenceProperty = new klass.model.meta.domain.ProjectionReferenceProperty();
                bootstrappedProjectionReferenceProperty.setId(bootstrappedProjectionElement.getId());
                bootstrappedProjectionReferenceProperty.insert();

                for (ProjectionChild projectionChild : projectionReferenceProperty.getChildren())
                {
                    KlassBootstrapWriter.this.handleElementProjection(projectionChild, bootstrappedProjectionElement);
                }
            }

            @Override
            public void visitProjectionProjectionReference(@Nonnull ProjectionProjectionReference projectionProjectionReference)
            {
                var bootstrappedProjectionElement = new klass.model.meta.domain.ProjectionElement();
                bootstrappedProjectionElement.setName(projectionProjectionReference.getName());
                bootstrappedProjectionElement.setOrdinal(projectionProjectionReference.getOrdinal());
                bootstrappedProjectionElement.setParentId(bootstrappedProjectionParent.getId());
                bootstrappedProjectionElement.insert();

                var bootstrappedProjectionWithAssociationEnd = new klass.model.meta.domain.ProjectionWithAssociationEnd();
                bootstrappedProjectionWithAssociationEnd.setId(bootstrappedProjectionElement.getId());
                bootstrappedProjectionWithAssociationEnd.setAssociationEndClass(projectionProjectionReference
                        .getProperty()
                        .getOwningClassifier()
                        .getName());
                bootstrappedProjectionWithAssociationEnd.setAssociationEndName(projectionProjectionReference
                        .getProperty()
                        .getName());
                bootstrappedProjectionWithAssociationEnd.insert();

                var bootstrappedProjectionProjectionReference = new klass.model.meta.domain.ProjectionProjectionReference();
                bootstrappedProjectionProjectionReference.setId(bootstrappedProjectionElement.getId());
                bootstrappedProjectionProjectionReference.setProjectionName(projectionProjectionReference
                        .getProjection()
                        .getName());
                bootstrappedProjectionProjectionReference.insert();
            }

            @Override
            public void visitProjectionDataTypeProperty(@Nonnull ProjectionDataTypeProperty projectionDataTypeProperty)
            {
                var bootstrappedProjectionElement = new klass.model.meta.domain.ProjectionElement();
                bootstrappedProjectionElement.setName(projectionDataTypeProperty.getName());
                bootstrappedProjectionElement.setOrdinal(projectionDataTypeProperty.getOrdinal());
                bootstrappedProjectionElement.setParentId(bootstrappedProjectionParent.getId());
                bootstrappedProjectionElement.insert();

                var bootstrappedProjectionDataTypeProperty = new klass.model.meta.domain.ProjectionDataTypeProperty();
                bootstrappedProjectionDataTypeProperty.setId(bootstrappedProjectionElement.getId());
                bootstrappedProjectionDataTypeProperty.setPropertyClassifierName(projectionDataTypeProperty
                        .getProperty()
                        .getOwningClassifier()
                        .getName());
                bootstrappedProjectionDataTypeProperty.setPropertyName(projectionDataTypeProperty
                        .getProperty()
                        .getName());
                bootstrappedProjectionDataTypeProperty.insert();
            }
        });
    }

    private NamedProjection handleNamedProjection(
            @Nonnull Projection projection,
            @Nonnull MutableMap<Projection, klass.model.meta.domain.ProjectionElement> rootProjectionByProjection)
    {
        var bootstrappedRootProjection = rootProjectionByProjection.get(projection);
        var bootstrappedProjection     = new klass.model.meta.domain.NamedProjection();
        handlePackageableElement(bootstrappedProjection, projection);
        bootstrappedProjection.setProjectionId(bootstrappedRootProjection.getId());
        return bootstrappedProjection;
    }

    private klass.model.meta.domain.ServiceGroup handleServiceGroup(@Nonnull ServiceGroup serviceGroup)
    {
        klass.model.meta.domain.ServiceGroup bootstrappedServiceGroup = new klass.model.meta.domain.ServiceGroup();
        KlassBootstrapWriter.handlePackageableElement(bootstrappedServiceGroup, serviceGroup);
        return bootstrappedServiceGroup;
    }

    private klass.model.meta.domain.Url handleUrl(@Nonnull Url url)
    {
        ServiceGroup serviceGroup = url.getServiceGroup();

        var bootstrappedUrl = new klass.model.meta.domain.Url();
        bootstrappedUrl.setClassName(serviceGroup.getKlass().getName());
        bootstrappedUrl.setUrl(url.getUrlString());
        return bootstrappedUrl;
    }

    private void handleUrlChildren(@Nonnull Url url)
    {
        ServiceGroup serviceGroup = url.getServiceGroup();

        MutableMap<Parameter, klass.model.meta.domain.Parameter> bootstrappedParametersByParameter = Maps.mutable.empty();

        for (Parameter pathParameter : url.getPathParameters())
        {
            this.handleUrlParameter(url, pathParameter, "path", bootstrappedParametersByParameter);
        }

        for (Parameter queryParameter : url.getQueryParameters())
        {
            this.handleUrlParameter(url, queryParameter, "query", bootstrappedParametersByParameter);
        }

        for (Service service : url.getServices())
        {
            this.handleService(serviceGroup, url, service, bootstrappedParametersByParameter.toImmutable());
        }
    }

    private void handleUrlParameter(
            Url url,
            @Nonnull Parameter parameter,
            String urlParameterType,
            @Nonnull MutableMap<Parameter, klass.model.meta.domain.Parameter> bootstrappedParametersByParameter)
    {
        var bootstrappedParameter = new klass.model.meta.domain.Parameter();
        handleNamedElement(bootstrappedParameter, parameter);
        bootstrappedParameter.setMultiplicity(parameter.getMultiplicity().getPrettyName());
        bootstrappedParameter.insert();

        DataType dataType = parameter.getType();
        if (dataType instanceof PrimitiveType primitiveType)
        {
            PrimitiveParameter bootstrappedPrimitiveParameter = new PrimitiveParameter();
            bootstrappedPrimitiveParameter.setPrimitiveType(primitiveType.getPrettyName());
            bootstrappedPrimitiveParameter.setId(bootstrappedParameter.getId());
        }
        else if (dataType instanceof Enumeration enumeration)
        {
            EnumerationParameter bootstrappedEnumerationParameter = new EnumerationParameter();
            bootstrappedEnumerationParameter.setEnumerationName(enumeration.getName());
            bootstrappedEnumerationParameter.setId(bootstrappedParameter.getId());
        }
        else
        {
            throw new AssertionError();
        }

        UrlParameter bootstrappedUrlParameter = new UrlParameter();
        bootstrappedUrlParameter.setParameter(bootstrappedParameter);
        bootstrappedUrlParameter.setUrlClassName(url.getServiceGroup().getKlass().getName());
        bootstrappedUrlParameter.setUrlString(url.getUrlString());
        bootstrappedUrlParameter.setType(urlParameterType);
        bootstrappedUrlParameter.insert();

        bootstrappedParametersByParameter.put(parameter, bootstrappedParameter);
    }

    private void handleService(
            @Nonnull ServiceGroup serviceGroup,
            @Nonnull Url url,
            @Nonnull Service service,
            ImmutableMap<Parameter, klass.model.meta.domain.Parameter> bootstrappedParametersByParameter)
    {
        /*
        Optional<klass.model.meta.domain.Criteria> optionalBootstrappedQueryCriteria = service.getQueryCriteria()
                .map(criteria -> BootstrapCriteriaVisitor.convert(bootstrappedParametersByParameter, criteria));
        Optional<klass.model.meta.domain.Criteria> optionalBootstrappedAuthorizeCriteria = service
                .getAuthorizeCriteria()
                .map(criteria -> BootstrapCriteriaVisitor.convert(bootstrappedParametersByParameter, criteria));
        Optional<klass.model.meta.domain.Criteria> optionalBootstrappedValidateCriteria = service.getValidateCriteria()
                .map(criteria -> BootstrapCriteriaVisitor.convert(bootstrappedParametersByParameter, criteria));
        Optional<klass.model.meta.domain.Criteria> optionalBootstrappedConflictCriteria = service.getConflictCriteria()
                .map(criteria -> BootstrapCriteriaVisitor.convert(bootstrappedParametersByParameter, criteria));

        klass.model.meta.domain.Service bootstrappedService = new klass.model.meta.domain.Service();
        bootstrappedService.setClassName(serviceGroup.getKlass().getName());
        bootstrappedService.setUrlString(url.getUrlString());
        bootstrappedService.setVerb(service.getVerb().name());
        bootstrappedService.setServiceMultiplicity(service.getServiceMultiplicity().getPrettyName());
        service.getProjectionDispatch()
                .map(ServiceProjectionDispatch::getProjection)
                .map(NamedElement::getName)
                .ifPresent(bootstrappedService::setProjectionName);
        optionalBootstrappedQueryCriteria.ifPresent(bootstrappedService::setQueryCriteria);
        optionalBootstrappedAuthorizeCriteria.ifPresent(bootstrappedService::setAuthorizeCriteria);
        optionalBootstrappedValidateCriteria.ifPresent(bootstrappedService::setValidateCriteria);
        optionalBootstrappedConflictCriteria.ifPresent(bootstrappedService::setConflictCriteria);
        // TODO: Bootstrap service orderBy
        Optional<OrderBy> orderBy = service.getOrderBy();
        bootstrappedService.insert();
        */
    }

    @Nonnull
    private klass.model.meta.domain.ThisMemberReferencePath bootstrapThisMemberReferencePath(@Nonnull ThisMemberReferencePath thisMemberReferencePath)
    {
        var bootstrappedExpressionValue = new klass.model.meta.domain.ExpressionValue();
        bootstrappedExpressionValue.insert();

        Klass            klass    = thisMemberReferencePath.getKlass();
        DataTypeProperty property = thisMemberReferencePath.getProperty();

        var bootstrappedMemberReferencePath = new klass.model.meta.domain.MemberReferencePath();
        bootstrappedMemberReferencePath.setId(bootstrappedExpressionValue.getId());
        bootstrappedMemberReferencePath.setClassName(klass.getName());
        bootstrappedMemberReferencePath.setPropertyClassName(property.getOwningClassifier().getName());
        bootstrappedMemberReferencePath.setPropertyName(property.getName());

        var bootstrappedThisMemberReferencePath = new klass.model.meta.domain.ThisMemberReferencePath();
        bootstrappedThisMemberReferencePath.setId(bootstrappedExpressionValue.getId());

        if (thisMemberReferencePath.getAssociationEnds().notEmpty())
        {
            throw new AssertionError("TODO");
        }
        return bootstrappedThisMemberReferencePath;
    }

    public static void handleNamedElement(
            @Nonnull NamedElementAbstract bootstrappedNamedElement,
            @Nonnull NamedElement namedElement)
    {
        bootstrappedNamedElement.setName(namedElement.getName());
        bootstrappedNamedElement.setOrdinal(namedElement.getOrdinal());
    }

    public static void handlePackageableElement(
            @Nonnull PackageableElementAbstract bootstrappedPackageableElement,
            @Nonnull PackageableElement packageableElement)
    {
        KlassBootstrapWriter.handleNamedElement(bootstrappedPackageableElement, packageableElement);
        bootstrappedPackageableElement.setPackageName(packageableElement.getPackageName());
    }
}
