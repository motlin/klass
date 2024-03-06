package cool.klass.model.meta.domain.value;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.value.VariableReference;
import cool.klass.model.meta.domain.parameter.ParameterImpl;
import cool.klass.model.meta.domain.parameter.ParameterImpl.ParameterBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class VariableReferenceImpl extends AbstractExpressionValue implements VariableReference
{
    @Nonnull
    private final ParameterImpl parameter;

    private VariableReferenceImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParameterImpl parameter)
    {
        super(elementContext, inferred);
        this.parameter = Objects.requireNonNull(parameter);
    }

    @Override
    @Nonnull
    public ParameterImpl getParameter()
    {
        return this.parameter;
    }

    public static final class VariableReferenceBuilder extends AbstractExpressionValueBuilder<VariableReferenceImpl>
    {
        @Nonnull
        private final ParameterBuilder parameterBuilder;

        public VariableReferenceBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParameterBuilder parameterBuilder)
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
