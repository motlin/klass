package cool.klass.model.converter.bootstrap.writer;

import com.gs.fw.common.mithra.MithraManagerProvider;
import cool.klass.model.meta.domain.Association;
import cool.klass.model.meta.domain.ClassModifier;
import cool.klass.model.meta.domain.DomainModel;
import cool.klass.model.meta.domain.Enumeration;
import cool.klass.model.meta.domain.EnumerationLiteral;
import cool.klass.model.meta.domain.Klass;
import cool.klass.model.meta.domain.property.AssociationEnd;
import cool.klass.model.meta.domain.property.AssociationEndModifier;
import cool.klass.model.meta.domain.property.DataTypeProperty;
import cool.klass.model.meta.domain.property.EnumerationProperty;
import cool.klass.model.meta.domain.property.PrimitiveProperty;
import cool.klass.model.meta.domain.property.PropertyModifier;
import klass.model.meta.domain.EnumerationPropertyModifier;
import klass.model.meta.domain.PrimitivePropertyModifier;

public class KlassBootstrapWriter
{
    private final DomainModel domainModel;

    public KlassBootstrapWriter(DomainModel domainModel)
    {
        this.domainModel = domainModel;
    }

    public void bootstrapMetaModel()
    {
        MithraManagerProvider.getMithraManager().executeTransactionalCommand(tx ->
        {
            this.bootstrapMetaModelInTransaction();
            return null;
        });
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
            bootstrappedEnumeration.insert();

            for (EnumerationLiteral enumerationLiteral : enumeration.getEnumerationLiterals())
            {
                klass.model.meta.domain.EnumerationLiteral bootstrappedEnumerationLiteral = new klass.model.meta.domain.EnumerationLiteral();
                bootstrappedEnumerationLiteral.setName(enumerationLiteral.getName());
                bootstrappedEnumerationLiteral.setInferred(enumerationLiteral.isInferred());
                bootstrappedEnumerationLiteral.setOrdinal(enumerationLiteral.getOrdinal());
                bootstrappedEnumerationLiteral.setPrettyName(enumerationLiteral.getPrettyName());
                bootstrappedEnumerationLiteral.setEnumeration(bootstrappedEnumeration);
                bootstrappedEnumerationLiteral.insert();
            }
        }

        for (Klass klass : this.domainModel.getKlasses())
        {
            klass.model.meta.domain.Class bootstrappedClass = new klass.model.meta.domain.Class();
            bootstrappedClass.setName(klass.getName());
            bootstrappedClass.setInferred(klass.isInferred());
            bootstrappedClass.setOrdinal(klass.getOrdinal());
            bootstrappedClass.setPackageName(klass.getPackageName());
            bootstrappedClass.insert();

            for (ClassModifier classModifier : klass.getClassModifiers())
            {
                klass.model.meta.domain.ClassModifier bootstrappedClassModifier = new klass.model.meta.domain.ClassModifier();
                bootstrappedClassModifier.setClassName(klass.getName());
                bootstrappedClassModifier.setInferred(classModifier.isInferred());
                bootstrappedClassModifier.setName(classModifier.getName());
                bootstrappedClassModifier.setOrdinal(classModifier.getOrdinal());
                bootstrappedClassModifier.insert();
            }

            for (DataTypeProperty<?> dataTypeProperty : klass.getDataTypeProperties())
            {
                if (dataTypeProperty instanceof PrimitiveProperty)
                {
                    PrimitiveProperty primitiveProperty = (PrimitiveProperty) dataTypeProperty;

                    klass.model.meta.domain.PrimitiveProperty bootstrappedPrimitiveProperty = new klass.model.meta.domain.PrimitiveProperty();
                    bootstrappedPrimitiveProperty.setName(primitiveProperty.getName());
                    bootstrappedPrimitiveProperty.setInferred(primitiveProperty.isInferred());
                    bootstrappedPrimitiveProperty.setOrdinal(primitiveProperty.getOrdinal());
                    bootstrappedPrimitiveProperty.setPrimitiveType(primitiveProperty.getType().getName());
                    bootstrappedPrimitiveProperty.setClassName(klass.getName());
                    bootstrappedPrimitiveProperty.setKey(primitiveProperty.isKey());
                    bootstrappedPrimitiveProperty.setId(primitiveProperty.isID());
                    bootstrappedPrimitiveProperty.setOptional(primitiveProperty.isOptional());
                    bootstrappedPrimitiveProperty.insert();

                    for (PropertyModifier propertyModifier : dataTypeProperty.getPropertyModifiers())
                    {
                        PrimitivePropertyModifier primitivePropertyModifier = new PrimitivePropertyModifier();
                        primitivePropertyModifier.setClassName(klass.getName());
                        primitivePropertyModifier.setPropertyName(primitiveProperty.getName());
                        primitivePropertyModifier.setName(propertyModifier.getName());
                        primitivePropertyModifier.setInferred(propertyModifier.isInferred());
                        primitivePropertyModifier.setOrdinal(propertyModifier.getOrdinal());
                        primitivePropertyModifier.insert();
                    }
                }
                else if (dataTypeProperty instanceof EnumerationProperty)
                {
                    EnumerationProperty enumerationProperty = (EnumerationProperty) dataTypeProperty;

                    klass.model.meta.domain.EnumerationProperty bootstrappedEnumerationProperty = new klass.model.meta.domain.EnumerationProperty();
                    bootstrappedEnumerationProperty.setName(enumerationProperty.getName());
                    bootstrappedEnumerationProperty.setInferred(enumerationProperty.isInferred());
                    bootstrappedEnumerationProperty.setOrdinal(enumerationProperty.getOrdinal());
                    bootstrappedEnumerationProperty.setEnumerationName(enumerationProperty.getType().getName());
                    bootstrappedEnumerationProperty.setClassName(klass.getName());
                    bootstrappedEnumerationProperty.setKey(enumerationProperty.isKey());
                    bootstrappedEnumerationProperty.setOptional(enumerationProperty.isOptional());
                    bootstrappedEnumerationProperty.insert();

                    for (PropertyModifier propertyModifier : dataTypeProperty.getPropertyModifiers())
                    {
                        EnumerationPropertyModifier enumerationPropertyModifier = new EnumerationPropertyModifier();
                        enumerationPropertyModifier.setClassName(klass.getName());
                        enumerationPropertyModifier.setPropertyName(enumerationProperty.getName());
                        enumerationPropertyModifier.setName(propertyModifier.getName());
                        enumerationPropertyModifier.setInferred(propertyModifier.isInferred());
                        enumerationPropertyModifier.setOrdinal(propertyModifier.getOrdinal());
                        enumerationPropertyModifier.insert();
                    }
                }
                else
                {
                    throw new AssertionError();
                }
            }
        }

        for (Association association : this.domainModel.getAssociations())
        {
            klass.model.meta.domain.Association bootstrappedAssociation = new klass.model.meta.domain.Association();
            bootstrappedAssociation.setName(association.getName());
            bootstrappedAssociation.setInferred(association.isInferred());
            bootstrappedAssociation.setOrdinal(association.getOrdinal());
            bootstrappedAssociation.setPackageName(association.getPackageName());
            bootstrappedAssociation.insert();

            AssociationEnd sourceAssociationEnd = association.getSourceAssociationEnd();
            AssociationEnd targetAssociationEnd = association.getTargetAssociationEnd();

            this.bootstrapAssociationEnd(sourceAssociationEnd, "SOURCE");
            this.bootstrapAssociationEnd(targetAssociationEnd, "TARGET");
        }
    }

    private void bootstrapAssociationEnd(AssociationEnd associationEnd, String direction)
    {
        klass.model.meta.domain.AssociationEnd bootstrappedAssociationEnd = new klass.model.meta.domain.AssociationEnd();
        bootstrappedAssociationEnd.setOwningClassName(associationEnd.getOwningKlass().getName());
        bootstrappedAssociationEnd.setName(associationEnd.getName());
        bootstrappedAssociationEnd.setInferred(associationEnd.isInferred());
        bootstrappedAssociationEnd.setOrdinal(associationEnd.getOrdinal());
        bootstrappedAssociationEnd.setAssociationName(associationEnd.getOwningAssociation().getName());
        bootstrappedAssociationEnd.setDirection(direction);
        bootstrappedAssociationEnd.setMultiplicity(associationEnd.getMultiplicity().name());
        bootstrappedAssociationEnd.setResultTypeName(associationEnd.getType().getName());
        bootstrappedAssociationEnd.insert();

        for (AssociationEndModifier associationEndModifier : associationEnd.getAssociationEndModifiers())
        {
            klass.model.meta.domain.AssociationEndModifier bootstrappedAssociationEndModifier = new klass.model.meta.domain.AssociationEndModifier();
            bootstrappedAssociationEndModifier.setOwningClassName(associationEnd.getOwningKlass().getName());
            bootstrappedAssociationEndModifier.setAssociationEndName(associationEnd.getName());
            bootstrappedAssociationEndModifier.setName(associationEndModifier.getName());
            bootstrappedAssociationEndModifier.setInferred(associationEndModifier.isInferred());
            bootstrappedAssociationEndModifier.setOrdinal(associationEndModifier.getOrdinal());
            bootstrappedAssociationEndModifier.insert();
        }
    }
}
