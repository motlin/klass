package cool.klass.model.meta.domain.value;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.service.url.UrlParameter;
import cool.klass.model.meta.domain.service.url.UrlParameter.UrlParameterBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class VariableReference extends ExpressionValue
{
    @Nonnull
    private final UrlParameter urlParameter;

    private VariableReference(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull UrlParameter urlParameter)
    {
        super(elementContext, inferred);
        this.urlParameter = Objects.requireNonNull(urlParameter);
    }

    @Nonnull
    public UrlParameter getUrlParameter()
    {
        return this.urlParameter;
    }

    @Override
    public void visit(@Nonnull ExpressionValueVisitor visitor)
    {
        visitor.visitVariableReference(this);
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
        public VariableReference build()
        {
            return new VariableReference(this.elementContext, this.inferred, this.urlParameterBuilder.getUrlParameter());
        }
    }
}
