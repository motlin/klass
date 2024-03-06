package cool.klass.model.meta.domain.value.literal;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.api.value.literal.LiteralValue;
import cool.klass.model.meta.domain.value.AbstractExpressionValue;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AbstractLiteralValue
        extends AbstractExpressionValue
        implements LiteralValue
{
    protected AbstractLiteralValue(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode)
    {
        super(elementContext, macroElement, sourceCode);
    }

    public abstract static class AbstractLiteralValueBuilder<BuiltElement extends AbstractLiteralValue>
            extends AbstractExpressionValueBuilder<BuiltElement>
    {
        protected AbstractLiteralValueBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode)
        {
            super(elementContext, macroElement, sourceCode);
        }
    }
}
