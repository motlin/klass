package cool.klass.model.meta.domain.property;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.DataType;
import cool.klass.model.meta.domain.NamedElement;
import cool.klass.model.meta.domain.visitor.PrimitiveTypeVisitor;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.impl.factory.Lists;

/**
 * Predefined native types.
 */
public abstract class PrimitiveType extends DataType
{
    public static final PrimitiveType INTEGER          = new PrimitiveType("Integer", 1, true)
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
    };
    public static final PrimitiveType LONG             = new PrimitiveType("Long", 2, true)
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
    };
    public static final PrimitiveType DOUBLE           = new PrimitiveType("Double", 3, true)
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
    };
    public static final PrimitiveType FLOAT            = new PrimitiveType("Float", 4, true)
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
    };
    public static final PrimitiveType BOOLEAN          = new PrimitiveType("Boolean", 5, false)
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
    };
    public static final PrimitiveType STRING           = new PrimitiveType("String", 6, false)
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
    };
    public static final PrimitiveType INSTANT          = new PrimitiveType("Instant", 7, false)
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
    };
    public static final PrimitiveType LOCAL_DATE       = new PrimitiveType("LocalDate", 8, false)
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
    };
    public static final PrimitiveType TEMPORAL_INSTANT = new PrimitiveType("TemporalInstant", 9, false)
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
    };
    public static final PrimitiveType TEMPORAL_RANGE   = new PrimitiveType("TemporalRange", 10, false)
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

    public static final String META_PACKAGE = "klass.meta";

    private static final ImmutableMap<String, PrimitiveType> BY_NAME = PRIMITIVE_TYPES.groupByUniqueKey(NamedElement::getName);

    private final boolean isNumeric;

    private PrimitiveType(@Nonnull String name, int ordinal, boolean isNumeric)
    {
        super(NO_CONTEXT, false, NO_CONTEXT, name, ordinal, META_PACKAGE);
        this.isNumeric = isNumeric;
    }

    public static PrimitiveType valueOf(String name)
    {
        return Objects.requireNonNull(BY_NAME.get(name));
    }

    @Nonnull
    @Override
    public String toString()
    {
        return this.getName();
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

    public static final class PrimitiveTypeBuilder extends DataTypeBuilder
    {
        public static final PrimitiveTypeBuilder INTEGER          = new PrimitiveTypeBuilder(PrimitiveType.INTEGER, 1);
        public static final PrimitiveTypeBuilder LONG             = new PrimitiveTypeBuilder(PrimitiveType.LONG, 2);
        public static final PrimitiveTypeBuilder DOUBLE           = new PrimitiveTypeBuilder(PrimitiveType.DOUBLE, 3);
        public static final PrimitiveTypeBuilder FLOAT            = new PrimitiveTypeBuilder(PrimitiveType.FLOAT, 4);
        public static final PrimitiveTypeBuilder BOOLEAN          = new PrimitiveTypeBuilder(PrimitiveType.BOOLEAN, 5);
        public static final PrimitiveTypeBuilder STRING           = new PrimitiveTypeBuilder(PrimitiveType.STRING, 6);
        public static final PrimitiveTypeBuilder INSTANT          = new PrimitiveTypeBuilder(PrimitiveType.INSTANT, 7);
        public static final PrimitiveTypeBuilder LOCAL_DATE       = new PrimitiveTypeBuilder(PrimitiveType.LOCAL_DATE, 8);
        public static final PrimitiveTypeBuilder TEMPORAL_INSTANT = new PrimitiveTypeBuilder(PrimitiveType.TEMPORAL_INSTANT, 9);
        public static final PrimitiveTypeBuilder TEMPORAL_RANGE   = new PrimitiveTypeBuilder(PrimitiveType.TEMPORAL_RANGE, 10);

        public static final ImmutableList<PrimitiveTypeBuilder> PRIMITIVE_TYPE_BUILDERS = Lists.immutable.with(
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

        private static final ImmutableMap<PrimitiveType, PrimitiveTypeBuilder> BY_TYPE =
                PRIMITIVE_TYPE_BUILDERS.groupByUniqueKey(PrimitiveTypeBuilder::getPrimitiveType);

        @Nonnull
        private final PrimitiveType primitiveType;

        private PrimitiveTypeBuilder(@Nonnull PrimitiveType primitiveType, int ordinal)
        {
            super(
                    new ParserRuleContext(),
                    false,
                    new ParserRuleContext(),
                    new ParserRuleContext().getText(),
                    ordinal,
                    "klass.meta");
            this.primitiveType = Objects.requireNonNull(primitiveType);
        }

        public static PrimitiveTypeBuilder valueOf(PrimitiveType type)
        {
            return BY_TYPE.get(type);
        }

        @Nonnull
        public PrimitiveType getPrimitiveType()
        {
            return Objects.requireNonNull(this.primitiveType);
        }

        @Override
        public PrimitiveType getType()
        {
            return Objects.requireNonNull(this.primitiveType);
        }
    }
}
