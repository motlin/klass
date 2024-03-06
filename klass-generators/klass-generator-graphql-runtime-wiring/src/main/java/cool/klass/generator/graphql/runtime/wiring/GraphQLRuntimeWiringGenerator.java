package cool.klass.generator.graphql.runtime.wiring;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.PackageableElement;
import cool.klass.model.meta.domain.api.property.Property;

public class GraphQLRuntimeWiringGenerator
{
    @Nonnull
    private final DomainModel domainModel;
    @Nonnull
    private final String      rootPackageName;
    @Nonnull
    private final Instant     now;

    public GraphQLRuntimeWiringGenerator(
            @Nonnull DomainModel domainModel,
            String rootPackageName,
            @Nonnull Instant now)
    {
        this.domainModel = Objects.requireNonNull(domainModel);
        this.rootPackageName = Objects.requireNonNull(rootPackageName);
        this.now = Objects.requireNonNull(now);
    }

    public void writeTypeRuntimeWiringFiles(@Nonnull Path outputPath)
    {
        this.domainModel
                .getClasses()
                .reject(Klass::isAbstract)
                .forEachWith(this::writeTypeRuntimeWiringFile, outputPath);
    }

    private void writeTypeRuntimeWiringFile(@Nonnull Klass klass, @Nonnull Path outputPath)
    {
        Path   runtimeWiringOutputPath = this.getRuntimeWiringOutputPath(outputPath, klass);
        String classSourceCode         = this.getTypeRuntimeWiringSourceCode(klass);
        this.printStringToFile(runtimeWiringOutputPath, classSourceCode);
    }

    @Nonnull
    private Path getRuntimeWiringOutputPath(
            @Nonnull Path outputPath,
            @Nonnull PackageableElement packageableElement)
    {
        String packageRelativePath = packageableElement.getPackageName()
                .replaceAll("\\.", "/");
        Path runtimeWiringDirectory = outputPath
                .resolve(packageRelativePath)
                .resolve("graphql")
                .resolve("type")
                .resolve("runtime")
                .resolve("wiring");
        runtimeWiringDirectory.toFile().mkdirs();
        String fileName = packageableElement.getName() + "TypeRuntimeWiringProvider.java";
        return runtimeWiringDirectory.resolve(fileName);
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

    @Nonnull
    private String getTypeRuntimeWiringSourceCode(@Nonnull Klass klass)
    {
        String dataFetchersSourceCode = klass
                .getProperties()
                .collectWith(this::getDataFetcherSourceCode, klass)
                .makeString("");

        // @formatter:off
        //language=JAVA
        String sourceCode = ""
                + "package " + klass.getPackageName() + ".graphql.type.runtime.wiring;\n"
                + "\n"
                + "import javax.annotation.Generated;\n"
                + "\n"
                + "import " + this.rootPackageName + ".*;\n"
                + "import cool.klass.graphql.type.runtime.wiring.provider.GraphQLTypeRuntimeWiringProvider;\n"
                + "import cool.klass.reladomo.graphql.data.fetcher.*;\n"
                + "import graphql.schema.PropertyDataFetcher;\n"
                + "import graphql.schema.idl.TypeRuntimeWiring.Builder;\n"
                + "\n"
                + "/**\n"
                + " * Auto-generated by {@link cool.klass.generator.graphql.runtime.wiring.GraphQLRuntimeWiringGenerator}\n"
                + " */\n"
                + "@Generated(\n"
                + "        value = \"cool.klass.generator.graphql.runtime.wiring.GraphQLRuntimeWiringGenerator\",\n"
                + "        date = \"" + this.now + "\")\n"
                + "public class " + klass.getName() + "TypeRuntimeWiringProvider implements GraphQLTypeRuntimeWiringProvider\n"
                + "{\n"
                + "    @Override\n"
                + "    public Builder get()\n"
                + "    {\n"
                + "        Builder builder = new Builder();\n"
                + "        builder.typeName(\"" + klass.getName() + "\");\n"
                + dataFetchersSourceCode
                + "        return builder;\n"
                + "    }\n"
                + "}\n";
        // @formatter:on

        return sourceCode;
    }

    private String getDataFetcherSourceCode(@Nonnull Property property, Classifier owningClassifier)
    {
        DataFetcherSourceCodePropertyVisitor visitor = new DataFetcherSourceCodePropertyVisitor(owningClassifier, property);
        property.visit(visitor);

        return String.format(
                "        builder.dataFetcher(\"%s\", %s);\n",
                property.getName(),
                visitor.getSourceCode());
    }
}
