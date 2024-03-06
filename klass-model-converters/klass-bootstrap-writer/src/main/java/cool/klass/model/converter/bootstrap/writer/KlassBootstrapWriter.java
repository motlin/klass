package cool.klass.model.converter.bootstrap.writer;

import com.gs.fw.common.mithra.MithraManagerProvider;
import cool.klass.model.meta.domain.Association;
import cool.klass.model.meta.domain.DomainModel;
import cool.klass.model.meta.domain.Enumeration;
import cool.klass.model.meta.domain.EnumerationLiteral;
import cool.klass.model.meta.domain.Klass;
import cool.klass.model.meta.domain.property.AssociationEnd;
import cool.klass.model.meta.domain.property.DataTypeProperty;
import cool.klass.model.meta.domain.property.EnumerationProperty;
import cool.klass.model.meta.domain.property.PrimitiveProperty;

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
            bootstrappedEnumeration.setOrdinal(enumeration.getOrdinal());
            bootstrappedEnumeration.setPackageName(enumeration.getPackageName());
            bootstrappedEnumeration.insert();

            for (EnumerationLiteral enumerationLiteral : enumeration.getEnumerationLiterals())
            {
                klass.model.meta.domain.EnumerationLiteral bootstrappedEnumerationLiteral = new klass.model.meta.domain.EnumerationLiteral();
                bootstrappedEnumerationLiteral.setName(enumerationLiteral.getName());
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
            bootstrappedClass.setOrdinal(klass.getOrdinal());
            bootstrappedClass.setPackageName(klass.getPackageName());
            bootstrappedClass.insert();

            for (DataTypeProperty<?> dataTypeProperty : klass.getDataTypeProperties())
            {
                if (dataTypeProperty instanceof PrimitiveProperty)
                {
                    PrimitiveProperty primitiveProperty = (PrimitiveProperty) dataTypeProperty;

                    klass.model.meta.domain.PrimitiveProperty bootstrappedPrimitiveProperty = new klass.model.meta.domain.PrimitiveProperty();
                    bootstrappedPrimitiveProperty.setName(primitiveProperty.getName());
                    bootstrappedPrimitiveProperty.setOrdinal(primitiveProperty.getOrdinal());
                    bootstrappedPrimitiveProperty.setPrimitiveType(primitiveProperty.getType().getName());
                    bootstrappedPrimitiveProperty.setClassName(klass.getName());
                    bootstrappedPrimitiveProperty.setKey(primitiveProperty.isKey());
                    bootstrappedPrimitiveProperty.setId(primitiveProperty.isID());
                    bootstrappedPrimitiveProperty.setOptional(primitiveProperty.isOptional());
                    bootstrappedPrimitiveProperty.insert();
                }
                else if (dataTypeProperty instanceof EnumerationProperty)
                {
                    EnumerationProperty enumerationProperty = (EnumerationProperty) dataTypeProperty;

                    klass.model.meta.domain.EnumerationProperty bootstrappedEnumerationProperty = new klass.model.meta.domain.EnumerationProperty();
                    bootstrappedEnumerationProperty.setName(enumerationProperty.getName());
                    bootstrappedEnumerationProperty.setOrdinal(enumerationProperty.getOrdinal());
                    bootstrappedEnumerationProperty.setEnumerationName(enumerationProperty.getType().getName());
                    bootstrappedEnumerationProperty.setClassName(klass.getName());
                    bootstrappedEnumerationProperty.setKey(enumerationProperty.isKey());
                    bootstrappedEnumerationProperty.setOptional(enumerationProperty.isOptional());
                    bootstrappedEnumerationProperty.insert();
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
        klass.model.meta.domain.AssociationEnd boostrappedAssociationEnd = new klass.model.meta.domain.AssociationEnd();
        boostrappedAssociationEnd.setOwningClassName(associationEnd.getOwningKlass().getName());
        boostrappedAssociationEnd.setName(associationEnd.getName());
        boostrappedAssociationEnd.setOrdinal(associationEnd.getOrdinal());
        boostrappedAssociationEnd.setAssociationName(associationEnd.getOwningAssociation().getName());
        boostrappedAssociationEnd.setDirection(direction);
        boostrappedAssociationEnd.setMultiplicity(associationEnd.getMultiplicity().name());
        boostrappedAssociationEnd.setResultTypeName(associationEnd.getType().getName());
        boostrappedAssociationEnd.insert();
    }
}
