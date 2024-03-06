package cool.klass.model.meta.domain.value;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.api.value.ExpressionValue;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AbstractExpressionValue
        extends AbstractElement
        implements ExpressionValue
{
    protected AbstractExpressionValue(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode)
    {
        super(elementContext, macroElement, sourceCode);
    }

    public abstract static class AbstractExpressionValueBuilder<BuiltElement extends AbstractExpressionValue>
            extends ElementBuilder<BuiltElement>
    {
        protected AbstractExpressionValueBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode)
        {
            super(elementContext, macroElement, sourceCode);
        }
    }
}
