package cool.klass.model.converter.compiler.state;

import java.util.LinkedHashMap;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.meta.domain.AssociationEnd.AssociationEndBuilder;
import cool.klass.model.meta.domain.DataTypeProperty.DataTypePropertyBuilder;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class AntlrClass extends AntlrPackageableElement
{
    public static final AntlrClass AMBIGUOUS = new AntlrClass(
            new ClassDeclarationContext(null, -1),
            null,
            true,
            new ParserRuleContext(),
            "ambiguous class",
            null);
    public static final AntlrClass NOT_FOUND = new AntlrClass(
            new ClassDeclarationContext(null, -1),
            null,
            true,
            new ParserRuleContext(),
            "not found class",
            null);

    private final MutableList<AntlrDataTypeProperty<?>>               dataTypePropertyStates   = Lists.mutable.empty();
    private final MutableOrderedMap<String, AntlrDataTypeProperty<?>> dataTypePropertiesByName = OrderedMapAdapter.adapt(
            new LinkedHashMap<>());

    private final MutableList<AntlrAssociationEnd>               associationEndStates  = Lists.mutable.empty();
    private final MutableOrderedMap<String, AntlrAssociationEnd> associationEndsByName = OrderedMapAdapter.adapt(new LinkedHashMap<>());

    private KlassBuilder klassBuilder;

    public AntlrClass(
            ClassDeclarationContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            ParserRuleContext nameContext,
            String name,
            String packageName)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, packageName);
    }

    public MutableList<AntlrDataTypeProperty<?>> getDataTypeProperties()
    {
        return this.dataTypePropertyStates;
    }

    public void enterDataTypeProperty(AntlrDataTypeProperty<?> antlrDataTypeProperty)
    {
        this.dataTypePropertyStates.add(antlrDataTypeProperty);
        this.dataTypePropertiesByName.compute(
                antlrDataTypeProperty.getName(),
                (name, builder) -> builder == null
                        ? antlrDataTypeProperty
                        : AntlrPrimitiveProperty.AMBIGUOUS);
    }

    public void enterAssociationEnd(AntlrAssociationEnd antlrAssociationEnd)
    {
        this.associationEndStates.add(antlrAssociationEnd);
        this.associationEndsByName.compute(
                antlrAssociationEnd.getName(),
                (name, builder) -> builder == null
                        ? antlrAssociationEnd
                        : AntlrAssociationEnd.AMBIGUOUS);
    }

    public KlassBuilder build1()
    {
        if (this.klassBuilder != null)
        {
            throw new IllegalStateException();
        }

        this.klassBuilder = new KlassBuilder(
                this.elementContext,
                this.nameContext,
                this.name,
                this.packageName);

        ImmutableList<DataTypePropertyBuilder<?, ?>> dataTypePropertyBuilders = this.dataTypePropertyStates
                .<DataTypePropertyBuilder<?, ?>>collect(AntlrDataTypeProperty::build)
                .toImmutable();

        this.klassBuilder.setDataTypePropertyBuilders(dataTypePropertyBuilders);
        return this.klassBuilder;
    }

    public void build2()
    {
        if (this.klassBuilder == null)
        {
            throw new IllegalStateException();
        }

        ImmutableList<AssociationEndBuilder> associationEndBuilders = this.associationEndStates
                .collect(AntlrAssociationEnd::build)
                .toImmutable();

        this.klassBuilder.setAssociationEndBuilders(associationEndBuilders);
    }

    public KlassBuilder getKlassBuilder()
    {
        return this.klassBuilder;
    }

    public void reportDuplicateTopLevelName(CompilerErrorHolder compilerErrorHolder)
    {
        String message = String.format("ERR_DUP_TOP: Duplicate top level item name: '%s'.", this.name);
        compilerErrorHolder.add(this.compilationUnit, message, this.nameContext);
    }

    public void reportErrors(CompilerErrorHolder compilerErrorHolder)
    {
        ImmutableBag<String> duplicateMemberNames = this.getDuplicateMemberNames();

        for (AntlrDataTypeProperty<?> dataTypePropertyState : this.dataTypePropertyStates)
        {
            if (duplicateMemberNames.contains(dataTypePropertyState.getName()))
            {
                dataTypePropertyState.reportDuplicateMemberName(compilerErrorHolder);
            }
            dataTypePropertyState.reportErrors(compilerErrorHolder);
        }

        for (AntlrAssociationEnd associationEndState : this.associationEndStates)
        {
            if (duplicateMemberNames.contains(associationEndState.getName()))
            {
                associationEndState.reportDuplicateMemberName(compilerErrorHolder);
            }
            associationEndState.reportErrors(compilerErrorHolder);
        }

        // TODO: Warn if class is owned by multiple
        // TODO: Detect ownership cycles
        // TODO: Check that there's at least one key property
        // TODO: Check that ID properties aren't part of a composite key

        // TODO: parameterized properties
    }

    public ImmutableBag<String> getDuplicateMemberNames()
    {
        return this.getMemberNames()
                .toBag()
                .selectByOccurrences(occurrences -> occurrences > 1)
                .toImmutable();
    }

    private ImmutableList<String> getMemberNames()
    {
        MutableList<String> topLevelNames = Lists.mutable.empty();
        this.dataTypePropertyStates.collect(AntlrProperty::getName, topLevelNames);
        this.associationEndStates.collect(AntlrProperty::getName, topLevelNames);
        return topLevelNames.toImmutable();
    }

    @Override
    public ClassDeclarationContext getElementContext()
    {
        return (ClassDeclarationContext) super.getElementContext();
    }

    public AntlrDataTypeProperty<?> getDataTypePropertyByName(String name)
    {
        return this.dataTypePropertiesByName.getIfAbsentValue(name, AntlrEnumerationProperty.NOT_FOUND);
    }
}
