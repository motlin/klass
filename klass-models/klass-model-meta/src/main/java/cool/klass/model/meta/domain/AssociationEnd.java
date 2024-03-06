package cool.klass.model.meta.domain;

import java.util.Objects;

import cool.klass.model.meta.domain.Association.AssociationBuilder;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

// TODO: Super class for reference-type-property?
public final class AssociationEnd extends Property<Klass>
{
    private final Association  owningAssociation;
    private final Multiplicity multiplicity;
    private final boolean      owned;

    private AssociationEnd(
            ParserRuleContext elementContext,
            ParserRuleContext nameContext,
            String name,
            Klass type,
            Klass owningKlass,
            Association owningAssociation,
            Multiplicity multiplicity,
            boolean owned)
    {
        super(elementContext, nameContext, name, type, owningKlass);
        this.owningAssociation = Objects.requireNonNull(owningAssociation);
        this.multiplicity = Objects.requireNonNull(multiplicity);
        this.owned = owned;
    }

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

    public Association getOwningAssociation()
    {
        return this.owningAssociation;
    }

    public static class AssociationEndBuilder extends PropertyBuilder<Klass, KlassBuilder>
    {
        private final AssociationBuilder owningAssociation;
        private final Multiplicity       multiplicity;
        private final boolean            isOwned;

        private AssociationEnd associationEnd;

        public AssociationEndBuilder(
                ParserRuleContext elementContext,
                ParserRuleContext nameContext,
                String name,
                KlassBuilder type,
                KlassBuilder owningKlassBuilder,
                AssociationBuilder owningAssociation,
                Multiplicity multiplicity,
                boolean isOwned)
        {
            super(elementContext, nameContext, name, type, owningKlassBuilder);
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
