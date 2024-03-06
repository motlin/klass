package cool.klass.model.converter.compiler.state;

import java.util.LinkedHashMap;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.AntlrUtils;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.projection.AntlrProjection;
import cool.klass.model.converter.compiler.state.service.AntlrServiceGroup;
import cool.klass.model.meta.domain.AbstractClassifier.ClassifierBuilder;
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
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceGroupDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.TopLevelDeclarationContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class AntlrDomainModel
{
    private final MutableList<AntlrEnumeration>     enumerationStates     = Lists.mutable.empty();
    private final MutableList<AntlrClassifier>      classifierStates      = Lists.mutable.empty();
    private final MutableList<AntlrInterface>       interfaceStates       = Lists.mutable.empty();
    private final MutableList<AntlrClass>           classStates           = Lists.mutable.empty();
    private final MutableList<AntlrClass>           userClassStates       = Lists.mutable.empty();
    private final MutableList<AntlrAssociation>     associationStates     = Lists.mutable.empty();
    private final MutableList<AntlrProjection>      projectionStates      = Lists.mutable.empty();
    private final MutableList<AntlrServiceGroup>    serviceGroupStates    = Lists.mutable.empty();

    private final MutableOrderedMap<TopLevelDeclarationContext, AntlrTopLevelElement> topLevelElementsByContext        =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<TopLevelDeclarationContext, Integer>              topLevelElementOrdinalsByContext =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    private final MutableOrderedMap<EnumerationDeclarationContext, AntlrEnumeration>   enumerationsByContext  =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<ParserRuleContext, AntlrClassifier>                classifiersByContext   =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<InterfaceDeclarationContext, AntlrInterface>       interfacesByContext    =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<ClassDeclarationContext, AntlrClass>               classesByContext       =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<AssociationDeclarationContext, AntlrAssociation>   associationsByContext  =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<ProjectionDeclarationContext, AntlrProjection>     projectionsByContext   =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<ServiceGroupDeclarationContext, AntlrServiceGroup> serviceGroupsByContext =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    private final MutableOrderedMap<String, AntlrEnumeration> enumerationsByName = OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<String, AntlrClassifier>  classifiersByName  = OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<String, AntlrInterface>   interfacesByName   = OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<String, AntlrClass>       classesByName      = OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<String, AntlrAssociation> associationsByName = OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<String, AntlrProjection>  projectionsByName  = OrderedMapAdapter.adapt(new LinkedHashMap<>());

    // TODO: Or instead of embedding services inside classes, turn them into named elements. The name of the group can become the name of the resource. The group could have a base url.
    private final MutableOrderedMap<AntlrClass, AntlrServiceGroup> serviceGroupsByClass =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    public Optional<AntlrClass> getUserClassState()
    {
        if (this.userClassStates.size() == 1)
        {
            return this.userClassStates.getFirstOptional();
        }

        return Optional.empty();
    }

    public void enterTopLevelDeclaration(TopLevelDeclarationContext ctx)
    {
        Integer duplicate = this.topLevelElementOrdinalsByContext.put(
                ctx,
                this.topLevelElementOrdinalsByContext.size() + 1);

        if (duplicate != null)
        {
            throw new AssertionError();
        }
    }

    public void exitEnumerationDeclaration(@Nonnull AntlrEnumeration enumerationState)
    {
        this.enumerationStates.add(enumerationState);
        this.enumerationsByName.compute(
                enumerationState.getName(),
                (name, builder) -> builder == null
                        ? enumerationState
                        : AntlrEnumeration.AMBIGUOUS);

        AntlrEnumeration duplicateEnumeration = this.enumerationsByContext.put(
                enumerationState.getElementContext(),
                enumerationState);
        if (duplicateEnumeration != null)
        {
            throw new AssertionError();
        }

        TopLevelDeclarationContext topLevelDeclarationContext = AntlrUtils.getParentOfType(
                enumerationState.getElementContext(),
                TopLevelDeclarationContext.class);
        AntlrTopLevelElement duplicateTopLevelElement = this.topLevelElementsByContext.put(
                topLevelDeclarationContext,
                enumerationState);
        if (duplicateTopLevelElement != null)
        {
            throw new AssertionError();
        }
    }

    public void exitInterfaceDeclaration(@Nonnull AntlrInterface interfaceState)
    {
        this.classifierStates.add(interfaceState);
        this.interfaceStates.add(interfaceState);

        this.classifiersByName.compute(
                interfaceState.getName(),
                (name, builder) -> builder == null
                        ? interfaceState
                        : AntlrInterface.AMBIGUOUS);
        this.interfacesByName.compute(
                interfaceState.getName(),
                (name, builder) -> builder == null
                        ? interfaceState
                        : AntlrInterface.AMBIGUOUS);

        AntlrClassifier duplicateClassifier = this.classifiersByContext.put(
                interfaceState.getElementContext(),
                interfaceState);
        if (duplicateClassifier != null)
        {
            throw new AssertionError();
        }

        AntlrInterface duplicateInterface = this.interfacesByContext.put(
                interfaceState.getElementContext(),
                interfaceState);
        if (duplicateInterface != null)
        {
            throw new AssertionError();
        }

        TopLevelDeclarationContext topLevelDeclarationContext = AntlrUtils.getParentOfType(
                interfaceState.getElementContext(),
                TopLevelDeclarationContext.class);
        AntlrTopLevelElement duplicateTopLevelElement = this.topLevelElementsByContext.put(
                topLevelDeclarationContext,
                interfaceState);
        if (duplicateTopLevelElement != null)
        {
            throw new AssertionError();
        }
    }

    public void exitClassDeclaration(@Nonnull AntlrClass classState)
    {
        this.classifierStates.add(classState);
        this.classStates.add(classState);

        if (classState.isUser())
        {
            this.userClassStates.add(classState);
        }

        this.classifiersByName.compute(
                classState.getName(),
                (name, builder) -> builder == null
                        ? classState
                        : AntlrInterface.AMBIGUOUS);
        this.classesByName.compute(
                classState.getName(),
                (name, builder) -> builder == null
                        ? classState
                        : AntlrClass.AMBIGUOUS);

        AntlrClassifier duplicateClassifier = this.classifiersByContext.put(classState.getElementContext(), classState);
        if (duplicateClassifier != null)
        {
            throw new AssertionError();
        }

        AntlrClass duplicateClass = this.classesByContext.put(classState.getElementContext(), classState);
        if (duplicateClass != null)
        {
            throw new AssertionError();
        }

        TopLevelDeclarationContext topLevelDeclarationContext = AntlrUtils.getParentOfType(
                classState.getElementContext(),
                TopLevelDeclarationContext.class);
        AntlrTopLevelElement duplicateTopLevelElement = this.topLevelElementsByContext.put(
                topLevelDeclarationContext,
                classState);
        if (duplicateTopLevelElement != null)
        {
            throw new AssertionError();
        }
    }

    public void exitAssociationDeclaration(@Nonnull AntlrAssociation associationState)
    {
        this.associationStates.add(associationState);
        this.associationsByName.compute(
                associationState.getName(),
                (name, builder) -> builder == null
                        ? associationState
                        : AntlrAssociation.AMBIGUOUS);

        AntlrAssociation duplicateAssociation = this.associationsByContext.put(
                associationState.getElementContext(),
                associationState);
        if (duplicateAssociation != null)
        {
            throw new AssertionError();
        }

        TopLevelDeclarationContext topLevelDeclarationContext = AntlrUtils.getParentOfType(
                associationState.getElementContext(),
                TopLevelDeclarationContext.class);
        AntlrTopLevelElement duplicateTopLevelElement = this.topLevelElementsByContext.put(
                topLevelDeclarationContext,
                associationState);
        if (duplicateTopLevelElement != null)
        {
            throw new AssertionError();
        }
    }

    public void exitProjectionDeclaration(@Nonnull AntlrProjection projectionState)
    {
        this.projectionStates.add(projectionState);
        this.projectionsByName.compute(
                projectionState.getName(),
                (name, builder) -> builder == null
                        ? projectionState
                        : AntlrProjection.AMBIGUOUS);

        AntlrProjection duplicateProjection = this.projectionsByContext.put(
                projectionState.getElementContext(),
                projectionState);
        if (duplicateProjection != null)
        {
            throw new AssertionError();
        }

        TopLevelDeclarationContext topLevelDeclarationContext = AntlrUtils.getParentOfType(
                projectionState.getElementContext(),
                TopLevelDeclarationContext.class);
        AntlrTopLevelElement duplicateTopLevelElement = this.topLevelElementsByContext.put(
                topLevelDeclarationContext,
                projectionState);
        if (duplicateTopLevelElement != null)
        {
            throw new AssertionError();
        }
    }

    public void exitServiceGroupDeclaration(@Nonnull AntlrServiceGroup serviceGroupState)
    {
        this.serviceGroupStates.add(serviceGroupState);
        this.serviceGroupsByClass.compute(
                serviceGroupState.getKlass(),
                (name, builder) -> builder == null
                        ? serviceGroupState
                        : AntlrServiceGroup.AMBIGUOUS);

        AntlrServiceGroup duplicateServiceGroup = this.serviceGroupsByContext.put(
                serviceGroupState.getElementContext(),
                serviceGroupState);
        if (duplicateServiceGroup != null)
        {
            throw new AssertionError();
        }

        TopLevelDeclarationContext topLevelDeclarationContext = AntlrUtils.getParentOfType(
                serviceGroupState.getElementContext(),
                TopLevelDeclarationContext.class);
        AntlrTopLevelElement duplicateTopLevelElement = this.topLevelElementsByContext.put(
                topLevelDeclarationContext,
                serviceGroupState);
        if (duplicateTopLevelElement != null)
        {
            throw new AssertionError();
        }
    }

    public AntlrEnumeration getEnumerationByName(String enumerationName)
    {
        return this.enumerationsByName.getIfAbsentValue(enumerationName, AntlrEnumeration.NOT_FOUND);
    }

    public AntlrClassifier getClassifierByName(String classifierName)
    {
        return this.classifiersByName.getIfAbsentValue(classifierName, AntlrClass.NOT_FOUND);
    }

    public AntlrInterface getInterfaceByName(String interfaceName)
    {
        return this.interfacesByName.getIfAbsentValue(interfaceName, AntlrInterface.NOT_FOUND);
    }

    public AntlrClass getClassByName(String className)
    {
        return this.classesByName.getIfAbsentValue(className, AntlrClass.NOT_FOUND);
    }

    public AntlrProjection getProjectionByName(String projectionName)
    {
        return this.projectionsByName.getIfAbsentValue(projectionName, AntlrProjection.NOT_FOUND);
    }

    public AntlrTopLevelElement getTopLevelElementByContext(TopLevelDeclarationContext context)
    {
        return this.topLevelElementsByContext.get(context);
    }

    public Integer getTopLevelElementOrdinalByContext(TopLevelDeclarationContext context)
    {
        return this.topLevelElementOrdinalsByContext.get(context);
    }

    public AntlrEnumeration getEnumerationByContext(EnumerationDeclarationContext context)
    {
        return this.enumerationsByContext.get(context);
    }

    public AntlrClassifier getClassifierByContext(ParserRuleContext context)
    {
        return this.classifiersByContext.get(context);
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

    public AntlrProjection getProjectionByContext(ProjectionDeclarationContext context)
    {
        return this.projectionsByContext.get(context);
    }

    public AntlrServiceGroup getServiceGroupByContext(ServiceGroupDeclarationContext context)
    {
        return this.serviceGroupsByContext.get(context);
    }

    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        for (AntlrClass userClassState : this.userClassStates)
        {
            if (this.userClassStates.size() > 1)
            {
                userClassState.reportDuplicateUserClass(compilerErrorHolder);
            }

            userClassState.reportDuplicateUserProperties(compilerErrorHolder);
        }

        ImmutableList<String> topLevelNames = this.getTopLevelNames();

        ImmutableBag<String> duplicateTopLevelNames = topLevelNames
                .toBag()
                .selectByOccurrences(occurrences -> occurrences > 1)
                .toImmutable();

        this.topLevelElementsByContext
                .toSortedListBy(AntlrTopLevelElement::getOrdinal)
                .select(topLevelElementState -> duplicateTopLevelNames.contains(topLevelElementState.getName()))
                .forEachWith(AntlrTopLevelElement::reportDuplicateTopLevelName, compilerErrorHolder);

        for (AntlrEnumeration enumerationState : this.enumerationStates)
        {
            enumerationState.reportNameErrors(compilerErrorHolder);
            enumerationState.reportErrors(compilerErrorHolder);
        }

        for (AntlrClassifier classifierState : this.classifierStates)
        {
            classifierState.reportNameErrors(compilerErrorHolder);
            classifierState.reportErrors(compilerErrorHolder);
            if (this.userClassStates.isEmpty())
            {
                classifierState.reportAuditErrors(compilerErrorHolder);
            }
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
        this.classifierStates.collect(AntlrNamedElement::getName, topLevelNames);
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
        ImmutableList<EnumerationBuilder> enumerationBuilders = this.enumerationStates
                .collect(AntlrEnumeration::build)
                .toImmutable();
        ImmutableList<InterfaceBuilder> interfaceBuilders = this.interfaceStates
                .collect(AntlrInterface::build1)
                .toImmutable();
        ImmutableList<KlassBuilder> classBuilders = this.classStates
                .collect(AntlrClass::build1)
                .toImmutable();

        ImmutableList<ClassifierBuilder<?>> classifierBuilders = this.classifierStates.<ClassifierBuilder<?>>collect(
                AntlrClassifier::getElementBuilder).toImmutable();

        ImmutableList<AssociationBuilder> associationBuilders = this.associationStates
                .collect(AntlrAssociation::build)
                .toImmutable();
        this.interfaceStates.each(AntlrInterface::build2);
        this.classStates.each(AntlrClass::build2);

        ImmutableList<ProjectionBuilder> projectionBuilders = this.projectionStates
                .collect(AntlrProjection::build)
                .toImmutable();
        this.projectionStates.each(AntlrProjection::build2);
        ImmutableList<ServiceGroupBuilder> serviceGroupBuilders = this.serviceGroupStates
                .collect(AntlrServiceGroup::build)
                .toImmutable();

        ImmutableList<TopLevelElementBuilder> topLevelElementBuilders = this.topLevelElementsByContext
                .toSortedListBy(AntlrTopLevelElement::getOrdinal)
                .collect(AntlrTopLevelElement::getElementBuilder)
                .toImmutable();

        return new DomainModelBuilder(
                topLevelElementBuilders,
                enumerationBuilders,
                classifierBuilders,
                interfaceBuilders,
                classBuilders,
                associationBuilders,
                projectionBuilders,
                serviceGroupBuilders);
    }
}
