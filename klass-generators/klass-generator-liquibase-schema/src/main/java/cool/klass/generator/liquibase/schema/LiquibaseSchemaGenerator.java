package cool.klass.generator.liquibase.schema;

import java.nio.file.Path;
import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.generator.perpackage.AbstractPerPackageGenerator;
import cool.klass.model.meta.domain.api.DomainModel;

public class LiquibaseSchemaGenerator
        extends AbstractPerPackageGenerator
{
    private final String fileName;

    public LiquibaseSchemaGenerator(DomainModel domainModel)
    {
        this(domainModel, "migrations.xml");
    }

    public LiquibaseSchemaGenerator(DomainModel domainModel, String fileName)
    {
        super(domainModel);
        this.fileName = Objects.requireNonNull(fileName);
    }

    @Nonnull
    @Override
    protected Path getPluginRelativePath(Path path)
    {
        return path.resolve("liquibase").resolve("schema");
    }

    @Override
    @Nonnull
    protected String getFileName()
    {
        return this.fileName;
    }

    @Override
    @Nonnull
    protected String getPackageSourceCode(@Nonnull String fullyQualifiedPackage)
    {
        return SchemaGenerator.getSourceCode(this.domainModel, fullyQualifiedPackage);
    }
}
