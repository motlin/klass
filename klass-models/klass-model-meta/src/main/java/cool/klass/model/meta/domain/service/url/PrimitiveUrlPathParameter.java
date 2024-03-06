package cool.klass.model.meta.domain.service.url;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Multiplicity;
import cool.klass.model.meta.domain.property.PrimitiveType;
import cool.klass.model.meta.domain.service.url.Url.UrlBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class PrimitiveUrlPathParameter extends UrlPathParameter
{
    @Nonnull
    private final PrimitiveType primitiveType;

    private PrimitiveUrlPathParameter(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            @Nonnull Multiplicity multiplicity,
            @Nonnull Url url,
            @Nonnull PrimitiveType primitiveType)
    {
        super(elementContext, nameContext, name, multiplicity, url);
        this.primitiveType = Objects.requireNonNull(primitiveType);
    }

    @Override
    @Nonnull
    public PrimitiveType getType()
    {
        return this.primitiveType;
    }

    public static final class PrimitiveUrlPathParameterBuilder extends UrlPathParameterBuilder
    {
        @Nonnull
        private final PrimitiveType             primitiveType;
        private       PrimitiveUrlPathParameter primitiveUrlPathParameter;

        public PrimitiveUrlPathParameterBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                @Nonnull Multiplicity multiplicity,
                @Nonnull UrlBuilder urlBuilder,
                @Nonnull PrimitiveType primitiveType)
        {
            super(elementContext, nameContext, name, multiplicity, urlBuilder);
            this.primitiveType = Objects.requireNonNull(primitiveType);
        }

        @Nonnull
        @Override
        public PrimitiveUrlPathParameter build()
        {
            if (this.primitiveUrlPathParameter != null)
            {
                throw new IllegalStateException();
            }
            this.primitiveUrlPathParameter = new PrimitiveUrlPathParameter(
                    this.elementContext,
                    this.nameContext,
                    this.name,
                    this.multiplicity,
                    this.urlBuilder.getUrl(),
                    this.primitiveType);
            return this.primitiveUrlPathParameter;
        }

        @Nonnull
        @Override
        public PrimitiveUrlPathParameter getUrlParameter()
        {
            return Objects.requireNonNull(this.primitiveUrlPathParameter);
        }
    }
}
