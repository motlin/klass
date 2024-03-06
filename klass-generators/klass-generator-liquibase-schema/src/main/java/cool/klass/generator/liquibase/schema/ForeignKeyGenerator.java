package cool.klass.generator.liquibase.schema;

import java.util.Optional;

import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;

public final class ForeignKeyGenerator
{
    private ForeignKeyGenerator()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static Optional<String> getForeignKey(Klass klass, int ordinal)
    {
        MutableOrderedMap<AssociationEnd, MutableOrderedMap<DataTypeProperty, DataTypeProperty>> foreignKeys = klass.getForeignKeys();
        if (foreignKeys.isEmpty())
        {
            return Optional.empty();
        }

        if (klass.isTemporal())
        {
            return Optional.empty();
        }

        ImmutableList<String> foreignKeyStrings = foreignKeys
                .keyValuesView()
                .collect(keyValuePair -> getForeignKey(keyValuePair.getOne(), keyValuePair.getTwo(), ordinal))
                .toImmutableList();
        String result = foreignKeyStrings.makeString("");
        return Optional.of(result);
    }

    private static String getForeignKey(
            AssociationEnd associationEnd,
            MutableOrderedMap<DataTypeProperty, DataTypeProperty> dataTypeProperties,
            int ordinal)
    {
        String tableName = TableGenerator.TABLE_NAME_CONVERTER.convert(associationEnd.getOwningClassifier().getName());
        String constraintName = tableName
                + "_FK_"
                + TableGenerator.TABLE_NAME_CONVERTER.convert(associationEnd.getName());

        String foreignKeyColumnNames = dataTypeProperties
                .keysView()
                .collect(DataTypeProperty::getName)
                .collect(TableGenerator.COLUMN_NAME_CONVERTER::convert)
                .makeString(", ");

        String referencedTableName = TableGenerator.TABLE_NAME_CONVERTER.convert(associationEnd.getType().getName());

        String referencedKeyColumnNames = dataTypeProperties
                .valuesView()
                .collect(DataTypeProperty::getName)
                .collect(TableGenerator.COLUMN_NAME_CONVERTER::convert)
                .makeString(", ");

        //language=XML
        String format = """
                    <changeSet author="Klass" id="initial-foreign-key-%d-%s">
                        <addForeignKeyConstraint
                                constraintName="%s"
                                baseTableName="%s"
                                baseColumnNames="%s"
                                referencedTableName="%s"
                                referencedColumnNames="%s"
                        />
                    </changeSet>

                """;

        return format.formatted(
                ordinal,
                constraintName,
                constraintName,
                tableName,
                foreignKeyColumnNames,
                referencedTableName,
                referencedKeyColumnNames);
    }
}
