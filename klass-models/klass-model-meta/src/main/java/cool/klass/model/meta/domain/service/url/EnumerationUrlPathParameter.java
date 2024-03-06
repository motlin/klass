package cool.klass.model.meta.domain.service.url;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Enumeration;
import cool.klass.model.meta.domain.Enumeration.EnumerationBuilder;
import cool.klass.model.meta.domain.Multiplicity;
import cool.klass.model.meta.domain.service.url.Url.UrlBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class EnumerationUrlPathParameter extends UrlPathParameter
{
    @Nonnull
    private final Enumeration enumeration;

    private EnumerationUrlPathParameter(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull Multiplicity multiplicity,
            @Nonnull Url url,
            @Nonnull Enumeration enumeration)
    {
        super(elementContext, inferred, nameContext, name, ordinal, multiplicity, url);
        this.enumeration = Objects.requireNonNull(enumeration);
    }

    @Override
    @Nonnull
    public Enumeration getType()
    {
        return this.enumeration;
    }

    public static final class EnumerationUrlPathParameterBuilder extends UrlPathParameterBuilder
    {
        @Nonnull
        private final EnumerationBuilder          enumerationBuilder;
        private       EnumerationUrlPathParameter enumerationUrlPathParameter;

        public EnumerationUrlPathParameterBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull Multiplicity multiplicity,
                @Nonnull UrlBuilder urlBuilder,
                @Nonnull EnumerationBuilder enumerationBuilder)
        {
            super(elementContext, inferred, nameContext, name, ordinal, multiplicity, urlBuilder);
            this.enumerationBuilder = Objects.requireNonNull(enumerationBuilder);
        }

        @Nonnull
        @Override
        public EnumerationUrlPathParameter build()
        {
            if (this.enumerationUrlPathParameter != null)
            {
                throw new IllegalStateException();
            }
            this.enumerationUrlPathParameter = new EnumerationUrlPathParameter(
                    this.elementContext,
                    this.inferred,
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.multiplicity,
                    this.urlBuilder.getUrl(),
                    this.enumerationBuilder.getEnumeration());
            return this.enumerationUrlPathParameter;
        }

        @Override
        @Nonnull
        public EnumerationUrlPathParameter getUrlParameter()
        {
            return Objects.requireNonNull(this.enumerationUrlPathParameter);
        }
    }
}
