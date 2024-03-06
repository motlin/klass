package cool.klass.model.meta.domain;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractClassifier.ClassifierBuilder;
import cool.klass.model.meta.domain.AssociationImpl.AssociationBuilder;
import cool.klass.model.meta.domain.EnumerationImpl.EnumerationBuilder;
import cool.klass.model.meta.domain.InterfaceImpl.InterfaceBuilder;
import cool.klass.model.meta.domain.KlassImpl.KlassBuilder;
import cool.klass.model.meta.domain.api.Association;
import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.Enumeration;
import cool.klass.model.meta.domain.api.Interface;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.NamedElement;
import cool.klass.model.meta.domain.api.TopLevelElement;
import cool.klass.model.meta.domain.api.TopLevelElement.TopLevelElementBuilder;
import cool.klass.model.meta.domain.api.projection.Projection;
import cool.klass.model.meta.domain.api.service.ServiceGroup;
import cool.klass.model.meta.domain.projection.AbstractProjectionParent.AbstractProjectionParentBuilder;
import cool.klass.model.meta.domain.projection.ProjectionImpl.ProjectionBuilder;
import cool.klass.model.meta.domain.service.ServiceGroupImpl.ServiceGroupBuilder;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.ImmutableMap;

public final class DomainModelImpl implements DomainModel
{
    @Nonnull
    private final ImmutableList<TopLevelElement> topLevelElements;
    @Nonnull
    private final ImmutableList<Enumeration>     enumerations;
    @Nonnull
    private final ImmutableList<Classifier>      classifiers;
    @Nonnull
    private final ImmutableList<Interface>       interfaces;
    @Nonnull
    private final ImmutableList<Klass>           classes;
    @Nonnull
    private final ImmutableList<Association>     associations;
    @Nonnull
    private final ImmutableList<Projection>      projections;
    @Nonnull
    private final ImmutableList<ServiceGroup>    serviceGroups;

    private final ImmutableMap<String, Enumeration> enumerationsByName;
    private final ImmutableMap<String, Interface>   interfacesByName;
    private final ImmutableMap<String, Klass>       classesByName;
    private final ImmutableMap<String, Association> associationsByName;
    private final ImmutableMap<String, Projection>  projectionsByName;
    private final ImmutableMap<Klass, ServiceGroup> serviceGroupsByKlass;
    private final ImmutableMap<String, Classifier>  classifiersByName;

    private DomainModelImpl(
            @Nonnull ImmutableList<TopLevelElement> topLevelElements,
            @Nonnull ImmutableList<Enumeration> enumerations,
            @Nonnull ImmutableList<Classifier> classifiers,
            @Nonnull ImmutableList<Interface> interfaces,
            @Nonnull ImmutableList<Klass> classes,
            @Nonnull ImmutableList<Association> associations,
            @Nonnull ImmutableList<Projection> projections,
            @Nonnull ImmutableList<ServiceGroup> serviceGroups)
    {
        this.topLevelElements = Objects.requireNonNull(topLevelElements);
        this.enumerations     = Objects.requireNonNull(enumerations);
        this.classifiers      = Objects.requireNonNull(classifiers);
        this.interfaces       = Objects.requireNonNull(interfaces);
        this.classes          = Objects.requireNonNull(classes);
        this.associations     = Objects.requireNonNull(associations);
        this.projections      = Objects.requireNonNull(projections);
        this.serviceGroups    = Objects.requireNonNull(serviceGroups);

        this.enumerationsByName   = this.enumerations.groupByUniqueKey(NamedElement::getName).toImmutable();
        this.classifiersByName    = this.classifiers.groupByUniqueKey(NamedElement::getName).toImmutable();
        this.interfacesByName     = this.interfaces.groupByUniqueKey(NamedElement::getName).toImmutable();
        this.classesByName        = this.classes.groupByUniqueKey(NamedElement::getName).toImmutable();
        this.associationsByName   = this.associations.groupByUniqueKey(NamedElement::getName).toImmutable();
        this.projectionsByName    = this.projections.groupByUniqueKey(NamedElement::getName).toImmutable();
        this.serviceGroupsByKlass = this.serviceGroups.groupByUniqueKey(ServiceGroup::getKlass).toImmutable();
    }

    @Override
    @Nonnull
    public ImmutableList<TopLevelElement> getTopLevelElements()
    {
        return this.topLevelElements;
    }

    @Override
    @Nonnull
    public ImmutableList<Enumeration> getEnumerations()
    {
        return this.enumerations;
    }

    @Override
    @Nonnull
    public ImmutableList<Classifier> getClassifiers()
    {
        return this.classifiers;
    }

    @Override
    @Nonnull
    public ImmutableList<Interface> getInterfaces()
    {
        return this.interfaces;
    }

    @Override
    @Nonnull
    public ImmutableList<Klass> getClasses()
    {
        return this.classes;
    }

    @Override
    @Nonnull
    public ImmutableList<Association> getAssociations()
    {
        return this.associations;
    }

    @Override
    @Nonnull
    public ImmutableList<Projection> getProjections()
    {
        return this.projections;
    }

    @Override
    @Nonnull
    public ImmutableList<ServiceGroup> getServiceGroups()
    {
        return this.serviceGroups;
    }

    @Override
    public Enumeration getEnumerationByName(String name)
    {
        return this.enumerationsByName.get(name);
    }

    @Override
    public Classifier getClassifierByName(String name)
    {
        return this.classifiersByName.get(name);
    }

    @Override
    public Interface getInterfaceByName(String name)
    {
        return this.interfacesByName.get(name);
    }

    @Override
    public Klass getClassByName(String name)
    {
        return this.classesByName.get(name);
    }

    @Override
    public Association getAssociationByName(String name)
    {
        return this.associationsByName.get(name);
    }

    @Override
    public Projection getProjectionByName(String name)
    {
        return this.projectionsByName.get(name);
    }

    public static final class DomainModelBuilder
    {
        @Nonnull
        private final ImmutableList<TopLevelElementBuilder> topLevelElementBuilders;
        @Nonnull
        private final ImmutableList<EnumerationBuilder>     enumerationBuilders;
        @Nonnull
        private final ImmutableList<ClassifierBuilder<?>>   classifierBuilders;
        @Nonnull
        private final ImmutableList<InterfaceBuilder>       interfaceBuilders;
        @Nonnull
        private final ImmutableList<KlassBuilder>           classBuilders;
        @Nonnull
        private final ImmutableList<AssociationBuilder>     associationBuilders;
        @Nonnull
        private final ImmutableList<ProjectionBuilder>      projectionBuilders;
        @Nonnull
        private final ImmutableList<ServiceGroupBuilder>    serviceGroupBuilders;

        public DomainModelBuilder(
                @Nonnull ImmutableList<TopLevelElementBuilder> topLevelElementBuilders,
                @Nonnull ImmutableList<EnumerationBuilder> enumerationBuilders,
                @Nonnull ImmutableList<ClassifierBuilder<?>> classifierBuilders,
                @Nonnull ImmutableList<InterfaceBuilder> interfaceBuilders,
                @Nonnull ImmutableList<KlassBuilder> classBuilders,
                @Nonnull ImmutableList<AssociationBuilder> associationBuilders,
                @Nonnull ImmutableList<ProjectionBuilder> projectionBuilders,
                @Nonnull ImmutableList<ServiceGroupBuilder> serviceGroupBuilders)
        {
            this.topLevelElementBuilders = Objects.requireNonNull(topLevelElementBuilders);
            this.enumerationBuilders     = Objects.requireNonNull(enumerationBuilders);
            this.classifierBuilders      = Objects.requireNonNull(classifierBuilders);
            this.interfaceBuilders       = Objects.requireNonNull(interfaceBuilders);
            this.classBuilders           = Objects.requireNonNull(classBuilders);
            this.associationBuilders     = Objects.requireNonNull(associationBuilders);
            this.projectionBuilders      = Objects.requireNonNull(projectionBuilders);
            this.serviceGroupBuilders    = Objects.requireNonNull(serviceGroupBuilders);
        }

        @Nonnull
        public DomainModelImpl build()
        {
            ImmutableList<Enumeration> enumerations = this.enumerationBuilders.<Enumeration>collect(EnumerationBuilder::build).toImmutable();
            ImmutableList<Interface>   interfaces   = this.interfaceBuilders.<Interface>collect(InterfaceBuilder::build).toImmutable();
            ImmutableList<Klass>       classes      = this.classBuilders.<Klass>collect(KlassBuilder::build).toImmutable();
            ImmutableList<Classifier>  classifiers  = this.classifierBuilders.collect(ClassifierBuilder::getElement);
            ImmutableList<Association> associations = this.associationBuilders.<Association>collect(AssociationBuilder::build).toImmutable();
            this.interfaceBuilders.each(InterfaceBuilder::build2);
            this.classBuilders.each(KlassBuilder::build2);

            ImmutableList<Projection> projections = this.projectionBuilders.<Projection>collect(ProjectionBuilder::build).toImmutable();
            this.projectionBuilders.each(AbstractProjectionParentBuilder::build2);
            ImmutableList<ServiceGroup> serviceGroups =
                    this.serviceGroupBuilders.<ServiceGroup>collect(ServiceGroupBuilder::build).toImmutable();
            ImmutableList<TopLevelElement> topLevelElements =
                    this.topLevelElementBuilders.collect(TopLevelElementBuilder::getElement);

            return new DomainModelImpl(
                    topLevelElements,
                    enumerations,
                    classifiers,
                    interfaces,
                    classes,
                    associations,
                    projections,
                    serviceGroups);
        }
    }
}
