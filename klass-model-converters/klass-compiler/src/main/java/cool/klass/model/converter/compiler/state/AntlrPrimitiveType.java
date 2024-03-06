package cool.klass.model.converter.compiler.state;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.PrimitiveTypeBuilder;
import cool.klass.model.meta.domain.api.PrimitiveType;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.ImmutableMap;
import org.eclipse.collections.impl.factory.Lists;

public final class AntlrPrimitiveType extends AntlrElement implements AntlrType
{
    public static final AntlrPrimitiveType INTEGER          = new AntlrPrimitiveType(PrimitiveType.INTEGER);
    public static final AntlrPrimitiveType LONG             = new AntlrPrimitiveType(PrimitiveType.LONG);
    public static final AntlrPrimitiveType DOUBLE           = new AntlrPrimitiveType(PrimitiveType.DOUBLE);
    public static final AntlrPrimitiveType FLOAT            = new AntlrPrimitiveType(PrimitiveType.FLOAT);
    public static final AntlrPrimitiveType BOOLEAN          = new AntlrPrimitiveType(PrimitiveType.BOOLEAN);
    public static final AntlrPrimitiveType STRING           = new AntlrPrimitiveType(PrimitiveType.STRING);
    public static final AntlrPrimitiveType INSTANT          = new AntlrPrimitiveType(PrimitiveType.INSTANT);
    public static final AntlrPrimitiveType LOCAL_DATE       = new AntlrPrimitiveType(PrimitiveType.LOCAL_DATE);
    public static final AntlrPrimitiveType TEMPORAL_INSTANT = new AntlrPrimitiveType(PrimitiveType.TEMPORAL_INSTANT);
    public static final AntlrPrimitiveType TEMPORAL_RANGE   = new AntlrPrimitiveType(PrimitiveType.TEMPORAL_RANGE);

    @Nonnull
    public static final AntlrPrimitiveType AMBIGUOUS = new AntlrPrimitiveType(null);

    public static final ImmutableList<AntlrPrimitiveType> PRIMITIVE_TYPES = Lists.immutable.with(
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

    private static final ImmutableMap<PrimitiveType, AntlrPrimitiveType> BY_TYPE = PRIMITIVE_TYPES.groupByUniqueKey(
            AntlrPrimitiveType::getPrimitiveType);

    private final PrimitiveType primitiveType;

    private AntlrPrimitiveType(PrimitiveType primitiveType)
    {
        super(new ParserRuleContext(), Optional.empty());
        this.primitiveType = primitiveType;
    }

    public static AntlrPrimitiveType valueOf(PrimitiveType type)
    {
        return Objects.requireNonNull(BY_TYPE.get(type));
    }

    @Override
    public boolean omitParentFromSurroundingElements()
    {
        return true;
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getSurroundingContext() not implemented yet");
    }

    @Nonnull
    public PrimitiveType getPrimitiveType()
    {
        return Objects.requireNonNull(this.primitiveType);
    }

    @Nonnull
    @Override
    public String toString()
    {
        return this.primitiveType.toString();
    }

    @Override
    public PrimitiveType getTypeGetter()
    {
        return this.primitiveType;
    }

    @Nonnull
    @Override
    public PrimitiveTypeBuilder getElementBuilder()
    {
        return new PrimitiveTypeBuilder(
                this.getElementContext(),
                this.getMacroElementBuilder(),
                this.primitiveType);
    }

    @Override
    public ImmutableList<AntlrType> getPotentialWiderTypes()
    {
        switch (this.primitiveType)
        {
            case INTEGER:
                return Lists.immutable.with(INTEGER, LONG, FLOAT, DOUBLE);
            case LONG:
                return Lists.immutable.with(LONG, DOUBLE);
            case DOUBLE:
                return Lists.immutable.with(DOUBLE);
            case FLOAT:
                return Lists.immutable.with(FLOAT, DOUBLE);
            case BOOLEAN:
                return Lists.immutable.with(BOOLEAN);
            case STRING:
                return Lists.immutable.with(STRING);
            case INSTANT:
                return Lists.immutable.with(INSTANT, TEMPORAL_INSTANT, TEMPORAL_RANGE);
            case LOCAL_DATE:
                return Lists.immutable.with(LOCAL_DATE);
            case TEMPORAL_INSTANT:
                return Lists.immutable.with(TEMPORAL_INSTANT);
            case TEMPORAL_RANGE:
                return Lists.immutable.with(TEMPORAL_RANGE);
            default:
                throw new AssertionError();
        }
    }

    public boolean isTemporal()
    {
        return this == TEMPORAL_INSTANT || this == TEMPORAL_RANGE;
    }
}
