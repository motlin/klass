package cool.klass.model.converter.bootstrap.writer;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import cool.klass.data.store.DataStore;
import cool.klass.model.meta.domain.api.Association;
import cool.klass.model.meta.domain.api.ClassModifier;
import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.Enumeration;
import cool.klass.model.meta.domain.api.EnumerationLiteral;
import cool.klass.model.meta.domain.api.Interface;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.NamedElement;
import cool.klass.model.meta.domain.api.PackageableElement;
import cool.klass.model.meta.domain.api.order.OrderBy;
import cool.klass.model.meta.domain.api.order.OrderByMemberReferencePath;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.AssociationEndModifier;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.property.EnumerationProperty;
import cool.klass.model.meta.domain.api.property.PrimitiveProperty;
import cool.klass.model.meta.domain.api.property.PropertyModifier;
import cool.klass.model.meta.domain.api.property.validation.NumericPropertyValidation;
import cool.klass.model.meta.domain.api.value.ThisMemberReferencePath;
import klass.model.meta.domain.AssociationEndOrderBy;
import klass.model.meta.domain.ClassifierInterfaceMapping;
import klass.model.meta.domain.ElementAbstract;
import klass.model.meta.domain.MaxLengthPropertyValidation;
import klass.model.meta.domain.MaxPropertyValidation;
import klass.model.meta.domain.MinLengthPropertyValidation;
import klass.model.meta.domain.MinPropertyValidation;
import klass.model.meta.domain.NamedElementAbstract;
import klass.model.meta.domain.PackageableElementAbstract;

public class KlassBootstrapWriter
{
    private final DomainModel domainModel;
    private final DataStore   dataStore;

    public KlassBootstrapWriter(DomainModel domainModel, DataStore dataStore)
    {
        this.domainModel = Objects.requireNonNull(domainModel);
        this.dataStore = Objects.requireNonNull(dataStore);
    }

    public void bootstrapMetaModel()
    {
        this.dataStore.runInTransaction(this::bootstrapMetaModelInTransaction);
    }

    private void bootstrapMetaModelInTransaction()
    {
        for (Enumeration enumeration : this.domainModel.getEnumerations())
        {
            klass.model.meta.domain.Enumeration bootstrappedEnumeration = new klass.model.meta.domain.Enumeration();
            KlassBootstrapWriter.handlePackageableElement(bootstrappedEnumeration, enumeration);
            bootstrappedEnumeration.insert();

            for (EnumerationLiteral enumerationLiteral : enumeration.getEnumerationLiterals())
            {
                klass.model.meta.domain.EnumerationLiteral bootstrappedEnumerationLiteral = new klass.model.meta.domain.EnumerationLiteral();
                KlassBootstrapWriter.handleNamedElement(bootstrappedEnumerationLiteral, enumerationLiteral);
                enumerationLiteral.getDeclaredPrettyName().ifPresent(bootstrappedEnumerationLiteral::setPrettyName);
                bootstrappedEnumerationLiteral.setEnumeration(bootstrappedEnumeration);
                bootstrappedEnumerationLiteral.insert();
            }
        }

        for (Interface anInterface : this.domainModel.getInterfaces())
        {
            klass.model.meta.domain.Interface bootstrappedInterface = new klass.model.meta.domain.Interface();
            KlassBootstrapWriter.handlePackageableElement(bootstrappedInterface, anInterface);
            // TODO: Report Reladomo bug. If any non-nullable properties are not set on a transient object, insert() ought to throw but doesn't
            bootstrappedInterface.insert();

            this.handleSuperInterfaces(anInterface);
            this.handleClassifierModifiers(anInterface);
            this.handleDataTypeProperties(anInterface);
        }

        for (Klass klass : this.domainModel.getClasses())
        {
            klass.model.meta.domain.Klass bootstrappedClass = new klass.model.meta.domain.Klass();
            KlassBootstrapWriter.handlePackageableElement(bootstrappedClass, klass);
            // TODO: Report Reladomo bug. If any non-nullable properties are not set on a transient object, insert() ought to throw but doesn't
            bootstrappedClass.setInheritanceType(klass.getInheritanceType().getPrettyName());
            bootstrappedClass.insert();

            klass.getSuperClass()
                    .map(NamedElement::getName)
                    .ifPresent(bootstrappedClass::setSuperClassName);

            this.handleSuperInterfaces(klass);
            this.handleClassifierModifiers(klass);
            this.handleDataTypeProperties(klass);
        }

        for (Association association : this.domainModel.getAssociations())
        {
            klass.model.meta.domain.Criteria bootstrappedCriteria = BootstrapCriteriaVisitor.convert(association.getCriteria());

            klass.model.meta.domain.Association bootstrappedAssociation = new klass.model.meta.domain.Association();
            KlassBootstrapWriter.handlePackageableElement(bootstrappedAssociation, association);
            bootstrappedAssociation.setCriteria(bootstrappedCriteria);
            bootstrappedAssociation.insert();

            AssociationEnd sourceAssociationEnd = association.getSourceAssociationEnd();
            AssociationEnd targetAssociationEnd = association.getTargetAssociationEnd();

            this.bootstrapAssociationEnd(sourceAssociationEnd, "source");
            this.bootstrapAssociationEnd(targetAssociationEnd, "target");
        }

        // TODO: Bootstrapped meta-model of projections and services
        // for (ServiceGroup serviceGroup: this.domainModel.getServiceGroups())
        // {
        // }
    }

    private void handleDataTypeProperties(Classifier classifier)
    {
        for (DataTypeProperty dataTypeProperty : classifier.getDeclaredDataTypeProperties())
        {
            if (dataTypeProperty instanceof PrimitiveProperty)
            {
                PrimitiveProperty primitiveProperty = (PrimitiveProperty) dataTypeProperty;

                klass.model.meta.domain.PrimitiveProperty bootstrappedPrimitiveProperty = new klass.model.meta.domain.PrimitiveProperty();
                this.handleDataTypeProperty(classifier, dataTypeProperty, bootstrappedPrimitiveProperty);
                bootstrappedPrimitiveProperty.setId(dataTypeProperty.isID());
                bootstrappedPrimitiveProperty.setPrimitiveType(primitiveProperty.getType().getPrettyName());
                bootstrappedPrimitiveProperty.insert();

                this.handlePropertyModifiers(classifier, dataTypeProperty);
                this.handleValidations(classifier, dataTypeProperty);
            }
            else if (dataTypeProperty instanceof EnumerationProperty)
            {
                EnumerationProperty enumerationProperty = (EnumerationProperty) dataTypeProperty;

                klass.model.meta.domain.EnumerationProperty bootstrappedEnumerationProperty = new klass.model.meta.domain.EnumerationProperty();
                this.handleDataTypeProperty(classifier, dataTypeProperty, bootstrappedEnumerationProperty);
                bootstrappedEnumerationProperty.setEnumerationName(enumerationProperty.getType().getName());
                bootstrappedEnumerationProperty.insert();

                this.handlePropertyModifiers(classifier, dataTypeProperty);
                this.handleValidations(classifier, dataTypeProperty);

                // TODO: dataTypeProperty.getMinLengthPropertyValidation();
                // TODO: dataTypeProperty.getMaxLengthPropertyValidation();
                // TODO: dataTypeProperty.getMinPropertyValidation();
                // TODO: dataTypeProperty.getMaxPropertyValidation();
            }
            else
            {
                throw new AssertionError();
            }
        }
    }

    private void handleDataTypeProperty(
            Classifier classifier,
            DataTypeProperty dataTypeProperty,
            klass.model.meta.domain.DataTypeProperty bootstrappedDataTypeProperty)
    {
        KlassBootstrapWriter.handleNamedElement(bootstrappedDataTypeProperty, dataTypeProperty);
        bootstrappedDataTypeProperty.setClassifierName(classifier.getName());
        bootstrappedDataTypeProperty.setKey(dataTypeProperty.isKey());
        bootstrappedDataTypeProperty.setOptional(dataTypeProperty.isOptional());
    }

    private void handlePropertyModifiers(Classifier classifier, DataTypeProperty dataTypeProperty)
    {
        for (PropertyModifier propertyModifier : dataTypeProperty.getPropertyModifiers())
        {
            klass.model.meta.domain.PropertyModifier bootstrappedPropertyModifier = new klass.model.meta.domain.PropertyModifier();
            KlassBootstrapWriter.handleNamedElement(bootstrappedPropertyModifier, propertyModifier);
            bootstrappedPropertyModifier.setClassifierName(classifier.getName());
            bootstrappedPropertyModifier.setPropertyName(dataTypeProperty.getName());
            bootstrappedPropertyModifier.insert();
        }
    }

    private void handleValidations(Classifier classifier, DataTypeProperty property)
    {
        property.getMinLengthPropertyValidation()
                .ifPresent(this.getValidationHandler(classifier, property, MinLengthPropertyValidation::new));

        property.getMaxLengthPropertyValidation()
                .ifPresent(this.getValidationHandler(classifier, property, MaxLengthPropertyValidation::new));

        property.getMinPropertyValidation()
                .ifPresent(this.getValidationHandler(classifier, property, MinPropertyValidation::new));

        property.getMaxPropertyValidation()
                .ifPresent(this.getValidationHandler(classifier, property, MaxPropertyValidation::new));
    }

    @Nonnull
    private Consumer<NumericPropertyValidation> getValidationHandler(
            Classifier classifier,
            DataTypeProperty dataTypeProperty,
            Supplier<klass.model.meta.domain.NumericPropertyValidation> bootstrappedValidationSupplier)
    {
        return validation ->
        {
            this.handleValidation(classifier, dataTypeProperty, bootstrappedValidationSupplier.get(), validation);
        };
    }

    protected void handleValidation(
            Classifier classifier,
            DataTypeProperty dataTypeProperty,
            klass.model.meta.domain.NumericPropertyValidation boostrappedValidation,
            NumericPropertyValidation validation)
    {
        // TODO: Fix reladomo bug causing abstract classes to not implement interfaces
        boostrappedValidation.setInferred(validation.isInferred());
        boostrappedValidation.setSourceCode(validation.getSourceCode());
        boostrappedValidation.setSourceCodeWithInference(validation.getSourceCodeWithInference());
        boostrappedValidation.setClassifierName(classifier.getName());
        boostrappedValidation.setPropertyName(dataTypeProperty.getName());
        boostrappedValidation.setNumber(validation.getNumber());
        boostrappedValidation.insert();
    }

    private void handleClassifierModifiers(Classifier classifier)
    {
        for (ClassModifier classModifier : classifier.getClassModifiers())
        {
            klass.model.meta.domain.ClassifierModifier bootstrappedClassModifier = new klass.model.meta.domain.ClassifierModifier();
            KlassBootstrapWriter.handleNamedElement(bootstrappedClassModifier, classModifier);
            bootstrappedClassModifier.setClassifierName(classifier.getName());
            bootstrappedClassModifier.insert();
        }
    }

    private void handleSuperInterfaces(Classifier classifier)
    {
        for (Interface superInterface : classifier.getInterfaces())
        {
            ClassifierInterfaceMapping classifierInterfaceMapping = new ClassifierInterfaceMapping();
            classifierInterfaceMapping.setClassifierName(classifier.getName());
            classifierInterfaceMapping.setInterfaceName(superInterface.getName());
            classifierInterfaceMapping.insert();
        }
    }

    private void bootstrapAssociationEnd(AssociationEnd associationEnd, String direction)
    {
        klass.model.meta.domain.AssociationEnd bootstrappedAssociationEnd = new klass.model.meta.domain.AssociationEnd();
        KlassBootstrapWriter.handleNamedElement(bootstrappedAssociationEnd, associationEnd);
        bootstrappedAssociationEnd.setOwningClassName(associationEnd.getOwningClassifier().getName());
        bootstrappedAssociationEnd.setAssociationName(associationEnd.getOwningAssociation().getName());
        bootstrappedAssociationEnd.setDirection(direction);
        bootstrappedAssociationEnd.setMultiplicity(associationEnd.getMultiplicity().getPrettyName());
        bootstrappedAssociationEnd.setResultTypeName(associationEnd.getType().getName());
        bootstrappedAssociationEnd.insert();

        for (AssociationEndModifier associationEndModifier : associationEnd.getAssociationEndModifiers())
        {
            klass.model.meta.domain.AssociationEndModifier bootstrappedAssociationEndModifier = new klass.model.meta.domain.AssociationEndModifier();
            bootstrappedAssociationEndModifier.setOwningClassName(associationEnd.getOwningClassifier().getName());
            bootstrappedAssociationEndModifier.setAssociationEndName(associationEnd.getName());
            KlassBootstrapWriter.handleNamedElement(bootstrappedAssociationEndModifier, associationEndModifier);
            bootstrappedAssociationEndModifier.insert();
        }

        associationEnd.getOrderBy().ifPresent(orderBy -> bootstrapAssociationEndOrderBy(associationEnd, orderBy));
    }

    private void bootstrapAssociationEndOrderBy(
            AssociationEnd associationEnd,
            OrderBy orderBy)
    {
        for (OrderByMemberReferencePath orderByMemberReferencePath : orderBy.getOrderByMemberReferencePaths())
        {
            ThisMemberReferencePath thisMemberReferencePath = orderByMemberReferencePath.getThisMemberReferencePath();

            klass.model.meta.domain.ThisMemberReferencePath bootstrappedThisMemberReferencePath =
                    this.bootstrapThisMemberReferencePath(thisMemberReferencePath);

            AssociationEndOrderBy associationEndOrderBy = new AssociationEndOrderBy();
            associationEndOrderBy.setAssociationEndClassName(associationEnd.getOwningClassifier().getName());
            associationEndOrderBy.setAssociationEndName(associationEnd.getName());
            associationEndOrderBy.setThisMemberReferencePathId(bootstrappedThisMemberReferencePath.getId());
            associationEndOrderBy.setOrderByDirection(orderByMemberReferencePath.getOrderByDirectionDeclaration().getOrderByDirection().name());
            associationEndOrderBy.insert();
        }
    }

    @Nonnull
    private klass.model.meta.domain.ThisMemberReferencePath bootstrapThisMemberReferencePath(ThisMemberReferencePath thisMemberReferencePath)
    {
        klass.model.meta.domain.ThisMemberReferencePath bootstrappedThisMemberReferencePath = new klass.model.meta.domain.ThisMemberReferencePath();
        bootstrappedThisMemberReferencePath.setClassName(thisMemberReferencePath.getKlass().getName());
        bootstrappedThisMemberReferencePath.setPropertyClassName(thisMemberReferencePath.getProperty().getOwningClassifier().getName());
        bootstrappedThisMemberReferencePath.setPropertyName(thisMemberReferencePath.getProperty().getName());
        if (thisMemberReferencePath.getAssociationEnds().notEmpty())
        {
            throw new AssertionError("TODO");
        }
        bootstrappedThisMemberReferencePath.insert();
        return bootstrappedThisMemberReferencePath;
    }

    public static void handleElement(ElementAbstract bootstrappedElement, Element element)
    {
        bootstrappedElement.setInferred(element.isInferred());
        bootstrappedElement.setSourceCode(element.getSourceCode());
        bootstrappedElement.setSourceCodeWithInference(element.getSourceCodeWithInference());
    }

    public static void handleNamedElement(
            NamedElementAbstract bootstrappedNamedElement,
            NamedElement namedElement)
    {
        KlassBootstrapWriter.handleElement(bootstrappedNamedElement, namedElement);
        bootstrappedNamedElement.setName(namedElement.getName());
        bootstrappedNamedElement.setOrdinal(namedElement.getOrdinal());
    }

    public static void handlePackageableElement(
            PackageableElementAbstract bootstrappedPackageableElement,
            PackageableElement packageableElement)
    {
        KlassBootstrapWriter.handleNamedElement(bootstrappedPackageableElement, packageableElement);
        bootstrappedPackageableElement.setPackageName(packageableElement.getPackageName());
    }
}
