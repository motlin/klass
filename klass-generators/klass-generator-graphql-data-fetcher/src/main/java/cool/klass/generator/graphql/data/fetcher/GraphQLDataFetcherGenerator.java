package cool.klass.generator.graphql.data.fetcher;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.PackageableElement;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.property.EnumerationProperty;
import cool.klass.model.meta.domain.api.property.ParameterizedProperty;
import cool.klass.model.meta.domain.api.property.PrimitiveProperty;
import cool.klass.model.meta.domain.api.property.PropertyVisitor;
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
        ImmutableList<Klass> concreteClasses = this.domainModel
                .getClasses()
                .reject(Klass::isAbstract);

        concreteClasses.forEachWith(this::writeAllDataFetcherFile, outputPath);
        concreteClasses.forEachWith(this::writeDataFetcherByKeyFile, outputPath);
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
                + "\n"
                + "import " + klass.getPackageName() + "." + klass.getName() + "Finder;\n"
                + "import " + klass.getPackageName() + "." + klass.getName() + "List;\n"
                + "import graphql.schema.DataFetcher;\n"
                + "import graphql.schema.DataFetchingEnvironment;\n"
                + "\n"
                + "/**\n"
                + " * Auto-generated by {@link cool.klass.generator.graphql.data.fetcher.GraphQLDataFetcherGenerator}\n"
                + " */\n"
                + "public class All" + klass.getName() + "DataFetcher implements DataFetcher<" + klass.getName() + "List>\n"
                + "{\n"
                + "    @Override\n"
                + "    public " + klass.getName() + "List get(DataFetchingEnvironment environment)\n"
                + "    {\n"
                + "        return " + klass.getName() + "Finder.findMany(" + klass.getName() + "Finder.all());\n"
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
                + "\n"
                + "import com.gs.fw.common.mithra.finder.Operation;\n"
                + "import " + klass.getPackageName() + "." + klass.getName() + ";\n"
                + "import " + klass.getPackageName() + "." + klass.getName() + "Finder;\n"
                + "import graphql.schema.DataFetcher;\n"
                + "import graphql.schema.DataFetchingEnvironment;\n"
                + "\n"
                + "/**\n"
                + " * Auto-generated by {@link cool.klass.generator.graphql.data.fetcher.GraphQLDataFetcherGenerator}\n"
                + " */\n"
                + "public class " + klass.getName() + "ByKeyDataFetcher implements DataFetcher<" + klass.getName() + ">\n"
                + "{\n"
                + "    @Override\n"
                + "    public " + klass.getName() + " get(DataFetchingEnvironment environment)\n"
                + "    {\n"
                + argumentsSourceCode
                + "        Operation operation = " + klass.getName() + "Finder.all()\n"
                + operationSourceCode
                + "        return " + klass.getName() + "Finder.findOne(operation);\n"
                + "    }\n"
                + "}\n";
        // @formatter:on

        return sourceCode;
    }

    private String getArgumentSourceCode(@Nonnull DataTypeProperty dataTypeProperty)
    {
        GraphQLScalarDataTypePropertyVisitor visitor = new GraphQLScalarDataTypePropertyVisitor();
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
        return String.format(
                "                .and(%1$sFinder.%2$s().eq(%2$s))",
                klass.getName(),
                dataTypeProperty.getName());
    }

    private static class GraphQLScalarDataTypePropertyVisitor implements PropertyVisitor
    {
        private String sourceCode;

        public String getSourceCode()
        {
            return this.sourceCode;
        }

        @Override
        public void visitPrimitiveProperty(@Nonnull PrimitiveProperty primitiveProperty)
        {
            this.sourceCode = primitiveProperty.getType().getJavaClass().getSimpleName();
        }

        @Override
        public void visitEnumerationProperty(EnumerationProperty enumerationProperty)
        {
            this.sourceCode = "String";
        }

        @Override
        public void visitAssociationEnd(AssociationEnd associationEnd)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".visitAssociationEnd() not implemented yet");
        }

        @Override
        public void visitParameterizedProperty(ParameterizedProperty parameterizedProperty)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".visitParameterizedProperty() not implemented yet");
        }
    }
}
