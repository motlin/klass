package cool.klass.model.meta.domain;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Association.AssociationBuilder;
import cool.klass.model.meta.domain.Enumeration.EnumerationBuilder;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import cool.klass.model.meta.domain.projection.Projection;
import cool.klass.model.meta.domain.projection.Projection.ProjectionBuilder;
import cool.klass.model.meta.domain.service.ServiceGroup;
import cool.klass.model.meta.domain.service.ServiceGroup.ServiceGroupBuilder;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.ImmutableMap;

public final class DomainModel
{
    @Nonnull
    private final ImmutableList<Enumeration>  enumerations;
    @Nonnull
    private final ImmutableList<Klass>        klasses;
    @Nonnull
    private final ImmutableList<Association>  associations;
    @Nonnull
    private final ImmutableList<Projection>   projections;
    @Nonnull
    private final ImmutableList<ServiceGroup> serviceGroups;

    private final ImmutableMap<String, Enumeration> enumerationsByName;
    private final ImmutableMap<String, Klass>       klassesByName;
    private final ImmutableMap<String, Association> associationsByName;
    private final ImmutableMap<String, Projection>  projectionsByName;
    private final ImmutableMap<Klass, ServiceGroup> serviceGroupsByKlass;

    private DomainModel(
            @Nonnull ImmutableList<Enumeration> enumerations,
            @Nonnull ImmutableList<Klass> klasses,
            @Nonnull ImmutableList<Association> associations,
            @Nonnull ImmutableList<Projection> projections,
            @Nonnull ImmutableList<ServiceGroup> serviceGroups)
    {
        this.enumerations = Objects.requireNonNull(enumerations);
        this.klasses = Objects.requireNonNull(klasses);
        this.associations = Objects.requireNonNull(associations);
        this.projections = Objects.requireNonNull(projections);
        this.serviceGroups = Objects.requireNonNull(serviceGroups);

        this.enumerationsByName = this.enumerations.groupByUniqueKey(NamedElement::getName).toImmutable();
        this.klassesByName = this.klasses.groupByUniqueKey(NamedElement::getName).toImmutable();
        this.associationsByName = this.associations.groupByUniqueKey(NamedElement::getName).toImmutable();
        this.projectionsByName = this.projections.groupByUniqueKey(NamedElement::getName).toImmutable();
        this.serviceGroupsByKlass = this.serviceGroups.groupByUniqueKey(ServiceGroup::getKlass).toImmutable();
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

    @Nonnull
    public ImmutableList<ServiceGroup> getServiceGroups()
    {
        return this.serviceGroups;
    }

    public Enumeration getEnumerationByName(String name)
    {
        return this.enumerationsByName.get(name);
    }

    public Klass getKlassByName(String name)
    {
        return this.klassesByName.get(name);
    }

    public Association getAssociationByName(String name)
    {
        return this.associationsByName.get(name);
    }

    public Projection getProjectionByName(String name)
    {
        return this.projectionsByName.get(name);
    }

    public static final class DomainModelBuilder
    {
        @Nonnull
        private final ImmutableList<EnumerationBuilder>  enumerationBuilders;
        @Nonnull
        private final ImmutableList<KlassBuilder>        klassBuilders;
        @Nonnull
        private final ImmutableList<AssociationBuilder>  associationBuilders;
        @Nonnull
        private final ImmutableList<ProjectionBuilder>   projectionBuilders;
        @Nonnull
        private final ImmutableList<ServiceGroupBuilder> serviceGroupBuilders;

        public DomainModelBuilder(
                @Nonnull ImmutableList<EnumerationBuilder> enumerationBuilders,
                @Nonnull ImmutableList<KlassBuilder> klassBuilders,
                @Nonnull ImmutableList<AssociationBuilder> associationBuilders,
                @Nonnull ImmutableList<ProjectionBuilder> projectionBuilders,
                @Nonnull ImmutableList<ServiceGroupBuilder> serviceGroupBuilders)
        {
            this.enumerationBuilders = Objects.requireNonNull(enumerationBuilders);
            this.klassBuilders = Objects.requireNonNull(klassBuilders);
            this.associationBuilders = Objects.requireNonNull(associationBuilders);
            this.projectionBuilders = Objects.requireNonNull(projectionBuilders);
            this.serviceGroupBuilders = Objects.requireNonNull(serviceGroupBuilders);
        }

        public DomainModel build()
        {
            ImmutableList<Enumeration> enumerations = this.enumerationBuilders.collect(EnumerationBuilder::build).toImmutable();
            ImmutableList<Klass>       klasses      = this.klassBuilders.collect(KlassBuilder::build1).toImmutable();
            ImmutableList<Association> associations = this.associationBuilders.collect(AssociationBuilder::build).toImmutable();
            this.klassBuilders.each(KlassBuilder::build2);
            ImmutableList<Projection>   projections   = this.projectionBuilders.collect(ProjectionBuilder::build).toImmutable();
            ImmutableList<ServiceGroup> serviceGroups = this.serviceGroupBuilders.collect(ServiceGroupBuilder::build).toImmutable();

            return new DomainModel(enumerations, klasses, associations, projections, serviceGroups);
        }
    }
}
