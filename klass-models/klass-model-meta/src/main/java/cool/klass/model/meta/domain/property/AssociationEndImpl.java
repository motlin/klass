package cool.klass.model.meta.domain.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AssociationImpl;
import cool.klass.model.meta.domain.AssociationImpl.AssociationBuilder;
import cool.klass.model.meta.domain.KlassImpl;
import cool.klass.model.meta.domain.KlassImpl.KlassBuilder;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.order.OrderBy;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.AssociationEndModifier;
import cool.klass.model.meta.domain.order.OrderByImpl.OrderByBuilder;
import cool.klass.model.meta.domain.property.AssociationEndModifierImpl.AssociationEndModifierBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

// TODO: Super class for reference-type-property?
public final class AssociationEndImpl extends AbstractProperty<KlassImpl> implements AssociationEnd
{
    @Nonnull
    private final AssociationImpl owningAssociation;
    @Nonnull
    private final Multiplicity    multiplicity;
    private final boolean         owned;

    private ImmutableList<AssociationEndModifier> associationEndModifiers;

    @Nonnull
    private Optional<OrderBy> orderBy = Optional.empty();

    private AssociationEndImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull KlassImpl type,
            @Nonnull KlassImpl owningKlass,
            @Nonnull AssociationImpl owningAssociation,
            @Nonnull Multiplicity multiplicity,
            boolean owned)
    {
        super(elementContext, inferred, nameContext, name, ordinal, type, owningKlass);
        this.owningAssociation = Objects.requireNonNull(owningAssociation);
        this.multiplicity = Objects.requireNonNull(multiplicity);
        this.owned = owned;
    }

    @Override
    @Nonnull
    public Multiplicity getMultiplicity()
    {
        return this.multiplicity;
    }

    @Override
    @Nonnull
    public Optional<OrderBy> getOrderBy()
    {
        return Objects.requireNonNull(this.orderBy);
    }

    private void setOrderBy(@Nonnull Optional<OrderBy> orderBy)
    {
        this.orderBy = Objects.requireNonNull(orderBy);
    }

    @Override
    @Nonnull
    public ImmutableList<AssociationEndModifier> getAssociationEndModifiers()
    {
        return this.associationEndModifiers;
    }

    @Override
    public boolean isOwned()
    {
        return this.owned;
    }

    @Override
    @Nonnull
    public AssociationImpl getOwningAssociation()
    {
        return this.owningAssociation;
    }

    private void setAssociationEndModifiers(ImmutableList<AssociationEndModifier> associationEndModifiers)
    {
        this.associationEndModifiers = associationEndModifiers;
    }

    public static final class AssociationEndBuilder extends PropertyBuilder<KlassImpl, KlassBuilder, AssociationEndImpl>
    {
        @Nonnull
        private final AssociationBuilder owningAssociation;
        @Nonnull
        private final Multiplicity       multiplicity;
        private final boolean            isOwned;

        @Nonnull
        private Optional<OrderByBuilder> orderByBuilder = Optional.empty();

        private ImmutableList<AssociationEndModifierBuilder> associationEndModifierBuilders;

        public AssociationEndBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull KlassBuilder type,
                @Nonnull KlassBuilder owningKlassBuilder,
                @Nonnull AssociationBuilder owningAssociation,
                @Nonnull Multiplicity multiplicity,
                boolean isOwned)
        {
            super(elementContext, inferred, nameContext, name, ordinal, type, owningKlassBuilder);
            this.owningAssociation = Objects.requireNonNull(owningAssociation);
            this.multiplicity = Objects.requireNonNull(multiplicity);
            this.isOwned = isOwned;
        }

        public void setOrderByBuilder(@Nonnull Optional<OrderByBuilder> orderByBuilder)
        {
            this.orderByBuilder = Objects.requireNonNull(orderByBuilder);
        }

        public void setAssociationEndModifierBuilders(ImmutableList<AssociationEndModifierBuilder> associationEndModifierBuilders)
        {
            this.associationEndModifierBuilders = associationEndModifierBuilders;
        }

        @Override
        @Nonnull
        protected AssociationEndImpl buildUnsafe()
        {
            return new AssociationEndImpl(
                    this.elementContext,
                    this.inferred,
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.typeBuilder.getElement(),
                    this.owningKlassBuilder.getElement(),
                    this.owningAssociation.getElement(),
                    this.multiplicity,
                    this.isOwned);
        }

        @Override
        protected void buildChildren()
        {
            ImmutableList<AssociationEndModifier> associationEndModifiers =
                    this.associationEndModifierBuilders.collect(AssociationEndModifierBuilder::build);
            this.element.setAssociationEndModifiers(associationEndModifiers);

            Optional<OrderBy> orderBy = this.orderByBuilder.map(OrderByBuilder::build);
            this.element.setOrderBy(orderBy);
        }
    }
}
