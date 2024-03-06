package cool.klass.model.converter.compiler.state;

import java.util.LinkedHashMap;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.projection.AntlrProjection;
import cool.klass.model.converter.compiler.state.service.AntlrServiceGroup;
import cool.klass.model.meta.domain.AssociationImpl.AssociationBuilder;
import cool.klass.model.meta.domain.DomainModelImpl.DomainModelBuilder;
import cool.klass.model.meta.domain.EnumerationImpl.EnumerationBuilder;
import cool.klass.model.meta.domain.InterfaceImpl.InterfaceBuilder;
import cool.klass.model.meta.domain.KlassImpl.KlassBuilder;
import cool.klass.model.meta.domain.TopLevelElement.TopLevelElementBuilder;
import cool.klass.model.meta.domain.projection.ProjectionImpl.ProjectionBuilder;
import cool.klass.model.meta.domain.service.ServiceGroupImpl.ServiceGroupBuilder;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.InterfaceDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceGroupDeclarationContext;
import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class AntlrDomainModel
{
    private final MutableList<AntlrTopLevelElement> topLevelElementStates = Lists.mutable.empty();
    private final MutableList<AntlrEnumeration>     enumerationStates     = Lists.mutable.empty();
    private final MutableList<AntlrInterface>       interfaceStates       = Lists.mutable.empty();
    private final MutableList<AntlrClass>           classStates           = Lists.mutable.empty();
    private final MutableList<AntlrAssociation>     associationStates     = Lists.mutable.empty();
    private final MutableList<AntlrProjection>      projectionStates      = Lists.mutable.empty();
    private final MutableList<AntlrServiceGroup>    serviceGroupStates    = Lists.mutable.empty();

    private final MutableOrderedMap<EnumerationDeclarationContext, AntlrEnumeration>   enumerationsByContext  =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<ClassDeclarationContext, AntlrClass>               classesByContext       =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<InterfaceDeclarationContext, AntlrInterface>       interfacesByContext    =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<AssociationDeclarationContext, AntlrAssociation>   associationsByContext  =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<ServiceGroupDeclarationContext, AntlrServiceGroup> serviceGroupsByContext =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    private final MutableOrderedMap<String, AntlrEnumeration> enumerationsByName = OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<String, AntlrInterface>   interfacesByName   = OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<String, AntlrClass>       classesByName      = OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<String, AntlrAssociation> associationsByName = OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<String, AntlrProjection>  projectionsByName  = OrderedMapAdapter.adapt(new LinkedHashMap<>());

    // TODO: Or instead of embedding services inside classes, turn them into named elements. The name of the group can become the name of the resource. The group could have a base url.
    private final MutableOrderedMap<AntlrClass, AntlrServiceGroup> serviceGroupsByClass =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    public int getNumTopLevelElements()
    {
        return this.topLevelElementStates.size();
    }

    public void exitEnumerationDeclaration(@Nonnull AntlrEnumeration enumerationState)
    {
        this.topLevelElementStates.add(enumerationState);
        this.enumerationStates.add(enumerationState);
        this.enumerationsByName.compute(
                enumerationState.getName(),
                (name, builder) -> builder == null
                        ? enumerationState
                        : AntlrEnumeration.AMBIGUOUS);

        AntlrEnumeration duplicate = this.enumerationsByContext.put(
                enumerationState.getElementContext(),
                enumerationState);
        if (duplicate != null)
        {
            throw new AssertionError();
        }
    }

    public void defineInterface(@Nonnull AntlrInterface interfaceState)
    {
        this.topLevelElementStates.add(interfaceState);
        this.interfaceStates.add(interfaceState);
        this.interfacesByName.compute(
                interfaceState.getName(),
                (name, builder) -> builder == null
                        ? interfaceState
                        : AntlrInterface.AMBIGUOUS);

        AntlrInterface duplicate = this.interfacesByContext.put(interfaceState.getElementContext(), interfaceState);
        if (duplicate != null)
        {
            throw new AssertionError();
        }
    }

    public void defineClass(@Nonnull AntlrClass classState)
    {
        this.topLevelElementStates.add(classState);
        this.classStates.add(classState);
        this.classesByName.compute(
                classState.getName(),
                (name, builder) -> builder == null
                        ? classState
                        : AntlrClass.AMBIGUOUS);

        AntlrClass duplicate = this.classesByContext.put(classState.getElementContext(), classState);
        if (duplicate != null)
        {
            throw new AssertionError();
        }
    }

    public void exitAssociationDeclaration(@Nonnull AntlrAssociation associationState)
    {
        this.topLevelElementStates.add(associationState);
        this.associationStates.add(associationState);
        this.associationsByName.compute(
                associationState.getName(),
                (name, builder) -> builder == null
                        ? associationState
                        : AntlrAssociation.AMBIGUOUS);

        AntlrAssociation duplicate = this.associationsByContext.put(
                associationState.getElementContext(),
                associationState);
        if (duplicate != null)
        {
            throw new AssertionError();
        }
    }

    public void exitProjectionDeclaration(@Nonnull AntlrProjection projectionState)
    {
        this.topLevelElementStates.add(projectionState);
        this.projectionStates.add(projectionState);
        this.projectionsByName.compute(
                projectionState.getName(),
                (name, builder) -> builder == null
                        ? projectionState
                        : AntlrProjection.AMBIGUOUS);
    }

    public void exitServiceGroupDeclaration(@Nonnull AntlrServiceGroup serviceGroupState)
    {
        this.topLevelElementStates.add(serviceGroupState);
        this.serviceGroupStates.add(serviceGroupState);
        this.serviceGroupsByClass.compute(
                serviceGroupState.getKlass(),
                (name, builder) -> builder == null
                        ? serviceGroupState
                        : AntlrServiceGroup.AMBIGUOUS);

        AntlrServiceGroup duplicate = this.serviceGroupsByContext.put(
                serviceGroupState.getElementContext(),
                serviceGroupState);
        if (duplicate != null)
        {
            throw new AssertionError();
        }
    }

    public AntlrEnumeration getEnumerationByName(String enumerationName)
    {
        return this.enumerationsByName.getIfAbsentValue(enumerationName, AntlrEnumeration.NOT_FOUND);
    }

    public AntlrInterface getInterfaceByName(String interfaceName)
    {
        return this.interfacesByName.getIfAbsentValue(interfaceName, AntlrInterface.NOT_FOUND);
    }

    public AntlrClass getClassByName(String className)
    {
        return this.classesByName.getIfAbsentValue(className, AntlrClass.NOT_FOUND);
    }

    public AntlrEnumeration getEnumerationByContext(EnumerationDeclarationContext context)
    {
        return this.enumerationsByContext.get(context);
    }

    public AntlrInterface getInterfaceByContext(InterfaceDeclarationContext context)
    {
        return this.interfacesByContext.get(context);
    }

    public AntlrClass getClassByContext(ClassDeclarationContext context)
    {
        return this.classesByContext.get(context);
    }

    public AntlrAssociation getAssociationByContext(AssociationDeclarationContext context)
    {
        return this.associationsByContext.get(context);
    }

    public AntlrProjection getProjectionByName(String projectionName)
    {
        return this.projectionsByName.getIfAbsentValue(projectionName, AntlrProjection.NOT_FOUND);
    }

    public AntlrServiceGroup getServiceGroupByContext(ServiceGroupDeclarationContext context)
    {
        return this.serviceGroupsByContext.get(context);
    }

    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        ImmutableList<String> topLevelNames = this.getTopLevelNames();

        ImmutableBag<String> duplicateTopLevelNames = topLevelNames
                .toBag()
                .selectByOccurrences(occurrences -> occurrences > 1)
                .toImmutable();

        for (AntlrTopLevelElement topLevelElementState : this.topLevelElementStates)
        {
            if (duplicateTopLevelNames.contains(topLevelElementState.getName()))
            {
                topLevelElementState.reportDuplicateTopLevelName(compilerErrorHolder);
            }
        }

        for (AntlrEnumeration enumerationState : this.enumerationStates)
        {
            enumerationState.reportNameErrors(compilerErrorHolder);
            enumerationState.reportErrors(compilerErrorHolder);
        }

        for (AntlrInterface interfaceState : this.interfaceStates)
        {
            interfaceState.reportNameErrors(compilerErrorHolder);
            interfaceState.reportErrors(compilerErrorHolder);
        }

        for (AntlrClass classState : this.classStates)
        {
            classState.reportNameErrors(compilerErrorHolder);
            classState.reportErrors(compilerErrorHolder);
        }

        for (AntlrAssociation associationState : this.associationStates)
        {
            associationState.reportNameErrors(compilerErrorHolder);
            associationState.reportErrors(compilerErrorHolder);
        }

        for (AntlrProjection projectionState : this.projectionStates)
        {
            projectionState.reportNameErrors(compilerErrorHolder);
            projectionState.reportErrors(compilerErrorHolder);
        }

        ImmutableBag<AntlrClass> duplicateServiceGroupKlasses = this.getDuplicateServiceGroupClasses();
        for (AntlrServiceGroup serviceGroupState : this.serviceGroupStates)
        {
            if (duplicateServiceGroupKlasses.contains(serviceGroupState.getKlass()))
            {
                serviceGroupState.reportDuplicateServiceGroupClass(compilerErrorHolder);
            }
            serviceGroupState.reportNameErrors(compilerErrorHolder);
            serviceGroupState.reportErrors(compilerErrorHolder);
        }
    }

    private ImmutableList<String> getTopLevelNames()
    {
        MutableList<String> topLevelNames = Lists.mutable.empty();
        this.enumerationStates.collect(AntlrEnumeration::getName, topLevelNames);
        this.interfaceStates.collect(AntlrInterface::getName, topLevelNames);
        this.classStates.collect(AntlrClass::getName, topLevelNames);
        this.associationStates.collect(AntlrAssociation::getName, topLevelNames);
        this.projectionStates.collect(AntlrProjection::getName, topLevelNames);
        return topLevelNames.toImmutable();
    }

    private ImmutableBag<AntlrClass> getDuplicateServiceGroupClasses()
    {
        return this.serviceGroupStates
                .collect(AntlrServiceGroup::getKlass)
                .toBag()
                .selectByOccurrences(occurrences -> occurrences > 1)
                .toImmutable();
    }

    @Nonnull
    public DomainModelBuilder build()
    {
        ImmutableList<EnumerationBuilder> enumerationBuilders = this.enumerationStates.collect(AntlrEnumeration::build).toImmutable();
        ImmutableList<InterfaceBuilder>   interfaceBuilders   = this.interfaceStates.collect(AntlrInterface::build1).toImmutable();
        ImmutableList<KlassBuilder>       classBuilders       = this.classStates.collect(AntlrClass::build1).toImmutable();

        ImmutableList<AssociationBuilder> associationBuilders = this.associationStates.collect(AntlrAssociation::build).toImmutable();
        this.interfaceStates.each(AntlrInterface::build2);
        this.classStates.each(AntlrClass::build2);

        ImmutableList<ProjectionBuilder>   projectionBuilders   = this.projectionStates.collect(AntlrProjection::build).toImmutable();
        ImmutableList<ServiceGroupBuilder> serviceGroupBuilders = this.serviceGroupStates.collect(AntlrServiceGroup::build).toImmutable();

        ImmutableList<TopLevelElementBuilder> topLevelElementBuilders = this.topLevelElementStates.collect(
                AntlrTopLevelElement::getElementBuilder).toImmutable();

        return new DomainModelBuilder(
                topLevelElementBuilders,
                enumerationBuilders,
                interfaceBuilders,
                classBuilders,
                associationBuilders,
                projectionBuilders,
                serviceGroupBuilders);
    }
}
