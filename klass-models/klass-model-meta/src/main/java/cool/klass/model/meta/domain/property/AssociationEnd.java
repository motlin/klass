package cool.klass.model.meta.domain.property;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Association;
import cool.klass.model.meta.domain.Association.AssociationBuilder;
import cool.klass.model.meta.domain.Klass;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import cool.klass.model.meta.domain.Multiplicity;
import org.antlr.v4.runtime.ParserRuleContext;

// TODO: Super class for reference-type-property?
public final class AssociationEnd extends Property<Klass>
{
    @Nonnull
    private final Association  owningAssociation;
    @Nonnull
    private final Multiplicity multiplicity;
    private final boolean      owned;

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

    public static class AssociationEndBuilder extends PropertyBuilder<Klass, KlassBuilder>
    {
        @Nonnull
        private final AssociationBuilder owningAssociation;
        @Nonnull
        private final Multiplicity       multiplicity;
        private final boolean            isOwned;

        private AssociationEnd associationEnd;

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
            return this.associationEnd;
        }

        public AssociationEnd getAssociationEnd()
        {
            return this.associationEnd;
        }
    }
}
