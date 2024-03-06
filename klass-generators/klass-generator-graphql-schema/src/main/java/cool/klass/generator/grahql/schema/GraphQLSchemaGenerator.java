package cool.klass.generator.grahql.schema;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.graphql.schema.writer.KlassGraphQLModelConverter;
import cool.klass.model.graphql.domain.GraphQLDomainModel;
import cool.klass.model.graphql.domain.GraphQLElement;
import cool.klass.model.meta.domain.api.DomainModel;

public class GraphQLSchemaGenerator
{
    @Nonnull
    private final DomainModel domainModel;
    @Nonnull
    private final String      rootPackageName;
    @Nonnull
    private final String      applicationName;
    @Nonnull
    private final Instant     now;

    public GraphQLSchemaGenerator(
            @Nonnull DomainModel domainModel,
            @Nonnull String rootPackageName,
            @Nonnull String applicationName,
            @Nonnull Instant now)
    {
        this.domainModel     = Objects.requireNonNull(domainModel);
        this.rootPackageName = Objects.requireNonNull(rootPackageName);
        this.applicationName = Objects.requireNonNull(applicationName);
        this.now             = Objects.requireNonNull(now);
    }

    public void writeSchemaFiles(@Nonnull Path outputPath)
    {
        KlassGraphQLModelConverter klassGraphQLModelConverter = new KlassGraphQLModelConverter(this.domainModel);
        GraphQLDomainModel         graphQLDomainModel         = klassGraphQLModelConverter.convert();

        String sourceCode = ""
                + "# Generated at " + this.now + "\n"
                + "# by cool.klass.generator.grahql.schema.GraphQLSchemaGenerator\n"
                + "\n"
                + graphQLDomainModel.getTopLevelElements()
                .collect(this::getSourceCode)
                .makeString("");

        Path schemaOutputPath = this.getOutputPath(outputPath);
        this.printStringToFile(schemaOutputPath, sourceCode);
    }

    @Nonnull
    private Path getOutputPath(@Nonnull Path outputPath)
    {
        String packageRelativePath = this.rootPackageName.replaceAll("\\.", "/");
        Path outputDirectory = outputPath
                .resolve(packageRelativePath)
                .resolve("graphql")
                .resolve("schema");
        outputDirectory.toFile().mkdirs();
        String fileName = this.applicationName + ".graphqls";
        return outputDirectory.resolve(fileName);
    }

    private String getSourceCode(@Nonnull GraphQLElement graphQLElement)
    {
        GraphQLElementToSchemaSourceVisitor visitor = new GraphQLElementToSchemaSourceVisitor();
        graphQLElement.visit(visitor);
        return visitor.getSourceCode();
    }

    private void printStringToFile(@Nonnull Path path, String contents)
    {
        try (PrintStream printStream = new PrintStream(new FileOutputStream(path.toFile())))
        {
            printStream.print(contents);
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }
}
