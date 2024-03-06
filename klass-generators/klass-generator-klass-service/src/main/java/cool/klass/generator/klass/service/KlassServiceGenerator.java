package cool.klass.generator.klass.service;

import java.nio.file.Path;

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

    @Nonnull
    @Override
    protected Path getPluginRelativePath(Path path)
    {
        return path
                .resolve("klass")
                .resolve("service");
    }

    @Override
    @Nonnull
    protected String getFileName()
    {
        return "generated-services.klass";
    }

    @Override
    @Nonnull
    protected String getPackageSourceCode(@Nonnull String fullyQualifiedPackage)
    {
        return KlassServiceSourceCodeGenerator.getPackageSourceCode(this.domainModel, fullyQualifiedPackage);
    }
}
