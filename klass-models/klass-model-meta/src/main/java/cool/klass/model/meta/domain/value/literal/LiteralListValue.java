package cool.klass.model.meta.domain.value.literal;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Type;
import cool.klass.model.meta.domain.Type.TypeGetter;
import cool.klass.model.meta.domain.value.ExpressionValue;
import cool.klass.model.meta.domain.value.ExpressionValueVisitor;
import cool.klass.model.meta.domain.value.literal.LiteralValue.LiteralValueBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class LiteralListValue extends ExpressionValue
{
    @Nonnull
    private final ImmutableList<LiteralValue> literalValues;
    private final Type                        type;

    private LiteralListValue(
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
    public void visit(@Nonnull ExpressionValueVisitor visitor)
    {
        visitor.visitLiteralList(this);
    }

    @Nonnull
    public ImmutableList<LiteralValue> getLiteralValues()
    {
        return this.literalValues;
    }

    public Type getType()
    {
        return this.type;
    }

    public static final class LiteralListValueBuilder extends ExpressionValueBuilder
    {
        @Nonnull
        private final ImmutableList<LiteralValueBuilder> literalValueBuilders;
        private final TypeGetter                         typeBuilder;

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
        public LiteralListValue build()
        {
            ImmutableList<LiteralValue> literalValues = this.literalValueBuilders.collect(LiteralValueBuilder::build);
            return new LiteralListValue(this.elementContext, this.inferred, literalValues, this.typeBuilder.getType());
        }
    }
}
