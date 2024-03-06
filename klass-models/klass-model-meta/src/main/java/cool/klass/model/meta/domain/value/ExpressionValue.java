package cool.klass.model.meta.domain.value;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Element;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class ExpressionValue extends Element
{
    protected ExpressionValue(@Nonnull ParserRuleContext elementContext)
    {
        super(elementContext);
    }

    public abstract void visit(ExpressionValueVisitor visitor);

    public abstract static class ExpressionValueBuilder extends ElementBuilder
    {
        protected ExpressionValueBuilder(@Nonnull ParserRuleContext elementContext)
        {
            super(elementContext);
        }

        @Nonnull
        public abstract ExpressionValue build();
    }
}
