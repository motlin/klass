package cool.klass.model.meta.domain.value;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.api.value.VariableReference;
import cool.klass.model.meta.domain.parameter.ParameterImpl;
import cool.klass.model.meta.domain.parameter.ParameterImpl.ParameterBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class VariableReferenceImpl
        extends AbstractExpressionValue
        implements VariableReference
{
    @Nonnull
    private final ParameterImpl parameter;

    private VariableReferenceImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nonnull Optional<SourceCode> sourceCode,
            @Nonnull ParameterImpl parameter)
    {
        super(elementContext, macroElement, sourceCode);
        this.parameter = Objects.requireNonNull(parameter);
    }

    @Override
    @Nonnull
    public ParameterImpl getParameter()
    {
        return this.parameter;
    }

    public static final class VariableReferenceBuilder
            extends AbstractExpressionValueBuilder<VariableReferenceImpl>
    {
        @Nonnull
        private final ParameterBuilder parameterBuilder;

        public VariableReferenceBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull Optional<SourceCodeBuilder> sourceCode,
                @Nonnull ParameterBuilder parameterBuilder)
        {
            super(elementContext, macroElement, sourceCode);
            this.parameterBuilder = Objects.requireNonNull(parameterBuilder);
        }

        @Override
        @Nonnull
        protected VariableReferenceImpl buildUnsafe()
        {
            return new VariableReferenceImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.map(SourceCodeBuilder::build),
                    this.parameterBuilder.getElement());
        }
    }
}
