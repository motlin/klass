package cool.klass.model.converter.bootstrap.writer;

import java.util.Objects;

import cool.klass.data.store.DataStore;
import cool.klass.model.meta.domain.api.Association;
import cool.klass.model.meta.domain.api.ClassModifier;
import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.Enumeration;
import cool.klass.model.meta.domain.api.EnumerationLiteral;
import cool.klass.model.meta.domain.api.Interface;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.NamedElement;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.AssociationEndModifier;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.property.EnumerationProperty;
import cool.klass.model.meta.domain.api.property.PrimitiveProperty;
import cool.klass.model.meta.domain.api.property.PropertyModifier;
import klass.model.meta.domain.ClassifierInterfaceMapping;

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
            bootstrappedEnumeration.setName(enumeration.getName());
            bootstrappedEnumeration.setInferred(enumeration.isInferred());
            bootstrappedEnumeration.setOrdinal(enumeration.getOrdinal());
            bootstrappedEnumeration.setPackageName(enumeration.getPackageName());
            bootstrappedEnumeration.setSourceCode(enumeration.getSourceCode());
            bootstrappedEnumeration.setSourceCodeWithInference(enumeration.getSourceCodeWithInference());
            bootstrappedEnumeration.insert();

            for (EnumerationLiteral enumerationLiteral : enumeration.getEnumerationLiterals())
            {
                klass.model.meta.domain.EnumerationLiteral bootstrappedEnumerationLiteral = new klass.model.meta.domain.EnumerationLiteral();
                bootstrappedEnumerationLiteral.setName(enumerationLiteral.getName());
                bootstrappedEnumerationLiteral.setInferred(enumerationLiteral.isInferred());
                enumerationLiteral.getDeclaredPrettyName().ifPresent(bootstrappedEnumerationLiteral::setPrettyName);
                bootstrappedEnumerationLiteral.setOrdinal(enumerationLiteral.getOrdinal());
                bootstrappedEnumerationLiteral.setEnumeration(bootstrappedEnumeration);
                bootstrappedEnumerationLiteral.setSourceCode(enumerationLiteral.getSourceCode());
                bootstrappedEnumerationLiteral.setSourceCodeWithInference(enumerationLiteral.getSourceCodeWithInference());
                bootstrappedEnumerationLiteral.insert();
            }
        }

        for (Interface anInterface : this.domainModel.getInterfaces())
        {
            klass.model.meta.domain.Interface bootstrappedInterface = new klass.model.meta.domain.Interface();
            this.handleClassifier(bootstrappedInterface, anInterface);
            // TODO: Report Reladomo bug. If any non-nullable properties are not set on a transient object, insert() ought to throw but doesn't
            bootstrappedInterface.insert();

            this.handleSuperInterfaces(anInterface);
            this.handleClassifierModifiers(anInterface);
            this.handleDataTypeProperties(anInterface);
        }

        for (Klass klass : this.domainModel.getKlasses())
        {
            klass.model.meta.domain.Klass bootstrappedClass = new klass.model.meta.domain.Klass();
            this.handleClassifier(bootstrappedClass, klass);
            // TODO: Report Reladomo bug. If any non-nullable properties are not set on a transient object, insert() ought to throw but doesn't
            bootstrappedClass.insert();

            bootstrappedClass.setInheritanceType(klass.getInheritanceType().getPrettyName());

            klass.getSuperClass()
                    .map(NamedElement::getName)
                    .ifPresent(bootstrappedClass::setSuperClassName);

            this.handleSuperInterfaces(klass);
            this.handleClassifierModifiers(klass);
            this.handleDataTypeProperties(klass);
        }

        for (Association association : this.domainModel.getAssociations())
        {
            klass.model.meta.domain.Association bootstrappedAssociation = new klass.model.meta.domain.Association();
            bootstrappedAssociation.setName(association.getName());
            bootstrappedAssociation.setInferred(association.isInferred());
            bootstrappedAssociation.setOrdinal(association.getOrdinal());
            bootstrappedAssociation.setPackageName(association.getPackageName());
            bootstrappedAssociation.setSourceCode(association.getSourceCode());
            bootstrappedAssociation.setSourceCodeWithInference(association.getSourceCodeWithInference());
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
        for (DataTypeProperty dataTypeProperty : classifier.getDataTypeProperties())
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
            }
            else if (dataTypeProperty instanceof EnumerationProperty)
            {
                EnumerationProperty enumerationProperty = (EnumerationProperty) dataTypeProperty;

                klass.model.meta.domain.EnumerationProperty bootstrappedEnumerationProperty = new klass.model.meta.domain.EnumerationProperty();
                this.handleDataTypeProperty(classifier, dataTypeProperty, bootstrappedEnumerationProperty);
                bootstrappedEnumerationProperty.setEnumerationName(enumerationProperty.getType().getName());
                bootstrappedEnumerationProperty.insert();

                this.handlePropertyModifiers(classifier, dataTypeProperty);

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
        bootstrappedDataTypeProperty.setName(dataTypeProperty.getName());
        bootstrappedDataTypeProperty.setInferred(dataTypeProperty.isInferred());
        bootstrappedDataTypeProperty.setOrdinal(dataTypeProperty.getOrdinal());
        bootstrappedDataTypeProperty.setClassifierName(classifier.getName());
        bootstrappedDataTypeProperty.setKey(dataTypeProperty.isKey());
        bootstrappedDataTypeProperty.setOptional(dataTypeProperty.isOptional());
        bootstrappedDataTypeProperty.setSourceCode(dataTypeProperty.getSourceCode());
        bootstrappedDataTypeProperty.setSourceCodeWithInference(dataTypeProperty.getSourceCodeWithInference());
    }

    private void handlePropertyModifiers(Classifier classifier, DataTypeProperty dataTypeProperty)
    {
        for (PropertyModifier propertyModifier : dataTypeProperty.getPropertyModifiers())
        {
            klass.model.meta.domain.PropertyModifier bootstrappedPropertyModifier = new klass.model.meta.domain.PropertyModifier();
            bootstrappedPropertyModifier.setClassifierName(classifier.getName());
            bootstrappedPropertyModifier.setPropertyName(dataTypeProperty.getName());
            bootstrappedPropertyModifier.setName(propertyModifier.getName());
            bootstrappedPropertyModifier.setInferred(propertyModifier.isInferred());
            bootstrappedPropertyModifier.setOrdinal(propertyModifier.getOrdinal());
            bootstrappedPropertyModifier.insert();
        }
    }

    private void handleClassifierModifiers(Classifier classifier)
    {
        for (ClassModifier classModifier : classifier.getClassModifiers())
        {
            klass.model.meta.domain.ClassifierModifier bootstrappedClassModifier = new klass.model.meta.domain.ClassifierModifier();
            bootstrappedClassModifier.setClassifierName(classifier.getName());
            bootstrappedClassModifier.setInferred(classModifier.isInferred());
            bootstrappedClassModifier.setName(classModifier.getName());
            bootstrappedClassModifier.setOrdinal(classModifier.getOrdinal());
            bootstrappedClassModifier.insert();
        }
    }

    private void handleClassifier(
            klass.model.meta.domain.Classifier bootstrappedClassifier,
            Classifier classifier)
    {
        bootstrappedClassifier.setName(classifier.getName());
        bootstrappedClassifier.setInferred(classifier.isInferred());
        bootstrappedClassifier.setOrdinal(classifier.getOrdinal());
        bootstrappedClassifier.setPackageName(classifier.getPackageName());
        bootstrappedClassifier.setSourceCode(classifier.getSourceCode());
        bootstrappedClassifier.setSourceCodeWithInference(classifier.getSourceCodeWithInference());
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
        bootstrappedAssociationEnd.setOwningClassName(associationEnd.getOwningClassifier().getName());
        bootstrappedAssociationEnd.setName(associationEnd.getName());
        bootstrappedAssociationEnd.setInferred(associationEnd.isInferred());
        bootstrappedAssociationEnd.setOrdinal(associationEnd.getOrdinal());
        bootstrappedAssociationEnd.setAssociationName(associationEnd.getOwningAssociation().getName());
        bootstrappedAssociationEnd.setDirection(direction);
        bootstrappedAssociationEnd.setMultiplicity(associationEnd.getMultiplicity().getPrettyName());
        bootstrappedAssociationEnd.setResultTypeName(associationEnd.getType().getName());
        bootstrappedAssociationEnd.setSourceCode(associationEnd.getSourceCode());
        bootstrappedAssociationEnd.setSourceCodeWithInference(associationEnd.getSourceCodeWithInference());
        bootstrappedAssociationEnd.insert();

        for (AssociationEndModifier associationEndModifier : associationEnd.getAssociationEndModifiers())
        {
            klass.model.meta.domain.AssociationEndModifier bootstrappedAssociationEndModifier = new klass.model.meta.domain.AssociationEndModifier();
            bootstrappedAssociationEndModifier.setOwningClassName(associationEnd.getOwningClassifier().getName());
            bootstrappedAssociationEndModifier.setAssociationEndName(associationEnd.getName());
            bootstrappedAssociationEndModifier.setName(associationEndModifier.getName());
            bootstrappedAssociationEndModifier.setInferred(associationEndModifier.isInferred());
            bootstrappedAssociationEndModifier.setOrdinal(associationEndModifier.getOrdinal());
            bootstrappedAssociationEndModifier.insert();
        }
    }
}
