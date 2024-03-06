package cool.klass.model.meta.domain.service.url;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.EnumerationImpl;
import cool.klass.model.meta.domain.EnumerationImpl.EnumerationBuilder;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.service.url.EnumerationUrlQueryParameter;
import cool.klass.model.meta.domain.service.url.UrlImpl.UrlBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class EnumerationUrlQueryParameterImpl extends UrlQueryParameterImpl implements EnumerationUrlQueryParameter
{
    @Nonnull
    private final EnumerationImpl enumeration;

    private EnumerationUrlQueryParameterImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull Multiplicity multiplicity,
            @Nonnull UrlImpl url,
            @Nonnull EnumerationImpl enumeration)
    {
        super(elementContext, inferred, nameContext, name, ordinal, multiplicity, url);
        this.enumeration = Objects.requireNonNull(enumeration);
    }

    @Override
    @Nonnull
    public EnumerationImpl getType()
    {
        return this.enumeration;
    }

    public static final class EnumerationUrlQueryParameterBuilder extends UrlQueryParameterBuilder
    {
        @Nonnull
        private final EnumerationBuilder               enumerationBuilder;
        private       EnumerationUrlQueryParameterImpl enumerationUrlQueryParameter;

        public EnumerationUrlQueryParameterBuilder(
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
        public EnumerationUrlQueryParameterImpl build()
        {
            if (this.enumerationUrlQueryParameter != null)
            {
                throw new IllegalStateException();
            }
            this.enumerationUrlQueryParameter = new EnumerationUrlQueryParameterImpl(
                    this.elementContext,
                    this.inferred,
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.multiplicity,
                    this.urlBuilder.getUrl(),
                    this.enumerationBuilder.getElement());
            return this.enumerationUrlQueryParameter;
        }

        @Nonnull
        @Override
        public EnumerationUrlQueryParameterImpl getUrlParameter()
        {
            return Objects.requireNonNull(this.enumerationUrlQueryParameter);
        }
    }
}
