package cool.klass.model.meta.domain.parameter;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Multiplicity;
import cool.klass.model.meta.domain.property.PrimitiveType;
import org.antlr.v4.runtime.ParserRuleContext;

public final class PrimitiveParameter extends Parameter
{
    @Nonnull
    private final PrimitiveType primitiveType;

    private PrimitiveParameter(
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

    public static final class PrimitiveParameterBuilder extends ParameterBuilder
    {
        @Nonnull
        private final PrimitiveType      primitiveType;
        private       PrimitiveParameter primitiveParameter;

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

        @Nonnull
        @Override
        public PrimitiveParameter build()
        {
            if (this.primitiveParameter != null)
            {
                throw new IllegalStateException();
            }
            this.primitiveParameter = new PrimitiveParameter(
                    this.elementContext,
                    this.inferred,
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.multiplicity,
                    this.primitiveType);
            return this.primitiveParameter;
        }

        @Nonnull
        @Override
        public PrimitiveParameter getParameter()
        {
            return Objects.requireNonNull(this.primitiveParameter);
        }
    }
}
