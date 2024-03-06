package cool.klass.model.meta.domain.operator;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.operator.StringOperator;
import org.antlr.v4.runtime.ParserRuleContext;

public final class StringOperatorImpl extends AbstractOperator implements StringOperator
{
    private StringOperatorImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nonnull String operatorText)
    {
        super(elementContext, macroElement, operatorText);
    }

    public static final class StringOperatorBuilder extends AbstractOperatorBuilder<StringOperatorImpl>
    {
        public StringOperatorBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull String operatorText)
        {
            super(elementContext, macroElement, operatorText);
        }

        @Override
        @Nonnull
        protected StringOperatorImpl buildUnsafe()
        {
            return new StringOperatorImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.operatorText);
        }
    }
}
