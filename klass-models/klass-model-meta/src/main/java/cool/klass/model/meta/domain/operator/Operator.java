package cool.klass.model.meta.domain.operator;

import java.util.Objects;

import cool.klass.model.meta.domain.Element;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class Operator extends Element
{
    private final String operatorText;

    protected Operator(ParserRuleContext elementContext, String operatorText)
    {
        super(elementContext);
        this.operatorText = Objects.requireNonNull(operatorText);
    }

    public String getOperatorText()
    {
        return this.operatorText;
    }

    public abstract static class OperatorBuilder extends ElementBuilder
    {
        protected final String operatorText;

        protected OperatorBuilder(ParserRuleContext elementContext, String operatorText)
        {
            super(elementContext);
            this.operatorText = Objects.requireNonNull(operatorText);
        }

        public abstract Operator build();
    }
}
