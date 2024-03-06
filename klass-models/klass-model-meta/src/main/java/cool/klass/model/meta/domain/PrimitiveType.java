package cool.klass.model.meta.domain;

import javax.annotation.Nonnull;

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
    public static final PrimitiveType ID               = new PrimitiveType("ID")
    {
        @Override
        public void visit(PrimitiveTypeVisitor visitor)
        {
            visitor.visitID();
        }
    };
    public static final PrimitiveType INTEGER          = new PrimitiveType("Integer")
    {
        @Override
        public void visit(PrimitiveTypeVisitor visitor)
        {
            visitor.visitInteger();
        }
    };
    public static final PrimitiveType LONG             = new PrimitiveType("Long")
    {
        @Override
        public void visit(PrimitiveTypeVisitor visitor)
        {
            visitor.visitLong();
        }
    };
    public static final PrimitiveType DOUBLE           = new PrimitiveType("Double")
    {
        @Override
        public void visit(PrimitiveTypeVisitor visitor)
        {
            visitor.visitDouble();
        }
    };
    public static final PrimitiveType FLOAT            = new PrimitiveType("Float")
    {
        @Override
        public void visit(PrimitiveTypeVisitor visitor)
        {
            visitor.visitFloat();
        }
    };
    public static final PrimitiveType BOOLEAN          = new PrimitiveType("Boolean")
    {
        @Override
        public void visit(PrimitiveTypeVisitor visitor)
        {
            visitor.visitBoolean();
        }
    };
    public static final PrimitiveType STRING           = new PrimitiveType("String")
    {
        @Override
        public void visit(PrimitiveTypeVisitor visitor)
        {
            visitor.visitString();
        }
    };
    public static final PrimitiveType INSTANT          = new PrimitiveType("Instant")
    {
        @Override
        public void visit(PrimitiveTypeVisitor visitor)
        {
            visitor.visitInstant();
        }
    };
    public static final PrimitiveType LOCAL_DATE       = new PrimitiveType("LocalDate")
    {
        @Override
        public void visit(PrimitiveTypeVisitor visitor)
        {
            visitor.visitLocalDate();
        }
    };
    public static final PrimitiveType TEMPORAL_INSTANT = new PrimitiveType("TemporalInstant")
    {
        @Override
        public void visit(PrimitiveTypeVisitor visitor)
        {
            visitor.visitTemporalInstant();
        }
    };
    public static final PrimitiveType TEMPORAL_RANGE   = new PrimitiveType("TemporalRange")
    {
        @Override
        public void visit(PrimitiveTypeVisitor visitor)
        {
            visitor.visitTemporalRange();
        }
    };

    public static final ImmutableList<PrimitiveType> PRIMITIVE_TYPES = Lists.immutable.with(
            ID,
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

    private static final ImmutableMap<String, PrimitiveType> BY_NAME = PRIMITIVE_TYPES.groupByUniqueKey(NamedElement::getName);

    private PrimitiveType(@Nonnull String name)
    {
        super(NO_CONTEXT, NO_CONTEXT, name, "klass.meta");
    }

    public static PrimitiveType valueOf(String name)
    {
        return BY_NAME.get(name);
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

    public static class PrimitiveTypeBuilder extends DataTypeBuilder<PrimitiveType>
    {
        private final PrimitiveType primitiveType;

        public PrimitiveTypeBuilder(
                @Nonnull ParserRuleContext elementContext,
                PrimitiveType primitiveType)
        {
            super(elementContext, elementContext, elementContext.getText(), "klass.meta");
            this.primitiveType = primitiveType;
        }

        public PrimitiveType getPrimitiveType()
        {
            return this.primitiveType;
        }
    }
}
