package cool.klass.model.converter.compiler.state;

import java.util.LinkedHashMap;

import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.meta.domain.Association.AssociationBuilder;
import cool.klass.model.meta.domain.DomainModel.DomainModelBuilder;
import cool.klass.model.meta.domain.Enumeration.EnumerationBuilder;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.api.map.OrderedMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class AntlrDomainModel
{
    private final MutableList<AntlrEnumeration> enumerationStates = Lists.mutable.empty();
    private final MutableList<AntlrClass>       classStates       = Lists.mutable.empty();
    private final MutableList<AntlrAssociation> associationStates = Lists.mutable.empty();

    private final MutableOrderedMap<String, AntlrEnumeration> enumerationsByName = OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<String, AntlrClass>       classesByName      = OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<String, AntlrAssociation> associationsByName = OrderedMapAdapter.adapt(new LinkedHashMap<>());

    private final CompilerErrorHolder compilerErrorHolder;

    public AntlrDomainModel(CompilerErrorHolder compilerErrorHolder)
    {
        this.compilerErrorHolder = compilerErrorHolder;
    }

    public void enterEnumerationDeclaration(AntlrEnumeration enumerationState)
    {
        this.enumerationStates.add(enumerationState);
        this.enumerationsByName.compute(
                enumerationState.getName(),
                (name, builder) -> builder == null
                        ? enumerationState
                        : AntlrEnumeration.AMBIGUOUS);
    }

    public void enterClassDeclaration(AntlrClass classState)
    {
        this.classStates.add(classState);
        this.classesByName.compute(
                classState.getName(),
                (name, builder) -> builder == null
                        ? classState
                        : AntlrClass.AMBIGUOUS);
    }

    public void enterAssociationDeclaration(AntlrAssociation associationState)
    {
        this.associationStates.add(associationState);
        this.associationsByName.compute(
                associationState.getName(),
                (name, builder) -> builder == null
                        ? associationState
                        : AntlrAssociation.AMBIGUOUS);
    }

    public AntlrEnumeration getEnumerationByName(String enumerationName)
    {
        return this.enumerationsByName.getIfAbsentValue(enumerationName, AntlrEnumeration.NOT_FOUND);
    }

    public AntlrClass getClassByName(String className)
    {
        return this.classesByName.getIfAbsentValue(className, AntlrClass.NOT_FOUND);
    }

    public ImmutableList<AntlrEnumeration> getEnumerationStates()
    {
        return this.enumerationStates.toImmutable();
    }

    public ImmutableList<AntlrClass> getClassStates()
    {
        return this.classStates.toImmutable();
    }

    public ImmutableList<AntlrAssociation> getAssociationStates()
    {
        return this.associationStates.toImmutable();
    }

    public OrderedMap<String, AntlrEnumeration> getEnumerationsByName()
    {
        return this.enumerationsByName;
    }

    public OrderedMap<String, AntlrClass> getClassesByName()
    {
        return this.classesByName;
    }

    public OrderedMap<String, AntlrAssociation> getAssociationsByName()
    {
        return this.associationsByName;
    }

    public void reportErrors()
    {
        // TODO
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
