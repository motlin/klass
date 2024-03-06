package cool.klass.model.converter.compiler.state;

import java.util.LinkedHashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.property.AntlrEnumerationProperty;
import cool.klass.model.converter.compiler.state.property.AntlrPrimitiveProperty;
import cool.klass.model.converter.compiler.state.property.AntlrProperty;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import cool.klass.model.meta.domain.property.AssociationEnd.AssociationEndBuilder;
import cool.klass.model.meta.domain.property.DataTypeProperty.DataTypePropertyBuilder;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class AntlrClass extends AntlrPackageableElement implements AntlrType
{
    @Nonnull
    public static final AntlrClass AMBIGUOUS = new AntlrClass(
            new ClassDeclarationContext(null, -1),
            null,
            true,
            new ParserRuleContext(),
            "ambiguous class",
            null,
            Lists.immutable.empty(),
            false)
    {
        @Override
        public void enterDataTypeProperty(@Nonnull AntlrDataTypeProperty<?> antlrDataTypeProperty)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".enterDataTypeProperty() not implemented yet");
        }

        @Override
        public void enterAssociationEnd(@Nonnull AntlrAssociationEnd antlrAssociationEnd)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".enterAssociationEnd() not implemented yet");
        }
    };
    @Nonnull
    public static final AntlrClass NOT_FOUND = new AntlrClass(
            new ClassDeclarationContext(null, -1),
            null,
            true,
            new ParserRuleContext(),
            "not found class",
            null,
            Lists.immutable.empty(),
            false)
    {
        @Override
        public void enterDataTypeProperty(@Nonnull AntlrDataTypeProperty<?> antlrDataTypeProperty)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".enterDataTypeProperty() not implemented yet");
        }

        @Override
        public void enterAssociationEnd(@Nonnull AntlrAssociationEnd antlrAssociationEnd)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".enterAssociationEnd() not implemented yet");
        }
    };

    private final MutableList<AntlrDataTypeProperty<?>>               dataTypePropertyStates   = Lists.mutable.empty();
    private final MutableOrderedMap<String, AntlrDataTypeProperty<?>> dataTypePropertiesByName = OrderedMapAdapter.adapt(
            new LinkedHashMap<>());

    private final MutableList<AntlrAssociationEnd>               associationEndStates  = Lists.mutable.empty();
    private final MutableOrderedMap<String, AntlrAssociationEnd> associationEndsByName = OrderedMapAdapter.adapt(new LinkedHashMap<>());

    private MutableList<AntlrClass> versionClasses = Lists.mutable.empty();
    private AntlrClass              versionedClass;

    private MutableList<AntlrAssociation> versionAssociations = Lists.mutable.empty();

    private final ImmutableList<AntlrClassModifier> classModifiers;
    private       boolean                           isUser;

    private KlassBuilder klassBuilder;

    public AntlrClass(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            String packageName,
            ImmutableList<AntlrClassModifier> classModifiers,
            boolean isUser)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, packageName);
        this.classModifiers = Objects.requireNonNull(classModifiers);
        this.isUser = isUser;
    }

    public MutableList<AntlrDataTypeProperty<?>> getDataTypeProperties()
    {
        return this.dataTypePropertyStates;
    }

    public void enterDataTypeProperty(@Nonnull AntlrDataTypeProperty<?> antlrDataTypeProperty)
    {
        this.dataTypePropertyStates.add(antlrDataTypeProperty);
        this.dataTypePropertiesByName.compute(
                antlrDataTypeProperty.getName(),
                (name, builder) -> builder == null
                        ? antlrDataTypeProperty
                        : AntlrPrimitiveProperty.AMBIGUOUS);
    }

    public void enterAssociationEnd(@Nonnull AntlrAssociationEnd antlrAssociationEnd)
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

        // TODO: Pass through class modifiers?
        this.klassBuilder = new KlassBuilder(
                this.elementContext,
                this.nameContext,
                this.name,
                this.packageName,
                isUser);

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

        if (this.versionClasses.notEmpty())
        {
            this.klassBuilder.setVersionClassBuilder(this.versionClasses.getOnly().getKlassBuilder());
        }

        if (this.versionedClass != null)
        {
            this.klassBuilder.setVersionedClassBuilder(this.versionedClass.getKlassBuilder());
        }
    }

    public KlassBuilder getKlassBuilder()
    {
        return Objects.requireNonNull(this.klassBuilder);
    }

    public void build3()
    {
        if (this.klassBuilder == null)
        {
            throw new IllegalStateException();
        }

        ImmutableList<AssociationEndBuilder> associationEndBuilders = this.associationEndStates
                .collect(AntlrAssociationEnd::getAssociationEndBuilder)
                .toImmutable();

        this.klassBuilder.setAssociationEndBuilders(associationEndBuilders);
    }

    public void reportDuplicateTopLevelName(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        String message = String.format("ERR_DUP_TOP: Duplicate top level item name: '%s'.", this.name);
        compilerErrorHolder.add(this.compilationUnit, message, this.nameContext);
    }

    public void reportErrors(@Nonnull CompilerErrorHolder compilerErrorHolder)
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

        if (this.versionClasses.size() > 1)
        {
            for (AntlrClass versionClass : this.versionClasses)
            {
                versionClass.reportDuplicateVersionClass(compilerErrorHolder, this);
            }
        }

        if (this.versionAssociations.size() > 1)
        {
            for (AntlrAssociation versionAssociation : this.versionAssociations)
            {
                versionAssociation.reportDuplicateVersionAssociation(compilerErrorHolder, this);
            }
        }

        if (this.versionedClass == null)
        {
            for (AntlrAssociation versionAssociation : this.versionAssociations)
            {
                versionAssociation.reportVersionAssociation(compilerErrorHolder, this);
            }
        }

        if (this.versionedClass != null && this.versionClasses.notEmpty())
        {
            String message = String.format("ERR_VER_VER: Class is a version and has a version: '%s'.", this.name);
            compilerErrorHolder.add(
                    this.compilationUnit,
                    message,
                    this.getElementContext().versions().classReference());
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

    private void reportDuplicateVersionClass(CompilerErrorHolder compilerErrorHolder, AntlrClass versionedClass)
    {
        String message = String.format(
                "ERR_VER_CLS: Multiple version classes on '%s'.",
                versionedClass.getName());
        compilerErrorHolder.add(this.compilationUnit, message, this.getElementContext().versions().classReference());
    }

    private ImmutableList<String> getMemberNames()
    {
        MutableList<String> topLevelNames = Lists.mutable.empty();
        this.dataTypePropertyStates.collect(AntlrProperty::getName, topLevelNames);
        this.associationEndStates.collect(AntlrProperty::getName, topLevelNames);
        return topLevelNames.toImmutable();
    }

    @Nonnull
    @Override
    public ClassDeclarationContext getElementContext()
    {
        return (ClassDeclarationContext) super.getElementContext();
    }

    public AntlrDataTypeProperty<?> getDataTypePropertyByName(String name)
    {
        return this.dataTypePropertiesByName.getIfAbsentValue(name, AntlrEnumerationProperty.NOT_FOUND);
    }

    public AntlrAssociationEnd getAssociationEndByName(String name)
    {
        return this.associationEndsByName.getIfAbsentValue(name, AntlrAssociationEnd.NOT_FOUND);
    }

    @Nonnull
    @Override
    public KlassBuilder getTypeBuilder()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getTypeBuilder() not implemented yet");
    }

    public void addVersionClass(AntlrClass versionClass)
    {
        this.versionClasses.add(Objects.requireNonNull(versionClass));
    }

    public void setVersionedClass(AntlrClass versionedClass)
    {
        if (this.versionedClass != null)
        {
            throw new AssertionError();
        }
        this.versionedClass = Objects.requireNonNull(versionedClass);
    }

    public void addVersionAssociation(AntlrAssociation versionAssociation)
    {
        this.versionAssociations.add(Objects.requireNonNull(versionAssociation));
    }

    public boolean hasVersion()
    {
        return this.versionClasses.notEmpty();
    }

    public boolean hasVersionedModifier()
    {
        return this.classModifiers.anySatisfy(AntlrClassModifier::isVersioned);
    }
}
