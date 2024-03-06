package cool.klass.model.meta.domain.value.literal;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.value.ExpressionValue;
import cool.klass.model.meta.domain.value.ExpressionValueVisitor;
import cool.klass.model.meta.domain.value.literal.LiteralValue.LiteralValueBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class LiteralListValue extends ExpressionValue
{
    @Nonnull
    private final ImmutableList<ExpressionValue> expressionValues;

    private LiteralListValue(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull ImmutableList<ExpressionValue> expressionValues)
    {
        super(elementContext);
        this.expressionValues = Objects.requireNonNull(expressionValues);
    }

    @Override
    public void visit(@Nonnull ExpressionValueVisitor visitor)
    {
        visitor.visitLiteralList(this);
    }

    @Nonnull
    public ImmutableList<ExpressionValue> getExpressionValues()
    {
        return this.expressionValues;
    }

    public static final class LiteralListValueBuilder extends ExpressionValueBuilder
    {
        @Nonnull
        private final ImmutableList<LiteralValueBuilder> literalValueBuilders;

        public LiteralListValueBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull ImmutableList<LiteralValueBuilder> literalValueBuilders)
        {
            super(elementContext);
            this.literalValueBuilders = Objects.requireNonNull(literalValueBuilders);
        }

        @Nonnull
        @Override
        public LiteralListValue build()
        {
            ImmutableList<ExpressionValue> expressionValues = this.literalValueBuilders.collect(ExpressionValueBuilder::build);
            return new LiteralListValue(this.elementContext, expressionValues);
        }
    }
}
