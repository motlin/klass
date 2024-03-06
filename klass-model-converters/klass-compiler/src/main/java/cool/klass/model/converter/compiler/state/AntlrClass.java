package cool.klass.model.converter.compiler.state;

import java.util.LinkedHashMap;
import java.util.Objects;

import cool.klass.model.converter.compiler.phase.AbstractCompilerPhase;
import cool.klass.model.meta.domain.AssociationEnd.AssociationEndBuilder;
import cool.klass.model.meta.domain.DataTypeProperty.DataTypePropertyBuilder;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class AntlrClass
{
    public static final AntlrClass AMBIGUOUS = new AntlrClass(null, new ClassDeclarationContext(null, -1),
            "ambiguous class");
    public static final AntlrClass NOT_FOUND = new AntlrClass(
            null,
            new ClassDeclarationContext(null, -1),
            "not found class");

    private final String                  packageName;
    private final ClassDeclarationContext context;
    private final String                  name;

    private final MutableList<AntlrDataTypeProperty<?, ?>>               dataTypePropertyStates   = Lists.mutable.empty();
    private final MutableOrderedMap<String, AntlrDataTypeProperty<?, ?>> dataTypePropertiesByName = OrderedMapAdapter.adapt(
            new LinkedHashMap<>());

    private final MutableList<AntlrAssociationEnd>               associationEndStates  = Lists.mutable.empty();
    private final MutableOrderedMap<String, AntlrAssociationEnd> associationEndsByName = OrderedMapAdapter.adapt(new LinkedHashMap<>());

    private KlassBuilder klassBuilder;

    public AntlrClass(String packageName, ClassDeclarationContext context, String name)
    {
        this.packageName = packageName;
        this.context = Objects.requireNonNull(context);
        this.name = Objects.requireNonNull(name);
    }

    public String getName()
    {
        return this.name;
    }

    public void enterDataTypeProperty(AntlrDataTypeProperty<?, ?> antlrDataTypeProperty)
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

    // TODO: Error processing
    public void exitClassDeclaration(AbstractCompilerPhase compilerPhase)
    {
        MutableBag<String> duplicateNames = this.getDuplicateMemberNames();
        this.checkDuplicateMemberNames(compilerPhase, duplicateNames);
    }

    private MutableBag<String> getDuplicateMemberNames()
    {
        return this.dataTypePropertyStates
                .asLazy()
                .collect(AntlrProperty::getName)
                .toBag()
                .selectByOccurrences(occurrences -> occurrences > 1);
    }

    private void checkDuplicateMemberNames(
            AbstractCompilerPhase classPhase,
            MutableBag<String> duplicateNames)
    {
        this.dataTypePropertyStates
                .asLazy()
                .select(each -> duplicateNames.contains(each.getName()))
                .each(each ->
                {
                    String message = String.format("Duplicate member: '%s'.", each.getName());
                    classPhase.error(message, each.getNameContext(), this.context);
                });
    }

    public KlassBuilder build1()
    {
        if (this.klassBuilder != null)
        {
            throw new IllegalStateException();
        }

        this.klassBuilder = new KlassBuilder(
                this.context,
                this.context.identifier(),
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
}
