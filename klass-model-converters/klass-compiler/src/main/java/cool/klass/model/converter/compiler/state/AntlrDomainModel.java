package cool.klass.model.converter.compiler.state;

import java.util.LinkedHashMap;

import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.meta.domain.Association.AssociationBuilder;
import cool.klass.model.meta.domain.DomainModel.DomainModelBuilder;
import cool.klass.model.meta.domain.Enumeration.EnumerationBuilder;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class AntlrDomainModel
{
    private final MutableList<AntlrEnumeration> enumerationStates = Lists.mutable.empty();
    private final MutableList<AntlrClass>       classStates       = Lists.mutable.empty();
    private final MutableList<AntlrAssociation> associationStates = Lists.mutable.empty();

    private final MutableOrderedMap<EnumerationDeclarationContext, AntlrEnumeration> enumerationsByContext = OrderedMapAdapter.adapt(
            new LinkedHashMap<>());
    private final MutableOrderedMap<ClassDeclarationContext, AntlrClass>             classesByContext      = OrderedMapAdapter.adapt(
            new LinkedHashMap<>());
    private final MutableOrderedMap<AssociationDeclarationContext, AntlrAssociation> associationsByContext = OrderedMapAdapter.adapt(
            new LinkedHashMap<>());

    private final MutableOrderedMap<String, AntlrEnumeration> enumerationsByName = OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<String, AntlrClass>       classesByName      = OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<String, AntlrAssociation> associationsByName = OrderedMapAdapter.adapt(new LinkedHashMap<>());

    public void enterEnumerationDeclaration(AntlrEnumeration enumerationState)
    {
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

    public void enterClassDeclaration(AntlrClass classState)
    {
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

    public void enterAssociationDeclaration(AntlrAssociation associationState)
    {
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

    public AntlrEnumeration getEnumerationByName(String enumerationName)
    {
        return this.enumerationsByName.getIfAbsentValue(enumerationName, AntlrEnumeration.NOT_FOUND);
    }

    public AntlrClass getClassByName(String className)
    {
        return this.classesByName.getIfAbsentValue(className, AntlrClass.NOT_FOUND);
    }

    public AntlrEnumeration getEnumerationByContext(EnumerationDeclarationContext context)
    {
        return this.enumerationsByContext.get(context);
    }

    public AntlrClass getClassByContext(ClassDeclarationContext context)
    {
        return this.classesByContext.get(context);
    }

    public void reportErrors(CompilerErrorHolder compilerErrorHolder)
    {
        ImmutableList<String> topLevelNames = this.getTopLevelNames();

        ImmutableBag<String> duplicateTopLevelNames = topLevelNames
                .toBag()
                .selectByOccurrences(occurrences -> occurrences > 1)
                .toImmutable();

        for (AntlrEnumeration enumerationState : this.enumerationStates)
        {
            if (duplicateTopLevelNames.contains(enumerationState.getName()))
            {
                enumerationState.reportDuplicateTopLevelName(compilerErrorHolder);
            }
            enumerationState.reportErrors(compilerErrorHolder);
        }

        for (AntlrClass classState : this.classStates)
        {
            if (duplicateTopLevelNames.contains(classState.getName()))
            {
                classState.reportDuplicateTopLevelName(compilerErrorHolder);
            }
            classState.reportErrors(compilerErrorHolder);
        }

        for (AntlrAssociation associationState : this.associationStates)
        {
            if (duplicateTopLevelNames.contains(associationState.getName()))
            {
                associationState.reportDuplicateTopLevelName(compilerErrorHolder);
            }
            associationState.reportErrors(compilerErrorHolder);
        }
    }

    private ImmutableList<String> getTopLevelNames()
    {
        MutableList<String> topLevelNames = Lists.mutable.empty();
        this.enumerationStates.collect(AntlrEnumeration::getName, topLevelNames);
        this.classStates.collect(AntlrClass::getName, topLevelNames);
        this.associationStates.collect(AntlrAssociation::getName, topLevelNames);
        return topLevelNames.toImmutable();
    }

    public DomainModelBuilder build()
    {
        ImmutableList<EnumerationBuilder> enumerationBuilders = this.enumerationStates.collect(AntlrEnumeration::build).toImmutable();
        ImmutableList<KlassBuilder>       classBuilders       = this.classStates.collect(AntlrClass::build1).toImmutable();
        this.classStates.each(AntlrClass::build2);
        ImmutableList<AssociationBuilder> associationBuilders = this.associationStates.collect(AntlrAssociation::build).toImmutable();

        return new DomainModelBuilder(enumerationBuilders, classBuilders, associationBuilders);
    }
}
