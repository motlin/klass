package cool.klass.model.meta.domain.service.url;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.NamedElement;
import org.antlr.v4.runtime.ParserRuleContext;

public final class UrlConstant extends NamedElement implements UrlPathSegment
{
    private UrlConstant(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal)
    {
        super(elementContext, inferred, nameContext, name, ordinal);
    }

    @Override
    public String toString()
    {
        return this.getName();
    }

    public static final class UrlConstantBuilder extends NamedElementBuilder implements UrlPathSegmentBuilder
    {
        public UrlConstantBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal)
        {
            super(elementContext, inferred, nameContext, name, ordinal);
        }

        @Override
        public UrlConstant build()
        {
            return new UrlConstant(this.elementContext, this.inferred, this.nameContext, this.name, this.ordinal);
        }
    }
}
