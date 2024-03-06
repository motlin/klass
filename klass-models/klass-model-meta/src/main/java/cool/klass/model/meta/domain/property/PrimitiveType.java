package cool.klass.model.meta.domain.property;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.DataType;
import cool.klass.model.meta.domain.DataType.DataTypeGetter;
import cool.klass.model.meta.domain.visitor.PrimitiveTypeVisitor;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.impl.factory.Lists;

/**
 * Predefined native types.
 */
public enum PrimitiveType implements DataType, DataTypeGetter
{
    INTEGER("Integer", true)
            {
                @Override
                public void visit(@Nonnull PrimitiveTypeVisitor visitor)
                {
                    try
                    {
                        visitor.visitInteger();
                    }
                    catch (RuntimeException e)
                    {
                        throw e;
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            },
    LONG("Long", true)
            {
                @Override
                public void visit(@Nonnull PrimitiveTypeVisitor visitor)
                {
                    try
                    {
                        visitor.visitLong();
                    }
                    catch (RuntimeException e)
                    {
                        throw e;
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            },
    DOUBLE("Double", true)
            {
                @Override
                public void visit(@Nonnull PrimitiveTypeVisitor visitor)
                {
                    try
                    {
                        visitor.visitDouble();
                    }
                    catch (RuntimeException e)
                    {
                        throw e;
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            },
    FLOAT("Float", true)
            {
                @Override
                public void visit(@Nonnull PrimitiveTypeVisitor visitor)
                {
                    try
                    {
                        visitor.visitFloat();
                    }
                    catch (RuntimeException e)
                    {
                        throw e;
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            },
    BOOLEAN("Boolean", false)
            {
                @Override
                public void visit(@Nonnull PrimitiveTypeVisitor visitor)
                {
                    try
                    {
                        visitor.visitBoolean();
                    }
                    catch (RuntimeException e)
                    {
                        throw e;
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            },
    STRING("String", false)
            {
                @Override
                public void visit(@Nonnull PrimitiveTypeVisitor visitor)
                {
                    try
                    {
                        visitor.visitString();
                    }
                    catch (RuntimeException e)
                    {
                        throw e;
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            },
    INSTANT("Instant", false)
            {
                @Override
                public void visit(@Nonnull PrimitiveTypeVisitor visitor)
                {
                    try
                    {
                        visitor.visitInstant();
                    }
                    catch (RuntimeException e)
                    {
                        throw e;
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            },
    LOCAL_DATE("LocalDate", false)
            {
                @Override
                public void visit(@Nonnull PrimitiveTypeVisitor visitor)
                {
                    try
                    {
                        visitor.visitLocalDate();
                    }
                    catch (RuntimeException e)
                    {
                        throw e;
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            },
    TEMPORAL_INSTANT("TemporalInstant", false)
            {
                @Override
                public void visit(@Nonnull PrimitiveTypeVisitor visitor)
                {
                    try
                    {
                        visitor.visitTemporalInstant();
                    }
                    catch (RuntimeException e)
                    {
                        throw e;
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            },
    TEMPORAL_RANGE("TemporalRange", false)
            {
                @Override
                public void visit(@Nonnull PrimitiveTypeVisitor visitor)
                {
                    try
                    {
                        visitor.visitTemporalRange();
                    }
                    catch (RuntimeException e)
                    {
                        throw e;
                    }
                    catch (Exception e)
                    {
                        throw new RuntimeException(e);
                    }
                }
            };

    public static final ImmutableList<PrimitiveType> PRIMITIVE_TYPES = Lists.immutable.with(
            STRING,
            INTEGER,
            LONG,
            DOUBLE,
            FLOAT,
            BOOLEAN,
            INSTANT,
            LOCAL_DATE,
            TEMPORAL_INSTANT,
            TEMPORAL_RANGE);

    private static final ImmutableMap<String, PrimitiveType> BY_PRETTY_NAME =
            PRIMITIVE_TYPES.groupByUniqueKey(PrimitiveType::getPrettyName);

    @Nonnull
    private final String  prettyName;
    private final boolean isNumeric;

    PrimitiveType(@Nonnull String prettyName, boolean isNumeric)
    {
        this.prettyName = prettyName;
        this.isNumeric = isNumeric;
    }

    public static PrimitiveType byPrettyName(String name)
    {
        return Objects.requireNonNull(BY_PRETTY_NAME.get(name));
    }

    @Nonnull
    public String getPrettyName()
    {
        return this.prettyName;
    }

    @Nonnull
    @Override
    public String toString()
    {
        return this.getPrettyName();
    }

    public abstract void visit(PrimitiveTypeVisitor visitor);

    public boolean isTemporal()
    {
        return this.isTemporalRange() || this.isTemporalInstant();
    }

    public boolean isTemporalRange()
    {
        return this == TEMPORAL_RANGE;
    }

    public boolean isTemporalInstant()
    {
        return this == TEMPORAL_INSTANT;
    }

    public boolean isNumeric()
    {
        return this.isNumeric;
    }

    @Override
    public PrimitiveType getType()
    {
        return this;
    }
}
