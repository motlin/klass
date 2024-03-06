package cool.klass.model.meta.domain;

import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.impl.factory.Lists;

/**
 * Predefined native types.
 */
public final class PrimitiveType extends DataType
{
    public static final PrimitiveType INTEGER    = new PrimitiveType("Integer");
    public static final PrimitiveType LONG       = new PrimitiveType("Long");
    public static final PrimitiveType DOUBLE     = new PrimitiveType("Double");
    public static final PrimitiveType FLOAT      = new PrimitiveType("Float");
    public static final PrimitiveType BOOLEAN    = new PrimitiveType("Boolean");
    public static final PrimitiveType STRING     = new PrimitiveType("String");
    public static final PrimitiveType INSTANT    = new PrimitiveType("Instant");
    public static final PrimitiveType LOCAL_DATE = new PrimitiveType("LocalDate");
    public static final PrimitiveType ID         = new PrimitiveType("ID");

    public static final ImmutableList<PrimitiveType> PRIMITIVE_TYPES = Lists.immutable.with(
            INTEGER,
            LONG,
            DOUBLE,
            FLOAT,
            BOOLEAN,
            STRING,
            INSTANT,
            LOCAL_DATE,
            ID);

    private static final ImmutableMap<String, PrimitiveType> BY_NAME = PRIMITIVE_TYPES.groupByUniqueKey(NamedElement::getName);

    private PrimitiveType(String name)
    {
        super(name, "klass.meta");
    }

    public static PrimitiveType valueOf(String name)
    {
        return BY_NAME.get(name);
    }
}
