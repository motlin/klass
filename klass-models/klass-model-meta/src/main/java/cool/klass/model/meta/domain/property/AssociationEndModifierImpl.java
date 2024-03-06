package cool.klass.model.meta.domain.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractNamedElement;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.property.AssociationEndModifier;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class AssociationEndModifierImpl extends AbstractNamedElement implements AssociationEndModifier
{
    private final AssociationEndImpl associationEnd;

    private AssociationEndModifierImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AssociationEndImpl associationEnd)
    {
        super(elementContext, macroElement, nameContext, name, ordinal);
        this.associationEnd = Objects.requireNonNull(associationEnd);
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
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull AssociationEndBuilder associationEndBuilder)
        {
            super(elementContext, macroElement, nameContext, name, ordinal);
            this.associationEndBuilder = Objects.requireNonNull(associationEndBuilder);
        }

        @Override
        @Nonnull
        protected AssociationEndModifierImpl buildUnsafe()
        {
            return new AssociationEndModifierImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.associationEndBuilder.getElement());
        }
    }
}
