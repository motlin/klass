package cool.klass.model.meta.domain.property;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractNamedElement;
import cool.klass.model.meta.domain.api.property.AssociationEndModifier;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class AssociationEndModifierImpl extends AbstractNamedElement implements AssociationEndModifier
{
    private final AssociationEndImpl associationEnd;

    private AssociationEndModifierImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            AssociationEndImpl associationEnd)
    {
        super(elementContext, inferred, nameContext, name, ordinal);
        this.associationEnd = associationEnd;
    }

    @Override
    public AssociationEndImpl getAssociationEnd()
    {
        return this.associationEnd;
    }

    public static final class AssociationEndModifierBuilder extends NamedElementBuilder<AssociationEndModifierImpl>
    {
        private final AssociationEndBuilder associationEndBuilder;

        public AssociationEndModifierBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                AssociationEndBuilder associationEndBuilder)
        {
            super(elementContext, inferred, nameContext, name, ordinal);
            this.associationEndBuilder = associationEndBuilder;
        }

        @Override
        @Nonnull
        protected AssociationEndModifierImpl buildUnsafe()
        {
            return new AssociationEndModifierImpl(
                    this.elementContext,
                    this.inferred,
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.associationEndBuilder.getElement());
        }
    }
}
