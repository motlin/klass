package cool.klass.model.meta.domain.service.url;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.EnumerationImpl;
import cool.klass.model.meta.domain.EnumerationImpl.EnumerationBuilder;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.service.url.EnumerationUrlPathParameter;
import cool.klass.model.meta.domain.service.url.UrlImpl.UrlBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class EnumerationUrlPathParameterImpl extends AbstractUrlPathParameter implements EnumerationUrlPathParameter
{
    @Nonnull
    private final EnumerationImpl enumeration;

    private EnumerationUrlPathParameterImpl(
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

    public static final class EnumerationUrlPathParameterBuilder extends UrlPathParameterBuilder
    {
        @Nonnull
        private final EnumerationBuilder              enumerationBuilder;
        private       EnumerationUrlPathParameterImpl enumerationUrlPathParameter;

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
        public EnumerationUrlPathParameterImpl build()
        {
            if (this.enumerationUrlPathParameter != null)
            {
                throw new IllegalStateException();
            }
            this.enumerationUrlPathParameter = new EnumerationUrlPathParameterImpl(
                    this.elementContext,
                    this.inferred,
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.multiplicity,
                    this.urlBuilder.getUrl(),
                    this.enumerationBuilder.getElement());
            return this.enumerationUrlPathParameter;
        }

        @Override
        @Nonnull
        public EnumerationUrlPathParameterImpl getUrlParameter()
        {
            return Objects.requireNonNull(this.enumerationUrlPathParameter);
        }
    }
}
