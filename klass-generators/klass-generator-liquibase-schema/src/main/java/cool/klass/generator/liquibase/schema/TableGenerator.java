package cool.klass.generator.liquibase.schema;

import java.util.Objects;

import javax.annotation.Nonnull;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.NamedElement;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;

public final class TableGenerator
{
    public static final Converter<String, String> TABLE_NAME_CONVERTER =
            CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.UPPER_UNDERSCORE);

    public static final Converter<String, String> COLUMN_NAME_CONVERTER =
            CaseFormat.LOWER_CAMEL.converterTo(CaseFormat.UPPER_UNDERSCORE);

    private TableGenerator()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static String getTable(@Nonnull Klass klass, int ordinal)
    {
        Objects.requireNonNull(klass);

        String tableName            = getTableName(klass);
        String propertiesSourceCode = getPropertiesSourceCode(klass, tableName);

        return "    <changeSet author=\"Klass\" id=\"initial-table-" + ordinal + "-" + tableName + "\">\n"
                + "        <createTable tableName=\"" + tableName + "\">\n"
                + propertiesSourceCode
                + "        </createTable>\n"
                + "    </changeSet>\n\n";
    }

    @Nonnull
    public static String getTableName(@Nonnull Klass klass)
    {
        return TABLE_NAME_CONVERTER.convert(klass.getName());
    }

    @Nonnull
    private static String getPropertiesSourceCode(@Nonnull Klass klass, String tableName)
    {
        return getDataTypeProperties(klass)
                .reject(DataTypeProperty::isDerived)
                .reject(DataTypeProperty::isTemporalRange)
                .collect(dataTypeProperty -> getPropertySourceCode(dataTypeProperty, tableName))
                .makeString("\n");
    }

    private static ImmutableList<DataTypeProperty> getDataTypeProperties(@Nonnull Klass klass)
    {
        ImmutableList<String> superClassPropertyNames = klass
                .getSuperClass()
                .map(Classifier::getDataTypeProperties)
                .orElseGet(Lists.immutable::empty)
                .collect(NamedElement::getName);

        return klass.getDataTypeProperties()
                .select(each -> each.isKey() || !superClassPropertyNames.contains(each.getName()));
    }

    private static String getPropertySourceCode(DataTypeProperty dataTypeProperty, String tableName)
    {
        String name        = COLUMN_NAME_CONVERTER.convert(dataTypeProperty.getName());
        String dataType    = getDataType(dataTypeProperty);
        String nullability = getNullability(dataTypeProperty);
        String primaryKey  = getPrimaryKey(dataTypeProperty, tableName);

        if (nullability.isEmpty() && primaryKey.isEmpty())
        {
            return "            <column name=\"" + name + "\" type=\"" + dataType + "\" />\n";
        }

        return "            <column name=\"" + name + "\" type=\"" + dataType + "\">\n"
                + "                <constraints" + nullability + primaryKey + " />\n"
                + "            </column>\n";
    }

    private static String getDataType(DataTypeProperty dataTypeProperty)
    {
        var visitor = new LiquibaseSchemaGeneratorDataTypePropertyVisitor();
        dataTypeProperty.visit(visitor);
        return visitor.getDataTypeSourceCode();
    }

    private static String getNullability(DataTypeProperty dataTypeProperty)
    {
        return dataTypeProperty.isTemporalInstant() || dataTypeProperty.isRequired()
                ? " nullable=\"false\""
                : "";
    }

    private static String getPrimaryKey(DataTypeProperty dataTypeProperty, String tableName)
    {
        if (!dataTypeProperty.isKey() && !dataTypeProperty.isTo())
        {
            return "";
        }
        return " primaryKey=\"true\" primaryKeyName=\"" + tableName + "_PK\"";
    }
}
