package cool.klass.generator.grahql.fragment;

import java.nio.file.Path;

import javax.annotation.Nonnull;

import cool.klass.generator.perpackage.AbstractPerPackageGenerator;
import cool.klass.model.meta.domain.api.DomainModel;

public class GraphQLFragmentGenerator
        extends AbstractPerPackageGenerator
{
    public GraphQLFragmentGenerator(@Nonnull DomainModel domainModel)
    {
        super(domainModel);
    }

    @Nonnull
    @Override
    protected Path getPluginRelativePath(Path path)
    {
        return path
                .resolve("graphql")
                .resolve("fragment");
    }

    @Nonnull
    @Override
    protected String getFileName()
    {
        return "GraphQLFragment.graphqls";
    }

    @Nonnull
    @Override
    protected String getPackageSourceCode(@Nonnull String fullyQualifiedPackage)
    {
        return GraphQLFragmentSourceCodeGenerator.getPackageSourceCode(this.domainModel, fullyQualifiedPackage);
    }
}
