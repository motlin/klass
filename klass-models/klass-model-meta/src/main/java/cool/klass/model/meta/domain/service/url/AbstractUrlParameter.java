package cool.klass.model.meta.domain.service.url;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.service.url.UrlParameter;
import cool.klass.model.meta.domain.parameter.AbstractParameter;
import cool.klass.model.meta.domain.service.url.UrlImpl.UrlBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AbstractUrlParameter extends AbstractParameter implements UrlParameter
{
    @Nonnull
    protected final UrlImpl url;

    protected AbstractUrlParameter(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull Multiplicity multiplicity, @Nonnull UrlImpl url)
    {
        super(elementContext, inferred, nameContext, name, ordinal, multiplicity);
        this.url = Objects.requireNonNull(url);
    }

    public abstract static class AbstractUrlParameterBuilder<BuiltElement extends AbstractUrlParameter> extends AbstractParameterBuilder<BuiltElement>
    {
        @Nonnull
        protected final UrlBuilder urlBuilder;

        protected AbstractUrlParameterBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull Multiplicity multiplicity,
                @Nonnull UrlBuilder urlBuilder)
        {
            super(elementContext, inferred, nameContext, name, ordinal, multiplicity);
            this.urlBuilder = Objects.requireNonNull(urlBuilder);
        }
    }
}
