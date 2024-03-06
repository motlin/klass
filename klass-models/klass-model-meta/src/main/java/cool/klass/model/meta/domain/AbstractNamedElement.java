package cool.klass.model.meta.domain;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.NamedElement;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AbstractNamedElement extends AbstractElement implements NamedElement
{
    @Nonnull
    private final ParserRuleContext nameContext;
    @Nonnull
    private final String            name;
    private final int               ordinal;

    protected AbstractNamedElement(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nonnull Optional<SourceCode> sourceCode,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal)
    {
        super(elementContext, macroElement, sourceCode);
        this.nameContext = Objects.requireNonNull(nameContext);
        this.name        = Objects.requireNonNull(name);
        this.ordinal     = ordinal;
    }

    @Override
    @Nonnull
    public final String getName()
    {
        return this.name;
    }

    @Override
    public int getOrdinal()
    {
        return this.ordinal;
    }

    @Override
    public String toString()
    {
        return this.name;
    }

    public abstract static class NamedElementBuilder<BuiltElement extends AbstractNamedElement>
            extends ElementBuilder<BuiltElement>
    {
        @Nonnull
        protected final ParserRuleContext nameContext;
        @Nonnull
        protected final String            name;
        protected final int               ordinal;

        protected NamedElementBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull Optional<SourceCodeBuilder> sourceCode,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal)
        {
            super(elementContext, macroElement, sourceCode);
            this.nameContext = Objects.requireNonNull(nameContext);
            this.name        = Objects.requireNonNull(name);
            this.ordinal     = ordinal;
        }
    }
}
