package cool.klass.model.meta.domain.value;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
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
            Optional<Element> macroElement,
            @Nonnull ParameterImpl parameter)
    {
        super(elementContext, macroElement);
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
                Optional<ElementBuilder<?>> macroElement,
                @Nonnull ParameterBuilder parameterBuilder)
        {
            super(elementContext, macroElement);
            this.parameterBuilder = Objects.requireNonNull(parameterBuilder);
        }

        @Override
        @Nonnull
        protected VariableReferenceImpl buildUnsafe()
        {
            return new VariableReferenceImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.parameterBuilder.getElement());
        }
    }
}
