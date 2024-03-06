package cool.klass.model.meta.domain.value.literal;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Type;
import cool.klass.model.meta.domain.api.Type.TypeGetter;
import cool.klass.model.meta.domain.api.value.literal.LiteralListValue;
import cool.klass.model.meta.domain.api.value.literal.LiteralValue;
import cool.klass.model.meta.domain.value.AbstractExpressionValue;
import cool.klass.model.meta.domain.value.literal.AbstractLiteralValue.AbstractLiteralValueBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class LiteralListValueImpl extends AbstractExpressionValue implements LiteralListValue
{
    private final Type type;

    private ImmutableList<LiteralValue> literalValues;

    private LiteralListValueImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            Type type)
    {
        super(elementContext, inferred);
        this.type = Objects.requireNonNull(type);
    }

    @Override
    @Nonnull
    public ImmutableList<LiteralValue> getLiteralValues()
    {
        return Objects.requireNonNull(this.literalValues);
    }

    public void setLiteralValues(ImmutableList<LiteralValue> literalValues)
    {
        if (this.literalValues != null)
        {
            throw new IllegalArgumentException();
        }
        this.literalValues = Objects.requireNonNull(literalValues);
    }

    @Override
    public Type getType()
    {
        return this.type;
    }

    public static final class LiteralListValueBuilder extends AbstractExpressionValueBuilder<LiteralListValueImpl>
    {
        private final TypeGetter typeBuilder;
        private ImmutableList<AbstractLiteralValueBuilder<?>> literalValueBuilders;

        public LiteralListValueBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                TypeGetter typeBuilder)
        {
            super(elementContext, inferred);
            this.typeBuilder = Objects.requireNonNull(typeBuilder);
        }

        public void setLiteralValueBuilders(ImmutableList<AbstractLiteralValueBuilder<?>> literalValueBuilders)
        {
            if (this.literalValueBuilders != null)
            {
                throw new IllegalStateException();
            }
            this.literalValueBuilders = Objects.requireNonNull(literalValueBuilders);
        }

        @Override
        @Nonnull
        protected LiteralListValueImpl buildUnsafe()
        {
            return new LiteralListValueImpl(
                    this.elementContext,
                    this.inferred,
                    this.typeBuilder.getType());
        }

        @Override
        protected void buildChildren()
        {
            this.element.setLiteralValues(this.literalValueBuilders.collect(AbstractLiteralValueBuilder::build));
        }
    }
}
