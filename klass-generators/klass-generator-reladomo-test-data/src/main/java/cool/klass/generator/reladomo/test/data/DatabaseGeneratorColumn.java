package cool.klass.generator.reladomo.test.data;

import cool.klass.model.meta.domain.property.PrimitiveType;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

public class DatabaseGeneratorColumn
{
    private final PrimitiveType       primitiveType;
    private final MutableList<String> values = Lists.mutable.empty();
    private       int                 maxWidth;

    public DatabaseGeneratorColumn(PrimitiveType primitiveType)
    {
        this.primitiveType = primitiveType;
    }

    public void addValue(String value)
    {
        this.maxWidth = Math.max(this.maxWidth, value.length());
        this.values.add(value);
    }

    public String getPaddedValue(int rowIndex)
    {
        String value = this.values.get(rowIndex);
        return this.primitiveType.isNumeric()
                ? padLeft(value, this.maxWidth)
                : padRight(value, this.maxWidth);
    }

    public static String padLeft(String string, int width)
    {
        return padString(string, width, "");
    }

    public static String padRight(String string, int width)
    {
        return padString(string, width, "-");
    }

    public static String padString(String string, int width, String direction)
    {
        return String.format("%1$" + direction + width + 's', string);
    }
}
