package cool.klass.generator.liquibase.schema;

import java.util.Optional;

import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.Klass;
import org.eclipse.collections.api.list.ImmutableList;

public final class SchemaGenerator
{
    private SchemaGenerator()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static String getSourceCode(DomainModel domainModel, String fullyQualifiedPackage)
    {
        ImmutableList<Klass> classes = domainModel
                .getClasses()
                .select(each -> each.getPackageName().equals(fullyQualifiedPackage));
        ImmutableList<String> sourceCodes = classes.collectWithIndex(SchemaGenerator::getSourceCode);

        //language=XML
        return "<?xml version=\"1.1\" encoding=\"UTF-8\" standalone=\"no\"?>\n"
               + "<databaseChangeLog xmlns=\"http://www.liquibase.org/xml/ns/dbchangelog\"\n"
               + "        xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"\n"
               + "        xsi:schemaLocation=\"http://www.liquibase.org/xml/ns/dbchangelog http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.10.xsd\">\n"
               + "\n"
               + sourceCodes.makeString("")
               + "</databaseChangeLog>\n";
    }

    public static String getSourceCode(Klass klass, int index)
    {
        int              ordinal           = index + 1;
        String           tableSourceCode   = TableGenerator.getTable(klass, ordinal);
        String           idxSourceCode     = IndexGenerator.getIndex(klass, ordinal);
        Optional<String> maybeFkSourceCode = ForeignKeyGenerator.getForeignKey(klass, ordinal);

        return tableSourceCode + idxSourceCode + maybeFkSourceCode.orElse("");
    }
}
