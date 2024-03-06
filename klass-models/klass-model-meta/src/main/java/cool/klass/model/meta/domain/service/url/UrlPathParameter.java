package cool.klass.model.meta.domain.service.url;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Multiplicity;
import cool.klass.model.meta.domain.service.url.Url.UrlBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class UrlPathParameter extends UrlParameter implements UrlPathSegment
{
    protected UrlPathParameter(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            @Nonnull Multiplicity multiplicity,
            @Nonnull Url url)
    {
        super(elementContext, nameContext, name, multiplicity, url);
    }

    public abstract static class UrlPathParameterBuilder extends UrlParameterBuilder implements UrlPathSegmentBuilder
    {
        protected UrlPathParameterBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                @Nonnull Multiplicity multiplicity,
                @Nonnull UrlBuilder urlBuilder)
        {
            super(elementContext, nameContext, name, multiplicity, urlBuilder);
        }

        @Nonnull
        @Override
        public abstract UrlPathParameter build();

        @Nonnull
        @Override
        public abstract UrlPathParameter getUrlParameter();
    }
}
