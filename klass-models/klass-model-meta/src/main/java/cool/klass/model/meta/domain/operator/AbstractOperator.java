package cool.klass.model.meta.domain.operator;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.api.operator.Operator;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AbstractOperator extends AbstractElement implements Operator
{
    @Nonnull
    private final String operatorText;

    protected AbstractOperator(@Nonnull ParserRuleContext elementContext, boolean inferred, @Nonnull String operatorText)
    {
        super(elementContext, inferred);
        this.operatorText = Objects.requireNonNull(operatorText);
    }

    @Override
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
        public abstract AbstractOperator build();
    }
}
