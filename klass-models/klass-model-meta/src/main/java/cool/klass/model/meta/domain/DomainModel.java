package cool.klass.model.meta.domain;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Association.AssociationBuilder;
import cool.klass.model.meta.domain.Enumeration.EnumerationBuilder;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import cool.klass.model.meta.domain.projection.Projection;
import cool.klass.model.meta.domain.projection.Projection.ProjectionBuilder;
import org.eclipse.collections.api.list.ImmutableList;

public final class DomainModel
{
    @Nonnull
    private final ImmutableList<Enumeration> enumerations;
    @Nonnull
    private final ImmutableList<Klass>       klasses;
    @Nonnull
    private final ImmutableList<Association> associations;
    @Nonnull
    private final ImmutableList<Projection>  projections;

    private DomainModel(
            @Nonnull ImmutableList<Enumeration> enumerations,
            @Nonnull ImmutableList<Klass> klasses,
            @Nonnull ImmutableList<Association> associations,
            @Nonnull ImmutableList<Projection> projections)
    {
        this.enumerations = Objects.requireNonNull(enumerations);
        this.klasses = Objects.requireNonNull(klasses);
        this.associations = Objects.requireNonNull(associations);
        this.projections = Objects.requireNonNull(projections);
    }

    @Nonnull
    public ImmutableList<Enumeration> getEnumerations()
    {
        return this.enumerations;
    }

    @Nonnull
    public ImmutableList<Klass> getKlasses()
    {
        return this.klasses;
    }

    @Nonnull
    public ImmutableList<Association> getAssociations()
    {
        return this.associations;
    }

    @Nonnull
    public ImmutableList<Projection> getProjections()
    {
        return this.projections;
    }

    public static final class DomainModelBuilder
    {
        @Nonnull
        private final ImmutableList<EnumerationBuilder> enumerationBuilders;
        @Nonnull
        private final ImmutableList<KlassBuilder>       klassBuilders;
        @Nonnull
        private final ImmutableList<AssociationBuilder> associationBuilders;
        @Nonnull
        private final ImmutableList<ProjectionBuilder>  projectionBuilders;

        public DomainModelBuilder(
                @Nonnull ImmutableList<EnumerationBuilder> enumerationBuilders,
                @Nonnull ImmutableList<KlassBuilder> klassBuilders,
                @Nonnull ImmutableList<AssociationBuilder> associationBuilders,
                @Nonnull ImmutableList<ProjectionBuilder> projectionBuilders)
        {
            this.enumerationBuilders = Objects.requireNonNull(enumerationBuilders);
            this.klassBuilders = Objects.requireNonNull(klassBuilders);
            this.associationBuilders = Objects.requireNonNull(associationBuilders);
            this.projectionBuilders = Objects.requireNonNull(projectionBuilders);
        }

        public DomainModel build()
        {
            ImmutableList<Enumeration> enumerations = this.enumerationBuilders.collect(EnumerationBuilder::build).toImmutable();
            ImmutableList<Klass>       klasses      = this.klassBuilders.collect(KlassBuilder::build1).toImmutable();
            ImmutableList<Association> associations = this.associationBuilders.collect(AssociationBuilder::build).toImmutable();
            this.klassBuilders.each(KlassBuilder::build2);
            ImmutableList<Projection> projections = this.projectionBuilders.collect(ProjectionBuilder::build).toImmutable();

            return new DomainModel(enumerations, klasses, associations, projections);
        }
    }
}
