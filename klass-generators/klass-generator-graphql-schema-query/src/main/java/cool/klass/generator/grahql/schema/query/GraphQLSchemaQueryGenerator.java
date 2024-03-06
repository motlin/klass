package cool.klass.generator.grahql.schema.query;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Objects;

import javax.annotation.Nonnull;

import com.google.common.base.CaseFormat;
import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.Enumeration;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.domain.api.Type;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import org.atteo.evo.inflector.English;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.list.fixed.ArrayAdapter;

public class GraphQLSchemaQueryGenerator
{
    @Nonnull
    private final DomainModel domainModel;
    @Nonnull
    private final String      rootPackageName;
    @Nonnull
    private final String      applicationName;

    public GraphQLSchemaQueryGenerator(
            @Nonnull DomainModel domainModel,
            @Nonnull String rootPackageName,
            @Nonnull String applicationName)
    {
        this.domainModel     = Objects.requireNonNull(domainModel);
        this.rootPackageName = Objects.requireNonNull(rootPackageName);
        this.applicationName = Objects.requireNonNull(applicationName);
    }

    public void writeQueryFile(@Nonnull Path outputPath)
    {
        String allSourceCode = this.domainModel
                .getClassifiers()
                .collect(this::getAllSourceCode)
                .makeString("");

        String byKeySourceCode = this.domainModel
                .getClassifiers()
                .collect(this::getByKeySourceCode)
                .makeString("");

        String byOperationSourceCode = this.domainModel
                .getClassifiers()
                .collect(this::getByOperationSourceCode)
                .makeString("");

        String byFinderSourceCode = this.domainModel
                .getClassifiers()
                .collect(this::getByFinderSourceCode)
                .makeString("");

        String sourceCode = ""
                + "# Auto-generated by " + this.getClass().getCanonicalName() + "\n"
                + "\n"
                + "type Query {\n"
                + allSourceCode
                + "\n"
                + byKeySourceCode
                + "\n"
                + byOperationSourceCode
                + "\n"
                + byFinderSourceCode
                + "}\n"
                + "\n";

        Path schemaOutputPath = this.getOutputPath(outputPath);
        this.printStringToFile(schemaOutputPath, sourceCode);
    }

    private String getAllSourceCode(Classifier classifier)
    {
        return "    " + this.getPropertyName(classifier) + ": [" + classifier.getName() + "!]!\n";
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

    private String getByKeySourceCode(Classifier classifier)
    {
        String classifierName = classifier.getName();
        String lowerCaseName  = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, classifierName);

        String parameters = classifier.getDataTypeProperties()
                .select(DataTypeProperty::isKey)
                .collect(this::getParameterSourceCode)
                .makeString();
        return "    " + lowerCaseName + "(" + parameters + "): " + classifierName + "\n";
    }

    private String getByOperationSourceCode(Classifier classifier)
    {
        String classifierName = classifier.getName();
        String lowerCaseName  = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, classifierName);
        return "    " + lowerCaseName + "ByOperation(operation: String!, limit: Int): [" + classifierName + "!]!\n";
    }

    private String getByFinderSourceCode(Classifier classifier)
    {
        String classifierName = classifier.getName();
        String lowerCaseName  = CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, classifierName);

        return MessageFormat.format(
                "    {0}ByFinder(operation: {1}Finder!, limit: Int): [{1}!]!\n",
                lowerCaseName,
                classifierName);
    }

    private String getParameterSourceCode(DataTypeProperty dataTypeProperty)
    {
        String propertyName = dataTypeProperty.getName();
        String scalarType   = GraphQLSchemaQueryGenerator.convertType(dataTypeProperty.getType());
        String multiplicity = dataTypeProperty.isRequired() ? "!" : "";
        return String.format("%s: %s%s", propertyName, scalarType, multiplicity);
    }

    private static String convertType(@Nonnull Type type)
    {
        if (type instanceof Enumeration)
        {
            return "String";
        }
        if (type == PrimitiveType.INTEGER || type == PrimitiveType.LONG)
        {
            return "Int";
        }
        if (type == PrimitiveType.DOUBLE)
        {
            return "Float";
        }
        return type.toString();
    }

    @Nonnull
    private Path getOutputPath(@Nonnull Path outputPath)
    {
        String packageRelativePath = this.rootPackageName.replaceAll("\\.", "/");
        Path outputDirectory = outputPath
                .resolve(packageRelativePath)
                .resolve("graphql")
                .resolve("schema")
                .resolve("query");
        outputDirectory.toFile().mkdirs();
        String fileName = this.applicationName + "Query.graphqls";
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
