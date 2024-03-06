package cool.klass.model.meta.domain.service.url;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractNamedElement;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.service.url.UrlParameter;
import cool.klass.model.meta.domain.service.url.UrlImpl.UrlBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AbstractUrlParameter extends AbstractNamedElement implements UrlParameter
{
    @Nonnull
    protected final Multiplicity multiplicity;
    @Nonnull
    protected final UrlImpl      url;

    protected AbstractUrlParameter(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull Multiplicity multiplicity,
            @Nonnull UrlImpl url)
    {
        super(elementContext, inferred, nameContext, name, ordinal);
        this.url = Objects.requireNonNull(url);
        this.multiplicity = Objects.requireNonNull(multiplicity);
    }

    @Override
    @Nonnull
    public Multiplicity getMultiplicity()
    {
        return this.multiplicity;
    }

    @Override
    public String toString()
    {
        return String.format("{%s}", this.getName());
    }

    public abstract static class UrlParameterBuilder extends NamedElementBuilder
    {
        @Nonnull
        protected final Multiplicity multiplicity;
        @Nonnull
        protected final UrlBuilder   urlBuilder;

        public UrlParameterBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull Multiplicity multiplicity,
                @Nonnull UrlBuilder urlBuilder)
        {
            super(elementContext, inferred, nameContext, name, ordinal);
            this.multiplicity = Objects.requireNonNull(multiplicity);
            this.urlBuilder = Objects.requireNonNull(urlBuilder);
        }

        @Nonnull
        public abstract AbstractUrlParameter build();

        @Nonnull
        public abstract AbstractUrlParameter getUrlParameter();
    }
}
