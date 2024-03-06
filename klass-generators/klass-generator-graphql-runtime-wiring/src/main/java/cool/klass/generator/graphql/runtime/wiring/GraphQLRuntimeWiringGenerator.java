package cool.klass.generator.graphql.runtime.wiring;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.PackageableElement;
import cool.klass.model.meta.domain.api.property.Property;
import cool.klass.model.meta.domain.api.property.ReferenceProperty;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;

public class GraphQLRuntimeWiringGenerator
{
    @Nonnull
    private final DomainModel domainModel;

    public GraphQLRuntimeWiringGenerator(@Nonnull DomainModel domainModel)
    {
        this.domainModel = Objects.requireNonNull(domainModel);
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

        ImmutableList<Klass> associatedTypes = klass
                .getProperties()
                .selectInstancesOf(ReferenceProperty.class)
                .collect(ReferenceProperty::getType);

        ImmutableList<Klass> importTypes = Lists.immutable.with(klass).newWithAll(associatedTypes);
        ImmutableList<String> imports    = importTypes.collect(this::getImport);

        // @formatter:off
        //language=JAVA
        String sourceCode = ""
                + "package " + klass.getPackageName() + ".graphql.type.runtime.wiring;\n"
                + "\n"
                + imports.makeString("")
                + "import cool.klass.graphql.type.runtime.wiring.provider.GraphQLTypeRuntimeWiringProvider;\n"
                + "import io.liftwizard.reladomo.graphql.deep.fetcher.GraphQLPropertyDataDeepFetcher;\n"
                + "import io.liftwizard.reladomo.graphql.data.fetcher.*;\n"
                + "import graphql.schema.PropertyDataFetcher;\n"
                + "import graphql.schema.idl.TypeRuntimeWiring.Builder;\n"
                + "\n"
                + "/**\n"
                + " * Auto-generated by {@link cool.klass.generator.graphql.runtime.wiring.GraphQLRuntimeWiringGenerator}\n"
                + " */\n"
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

    private String getImport(Klass klass)
    {
        String fullyQualifiedName = klass.getFullyQualifiedName();
        return "import " + fullyQualifiedName + ";\n"
                + "import " + fullyQualifiedName + "Finder;\n";
    }

    private String getDataFetcherSourceCode(@Nonnull Property property, Classifier owningClassifier)
    {
        DataFetcherSourceCodePropertyVisitor visitor = new DataFetcherSourceCodePropertyVisitor(
                owningClassifier,
                property);
        property.visit(visitor);

        return String.format(
                "        builder.dataFetcher(\"%s\", %s);\n",
                property.getName(),
                visitor.getSourceCode());
    }
}
