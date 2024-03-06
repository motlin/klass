package cool.klass.model.meta.domain.service.url;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Enumeration;
import cool.klass.model.meta.domain.Enumeration.EnumerationBuilder;
import cool.klass.model.meta.domain.Multiplicity;
import cool.klass.model.meta.domain.service.url.Url.UrlBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class EnumerationUrlQueryParameter extends UrlQueryParameter
{
    @Nonnull
    private final Enumeration enumeration;

    private EnumerationUrlQueryParameter(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            @Nonnull Multiplicity multiplicity,
            @Nonnull Url url,
            @Nonnull Enumeration enumeration)
    {
        super(elementContext, nameContext, name, multiplicity, url);
        this.enumeration = Objects.requireNonNull(enumeration);
    }

    @Override
    @Nonnull
    public Enumeration getType()
    {
        return this.enumeration;
    }

    public static final class EnumerationUrlQueryParameterBuilder extends UrlQueryParameterBuilder
    {
        @Nonnull
        private final EnumerationBuilder           enumerationBuilder;
        private       EnumerationUrlQueryParameter enumerationUrlQueryParameter;

        public EnumerationUrlQueryParameterBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                @Nonnull Multiplicity multiplicity,
                @Nonnull UrlBuilder urlBuilder,
                @Nonnull EnumerationBuilder enumerationBuilder)
        {
            super(elementContext, nameContext, name, multiplicity, urlBuilder);
            this.enumerationBuilder = Objects.requireNonNull(enumerationBuilder);
        }

        @Nonnull
        @Override
        public EnumerationUrlQueryParameter build()
        {
            if (this.enumerationUrlQueryParameter != null)
            {
                throw new IllegalStateException();
            }
            this.enumerationUrlQueryParameter = new EnumerationUrlQueryParameter(
                    this.elementContext,
                    this.nameContext,
                    this.name,
                    this.multiplicity,
                    this.urlBuilder.getUrl(),
                    this.enumerationBuilder.getEnumeration());
            return this.enumerationUrlQueryParameter;
        }

        @Nonnull
        @Override
        public EnumerationUrlQueryParameter getUrlParameter()
        {
            return Objects.requireNonNull(this.enumerationUrlQueryParameter);
        }
    }
}
