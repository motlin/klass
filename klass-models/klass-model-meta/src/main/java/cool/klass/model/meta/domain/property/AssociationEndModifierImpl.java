package cool.klass.model.meta.domain.property;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractNamedElement;
import cool.klass.model.meta.domain.api.property.AssociationEndModifier;
import org.antlr.v4.runtime.ParserRuleContext;

public final class AssociationEndModifierImpl extends AbstractNamedElement implements AssociationEndModifier
{
    private AssociationEndModifierImpl(
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

        public AssociationEndModifierImpl build()
        {
            return new AssociationEndModifierImpl(
                    this.elementContext,
                    this.inferred,
                    this.nameContext,
                    this.name,
                    this.ordinal);
        }
    }
}
