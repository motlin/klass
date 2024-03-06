package cool.klass.model.meta.domain.service.url;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.service.url.UrlQueryParameter;
import cool.klass.model.meta.domain.service.url.UrlImpl.UrlBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AbstractUrlQueryParameter extends AbstractUrlParameter implements UrlQueryParameter
{
    protected AbstractUrlQueryParameter(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull Multiplicity multiplicity,
            @Nonnull UrlImpl url)
    {
        super(elementContext, inferred, nameContext, name, ordinal, multiplicity, url);
    }

    public abstract static class AbstractUrlQueryParameterBuilder<BuiltElement extends AbstractUrlQueryParameter> extends AbstractUrlParameterBuilder<BuiltElement>
    {
        protected AbstractUrlQueryParameterBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull Multiplicity multiplicity,
                @Nonnull UrlBuilder urlBuilder)
        {
            super(elementContext, inferred, nameContext, name, ordinal, multiplicity, urlBuilder);
        }
    }
}
