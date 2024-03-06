package cool.klass.generator.graphql.data.fetcher;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.PackageableElement;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import org.eclipse.collections.api.list.ImmutableList;

public class GraphQLDataFetcherGenerator
{
    @Nonnull
    private final DomainModel domainModel;
    @Nonnull
    private final String      rootPackageName;

    public GraphQLDataFetcherGenerator(
            @Nonnull DomainModel domainModel,
            @Nonnull String rootPackageName)
    {
        this.domainModel     = Objects.requireNonNull(domainModel);
        this.rootPackageName = Objects.requireNonNull(rootPackageName);
    }

    public void writeDataFetcherFiles(@Nonnull Path outputPath)
    {
        ImmutableList<Klass> classes = this.domainModel.getClasses();

        classes.forEachWith(this::writeAllDataFetcherFile, outputPath);
        classes.forEachWith(this::writeDataFetcherByKeyFile, outputPath);
    }

    private void writeAllDataFetcherFile(@Nonnull Klass klass, @Nonnull Path outputPath)
    {
        Path   dataFetcherOutputPath = this.getAllDataFetcherOutputPath(outputPath, klass);
        String classSourceCode       = this.getAllDataFetcherSourceCode(klass);
        this.printStringToFile(dataFetcherOutputPath, classSourceCode);
    }

    private void writeDataFetcherByKeyFile(@Nonnull Klass klass, @Nonnull Path outputPath)
    {
        Path   dataFetcherOutputPath = this.getDataFetcherByKeyOutputPath(outputPath, klass);
        String classSourceCode       = this.getDataFetcherByKeySourceCode(klass);
        this.printStringToFile(dataFetcherOutputPath, classSourceCode);
    }

    @Nonnull
    public Path getAllDataFetcherOutputPath(
            @Nonnull Path outputPath,
            @Nonnull PackageableElement packageableElement)
    {
        String packageRelativePath = packageableElement.getPackageName()
                .replaceAll("\\.", "/");
        Path dataFetcherDirectory = outputPath
                .resolve(packageRelativePath)
                .resolve("graphql")
                .resolve("data")
                .resolve("fetcher")
                .resolve("all");
        dataFetcherDirectory.toFile().mkdirs();
        String fileName = "All" + packageableElement.getName() + "DataFetcher.java";
        return dataFetcherDirectory.resolve(fileName);
    }

    @Nonnull
    public Path getDataFetcherByKeyOutputPath(
            @Nonnull Path outputPath,
            @Nonnull PackageableElement packageableElement)
    {
        String packageRelativePath = packageableElement.getPackageName()
                .replaceAll("\\.", "/");
        Path dataFetcherDirectory = outputPath
                .resolve(packageRelativePath)
                .resolve("graphql")
                .resolve("data")
                .resolve("fetcher")
                .resolve("key");
        dataFetcherDirectory.toFile().mkdirs();
        String fileName = packageableElement.getName() + "ByKeyDataFetcher.java";
        return dataFetcherDirectory.resolve(fileName);
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
    public String getAllDataFetcherSourceCode(@Nonnull Klass klass)
    {
        // @formatter:off
        //language=JAVA
        String sourceCode = ""
                + "package " + this.rootPackageName + ".graphql.data.fetcher.all;\n"
                + "\n"
                + "import java.util.Objects;\n"
                + "\n"
                + "import com.codahale.metrics.annotation.ExceptionMetered;\n"
                + "import com.codahale.metrics.annotation.Metered;\n"
                + "import com.codahale.metrics.annotation.Timed;\n"
                + "import com.gs.fw.finder.DomainList;\n"
                + "import cool.klass.data.store.DataStore;\n"
                + "import cool.klass.data.store.reladomo.ReladomoDataStore;\n"
                + "import cool.klass.model.meta.domain.api.DomainModel;\n"
                + "import cool.klass.model.meta.domain.api.Klass;\n"
                + "import cool.klass.model.reladomo.tree.RootReladomoTreeNode;\n"
                + "import cool.klass.model.reladomo.tree.converter.graphql.ReladomoTreeGraphqlConverter;\n"
                + "import cool.klass.reladomo.tree.deep.fetcher.ReladomoTreeNodeDeepFetcherListener;\n"
                + "import " + klass.getFullyQualifiedName() + "Finder;\n"
                + "import " + klass.getFullyQualifiedName() + "List;\n"
                + "import graphql.schema.DataFetcher;\n"
                + "import graphql.schema.DataFetchingEnvironment;\n"
                + "import graphql.schema.DataFetchingFieldSelectionSet;\n"
                + "\n"
                + "/**\n"
                + " * Auto-generated by {@link " + this.getClass().getCanonicalName() + "}\n"
                + " */\n"
                + "public class All" + klass.getName() + "DataFetcher\n"
                + "        implements DataFetcher<" + klass.getName() + "List>\n"
                + "{\n"
                + "    private final DomainModel domainModel;\n"
                + "    private final DataStore   dataStore;\n"
                + "\n"
                + "    public All" + klass.getName() + "DataFetcher(DomainModel domainModel, DataStore dataStore)\n"
                + "    {\n"
                + "        this.domainModel = Objects.requireNonNull(domainModel);\n"
                + "        this.dataStore   = Objects.requireNonNull(dataStore);\n"
                + "    }\n"
                + "\n"
                + "    @Timed\n"
                + "    @ExceptionMetered\n"
                + "    @Metered\n"
                + "    @Override\n"
                + "    public " + klass.getName() + "List get(DataFetchingEnvironment environment)\n"
                + "    {\n"
                + "        " + klass.getName() + "List result = " + klass.getName() + "Finder.findMany(" + klass.getName() + "Finder.all());\n"
                + "\n"
                + "        Klass klass = this.domainModel.getClassByName(\"" + klass.getName() + "\");\n"
                + "\n"
                + "        var deepFetcher = new ReladomoTreeNodeDeepFetcherListener(\n"
                + "                (ReladomoDataStore) this.dataStore,\n"
                + "                (DomainList) result,\n"
                + "                klass);\n"
                + "        var treeGraphqlConverter = new ReladomoTreeGraphqlConverter(this.domainModel);\n"
                + "        RootReladomoTreeNode rootReladomoTreeNode = treeGraphqlConverter.convert(\n"
                + "                klass,\n"
                + "                environment.getSelectionSet());\n"
                + "\n"
                + "        deepFetcher.enterRoot(rootReladomoTreeNode);\n"
                + "        return result;\n"
                + "    }\n"
                + "}\n";
        // @formatter:on

        return sourceCode;
    }

    @Nonnull
    public String getDataFetcherByKeySourceCode(@Nonnull Klass klass)
    {
        String argumentsSourceCode = klass
                .getKeyProperties()
                .collect(this::getArgumentSourceCode)
                .makeString("");

        String operationSourceCode = klass
                .getKeyProperties()
                .collectWith(this::getOperationSourceCode, klass)
                .makeString("", "\n", ";\n");

        // @formatter:off
        //language=JAVA
        String sourceCode = ""
                + "package " + this.rootPackageName + ".graphql.data.fetcher.key;\n"
                + "\n"
                + "import java.sql.*;\n"
                + "import java.time.*;\n"
                + "import java.util.Objects;\n"
                + "\n"
                + "import com.codahale.metrics.annotation.ExceptionMetered;\n"
                + "import com.codahale.metrics.annotation.Metered;\n"
                + "import com.codahale.metrics.annotation.Timed;\n"
                + "import com.gs.fw.common.mithra.finder.Operation;\n"
                + "import com.gs.fw.finder.DomainList;\n"
                + "import cool.klass.data.store.DataStore;\n"
                + "import cool.klass.data.store.reladomo.ReladomoDataStore;\n"
                + "import cool.klass.model.meta.domain.api.DomainModel;\n"
                + "import cool.klass.model.reladomo.tree.RootReladomoTreeNode;\n"
                + "import cool.klass.model.reladomo.tree.converter.graphql.ReladomoTreeGraphqlConverter;\n"
                + "import cool.klass.reladomo.tree.deep.fetcher.ReladomoTreeNodeDeepFetcherListener;\n"
                + "import " + klass.getFullyQualifiedName() + ";\n"
                + "import " + klass.getFullyQualifiedName() + "Finder;\n"
                + "import " + klass.getFullyQualifiedName() + "List;\n"
                + "import graphql.schema.DataFetcher;\n"
                + "import graphql.schema.DataFetchingEnvironment;\n"
                + "import graphql.schema.DataFetchingFieldSelectionSet;\n"
                + "\n"
                + "/**\n"
                + " * Auto-generated by {@link " + this.getClass().getCanonicalName() + "}\n"
                + " */\n"
                + "public class " + klass.getName() + "ByKeyDataFetcher\n"
                + "        implements DataFetcher<" + klass.getName() + ">\n"
                + "{\n"
                + "    private final DomainModel domainModel;\n"
                + "    private final DataStore   dataStore;\n"
                + "\n"
                + "    public " + klass.getName() + "ByKeyDataFetcher(DomainModel domainModel, DataStore dataStore)\n"
                + "    {\n"
                + "        this.domainModel = Objects.requireNonNull(domainModel);\n"
                + "        this.dataStore   = Objects.requireNonNull(dataStore);\n"
                + "    }\n"
                + "\n"
                + "    @Timed\n"
                + "    @ExceptionMetered\n"
                + "    @Metered\n"
                + "    @Override\n"
                + "    public " + klass.getName() + " get(DataFetchingEnvironment environment)\n"
                + "    {\n"
                + argumentsSourceCode
                + "\n"
                + "        Operation operation = " + klass.getName() + "Finder.all()\n"
                + operationSourceCode
                + "\n"
                + "        " + klass.getName() + "List result = " + klass.getName() + "Finder.findMany(operation);\n"
                + "\n"
                + "        DataFetchingFieldSelectionSet selectionSet = environment.getSelectionSet();\n"
                + "\n"
                // var here is to avoid ambiguity with bootstrapped Klass in KlassByKeyDataFetcher.
                + "        var klass                = this.domainModel.getClassByName(\"" + klass.getName() + "\");\n"
                + "        var treeGraphqlConverter = new ReladomoTreeGraphqlConverter(this.domainModel);\n"
                + "\n"
                + "        var deepFetcher = new ReladomoTreeNodeDeepFetcherListener(\n"
                + "                (ReladomoDataStore) this.dataStore,\n"
                + "                (DomainList) result,\n"
                + "                klass);\n"
                + "        RootReladomoTreeNode rootReladomoTreeNode = treeGraphqlConverter.convert(\n"
                + "                klass,\n"
                + "                environment.getSelectionSet());\n"
                + "\n"
                + "        deepFetcher.enterRoot(rootReladomoTreeNode);\n"
                + "\n"
                + "        return result.asEcList().getOnly();\n"
                + "    }\n"
                + "}\n";
        // @formatter:on

        return sourceCode;
    }

    private String getArgumentSourceCode(@Nonnull DataTypeProperty dataTypeProperty)
    {
        var visitor = new GraphQLScalarDataTypePropertyVisitor();
        dataTypeProperty.visit(visitor);
        String scalarTypeSourceCode = visitor.getSourceCode();
        String propertyName         = dataTypeProperty.getName();
        return String.format(
                "        %1$-20s %2$-20s = environment.getArgument(\"%2$s\");\n",
                scalarTypeSourceCode,
                propertyName);
    }

    private String getOperationSourceCode(
            @Nonnull DataTypeProperty dataTypeProperty,
            @Nonnull Klass klass)
    {
        var visitor = new GraphQLCoercedScalarDataTypePropertyVisitor(dataTypeProperty.getName());
        dataTypeProperty.visit(visitor);
        String coercedScalarSourceCode = visitor.getSourceCode();

        return String.format(
                "                .and(%1$sFinder.%2$s().eq(%3$s))",
                klass.getName(),
                dataTypeProperty.getName(),
                coercedScalarSourceCode);
    }
}
