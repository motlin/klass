package cool.klass.generator.klass.projection;

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
        return KlassProjectionSourceCodeGenerator.getPackageSourceCode(this.domainModel, fullyQualifiedPackage);
    }
}
