package cool.klass.generator.klass.service;

import javax.annotation.Nonnull;

import cool.klass.generator.perpackage.AbstractPerPackageGenerator;
import cool.klass.model.meta.domain.api.DomainModel;

public class KlassServiceGenerator
        extends AbstractPerPackageGenerator
{
    public KlassServiceGenerator(@Nonnull DomainModel domainModel)
    {
        super(domainModel);
    }

    @Override
    @Nonnull
    protected String getFileName(@Nonnull String fullyQualifiedPackage)
    {
        return fullyQualifiedPackage + ".klass";
    }

    @Override
    @Nonnull
    protected String getPackageSourceCode(@Nonnull String fullyQualifiedPackage)
    {
        return KlassServiceSourceCodeGenerator.getPackageSourceCode(this.domainModel, fullyQualifiedPackage);
    }
}
