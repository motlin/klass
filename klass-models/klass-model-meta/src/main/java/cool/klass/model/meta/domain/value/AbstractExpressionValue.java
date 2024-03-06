package cool.klass.model.meta.domain.value;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.api.value.ExpressionValue;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AbstractExpressionValue extends AbstractElement implements ExpressionValue
{
    protected AbstractExpressionValue(@Nonnull ParserRuleContext elementContext, boolean inferred)
    {
        super(elementContext, inferred);
    }

    public abstract static class ExpressionValueBuilder extends ElementBuilder
    {
        protected ExpressionValueBuilder(@Nonnull ParserRuleContext elementContext, boolean inferred)
        {
            super(elementContext, inferred);
        }

        @Nonnull
        public abstract AbstractExpressionValue build();
    }
}
