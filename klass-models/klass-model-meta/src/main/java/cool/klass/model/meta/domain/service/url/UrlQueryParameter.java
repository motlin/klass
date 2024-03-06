package cool.klass.model.meta.domain.service.url;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Multiplicity;
import cool.klass.model.meta.domain.service.url.Url.UrlBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class UrlQueryParameter extends UrlParameter
{
    protected UrlQueryParameter(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull Multiplicity multiplicity,
            @Nonnull Url url)
    {
        super(elementContext, inferred, nameContext, name, ordinal, multiplicity, url);
    }

    public abstract static class UrlQueryParameterBuilder extends UrlParameterBuilder
    {
        protected UrlQueryParameterBuilder(
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

        @Override
        @Nonnull
        public abstract UrlQueryParameter build();
    }
}
