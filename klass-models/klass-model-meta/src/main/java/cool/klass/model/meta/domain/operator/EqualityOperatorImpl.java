package cool.klass.model.meta.domain.operator;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.operator.EqualityOperator;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.grammar.KlassParser.EqualityOperatorContext;

public final class EqualityOperatorImpl
        extends AbstractOperator
        implements EqualityOperator
{
    private EqualityOperatorImpl(
            @Nonnull EqualityOperatorContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            @Nonnull String operatorText)
    {
        super(elementContext, macroElement, sourceCode, operatorText);
    }

    @Nonnull
    @Override
    public EqualityOperatorContext getElementContext()
    {
        return (EqualityOperatorContext) super.getElementContext();
    }

    public static final class EqualityOperatorBuilder
            extends AbstractOperatorBuilder<EqualityOperatorImpl>
    {
        public EqualityOperatorBuilder(
                @Nonnull EqualityOperatorContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                @Nonnull String operatorText)
        {
            super(elementContext, macroElement, sourceCode, operatorText);
        }

        @Override
        @Nonnull
        protected EqualityOperatorImpl buildUnsafe()
        {
            return new EqualityOperatorImpl(
                    (EqualityOperatorContext) this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.operatorText);
        }
    }
}
