package cool.klass.model.meta.domain.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.modifier.AssociationEndModifier;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class AssociationEndModifierImpl
        extends AbstractModifier
        implements AssociationEndModifier
{
    private AssociationEndModifierImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nonnull Optional<SourceCode> sourceCode,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AssociationEndImpl owningProperty)
    {
        super(elementContext, macroElement, sourceCode, nameContext, name, ordinal, owningProperty);
    }

    @Override
    public AssociationEnd getModifierOwner()
    {
        return (AssociationEnd) super.getModifierOwner();
    }

    public static final class AssociationEndModifierBuilder
            extends ModifierBuilder<AssociationEndModifierImpl>
    {
        @Nonnull
        private final AssociationEndBuilder owningPropertyBuilder;

        public AssociationEndModifierBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull Optional<SourceCodeBuilder> sourceCode,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull AssociationEndBuilder owningPropertyBuilder)
        {
            super(elementContext, macroElement, sourceCode, nameContext, name, ordinal);
            this.owningPropertyBuilder = Objects.requireNonNull(owningPropertyBuilder);
        }

        @Override
        @Nonnull
        protected AssociationEndModifierImpl buildUnsafe()
        {
            return new AssociationEndModifierImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.map(SourceCodeBuilder::build),
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.owningPropertyBuilder.getElement());
        }
    }
}
