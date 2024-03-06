package cool.klass.model.meta.domain.operator;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.operator.Operator;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AbstractOperator
        extends AbstractElement
        implements Operator
{
    @Nonnull
    private final String operatorText;

    protected AbstractOperator(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            @Nonnull String operatorText)
    {
        super(elementContext, macroElement, sourceCode);
        this.operatorText = Objects.requireNonNull(operatorText);
    }

    @Override
    @Nonnull
    public String getOperatorText()
    {
        return this.operatorText;
    }

    public abstract static class AbstractOperatorBuilder<BuiltElement extends AbstractOperator>
            extends ElementBuilder<BuiltElement>
    {
        @Nonnull
        protected final String operatorText;

        protected AbstractOperatorBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                @Nonnull String operatorText)
        {
            super(elementContext, macroElement, sourceCode);
            this.operatorText = Objects.requireNonNull(operatorText);
        }
    }
}
