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
    private final AssociationImpl                       owningAssociation;
    @Nonnull
    private final Multiplicity                          multiplicity;
    @Nonnull
    private final ImmutableList<AssociationEndModifier> associationEndModifiers;
    private final boolean                               owned;

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
            @Nonnull ImmutableList<AssociationEndModifier> associationEndModifiers,
            boolean owned)
    {
        super(elementContext, inferred, nameContext, name, ordinal, type, owningKlass);
        this.owningAssociation = Objects.requireNonNull(owningAssociation);
        this.multiplicity = Objects.requireNonNull(multiplicity);
        this.associationEndModifiers = Objects.requireNonNull(associationEndModifiers);
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
    public AssociationEnd getOpposite()
    {
        AssociationEnd sourceAssociationEnd = this.owningAssociation.getSourceAssociationEnd();
        AssociationEnd targetAssociationEnd = this.owningAssociation.getTargetAssociationEnd();
        if (this == sourceAssociationEnd)
        {
            return targetAssociationEnd;
        }
        if (this == targetAssociationEnd)
        {
            return sourceAssociationEnd;
        }
        throw new AssertionError();
    }

    @Override
    @Nonnull
    public AssociationImpl getOwningAssociation()
    {
        return this.owningAssociation;
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

    public static class AssociationEndBuilder extends PropertyBuilder<KlassImpl, KlassBuilder>
    {
        @Nonnull
        private final AssociationBuilder                           owningAssociation;
        @Nonnull
        private final Multiplicity                                 multiplicity;
        @Nonnull
        private final ImmutableList<AssociationEndModifierBuilder> associationEndModifierBuilders;
        private final boolean                                      isOwned;

        private AssociationEndImpl       associationEnd;
        @Nonnull
        private Optional<OrderByBuilder> orderByBuilder = Optional.empty();

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
                @Nonnull ImmutableList<AssociationEndModifierBuilder> associationEndModifierBuilders,
                boolean isOwned)
        {
            super(elementContext, inferred, nameContext, name, ordinal, type, owningKlassBuilder);
            this.owningAssociation = Objects.requireNonNull(owningAssociation);
            this.multiplicity = Objects.requireNonNull(multiplicity);
            this.associationEndModifierBuilders = Objects.requireNonNull(associationEndModifierBuilders);
            this.isOwned = isOwned;
        }

        public void setOrderByBuilder(@Nonnull Optional<OrderByBuilder> orderByBuilder)
        {
            this.orderByBuilder = Objects.requireNonNull(orderByBuilder);
        }

        @Override
        public AssociationEndImpl build()
        {
            if (this.associationEnd != null)
            {
                throw new IllegalStateException();
            }

            ImmutableList<AssociationEndModifier> associationEndModifiers =
                    this.associationEndModifierBuilders.collect(AssociationEndModifierBuilder::build);

            this.associationEnd = new AssociationEndImpl(
                    this.elementContext,
                    this.inferred,
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.typeBuilder.getElement(),
                    this.owningKlassBuilder.getElement(),
                    this.owningAssociation.getElement(),
                    this.multiplicity,
                    associationEndModifiers,
                    this.isOwned);

            Optional<OrderBy> orderBy = this.orderByBuilder.map(OrderByBuilder::build);
            this.associationEnd.setOrderBy(orderBy);

            return this.associationEnd;
        }

        public AssociationEndImpl getElement()
        {
            return this.associationEnd;
        }
    }
}
