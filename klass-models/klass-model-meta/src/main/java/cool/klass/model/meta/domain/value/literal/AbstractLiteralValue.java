package cool.klass.model.meta.domain.value.literal;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.value.literal.LiteralValue;
import cool.klass.model.meta.domain.value.AbstractExpressionValue;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AbstractLiteralValue extends AbstractExpressionValue implements LiteralValue
{
    protected AbstractLiteralValue(@Nonnull ParserRuleContext elementContext, boolean inferred)
    {
        super(elementContext, inferred);
    }

    public abstract static class AbstractLiteralValueBuilder<BuiltElement extends AbstractLiteralValue> extends AbstractExpressionValueBuilder<BuiltElement>
    {
        protected AbstractLiteralValueBuilder(@Nonnull ParserRuleContext elementContext, boolean inferred)
        {
            super(elementContext, inferred);
        }
    }
}
