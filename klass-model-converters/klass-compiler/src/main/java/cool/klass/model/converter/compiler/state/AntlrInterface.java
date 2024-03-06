package cool.klass.model.converter.compiler.state;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.property.AntlrProperty;
import cool.klass.model.meta.domain.ClassModifierImpl.ClassModifierBuilder;
import cool.klass.model.meta.domain.InterfaceImpl.InterfaceBuilder;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty.DataTypePropertyBuilder;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.InterfaceDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.InterfaceReferenceContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Sets;

public class AntlrInterface extends AntlrClassifier
{
    @Nonnull
    public static final AntlrInterface AMBIGUOUS = new AntlrInterface(
            new ClassDeclarationContext(null, -1),
            Optional.empty(),
            new ParserRuleContext(),
            "ambiguous interface",
            -1,
            new ParserRuleContext(),
            "klass.meta")
    {
        @Override
        public void enterDataTypeProperty(@Nonnull AntlrDataTypeProperty<?> antlrDataTypeProperty)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".enterDataTypeProperty() not implemented yet");
        }
    };

    @Nonnull
    public static final AntlrInterface NOT_FOUND = new AntlrInterface(
            new ClassDeclarationContext(null, -1),
            Optional.empty(),
            new ParserRuleContext(),
            "not found interface",
            -1,
            new ParserRuleContext(),
            "klass.meta")
    {
        @Override
        public void enterDataTypeProperty(@Nonnull AntlrDataTypeProperty<?> antlrDataTypeProperty)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".enterDataTypeProperty() not implemented yet");
        }
    };

    // TODO: Unified list of all members

    /*
    private final MutableList<AntlrAssociationEnd>               associationEndStates  = Lists.mutable.empty();
    private final MutableOrderedMap<String, AntlrAssociationEnd> associationEndsByName =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    private final MutableList<AntlrParameterizedProperty>                                     parameterizedPropertyStates      = Lists.mutable.empty();
    private final MutableOrderedMap<String, AntlrParameterizedProperty>                       parameterizedPropertiesByName    =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<ParameterizedPropertyContext, AntlrParameterizedProperty> parameterizedPropertiesByContext =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    */

    private InterfaceBuilder interfaceBuilder;

    public AntlrInterface(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull ParserRuleContext packageContext,
            @Nonnull String packageName)
    {
        super(elementContext, compilationUnit, nameContext, name, ordinal, packageContext, packageName);
    }

    @Override
    public int getNumMembers()
    {
        return this.getDataTypeProperties().size()
                + this.associationEndSignatureStates.size();
    }

    public InterfaceBuilder build1()
    {
        if (this.interfaceBuilder != null)
        {
            throw new IllegalStateException();
        }

        this.interfaceBuilder = new InterfaceBuilder(
                this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.nameContext,
                this.name,
                this.ordinal,
                this.packageName);

        ImmutableList<ClassModifierBuilder> classModifierBuilders = this.classModifierStates
                .collect(AntlrClassModifier::build)
                .toImmutable();
        this.interfaceBuilder.setClassModifierBuilders(classModifierBuilders);

        ImmutableList<DataTypePropertyBuilder<?, ?, ?>> dataTypePropertyBuilders = this.dataTypePropertyStates
                .<DataTypePropertyBuilder<?, ?, ?>>collect(AntlrDataTypeProperty::build)
                .toImmutable();

        this.interfaceBuilder.setDataTypePropertyBuilders(dataTypePropertyBuilders);
        return this.interfaceBuilder;
    }

    @Nonnull
    @Override
    public InterfaceBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.interfaceBuilder);
    }

    public void build2()
    {
        if (this.interfaceBuilder == null)
        {
            throw new IllegalStateException();
        }

        this.dataTypePropertyStates.each(AntlrDataTypeProperty::build2);

        ImmutableList<InterfaceBuilder> interfaceBuilders = this.interfaceStates
                .collect(AntlrInterface::getElementBuilder)
                .toImmutable();

        this.interfaceBuilder.setInterfaceBuilders(interfaceBuilders);
    }

    @Override
    public void reportNameErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        super.reportNameErrors(compilerErrorHolder);
        this.reportKeywordCollision(compilerErrorHolder);

        if (RELADOMO_TYPES.contains(this.getName()))
        {
            String message = String.format("'%s' is a Reladomo type.", this.getName());
            compilerErrorHolder.add("ERR_REL_NME", message, this);
        }

        this.dataTypePropertyStates.forEachWith(AntlrNamedElement::reportNameErrors, compilerErrorHolder);
    }

    @Override
    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        super.reportErrors(compilerErrorHolder);

        this.reportTransientModifier(compilerErrorHolder);

        // this.reportDuplicateParameterizedPropertyNames(compilerErrorHolder);
        // this.reportDuplicateAssociationEndNames(compilerErrorHolder);
    }

    /*
    private void reportDuplicateParameterizedPropertyNames(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        ImmutableBag<String> duplicateMemberNames = this.getDuplicateMemberNames();

        for (AntlrParameterizedProperty parameterizedPropertyState : this.parameterizedPropertyStates)
        {
            if (duplicateMemberNames.contains(parameterizedPropertyState.getName()))
            {
                parameterizedPropertyState.reportDuplicateMemberName(compilerErrorHolder);
            }
            parameterizedPropertyState.reportErrors(compilerErrorHolder);
        }
    }

    private void reportDuplicateAssociationEndNames(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        ImmutableBag<String> duplicateMemberNames = this.getDuplicateMemberNames();

        for (AntlrAssociationEnd associationEndState : this.associationEndStates)
        {
            if (duplicateMemberNames.contains(associationEndState.getName()))
            {
                associationEndState.reportDuplicateMemberName(compilerErrorHolder);
            }
            associationEndState.reportErrors(compilerErrorHolder);
        }
    }
    */

    private void reportTransientModifier(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        // Only need to check declared modifiers
        Optional<AntlrClassModifier> transientModifier = this.classModifierStates
                .detectOptional(AntlrClassModifier::isTransient);

        if (!transientModifier.isPresent())
        {
            return;
        }

        ParserRuleContext offendingToken = transientModifier.get().getElementContext();
        String message = String.format(
                "'%s' keyword not applicable to interfaces.",
                offendingToken.getText());
        compilerErrorHolder.add("ERR_INT_TRN", message, this, offendingToken);
    }

    @Override
    protected void reportCircularInheritance(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        for (int i = 0; i < this.interfaceStates.size(); i++)
        {
            AntlrInterface interfaceState = this.interfaceStates.get(i);
            if (interfaceState.extendsInterface(this, Sets.mutable.empty()))
            {
                InterfaceReferenceContext offendingToken = this.getOffendingInterfaceReference(i);
                String message = String.format(
                        "Circular inheritance '%s'.",
                        offendingToken.getText());
                compilerErrorHolder.add("ERR_IMP_SLF", message, this, offendingToken);
            }
        }
    }

    private boolean extendsInterface(
            AntlrInterface interfaceState,
            @Nonnull MutableSet<AntlrInterface> visitedInterfaces)
    {
        if (this.interfaceStates.contains(interfaceState))
        {
            return true;
        }

        if (visitedInterfaces.contains(this))
        {
            return false;
        }

        visitedInterfaces.add(this);
        return this.interfaceStates.anySatisfy(eachSuperInterface -> eachSuperInterface.extendsInterface(
                interfaceState,
                visitedInterfaces));
    }

    @Override
    protected InterfaceReferenceContext getOffendingInterfaceReference(int index)
    {
        return this.getElementContext().interfaceHeader().implementsDeclaration().interfaceReference().get(index);
    }

    @Override
    protected boolean isInterfaceRedundant(int index, AntlrInterface interfaceState)
    {
        return this.interfaceNotAtIndexImplements(index, interfaceState);
    }

    @Override
    public ImmutableBag<String> getDuplicateMemberNames()
    {
        return this.getDeclaredMemberNames()
                .toBag()
                .selectByOccurrences(occurrences -> occurrences > 1)
                .toImmutable();
    }

    @Override
    protected ImmutableList<String> getDeclaredMemberNames()
    {
        MutableList<String> topLevelNames = Lists.mutable.empty();
        this.dataTypePropertyStates.collect(AntlrProperty::getName, topLevelNames);
        this.associationEndSignatureStates.collect(AntlrProperty::getName, topLevelNames);
        return topLevelNames.toImmutable();
    }

    @Nonnull
    @Override
    public InterfaceDeclarationContext getElementContext()
    {
        return (InterfaceDeclarationContext) super.getElementContext();
    }

    @Nonnull
    @Override
    public InterfaceBuilder getTypeGetter()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getTypeBuilder() not implemented yet");
    }

    @Override
    public AntlrDataTypeProperty<?> getDataTypePropertyByName(String name)
    {
        if (this.dataTypePropertiesByName.containsKey(name))
        {
            return this.dataTypePropertiesByName.get(name);
        }

        return this.getInterfaceDataTypePropertyByName(name);
    }

    @Override
    public AntlrClassModifier getClassModifierByName(String name)
    {
        if (this.classModifiersByName.containsKey(name))
        {
            return this.classModifiersByName.get(name);
        }

        return this.getInterfaceClassModifierByName(name);
    }
}
