package cool.klass.model.meta.domain.operator;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.operator.InOperator;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.grammar.KlassParser.InOperatorContext;

public final class InOperatorImpl
        extends AbstractOperator
        implements InOperator
{
    private InOperatorImpl(
            @Nonnull InOperatorContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            @Nonnull String operatorText)
    {
        super(elementContext, macroElement, sourceCode, operatorText);
    }

    @Nonnull
    @Override
    public InOperatorContext getElementContext()
    {
        return (InOperatorContext) super.getElementContext();
    }

    public static final class InOperatorBuilder
            extends AbstractOperatorBuilder<InOperatorImpl>
    {
        public InOperatorBuilder(
                @Nonnull InOperatorContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                @Nonnull String operatorText)
        {
            super(elementContext, macroElement, sourceCode, operatorText);
        }

        @Override
        @Nonnull
        protected InOperatorImpl buildUnsafe()
        {
            return new InOperatorImpl(
                    (InOperatorContext) this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.operatorText);
        }
    }
}
