package cool.klass.generator.klass.projection;

import java.nio.file.Path;

import javax.annotation.Nonnull;

import cool.klass.generator.perpackage.AbstractPerPackageGenerator;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.PackageableElement;
import org.eclipse.collections.api.list.ImmutableList;

public class KlassProjectionGenerator
        extends AbstractPerPackageGenerator
{
    public KlassProjectionGenerator(@Nonnull DomainModel domainModel)
    {
        super(domainModel);
    }

    // Overridden to process all Classifiers instead of Classes
    @Override
    protected ImmutableList<String> getPackageNames()
    {
        return this.domainModel
                .getClassifiers()
                .asLazy()
                .collect(PackageableElement::getPackageName)
                .distinct()
                .toImmutableList();
    }

    @Nonnull
    @Override
    protected Path getPluginRelativePath(Path path)
    {
        return path
                .resolve("klass")
                .resolve("projection");
    }

    @Override
    @Nonnull
    protected String getFileName()
    {
        return "generated-projections.klass";
    }

    @Override
    @Nonnull
    protected String getPackageSourceCode(@Nonnull String fullyQualifiedPackage)
    {
        return KlassProjectionSourceCodeGenerator.getPackageSourceCode(this.domainModel, fullyQualifiedPackage);
    }
}
