package cool.klass.model.meta.domain.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Association;
import cool.klass.model.meta.domain.Association.AssociationBuilder;
import cool.klass.model.meta.domain.Klass;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import cool.klass.model.meta.domain.Multiplicity;
import cool.klass.model.meta.domain.order.OrderBy;
import cool.klass.model.meta.domain.order.OrderBy.OrderByBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

// TODO: Super class for reference-type-property?
public final class AssociationEnd extends Property<Klass>
{
    @Nonnull
    private final Association  owningAssociation;
    @Nonnull
    private final Multiplicity multiplicity;
    private final boolean      owned;

    @Nonnull
    private Optional<OrderBy> orderBy = Optional.empty();

    private AssociationEnd(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull Klass type,
            @Nonnull Klass owningKlass,
            @Nonnull Association owningAssociation,
            @Nonnull Multiplicity multiplicity,
            boolean owned)
    {
        super(elementContext, nameContext, name, ordinal, type, owningKlass);
        this.owningAssociation = Objects.requireNonNull(owningAssociation);
        this.multiplicity = Objects.requireNonNull(multiplicity);
        this.owned = owned;
    }

    @Nonnull
    public Multiplicity getMultiplicity()
    {
        return this.multiplicity;
    }

    public boolean isOwned()
    {
        return this.owned;
    }

    public AssociationEnd getOpposite()
    {
        Association    owningAssociation    = this.getOwningAssociation();
        AssociationEnd sourceAssociationEnd = owningAssociation.getSourceAssociationEnd();
        AssociationEnd targetAssociationEnd = owningAssociation.getTargetAssociationEnd();
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

    @Nonnull
    public Association getOwningAssociation()
    {
        return this.owningAssociation;
    }

    @Nonnull
    public Optional<OrderBy> getOrderBy()
    {
        return Objects.requireNonNull(this.orderBy);
    }

    private void setOrderBy(@Nonnull Optional<OrderBy> orderBy)
    {
        this.orderBy = Objects.requireNonNull(orderBy);
    }

    public static class AssociationEndBuilder extends PropertyBuilder<Klass, KlassBuilder>
    {
        @Nonnull
        private final AssociationBuilder owningAssociation;
        @Nonnull
        private final Multiplicity       multiplicity;
        private final boolean            isOwned;

        private AssociationEnd           associationEnd;
        @Nonnull
        private Optional<OrderByBuilder> orderByBuilder = Optional.empty();

        public AssociationEndBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull KlassBuilder type,
                @Nonnull KlassBuilder owningKlassBuilder,
                @Nonnull AssociationBuilder owningAssociation,
                @Nonnull Multiplicity multiplicity,
                boolean isOwned)
        {
            super(elementContext, nameContext, name, ordinal, type, owningKlassBuilder);
            this.owningAssociation = Objects.requireNonNull(owningAssociation);
            this.multiplicity = Objects.requireNonNull(multiplicity);
            this.isOwned = isOwned;
        }

        public void setOrderByBuilder(@Nonnull Optional<OrderByBuilder> orderByBuilder)
        {
            this.orderByBuilder = Objects.requireNonNull(orderByBuilder);
        }

        @Override
        public AssociationEnd build()
        {
            if (this.associationEnd != null)
            {
                throw new IllegalStateException();
            }

            this.associationEnd = new AssociationEnd(
                    this.elementContext,
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.typeBuilder.getKlass(),
                    this.owningKlassBuilder.getKlass(),
                    this.owningAssociation.getAssociation(),
                    this.multiplicity,
                    this.isOwned);

            Optional<OrderBy> orderBy = this.orderByBuilder.map(OrderByBuilder::build);
            this.associationEnd.setOrderBy(orderBy);

            return this.associationEnd;
        }

        public AssociationEnd getAssociationEnd()
        {
            return this.associationEnd;
        }
    }
}
