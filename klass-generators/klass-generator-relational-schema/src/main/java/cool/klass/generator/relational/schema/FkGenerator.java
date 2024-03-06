package cool.klass.generator.relational.schema;

import java.util.Optional;

import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import org.eclipse.collections.api.map.MutableOrderedMap;

public final class FkGenerator
{
    private FkGenerator()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static Optional<String> getFk(Klass klass)
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

        String result = foreignKeys
                .keyValuesView()
                .collect(keyValuePair -> getFk(klass, keyValuePair.getOne(), keyValuePair.getTwo()))
                .makeString("");
        return Optional.of(result);
    }

    private static String getFk(
            Klass klass,
            AssociationEnd associationEnd,
            MutableOrderedMap<DataTypeProperty, DataTypeProperty> dataTypeProperties)
    {
        String tableName = DdlGenerator.TABLE_NAME_CONVERTER.convert(klass.getName());
        String constraintName = tableName
                + "_FK_"
                + DdlGenerator.TABLE_NAME_CONVERTER.convert(associationEnd.getName());

        String foreignKeyColumnNames = dataTypeProperties
                .keysView()
                .collect(DataTypeProperty::getName)
                .collect(DdlGenerator.COLUMN_NAME_CONVERTER::convert)
                .makeString(", ");

        String referencedTableName = DdlGenerator.TABLE_NAME_CONVERTER.convert(associationEnd.getType().getName());

        String referencedKeyColumnNames = dataTypeProperties
                .valuesView()
                .collect(DataTypeProperty::getName)
                .collect(DdlGenerator.COLUMN_NAME_CONVERTER::convert)
                .makeString(", ");

        String format = "alter table %s add constraint %s foreign key (\n"
                + "    %s\n"
                + ")\n"
                + "references %s(\n"
                + "    %s\n"
                + ");\n\n";

        return format.formatted(
                tableName,
                constraintName,
                foreignKeyColumnNames,
                referencedTableName,
                referencedKeyColumnNames);
    }
}
