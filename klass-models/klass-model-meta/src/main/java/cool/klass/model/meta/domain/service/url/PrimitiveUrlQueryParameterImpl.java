package cool.klass.model.meta.domain.service.url;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.domain.api.service.url.PrimitiveUrlQueryParameter;
import cool.klass.model.meta.domain.service.url.UrlImpl.UrlBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class PrimitiveUrlQueryParameterImpl extends UrlQueryParameterImpl implements PrimitiveUrlQueryParameter
{
    @Nonnull
    private final PrimitiveType primitiveType;

    private PrimitiveUrlQueryParameterImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull Multiplicity multiplicity,
            @Nonnull UrlImpl url,
            @Nonnull PrimitiveType primitiveType)
    {
        super(elementContext, inferred, nameContext, name, ordinal, multiplicity, url);
        this.primitiveType = Objects.requireNonNull(primitiveType);
    }

    @Override
    @Nonnull
    public PrimitiveType getType()
    {
        return this.primitiveType;
    }

    public static final class PrimitiveUrlQueryParameterBuilder extends UrlQueryParameterBuilder
    {
        @Nonnull
        private final PrimitiveType                  primitiveType;
        private       PrimitiveUrlQueryParameterImpl primitiveUrlQueryParameter;

        public PrimitiveUrlQueryParameterBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull Multiplicity multiplicity,
                @Nonnull UrlBuilder urlBuilder,
                @Nonnull PrimitiveType primitiveType)
        {
            super(elementContext, inferred, nameContext, name, ordinal, multiplicity, urlBuilder);
            this.primitiveType = Objects.requireNonNull(primitiveType);
        }

        @Nonnull
        @Override
        public PrimitiveUrlQueryParameterImpl build()
        {
            if (this.primitiveUrlQueryParameter != null)
            {
                throw new IllegalStateException();
            }
            this.primitiveUrlQueryParameter = new PrimitiveUrlQueryParameterImpl(
                    this.elementContext,
                    this.inferred,
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.multiplicity,
                    this.urlBuilder.getUrl(),
                    this.primitiveType);
            return this.primitiveUrlQueryParameter;
        }

        @Override
        @Nonnull
        public PrimitiveUrlQueryParameterImpl getUrlParameter()
        {
            return Objects.requireNonNull(this.primitiveUrlQueryParameter);
        }
    }
}
