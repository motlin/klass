package cool.klass.model.meta.domain.operator;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Element;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class Operator extends Element
{
    @Nonnull
    private final String operatorText;

    protected Operator(@Nonnull ParserRuleContext elementContext, boolean inferred, @Nonnull String operatorText)
    {
        super(elementContext, inferred);
        this.operatorText = Objects.requireNonNull(operatorText);
    }

    public abstract void visit(OperatorVisitor visitor);

    @Nonnull
    public String getOperatorText()
    {
        return this.operatorText;
    }

    public abstract static class OperatorBuilder extends ElementBuilder
    {
        @Nonnull
        protected final String operatorText;

        protected OperatorBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull String operatorText)
        {
            super(elementContext, inferred);
            this.operatorText = Objects.requireNonNull(operatorText);
        }

        @Nonnull
        public abstract Operator build();
    }
}
