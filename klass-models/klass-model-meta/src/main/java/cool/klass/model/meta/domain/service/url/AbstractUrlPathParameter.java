package cool.klass.model.meta.domain.service.url;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.service.url.UrlPathParameter;
import cool.klass.model.meta.domain.service.url.UrlImpl.UrlBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AbstractUrlPathParameter extends AbstractUrlParameter implements AbstractUrlPathSegment, UrlPathParameter
{
    protected AbstractUrlPathParameter(
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

    public abstract static class UrlPathParameterBuilder extends UrlParameterBuilder implements UrlPathSegmentBuilder
    {
        protected UrlPathParameterBuilder(
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

        @Nonnull
        @Override
        public abstract AbstractUrlPathParameter build();

        @Nonnull
        @Override
        public abstract AbstractUrlPathParameter getUrlParameter();
    }
}
