package cool.klass.model.meta.domain;

import cool.klass.model.meta.domain.Klass.KlassBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

// TODO: Super class for reference-type-property?
public final class AssociationEnd extends Property<Klass>
{
    private final Multiplicity multiplicity;
    private final boolean      owned;

    private AssociationEnd(
            ParserRuleContext elementContext,
            ParserRuleContext nameContext,
            String name,
            Klass type,
            Klass owningKlass,
            Multiplicity multiplicity,
            boolean owned)
    {
        super(elementContext, nameContext, name, type, owningKlass);
        this.multiplicity = multiplicity;
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

    public static class AssociationEndBuilder extends PropertyBuilder<Klass, KlassBuilder>
    {
        private final Multiplicity   multiplicity;
        private final boolean        isOwned;
        private       AssociationEnd associationEnd;

        public AssociationEndBuilder(
                ParserRuleContext elementContext,
                ParserRuleContext nameContext,
                String name,
                KlassBuilder type,
                KlassBuilder owningKlassBuilder,
                Multiplicity multiplicity,
                boolean isOwned)
        {
            super(elementContext, nameContext, name, type, owningKlassBuilder);
            this.multiplicity = multiplicity;
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
