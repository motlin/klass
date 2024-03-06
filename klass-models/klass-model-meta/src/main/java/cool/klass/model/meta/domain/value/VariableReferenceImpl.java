package cool.klass.model.meta.domain.value;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.value.VariableReference;
import cool.klass.model.meta.domain.service.url.AbstractUrlParameter;
import cool.klass.model.meta.domain.service.url.AbstractUrlParameter.UrlParameterBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class VariableReferenceImpl extends AbstractExpressionValue implements VariableReference
{
    @Nonnull
    private final AbstractUrlParameter urlParameter;

    private VariableReferenceImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull AbstractUrlParameter urlParameter)
    {
        super(elementContext, inferred);
        this.urlParameter = Objects.requireNonNull(urlParameter);
    }

    @Override
    @Nonnull
    public AbstractUrlParameter getUrlParameter()
    {
        return this.urlParameter;
    }

    public static final class VariableReferenceBuilder extends ExpressionValueBuilder
    {
        @Nonnull
        private final UrlParameterBuilder urlParameterBuilder;

        public VariableReferenceBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull UrlParameterBuilder urlParameterBuilder)
        {
            super(elementContext, inferred);
            this.urlParameterBuilder = Objects.requireNonNull(urlParameterBuilder);
        }

        @Nonnull
        @Override
        public VariableReferenceImpl build()
        {
            return new VariableReferenceImpl(
                    this.elementContext,
                    this.inferred,
                    this.urlParameterBuilder.getUrlParameter());
        }
    }
}
