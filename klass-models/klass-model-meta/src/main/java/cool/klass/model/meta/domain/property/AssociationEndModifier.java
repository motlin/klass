package cool.klass.model.meta.domain.property;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.NamedElement;
import org.antlr.v4.runtime.ParserRuleContext;

public final class AssociationEndModifier extends NamedElement
{
    private AssociationEndModifier(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal)
    {
        super(elementContext, inferred, nameContext, name, ordinal);
    }

    public static final class AssociationEndModifierBuilder extends NamedElementBuilder
    {
        public AssociationEndModifierBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal)
        {
            super(elementContext, inferred, nameContext, name, ordinal);
        }

        public AssociationEndModifier build()
        {
            return new AssociationEndModifier(
                    this.elementContext,
                    this.inferred,
                    this.nameContext,
                    this.name,
                    this.ordinal);
        }
    }
}
