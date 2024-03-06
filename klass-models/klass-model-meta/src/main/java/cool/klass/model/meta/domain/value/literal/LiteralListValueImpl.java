package cool.klass.model.meta.domain.value.literal;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.Type;
import cool.klass.model.meta.domain.api.Type.TypeGetter;
import cool.klass.model.meta.domain.api.value.literal.LiteralListValue;
import cool.klass.model.meta.domain.api.value.literal.LiteralValue;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class LiteralListValueImpl extends AbstractLiteralValue implements LiteralListValue
{
    private final Type type;

    private ImmutableList<LiteralValue> literalValues;

    private LiteralListValueImpl(
            @Nonnull ParserRuleContext elementContext,
            Optional<Element> macroElement,
            Type type)
    {
        super(elementContext, macroElement);
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

    public static final class LiteralListValueBuilder extends AbstractLiteralValueBuilder<LiteralListValueImpl>
    {
        private final TypeGetter                                    typeBuilder;
        private       ImmutableList<AbstractLiteralValueBuilder<?>> literalValueBuilders;

        public LiteralListValueBuilder(
                @Nonnull ParserRuleContext elementContext,
                Optional<ElementBuilder<?>> macroElement,
                TypeGetter typeBuilder)
        {
            super(elementContext, macroElement);
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
                    this.macroElement.map(ElementBuilder::getElement),
                    this.typeBuilder.getType());
        }

        @Override
        protected void buildChildren()
        {
            this.element.setLiteralValues(this.literalValueBuilders.collect(AbstractLiteralValueBuilder::build));
        }
    }
}
