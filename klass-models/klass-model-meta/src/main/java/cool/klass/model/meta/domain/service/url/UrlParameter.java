package cool.klass.model.meta.domain.service.url;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.DataType;
import cool.klass.model.meta.domain.Multiplicity;
import cool.klass.model.meta.domain.NamedElement;
import cool.klass.model.meta.domain.service.url.Url.UrlBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class UrlParameter extends NamedElement
{
    @Nonnull
    protected final Multiplicity multiplicity;
    @Nonnull
    protected final Url          url;

    protected UrlParameter(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull Multiplicity multiplicity,
            @Nonnull Url url)
    {
        super(elementContext, nameContext, name, ordinal);
        this.url = Objects.requireNonNull(url);
        this.multiplicity = Objects.requireNonNull(multiplicity);
    }

    @Nonnull
    public abstract DataType getType();

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
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull Multiplicity multiplicity,
                @Nonnull UrlBuilder urlBuilder)
        {
            super(elementContext, nameContext, name, ordinal);
            this.multiplicity = Objects.requireNonNull(multiplicity);
            this.urlBuilder = Objects.requireNonNull(urlBuilder);
        }

        @Nonnull
        public abstract UrlParameter build();

        @Nonnull
        public abstract UrlParameter getUrlParameter();
    }
}
