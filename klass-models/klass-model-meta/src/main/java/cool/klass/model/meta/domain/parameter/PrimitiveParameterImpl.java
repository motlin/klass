package cool.klass.model.meta.domain.parameter;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.domain.api.parameter.PrimitiveParameter;
import org.antlr.v4.runtime.ParserRuleContext;

public final class PrimitiveParameterImpl extends AbstractParameter implements PrimitiveParameter
{
    @Nonnull
    private final PrimitiveType primitiveType;

    private PrimitiveParameterImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull Multiplicity multiplicity,
            @Nonnull PrimitiveType primitiveType)
    {
        super(elementContext, inferred, nameContext, name, ordinal, multiplicity);
        this.primitiveType = Objects.requireNonNull(primitiveType);
    }

    @Override
    @Nonnull
    public PrimitiveType getType()
    {
        return this.primitiveType;
    }

    public static final class PrimitiveParameterBuilder extends AbstractParameterBuilder<PrimitiveParameterImpl>
    {
        @Nonnull
        private final PrimitiveType primitiveType;

        public PrimitiveParameterBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull Multiplicity multiplicity,
                @Nonnull PrimitiveType primitiveType)
        {
            super(elementContext, inferred, nameContext, name, ordinal, multiplicity);
            this.primitiveType = Objects.requireNonNull(primitiveType);
        }

        @Override
        @Nonnull
        protected PrimitiveParameterImpl buildUnsafe()
        {
            return new PrimitiveParameterImpl(
                    this.elementContext,
                    this.inferred,
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.multiplicity,
                    this.primitiveType);
        }
    }
}
