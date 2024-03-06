package cool.klass.generator.reladomo;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Objects;

import javax.annotation.Nonnull;

import com.gs.fw.common.mithra.generator.metamodel.CardinalityType;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.property.AssociationEnd;

public class AbstractReladomoGenerator
{
    @Nonnull
    protected final DomainModel domainModel;

    public AbstractReladomoGenerator(@Nonnull DomainModel domainModel)
    {
        this.domainModel = Objects.requireNonNull(domainModel);
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
                        " (javaType|readonly|nullable|columnName|trim|primaryKey|maxLength|primaryKeyGeneratorStrategy|fromIsInclusive|toIsInclusive|infinityDate|futureExpiringRowsExist|isProcessingDate|fromColumnName|toColumnName|defaultIfNotSpecified|reverseRelationshipName|relatedObject|relatedIsDependent|cardinality|orderBy|sequenceName|sequenceObjectFactoryName|hasSourceAttribute|batchSize|initialValue|incrementSize|infinityIsNull|timezoneConversion|finalGetter)=",
                        "\n            $1=")
                .replaceAll(
                        " (initializePrimitivesToNull|objectType|superClassType)=",
                        "\n        $1=")
                .replaceAll(
                        "^<(MithraObject)",
                        ""
                                + "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
                                + "<!-- Generated by cool.klass.generator.reladomo.objectfile.ReladomoObjectFileGenerator -->\n"
                                + "<$1\n"
                                + "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
                                + "        xsi:noNamespaceSchemaLocation=\"https://raw.githubusercontent.com/goldmansachs/reladomo/master/reladomogen/src/main/xsd/mithraobject.xsd\"")
                .replaceAll(
                        "^<(MithraPureObject|MithraInterface|Mithra\\b)",
                        ""
                                + "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
                                + "<!-- Generated by cool.klass.generator.reladomo.objectfile.ReladomoObjectFileGenerator -->\n"
                                + "<$1\n"
                                + "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
                                + "        xsi:noNamespaceSchemaLocation=\"https://raw.githubusercontent.com/goldmansachs/reladomo/master/reladomogen/src/main/xsd/mithraobject.xsd\"")
                .replaceAll(
                        "<(MithraRuntime)>\n",
                        ""
                                + "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n"
                                + "<!-- Generated by cool.klass.generator.reladomo.runtimeconfig.ReladomoRuntimeConfigurationGenerator -->\n"
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

    protected CardinalityType getCardinality(
            @Nonnull AssociationEnd associationEnd,
            @Nonnull AssociationEnd opposite)
    {
        Multiplicity multiplicity         = associationEnd.getMultiplicity();
        Multiplicity oppositeMultiplicity = opposite.getMultiplicity();

        boolean fromOne  = oppositeMultiplicity.isToOne();
        boolean toOne    = multiplicity.isToOne();
        boolean fromMany = oppositeMultiplicity.isToMany();
        boolean toMany   = multiplicity.isToMany();

        if (fromOne && toOne)
        {
            return this.getCardinalityType("one-to-one");
        }
        if (fromOne && toMany)
        {
            return this.getCardinalityType("one-to-many");
        }
        if (fromMany && toOne)
        {
            return this.getCardinalityType("many-to-one");
        }
        if (fromMany && toMany)
        {
            return this.getCardinalityType("many-to-many");
        }
        throw new AssertionError();
    }

    protected CardinalityType getCardinalityType(String attributeValue)
    {
        CardinalityType cardinalityType = new CardinalityType();
        return cardinalityType.with(attributeValue, cardinalityType);
    }
}
