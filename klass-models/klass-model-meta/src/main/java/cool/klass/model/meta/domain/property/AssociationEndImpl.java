package cool.klass.model.meta.domain.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.AssociationImpl;
import cool.klass.model.meta.domain.AssociationImpl.AssociationBuilder;
import cool.klass.model.meta.domain.KlassImpl;
import cool.klass.model.meta.domain.KlassImpl.KlassBuilder;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

// TODO: Super class for reference-type-property?
public final class AssociationEndImpl
        extends ReferencePropertyImpl<KlassImpl>
        implements AssociationEnd
{
    @Nonnull
    private final AssociationImpl owningAssociation;

    private AssociationEndImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull KlassImpl type,
            @Nonnull KlassImpl owningKlass,
            @Nonnull AssociationImpl owningAssociation,
            @Nonnull Multiplicity multiplicity,
            boolean owned)
    {
        super(
                elementContext,
                macroElement,
                sourceCode,
                nameContext,
                name,
                ordinal,
                type,
                owningKlass,
                multiplicity,
                owned);
        this.owningAssociation = Objects.requireNonNull(owningAssociation);
    }

    @Nonnull
    @Override
    public Klass getOwningClassifier()
    {
        return (Klass) super.getOwningClassifier();
    }

    @Override
    @Nonnull
    public AssociationImpl getOwningAssociation()
    {
        return this.owningAssociation;
    }

    @Override
    public String toString()
    {
        return String.format(
                "%s.%s: %s[%s]",
                this.getOwningClassifier().getName(),
                this.getName(),
                this.getType().getName(),
                this.multiplicity.getPrettyName());
    }

    public static final class AssociationEndBuilder
            extends ReferencePropertyBuilder<KlassImpl, KlassBuilder, AssociationEndImpl>
    {
        @Nonnull
        private final AssociationBuilder owningAssociation;

        public AssociationEndBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull KlassBuilder type,
                @Nonnull KlassBuilder owningKlassBuilder,
                @Nonnull AssociationBuilder owningAssociation,
                @Nonnull Multiplicity multiplicity,
                boolean isOwned)
        {
            super(
                    elementContext,
                    macroElement,
                    sourceCode,
                    nameContext,
                    name,
                    ordinal,
                    type,
                    owningKlassBuilder,
                    multiplicity,
                    isOwned);
            this.owningAssociation = Objects.requireNonNull(owningAssociation);
        }

        @Override
        @Nonnull
        protected AssociationEndImpl buildUnsafe()
        {
            return new AssociationEndImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.typeBuilder.getElement(),
                    (KlassImpl) this.owningClassifierBuilder.getElement(),
                    this.owningAssociation.getElement(),
                    this.multiplicity,
                    this.isOwned);
        }
    }
}
