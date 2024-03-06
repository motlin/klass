package cool.klass.model.meta.domain.value;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.value.VariableReference;
import cool.klass.model.meta.domain.parameter.AbstractParameter;
import cool.klass.model.meta.domain.parameter.AbstractParameter.AbstractParameterBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class VariableReferenceImpl extends AbstractExpressionValue implements VariableReference
{
    @Nonnull
    private final AbstractParameter parameter;

    private VariableReferenceImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull AbstractParameter parameter)
    {
        super(elementContext, inferred);
        this.parameter = Objects.requireNonNull(parameter);
    }

    @Override
    @Nonnull
    public AbstractParameter getParameter()
    {
        return this.parameter;
    }

    public static final class VariableReferenceBuilder extends AbstractExpressionValueBuilder<VariableReferenceImpl>
    {
        @Nonnull
        private final AbstractParameterBuilder<?> parameterBuilder;

        public VariableReferenceBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull AbstractParameterBuilder<?> parameterBuilder)
        {
            super(elementContext, inferred);
            this.parameterBuilder = Objects.requireNonNull(parameterBuilder);
        }

        @Override
        @Nonnull
        protected VariableReferenceImpl buildUnsafe()
        {
            return new VariableReferenceImpl(
                    this.elementContext,
                    this.inferred,
                    this.parameterBuilder.getElement());
        }
    }
}
