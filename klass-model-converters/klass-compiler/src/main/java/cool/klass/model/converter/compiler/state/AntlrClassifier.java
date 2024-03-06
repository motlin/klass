package cool.klass.model.converter.compiler.state;

import java.util.LinkedHashMap;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEndSignature;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.property.AntlrEnumerationProperty;
import cool.klass.model.converter.compiler.state.property.AntlrPrimitiveProperty;
import cool.klass.model.meta.domain.AbstractClassifier.ClassifierBuilder;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndSignatureContext;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassModifierContext;
import cool.klass.model.meta.grammar.KlassParser.InterfaceReferenceContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Sets;
import org.eclipse.collections.impl.list.Interval;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public abstract class AntlrClassifier extends AntlrPackageableElement implements AntlrType, AntlrTopLevelElement
{
    @Nonnull
    public static final AntlrClassifier AMBIGUOUS = new AntlrClassifier(
            new ClassDeclarationContext(null, -1),
            Optional.empty(),
            new ParserRuleContext(),
            "ambiguous class",
            -1,
            new ParserRuleContext(),
            "klass.meta")
    {
        @Override
        public int getNumMembers()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".getNumMembers() not implemented yet");
        }

        @Nonnull
        @Override
        public ClassifierBuilder<?> getElementBuilder()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".getElementBuilder() not implemented yet");
        }

        @Override
        protected void reportCircularInheritance(CompilerErrorState compilerErrorHolder)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".reportCircularInheritance() not implemented yet");
        }

        @Override
        protected boolean isInterfaceRedundant(
                int index, AntlrInterface interfaceState)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".isInterfaceRedundant() not implemented yet");
        }

        @Override
        public ImmutableBag<String> getDuplicateMemberNames()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".getDuplicateMemberNames() not implemented yet");
        }

        @Override
        protected ImmutableList<String> getDeclaredMemberNames()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".getDeclaredMemberNames() not implemented yet");
        }

        @Nonnull
        @Override
        public ClassifierBuilder<?> getTypeGetter()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".getTypeGetter() not implemented yet");
        }

        @Override
        public AntlrDataTypeProperty<?> getDataTypePropertyByName(String name)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".getDataTypePropertyByName() not implemented yet");
        }

        @Override
        public AntlrClassModifier getClassModifierByName(String name)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".getClassModifierByName() not implemented yet");
        }
    };

    @Nonnull
    public static final AntlrClassifier NOT_FOUND = new AntlrClassifier(
            new ClassDeclarationContext(null, -1),
            Optional.empty(),
            new ParserRuleContext(),
            "not found class",
            -1,
            new ParserRuleContext(),
            "klass.meta")
    {
        @Override
        public int getNumMembers()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".getNumMembers() not implemented yet");
        }

        @Nonnull
        @Override
        public ClassifierBuilder<?> getElementBuilder()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".getElementBuilder() not implemented yet");
        }

        @Override
        protected void reportCircularInheritance(CompilerErrorState compilerErrorHolder)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".reportCircularInheritance() not implemented yet");
        }

        @Override
        protected boolean isInterfaceRedundant(
                int index, AntlrInterface interfaceState)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".isInterfaceRedundant() not implemented yet");
        }

        @Override
        public ImmutableBag<String> getDuplicateMemberNames()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".getDuplicateMemberNames() not implemented yet");
        }

        @Override
        protected ImmutableList<String> getDeclaredMemberNames()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".getDeclaredMemberNames() not implemented yet");
        }

        @Nonnull
        @Override
        public ClassifierBuilder<?> getTypeGetter()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".getTypeGetter() not implemented yet");
        }

        @Override
        public AntlrDataTypeProperty<?> getDataTypePropertyByName(String name)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".getDataTypePropertyByName() not implemented yet");
        }

        @Override
        public AntlrClassModifier getClassModifierByName(String name)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".getClassModifierByName() not implemented yet");
        }
    };

    protected final MutableList<AntlrAssociationEndSignature>                              associationEndSignatureStates     =
            Lists.mutable.empty();
    protected final MutableOrderedMap<String, AntlrAssociationEndSignature>                associationEndSignaturesByName    =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    protected final MutableOrderedMap<AssociationEndSignatureContext, AntlrAssociationEndSignature> associationEndSignaturesByContext =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    protected final MutableList<AntlrClassModifier>       classModifierStates    = Lists.mutable.empty();
    protected final MutableList<AntlrDataTypeProperty<?>> dataTypePropertyStates = Lists.mutable.empty();
    protected final MutableList<AntlrInterface>           interfaceStates        = Lists.mutable.empty();

    protected final MutableOrderedMap<String, AntlrDataTypeProperty<?>>         dataTypePropertiesByName =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    protected final MutableOrderedMap<String, AntlrClassModifier>               classModifiersByName     =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    protected final MutableOrderedMap<ClassModifierContext, AntlrClassModifier> classModifiersByContext  =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    protected AntlrClassifier(
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

    protected ImmutableList<AntlrDataTypeProperty<?>> getDataTypeProperties(@Nonnull MutableList<AntlrClassifier> visited)
    {
        if (visited.contains(this))
        {
            return Lists.immutable.empty();
        }
        visited.add(this);

        MutableSet<String> propertyNames = this.dataTypePropertyStates.collect(AntlrNamedElement::getName).toSet();

        ImmutableList<AntlrDataTypeProperty<?>> inheritedProperties = this.getInheritedProperties(visited)
                .reject(inheritedProperty -> propertyNames.contains(inheritedProperty.getName()));

        return this.dataTypePropertyStates.toImmutable().newWithAll(inheritedProperties);
    }

    protected ImmutableList<AntlrDataTypeProperty<?>> getInheritedProperties(MutableList<AntlrClassifier> visited)
    {
        return this.interfaceStates
                .flatCollectWith(AntlrClassifier::getDataTypeProperties, visited)
                .distinctBy(AntlrNamedElement::getName)
                .toImmutable();
    }

    public final ImmutableList<AntlrDataTypeProperty<?>> getDataTypeProperties()
    {
        return this.getDataTypeProperties(Lists.mutable.empty());
    }

    protected ImmutableList<AntlrClassModifier> getClassModifiers(@Nonnull MutableList<AntlrClassifier> visited)
    {
        if (visited.contains(this))
        {
            return Lists.immutable.empty();
        }
        visited.add(this);

        MutableSet<String> modifierNames = this.classModifierStates.collect(AntlrNamedElement::getName).toSet();

        ImmutableList<AntlrClassModifier> inheritedModifiers = this.getInheritedModifiers(visited)
                .reject(inheritedProperty -> modifierNames.contains(inheritedProperty.getName()));

        return this.classModifierStates.toImmutable().newWithAll(inheritedModifiers);
    }

    protected ImmutableList<AntlrClassModifier> getInheritedModifiers(MutableList<AntlrClassifier> visited)
    {
        return this.interfaceStates
                .flatCollectWith(AntlrClassifier::getClassModifiers, visited)
                .distinctBy(AntlrNamedElement::getName)
                .toImmutable();
    }

    public final ImmutableList<AntlrClassModifier> getClassModifiers()
    {
        return this.getClassModifiers(Lists.mutable.empty());
    }

    public boolean isTransient()
    {
        return this.getClassModifiers().anySatisfy(AntlrClassModifier::isTransient);
    }

    public abstract int getNumMembers();

    public void enterDataTypeProperty(@Nonnull AntlrDataTypeProperty<?> antlrDataTypeProperty)
    {
        this.dataTypePropertyStates.add(antlrDataTypeProperty);
        this.dataTypePropertiesByName.compute(
                antlrDataTypeProperty.getName(),
                (name, builder) -> builder == null
                        ? antlrDataTypeProperty
                        : AntlrPrimitiveProperty.AMBIGUOUS);
    }

    public AntlrAssociationEndSignature getAssociationEndSignatureByContext(AssociationEndSignatureContext ctx)
    {
        return this.associationEndSignaturesByContext.get(ctx);
    }

    public void enterAssociationEndSignature(@Nonnull AntlrAssociationEndSignature associationEndSignatureState)
    {
        this.associationEndSignatureStates.add(associationEndSignatureState);
        this.associationEndSignaturesByName.compute(
                associationEndSignatureState.getName(),
                (name, builder) -> builder == null
                        ? associationEndSignatureState
                        : AntlrAssociationEndSignature.AMBIGUOUS);
        AntlrAssociationEndSignature duplicate = this.associationEndSignaturesByContext.put(
                associationEndSignatureState.getElementContext(),
                associationEndSignatureState);
        if (duplicate != null)
        {
            throw new AssertionError();
        }
    }

    public void enterClassModifier(@Nonnull AntlrClassModifier classModifierState)
    {
        this.classModifierStates.add(classModifierState);
        this.classModifiersByName.compute(
                classModifierState.getName(),
                (name, builder) -> builder == null
                        ? classModifierState
                        : AntlrClassModifier.AMBIGUOUS);

        AntlrClassModifier duplicate = this.classModifiersByContext.put(
                classModifierState.getElementContext(),
                classModifierState);
        if (duplicate != null)
        {
            throw new AssertionError();
        }
    }

    public AntlrClassModifier getClassModifierByContext(ClassModifierContext classModifierContext)
    {
        return this.classModifiersByContext.get(classModifierContext);
    }

    public int getNumClassModifiers()
    {
        return this.classModifierStates.size();
    }

    public void enterImplementsDeclaration(AntlrInterface interfaceState)
    {
        this.interfaceStates.add(interfaceState);
    }

    @Nonnull
    @Override
    public abstract ClassifierBuilder<?> getElementBuilder();

    protected boolean implementsInterface(AntlrInterface interfaceState)
    {
        return this.interfaceStates.contains(interfaceState)
                || this.interfaceStates.anySatisfyWith(AntlrClassifier::implementsInterface, interfaceState);
    }

    @OverridingMethodsMustInvokeSuper
    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        this.reportDuplicatePropertyNames(compilerErrorHolder);
        this.reportMultipleIdProperties(compilerErrorHolder);
        this.reportIdAndKeyProperties(compilerErrorHolder);
        this.reportInterfaceNotFound(compilerErrorHolder);
        this.reportRedundantInterface(compilerErrorHolder);
        this.reportCircularInheritance(compilerErrorHolder);
        this.reportDuplicateAssociationEndSignatureNames(compilerErrorHolder);

        // TODO: Warn if class is owned by multiple
        // TODO: Detect ownership cycles

        // TODO: duplicate modifiers
    }

    private void reportDuplicatePropertyNames(@Nonnull CompilerErrorState compilerErrorHolder)
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
    }

    private void reportMultipleIdProperties(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        int numIdProperties = this.dataTypePropertyStates.count(AntlrDataTypeProperty::isID);
        if (numIdProperties > 1)
        {
            String message = String.format(
                    "Class '%s' may only have one id property. Found: %s.",
                    this.name,
                    this.dataTypePropertyStates
                            .select(AntlrDataTypeProperty::isID)
                            .collect(AntlrDataTypeProperty::getShortString)
                            .makeString());
            compilerErrorHolder.add("ERR_MNY_IDS", message, this);
        }
    }

    private void reportIdAndKeyProperties(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        MutableList<String> idProperties = this.dataTypePropertyStates
                .select(AntlrDataTypeProperty::isID)
                .collect(AntlrDataTypeProperty::getShortString);
        if (idProperties.isEmpty())
        {
            return;
        }

        MutableList<String> nonIdKeyProperties = this.dataTypePropertyStates
                .select(AntlrDataTypeProperty::isKey)
                .reject(AntlrDataTypeProperty::isID)
                .collect(AntlrDataTypeProperty::getShortString);
        if (nonIdKeyProperties.isEmpty())
        {
            return;
        }

        String message = String.format(
                "Class '%s' may have id properties or non-id key properties, but not both. Found id properties: %s. Found non-id key properties: %s.",
                this.name,
                idProperties
                        .makeString(),
                nonIdKeyProperties
                        .makeString());
        compilerErrorHolder.add("ERR_KEY_IDS", message, this);
    }

    private void reportInterfaceNotFound(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        for (int i = 0; i < this.interfaceStates.size(); i++)
        {
            AntlrInterface interfaceState = this.interfaceStates.get(i);
            if (interfaceState == AntlrInterface.NOT_FOUND)
            {
                InterfaceReferenceContext offendingToken = this.getOffendingInterfaceReference(i);
                String message = String.format(
                        "Cannot find interface '%s'.",
                        offendingToken.getText());
                compilerErrorHolder.add("ERR_IMP_INT", message, this, offendingToken);
            }
        }
    }

    protected void reportRedundantInterface(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        MutableSet<AntlrInterface> visitedInterfaceStates = Sets.mutable.empty();

        for (int i = 0; i < this.interfaceStates.size(); i++)
        {
            AntlrInterface interfaceState = this.interfaceStates.get(i);
            if (interfaceState == AntlrInterface.NOT_FOUND)
            {
                continue;
            }

            if (visitedInterfaceStates.contains(interfaceState))
            {
                InterfaceReferenceContext offendingToken = this.getOffendingInterfaceReference(i);
                String message = String.format(
                        "Duplicate interface '%s'.",
                        offendingToken.getText());
                compilerErrorHolder.add("ERR_DUP_INT", message, this, offendingToken);
            }

            if (this.isInterfaceRedundant(i, interfaceState))
            {
                InterfaceReferenceContext offendingToken = this.getOffendingInterfaceReference(i);
                String message = String.format(
                        "Redundant interface '%s'.",
                        offendingToken.getText());
                compilerErrorHolder.add("ERR_RED_INT", message, this, offendingToken);
            }

            visitedInterfaceStates.add(interfaceState);
        }
    }

    private void reportDuplicateAssociationEndSignatureNames(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        ImmutableBag<String> duplicateMemberNames = this.getDuplicateMemberNames();

        for (AntlrAssociationEndSignature associationEndSignatureState : this.associationEndSignatureStates)
        {
            if (duplicateMemberNames.contains(associationEndSignatureState.getName()))
            {
                associationEndSignatureState.reportDuplicateMemberName(compilerErrorHolder);
            }
            associationEndSignatureState.reportErrors(compilerErrorHolder);
        }
    }

    protected abstract void reportCircularInheritance(CompilerErrorState compilerErrorHolder);

    protected abstract boolean isInterfaceRedundant(int index, AntlrInterface interfaceState);

    protected boolean interfaceNotAtIndexImplements(int index, AntlrInterface interfaceState)
    {
        return Interval.zeroTo(this.interfaceStates.size() - 1)
                .asLazy()
                .reject(i -> i == index)
                .collect(this.interfaceStates::get)
                .anySatisfyWith(AntlrClassifier::implementsInterface, interfaceState);
    }

    protected InterfaceReferenceContext getOffendingInterfaceReference(int index)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getOffendingInterfaceReference() not implemented yet");
    }

    public abstract ImmutableBag<String> getDuplicateMemberNames();

    protected abstract ImmutableList<String> getDeclaredMemberNames();

    @Nonnull
    @Override
    public abstract ClassifierBuilder<?> getTypeGetter();

    public abstract AntlrDataTypeProperty<?> getDataTypePropertyByName(String name);

    protected AntlrDataTypeProperty<?> getInterfaceDataTypePropertyByName(String name)
    {
        return this.interfaceStates
                .asLazy()
                .<String, AntlrDataTypeProperty<?>>collectWith(AntlrInterface::getDataTypePropertyByName, name)
                .detectOptional(interfaceProperty -> interfaceProperty != AntlrEnumerationProperty.NOT_FOUND)
                .orElse(AntlrEnumerationProperty.NOT_FOUND);
    }

    public abstract AntlrClassModifier getClassModifierByName(String name);

    protected AntlrClassModifier getInterfaceClassModifierByName(String name)
    {
        return this.interfaceStates
                .asLazy()
                .collectWith(AntlrInterface::getClassModifierByName, name)
                .detectOptional(interfaceModifier -> interfaceModifier != AntlrClassModifier.NOT_FOUND)
                .orElse(AntlrClassModifier.NOT_FOUND);
    }
}
