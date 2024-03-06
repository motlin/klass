package cool.klass.model.meta.domain;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class AbstractOrdinalElement
        extends AbstractElement
{
    protected final int ordinal;

    public AbstractOrdinalElement(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            int ordinal)
    {
        super(elementContext, macroElement, sourceCode);
        this.ordinal = ordinal;
    }

    public int getOrdinal()
    {
        return this.ordinal;
    }

    public abstract static class OrdinalElementBuilder<BuiltElement extends AbstractOrdinalElement>
            extends ElementBuilder<BuiltElement>
    {
        protected final int ordinal;

        protected OrdinalElementBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                int ordinal)
        {
            super(elementContext, macroElement, sourceCode);
            this.ordinal = ordinal;
        }
    }
}
