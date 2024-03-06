package cool.klass.model.meta.domain.value;

import cool.klass.model.meta.domain.Element;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class ExpressionValue extends Element
{
    protected ExpressionValue(ParserRuleContext elementContext)
    {
        super(elementContext);
    }

    public abstract void visit(ExpressionValueVisitor visitor);

    public abstract static class ExpressionValueBuilder extends ElementBuilder
    {
        protected ExpressionValueBuilder(ParserRuleContext elementContext)
        {
            super(elementContext);
        }

        public abstract ExpressionValue build();
    }
}
