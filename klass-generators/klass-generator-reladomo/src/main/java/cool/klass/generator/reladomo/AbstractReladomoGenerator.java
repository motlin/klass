package cool.klass.generator.reladomo;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.DomainModel;

public class AbstractReladomoGenerator
{
    protected final DomainModel domainModel;

    public AbstractReladomoGenerator(DomainModel domainModel)
    {
        this.domainModel = domainModel;
    }

    @Nonnull
    protected String sanitizeXmlString(@Nonnull StringBuilder stringBuilder)
    {
        return stringBuilder.toString()
                .replaceAll("(AsOfAttribute|Attribute|Relationship) name", "$1\n            name")
                .replaceAll("</(Relationship)>", "\n    </$1>\n")
                .replaceAll("</(MithraObject|DefaultTable)>", "</$1>\n")
                .replaceAll(">this", ">\n            this")
                .replaceAll("></SimulatedSequence>", " />")
                .replaceAll("\"/>", "\" />")
                .replaceAll(
                        " (javaType|nullable|columnName|trim|primaryKey|maxLength|primaryKeyGeneratorStrategy|fromIsInclusive|toIsInclusive|infinityDate|futureExpiringRowsExist|isProcessingDate|fromColumnName|toColumnName|defaultIfNotSpecified|reverseRelationshipName|relatedObject|relatedIsDependent|cardinality|orderBy|sequenceName|sequenceObjectFactoryName|hasSourceAttribute|batchSize|initialValue|incrementSize|infinityIsNull|timezoneConversion|finalGetter)=",
                        "\n            $1=")
                .replaceAll(
                        " (initializePrimitivesToNull|objectType|superClassType)=",
                        "\n        $1=")
                .replaceAll(
                        "^<(MithraObject)",
                        ""
                                + "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
                                + "<!-- Generated by cool.klass.generator.reladomo.ReladomoObjectFileGenerator -->\n"
                                + "<$1\n"
                                + "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
                                + "        xsi:noNamespaceSchemaLocation=\"https://raw.githubusercontent.com/goldmansachs/reladomo/master/reladomogen/src/main/xsd/mithraobject.xsd\"")
                .replaceAll(
                        "^<(MithraPureObject|MithraInterface|Mithra\\b)",
                        ""
                                + "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
                                + "<$1\n"
                                + "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
                                + "        xsi:noNamespaceSchemaLocation=\"https://raw.githubusercontent.com/goldmansachs/reladomo/master/reladomogen/src/main/xsd/mithraobject.xsd\"")
                .replaceAll(
                        "<(MithraRuntime)>\n",
                        ""
                                + "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
                                + "<$1\n"
                                + "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
                                + "        xsi:noNamespaceSchemaLocation=\"https://raw.githubusercontent.com/goldmansachs/reladomo/master/reladomo/src/main/xsd/mithraruntime.xsd\">\n");
    }

    protected void printStringToFile(@Nonnull Path path, String contents)
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
