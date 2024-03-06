package klass.model.meta.domain.dropwizard.application;

import java.util.ServiceLoader;

import javax.annotation.Nonnull;

import com.gs.fw.common.mithra.MithraManagerProvider;
import cool.klass.model.meta.domain.Association;
import cool.klass.model.meta.domain.Enumeration;
import cool.klass.model.meta.domain.EnumerationLiteral;
import cool.klass.model.meta.domain.Klass;
import cool.klass.model.meta.domain.property.AssociationEnd;
import cool.klass.model.meta.domain.property.DataTypeProperty;
import cool.klass.model.meta.domain.property.EnumerationProperty;
import cool.klass.model.meta.domain.property.PrimitiveProperty;
import io.dropwizard.Bundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import klass.model.meta.domain.Class;

public class BootstrappedMetaModelApplication extends AbstractBootstrappedMetaModelApplication
{
    public static void main(String[] args) throws Exception
    {
        new BootstrappedMetaModelApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<BootstrappedMetaModelConfiguration> bootstrap)
    {
        super.initialize(bootstrap);

        ServiceLoader<Bundle> bundleServiceLoader = ServiceLoader.load(Bundle.class);
        for (Bundle bundle : bundleServiceLoader)
        {
            bootstrap.addBundle(bundle);
        }

        // TODO: application initialization
    }

    @Override
    public void run(
            BootstrappedMetaModelConfiguration configuration,
            @Nonnull Environment environment)
    {
        super.run(configuration, environment);

        MithraManagerProvider.getMithraManager().executeTransactionalCommand(tx ->
        {
            this.bootstrapMetaModel();
            return null;
        });
    }

    private void bootstrapMetaModel()
    {
        for (Enumeration enumeration : this.domainModel.getEnumerations())
        {
            String enumerationName = enumeration.getName();
            String packageName     = enumeration.getPackageName();

            klass.model.meta.domain.Enumeration bootstrappedEnumeration = new klass.model.meta.domain.Enumeration();
            bootstrappedEnumeration.setName(enumerationName);
            bootstrappedEnumeration.setPackageName(packageName);
            bootstrappedEnumeration.insert();

            for (EnumerationLiteral enumerationLiteral : enumeration.getEnumerationLiterals())
            {
                String name       = enumerationLiteral.getName();
                String prettyName = enumerationLiteral.getPrettyName();

                klass.model.meta.domain.EnumerationLiteral bootstrappedEnumerationLiteral = new klass.model.meta.domain.EnumerationLiteral();
                bootstrappedEnumerationLiteral.setName(name);
                bootstrappedEnumerationLiteral.setPrettyName(prettyName);
                bootstrappedEnumerationLiteral.setEnumeration(bootstrappedEnumeration);
                bootstrappedEnumerationLiteral.insert();
            }
        }

        for (Klass klass : this.domainModel.getKlasses())
        {
            Class bootstrappedClass = new Class();
            bootstrappedClass.setName(klass.getName());
            bootstrappedClass.setPackageName(klass.getPackageName());
            bootstrappedClass.insert();

            for (DataTypeProperty<?> dataTypeProperty : klass.getDataTypeProperties())
            {
                if (dataTypeProperty instanceof PrimitiveProperty)
                {
                    PrimitiveProperty primitiveProperty = (PrimitiveProperty) dataTypeProperty;

                    klass.model.meta.domain.PrimitiveProperty bootstrappedPrimitiveProperty = new klass.model.meta.domain.PrimitiveProperty();
                    bootstrappedPrimitiveProperty.setName(primitiveProperty.getName());
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
        boostrappedAssociationEnd.setAssociationName(associationEnd.getOwningAssociation().getName());
        boostrappedAssociationEnd.setDirection(direction);
        boostrappedAssociationEnd.setMultiplicity(associationEnd.getMultiplicity().name());
        boostrappedAssociationEnd.setResultTypeName(associationEnd.getType().getName());
        boostrappedAssociationEnd.insert();
    }
}
