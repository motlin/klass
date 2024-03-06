package cool.klass.generator.graphql.runtime.wiring.query;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Objects;

import javax.annotation.Nonnull;

import com.google.common.base.CaseFormat;
import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.Klass;
import org.atteo.evo.inflector.English;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.list.fixed.ArrayAdapter;

public class GraphQLQueryRuntimeWiringGenerator
{
    @Nonnull
    private final DomainModel domainModel;
    @Nonnull
    private final String      rootPackageName;
    @Nonnull
    private final String      applicationName;

    public GraphQLQueryRuntimeWiringGenerator(
            @Nonnull DomainModel domainModel,
            @Nonnull String rootPackageName,
            @Nonnull String applicationName)
    {
        this.domainModel     = Objects.requireNonNull(domainModel);
        this.rootPackageName = Objects.requireNonNull(rootPackageName);
        this.applicationName = Objects.requireNonNull(applicationName);
    }

    public void writeQueryRuntimeWiringFile(@Nonnull Path outputPath)
    {
        //language=JAVA
        String sourceCode = ""
                + "package " + this.rootPackageName + ".graphql.runtime.wiring.query;\n"
                + "\n"
                + "import cool.klass.graphql.type.runtime.wiring.provider.GraphQLTypeRuntimeWiringProvider;\n"
                + "import " + this.rootPackageName + ".graphql.data.fetcher.all.*;\n"
                + "import " + this.rootPackageName + ".graphql.data.fetcher.key.*;\n"
                + "import graphql.schema.idl.TypeRuntimeWiring.Builder;\n"
                + "\n"
                + "/**\n"
                + " * Auto-generated by {@link cool.klass.generator.graphql.runtime.wiring.query.GraphQLQueryRuntimeWiringGenerator}\n"
                + " */\n"
                + "public class " + this.applicationName + "QueryTypeRuntimeWiringProvider\n"
                + "        implements GraphQLTypeRuntimeWiringProvider\n"
                + "{\n"
                + "    @Override\n"
                + "    public Builder get()\n"
                + "    {\n"
                + "        Builder builder = new Builder();\n"
                + "        builder.typeName(\"Query\");\n"
                + "\n"
                + this.getAllDataFetchersSourceCode().makeString("")
                + "\n"
                + this.getByTagDataFetchersSourceCode().makeString("")
                + "\n"
                + "        return builder;\n"
                + "    }\n"
                + "}\n";

        Path querySchemaOutputPath = this.getOutputPath(outputPath);
        this.printStringToFile(querySchemaOutputPath, sourceCode);
    }

    private ImmutableList<String> getAllDataFetchersSourceCode()
    {
        return this.domainModel.getClasses().reject(Klass::isAbstract).collect(this::getAllDataFetcherSourceCode);
    }

    private String getAllDataFetcherSourceCode(Classifier classifier)
    {
        return String.format(
                "        builder.dataFetcher(\"%s\", new All%sDataFetcher());\n",
                this.getPropertyName(classifier),
                classifier.getName());
    }

    private String getPropertyName(Classifier classifier)
    {
        String classifierName = classifier.getName();

        String              lowerUnderscore = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, classifierName);
        MutableList<String> splits          = ArrayAdapter.adapt(lowerUnderscore.split("_"));

        return splits
                .collectWithIndex((eachSplit, index) -> this.capitalizeSplit(eachSplit, index, splits.size()))
                .makeString("");
    }

    private String capitalizeSplit(String eachSplit, int index, int splitsSize)
    {
        return this.getCapitalized(index, this.getPluralized(index, splitsSize, eachSplit));
    }

    private String getPluralized(int index, int splitsSize, String eachSplit)
    {
        return index == splitsSize - 1 ? English.plural(eachSplit) : eachSplit;
    }

    private String getCapitalized(int index, String eachSplit)
    {
        return index != 0
                ? CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, eachSplit)
                : eachSplit;
    }

    private ImmutableList<String> getByTagDataFetchersSourceCode()
    {
        return this.domainModel.getClasses().reject(Klass::isAbstract).collect(this::getByTagDataFetcherSourceCode);
    }

    private String getByTagDataFetcherSourceCode(Classifier classifier)
    {
        String classifierName = classifier.getName();
        return String.format(
                "        builder.dataFetcher(\"%s\", new %sByKeyDataFetcher());\n",
                CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, classifierName),
                classifierName);
    }

    @Nonnull
    private Path getOutputPath(@Nonnull Path outputPath)
    {
        String packageRelativePath = this.rootPackageName.replaceAll("\\.", "/");
        Path outputDirectory = outputPath
                .resolve(packageRelativePath)
                .resolve("graphql")
                .resolve("runtime")
                .resolve("wiring")
                .resolve("query");
        outputDirectory.toFile().mkdirs();
        String fileName = this.applicationName + "QueryTypeRuntimeWiringProvider.java";
        return outputDirectory.resolve(fileName);
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
