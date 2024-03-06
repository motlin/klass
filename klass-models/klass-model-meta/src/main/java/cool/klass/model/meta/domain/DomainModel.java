package cool.klass.model.meta.domain;

import java.util.Objects;

import cool.klass.model.meta.domain.Association.AssociationBuilder;
import cool.klass.model.meta.domain.Enumeration.EnumerationBuilder;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import org.eclipse.collections.api.list.ImmutableList;

public final class DomainModel
{
    private final ImmutableList<Enumeration> enumerations;
    private final ImmutableList<Klass>       klasses;
    private final ImmutableList<Association> associations;

    private DomainModel(
            ImmutableList<Enumeration> enumerations,
            ImmutableList<Klass> klasses,
            ImmutableList<Association> associations)
    {
        this.enumerations = enumerations;
        this.klasses = klasses;
        this.associations = associations;
    }

    public ImmutableList<Enumeration> getEnumerations()
    {
        return this.enumerations;
    }

    public ImmutableList<Klass> getKlasses()
    {
        return this.klasses;
    }

    public ImmutableList<Association> getAssociations()
    {
        return this.associations;
    }

    public static final class DomainModelBuilder
    {
        private final ImmutableList<EnumerationBuilder> enumerationBuilders;
        private final ImmutableList<KlassBuilder>       klassBuilders;
        private final ImmutableList<AssociationBuilder> associationBuilders;

        public DomainModelBuilder(
                ImmutableList<EnumerationBuilder> enumerationBuilders,
                ImmutableList<KlassBuilder> klassBuilders,
                ImmutableList<AssociationBuilder> associationBuilders)
        {
            this.enumerationBuilders = Objects.requireNonNull(enumerationBuilders);
            this.klassBuilders = Objects.requireNonNull(klassBuilders);
            this.associationBuilders = Objects.requireNonNull(associationBuilders);
        }

        public DomainModel build()
        {
            ImmutableList<Enumeration> enumerations = this.enumerationBuilders.collect(EnumerationBuilder::build).toImmutable();
            ImmutableList<Klass>       klasses      = this.klassBuilders.collect(KlassBuilder::build1).toImmutable();
            ImmutableList<Association> associations = this.associationBuilders.collect(AssociationBuilder::build).toImmutable();
            this.klassBuilders.each(KlassBuilder::build2);

            return new DomainModel(enumerations, klasses, associations);
        }
    }
}
