package cool.klass.model.meta.domain.value.literal;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Type;
import cool.klass.model.meta.domain.api.Type.TypeGetter;
import cool.klass.model.meta.domain.api.value.literal.LiteralListValue;
import cool.klass.model.meta.domain.api.value.literal.LiteralValue;
import cool.klass.model.meta.domain.value.AbstractExpressionValue;
import cool.klass.model.meta.domain.value.literal.AbstractLiteralValue.LiteralValueBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class LiteralListValueImpl extends AbstractExpressionValue implements LiteralListValue
{
    @Nonnull
    private final ImmutableList<LiteralValue> literalValues;
    private final Type                        type;

    private LiteralListValueImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ImmutableList<LiteralValue> literalValues,
            Type type)
    {
        super(elementContext, inferred);
        this.literalValues = Objects.requireNonNull(literalValues);
        this.type = Objects.requireNonNull(type);
    }

    @Override
    @Nonnull
    public ImmutableList<LiteralValue> getLiteralValues()
    {
        return this.literalValues;
    }

    @Override
    public Type getType()
    {
        return this.type;
    }

    public static final class LiteralListValueBuilder extends ExpressionValueBuilder
    {
        @Nonnull
        private final ImmutableList<LiteralValueBuilder> literalValueBuilders;
        private final TypeGetter                        typeBuilder;

        public LiteralListValueBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ImmutableList<LiteralValueBuilder> literalValueBuilders,
                TypeGetter typeBuilder)
        {
            super(elementContext, inferred);
            this.literalValueBuilders = Objects.requireNonNull(literalValueBuilders);
            this.typeBuilder = Objects.requireNonNull(typeBuilder);
        }

        @Nonnull
        @Override
        public LiteralListValueImpl build()
        {
            ImmutableList<LiteralValue> literalValues = this.literalValueBuilders.collect(LiteralValueBuilder::build);
            return new LiteralListValueImpl(
                    this.elementContext,
                    this.inferred,
                    literalValues,
                    this.typeBuilder.getType());
        }
    }
}
