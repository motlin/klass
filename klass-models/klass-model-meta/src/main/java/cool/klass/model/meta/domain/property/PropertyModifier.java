package cool.klass.model.meta.domain.property;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.NamedElement;
import org.antlr.v4.runtime.ParserRuleContext;

public final class PropertyModifier extends NamedElement
{
    private PropertyModifier(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal)
    {
        super(elementContext, nameContext, name, ordinal);
    }

    public static final class PropertyModifierBuilder extends NamedElementBuilder
    {
        public PropertyModifierBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal)
        {
            super(elementContext, nameContext, name, ordinal);
        }

        public PropertyModifier build()
        {
            return new PropertyModifier(this.elementContext, this.nameContext, this.name, this.ordinal);
        }
    }
}
