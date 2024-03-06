package cool.klass.model.meta.domain.property;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.NamedElement;
import org.antlr.v4.runtime.ParserRuleContext;

public final class AssociationEndModifier extends NamedElement
{
    private AssociationEndModifier(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal)
    {
        super(elementContext, nameContext, name, ordinal);
    }

    public static final class AssociationEndModifierBuilder extends NamedElementBuilder
    {
        public AssociationEndModifierBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal)
        {
            super(elementContext, nameContext, name, ordinal);
        }

        public AssociationEndModifier build()
        {
            return new AssociationEndModifier(this.elementContext, this.nameContext, this.name, this.ordinal);
        }
    }
}
