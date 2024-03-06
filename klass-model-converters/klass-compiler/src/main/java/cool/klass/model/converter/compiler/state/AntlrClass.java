package cool.klass.model.converter.compiler.state;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEndSignature;
import cool.klass.model.converter.compiler.state.property.AntlrClassReferenceProperty;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.property.AntlrEnumerationProperty;
import cool.klass.model.converter.compiler.state.property.AntlrModifier;
import cool.klass.model.converter.compiler.state.property.AntlrParameterizedProperty;
import cool.klass.model.converter.compiler.state.property.AntlrProperty;
import cool.klass.model.converter.compiler.state.property.AntlrReferenceProperty;
import cool.klass.model.meta.domain.InterfaceImpl.InterfaceBuilder;
import cool.klass.model.meta.domain.KlassImpl.KlassBuilder;
import cool.klass.model.meta.domain.api.InheritanceType;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty.DataTypePropertyBuilder;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import cool.klass.model.meta.domain.property.AssociationEndSignatureImpl.AssociationEndSignatureBuilder;
import cool.klass.model.meta.domain.property.ModifierImpl.ModifierBuilder;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.InterfaceReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterizedPropertyContext;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Sets;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;
import org.eclipse.collections.impl.tuple.Tuples;

public class AntlrClass
        extends AntlrClassifier
{
    @Nonnull
    public static final AntlrClass AMBIGUOUS = new AntlrClass(
            new ClassDeclarationContext(null, -1),
            Optional.empty(),
            -1,
            new IdentifierContext(null, -1),
            AntlrCompilationUnit.AMBIGUOUS,
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

        @Override
        public void enterParameterizedProperty(@Nonnull AntlrParameterizedProperty parameterizedPropertyState)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".enterParameterizedProperty() not implemented yet");
        }
    };

    @Nonnull
    public static final AntlrClass NOT_FOUND = new AntlrClass(
            new ClassDeclarationContext(null, -1),
            Optional.empty(),
            -1,
            new IdentifierContext(null, -1),
            AntlrCompilationUnit.NOT_FOUND,
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

        @Override
        public void enterParameterizedProperty(@Nonnull AntlrParameterizedProperty parameterizedPropertyState)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".enterParameterizedProperty() not implemented yet");
        }
    };

    // TODO: Unified list of dataType and parameterized properties

    private final MutableList<AntlrAssociationEnd>               associationEndStates  = Lists.mutable.empty();
    private final MutableOrderedMap<String, AntlrAssociationEnd> associationEndsByName =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    private final MutableList<AntlrParameterizedProperty>                                     parameterizedPropertyStates      = Lists.mutable.empty();
    private final MutableOrderedMap<String, AntlrParameterizedProperty>                       parameterizedPropertiesByName    =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<ParameterizedPropertyContext, AntlrParameterizedProperty> parameterizedPropertiesByContext =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    private final boolean         isUser;
    private       InheritanceType inheritanceType = InheritanceType.NONE;

    private KlassBuilder         klassBuilder;
    @Nonnull
    private Optional<AntlrClass> superClassState = Optional.empty();

    public AntlrClass(
            @Nonnull ClassDeclarationContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            int ordinal,
            @Nonnull IdentifierContext nameContext,
            @Nonnull AntlrCompilationUnit compilationUnitState,
            boolean isUser)
    {
        super(elementContext, compilationUnit, ordinal, nameContext, compilationUnitState);
        this.isUser = isUser;
    }

    public boolean isUser()
    {
        return this.isUser;
    }

    @Override
    protected ImmutableList<AntlrProperty> getInheritedProperties(@Nonnull MutableList<AntlrClassifier> visited)
    {
        ImmutableList<AntlrProperty> superClassProperties = this.superClassState
                .map(antlrClass -> antlrClass.getProperties(visited))
                .orElseGet(Lists.immutable::empty);

        ImmutableList<AntlrProperty> interfaceProperties = this.interfaceStates
                .flatCollectWith(AntlrClassifier::getProperties, visited)
                .toImmutable();

        return superClassProperties.newWithAll(interfaceProperties).distinctBy(AntlrNamedElement::getName);
    }

    @Override
    protected ImmutableList<AntlrDataTypeProperty<?>> getInheritedDataTypeProperties(@Nonnull MutableList<AntlrClassifier> visited)
    {
        ImmutableList<AntlrDataTypeProperty<?>> superClassProperties = this.superClassState
                .map(antlrClass -> antlrClass.getDataTypeProperties(visited))
                .orElseGet(Lists.immutable::empty);

        ImmutableList<AntlrDataTypeProperty<?>> interfaceProperties = this.interfaceStates
                .flatCollectWith(AntlrClassifier::getDataTypeProperties, visited)
                .toImmutable();

        return superClassProperties.newWithAll(interfaceProperties).distinctBy(AntlrNamedElement::getName);
    }

    @Override
    protected ImmutableList<AntlrModifier> getInheritedModifiers(@Nonnull MutableList<AntlrClassifier> visited)
    {
        ImmutableList<AntlrModifier> superClassModifiers = this.superClassState
                .map(antlrClass -> antlrClass.getModifiers(visited)).orElseGet(Lists.immutable::empty);

        ImmutableList<AntlrModifier> interfaceModifiers = this.interfaceStates
                .flatCollectWith(AntlrClassifier::getModifiers, visited)
                .toImmutable();

        return superClassModifiers.newWithAll(interfaceModifiers).distinctBy(AntlrModifier::getKeyword);
    }

    @Override
    public int getNumMembers()
    {
        return this.getDataTypeProperties().size()
                + this.parameterizedPropertyStates.size()
                + this.associationEndStates.size()
                + this.associationEndSignatureStates.size();
    }

    @Override
    public AntlrReferenceProperty<?> getReferencePropertyByName(@Nonnull String name)
    {
        AntlrReferenceProperty<?> declaredProperty = this.referencePropertiesByName.get(name);
        if (declaredProperty != null)
        {
            return declaredProperty;
        }

        Optional<AntlrReferenceProperty<?>> superClassProperty = this.superClassState
                .map(superClass -> superClass.getReferencePropertyByName(name));
        if (superClassProperty.isPresent())
        {
            return superClassProperty.get();
        }

        return this.interfaceStates
                .asLazy()
                .collectWith(AntlrInterface::getReferencePropertyByName, name)
                .detect(Objects::nonNull);
    }

    public void enterAssociationEnd(@Nonnull AntlrAssociationEnd antlrAssociationEnd)
    {
        this.propertyStates.add(antlrAssociationEnd);
        this.associationEndStates.add(antlrAssociationEnd);
        this.associationEndsByName.compute(
                antlrAssociationEnd.getName(),
                (name, builder) -> builder == null
                        ? antlrAssociationEnd
                        : AntlrAssociationEnd.AMBIGUOUS);

        this.referencePropertyStates.add(antlrAssociationEnd);
        this.referencePropertiesByName.compute(
                antlrAssociationEnd.getName(),
                (name, builder) -> builder == null
                        ? antlrAssociationEnd
                        : AntlrAssociationEnd.AMBIGUOUS);
        AntlrReferenceProperty<?> duplicate2 = this.referencePropertiesByContext.put(
                antlrAssociationEnd.getElementContext(),
                antlrAssociationEnd);
        if (duplicate2 != null)
        {
            throw new AssertionError();
        }
    }

    public void enterParameterizedProperty(@Nonnull AntlrParameterizedProperty parameterizedPropertyState)
    {
        this.propertyStates.add(parameterizedPropertyState);
        this.parameterizedPropertyStates.add(parameterizedPropertyState);
        this.parameterizedPropertiesByName.compute(
                parameterizedPropertyState.getName(),
                (name, builder) -> builder == null
                        ? parameterizedPropertyState
                        : AntlrParameterizedProperty.AMBIGUOUS);

        AntlrParameterizedProperty duplicate1 = this.parameterizedPropertiesByContext.put(
                parameterizedPropertyState.getElementContext(),
                parameterizedPropertyState);
        if (duplicate1 != null)
        {
            throw new AssertionError();
        }

        this.referencePropertyStates.add(parameterizedPropertyState);
        this.referencePropertiesByName.compute(
                parameterizedPropertyState.getName(),
                (name, builder) -> builder == null
                        ? parameterizedPropertyState
                        : AntlrParameterizedProperty.AMBIGUOUS);
        AntlrReferenceProperty<?> duplicate2 = this.referencePropertiesByContext.put(
                parameterizedPropertyState.getElementContext(),
                parameterizedPropertyState);
        if (duplicate2 != null)
        {
            throw new AssertionError();
        }
    }

    public AntlrParameterizedProperty getParameterizedPropertyByContext(ParameterizedPropertyContext ctx)
    {
        return this.parameterizedPropertiesByContext.get(ctx);
    }

    public boolean isAbstract()
    {
        return this.inheritanceType != InheritanceType.NONE;
    }

    public InheritanceType getInheritanceType()
    {
        return this.inheritanceType;
    }

    public void setInheritanceType(InheritanceType inheritanceType)
    {
        this.inheritanceType = inheritanceType;
    }

    public void enterExtendsDeclaration(@Nonnull AntlrClass superClassState)
    {
        this.superClassState = Optional.of(superClassState);
    }

    @Nonnull
    @Override
    public KlassBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.klassBuilder);
    }

    @Override
    protected boolean implementsInterface(AntlrInterface interfaceState)
    {
        if (super.implementsInterface(interfaceState))
        {
            return true;
        }

        return this.superClassState
                .map(classState -> classState.implementsInterface(interfaceState))
                .orElse(false);
    }

    public KlassBuilder build1()
    {
        if (this.klassBuilder != null)
        {
            throw new IllegalStateException();
        }

        this.klassBuilder = new KlassBuilder(
                (ClassDeclarationContext) this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.ordinal,
                this.getNameContext(),
                this.getPackageName(),
                this.inheritanceType,
                this.isUser,
                this.isTransient());

        ImmutableList<ModifierBuilder> classifierModifierBuilders = this.modifierStates
                .collect(AntlrModifier::build)
                .toImmutable();
        this.klassBuilder.setModifierBuilders(classifierModifierBuilders);

        ImmutableList<DataTypePropertyBuilder<?, ?, ?>> dataTypePropertyBuilders = this.dataTypePropertyStates
                .<DataTypePropertyBuilder<?, ?, ?>>collect(AntlrDataTypeProperty::build)
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
                .collect(AntlrAssociationEnd::getElementBuilder)
                .toImmutable();

        this.klassBuilder.setAssociationEndBuilders(associationEndBuilders);

        Optional<AntlrAssociationEnd> versionAssociationEnd =
                this.associationEndStates.detectOptional(AntlrAssociationEnd::isVersion);
        Optional<AntlrAssociationEnd> versionedAssociationEnd =
                this.associationEndStates.detectOptional(AntlrAssociationEnd::isVersioned);

        Optional<AssociationEndBuilder> versionAssociationEndBuilder =
                versionAssociationEnd.map(AntlrAssociationEnd::getElementBuilder);
        Optional<AssociationEndBuilder> versionedAssociationEndBuilder =
                versionedAssociationEnd.map(AntlrAssociationEnd::getElementBuilder);

        this.klassBuilder.setVersionPropertyBuilder(versionAssociationEndBuilder);
        this.klassBuilder.setVersionedPropertyBuilder(versionedAssociationEndBuilder);

        ImmutableList<AssociationEndSignatureBuilder> associationEndSignatureBuilders = this.associationEndSignatureStates
                .collect(AntlrAssociationEndSignature::build)
                .toImmutable();

        this.klassBuilder.setAssociationEndSignatureBuilders(associationEndSignatureBuilders);

        this.dataTypePropertyStates.each(AntlrDataTypeProperty::build2);

        ImmutableList<InterfaceBuilder> interfaceBuilders = this.interfaceStates
                .collect(AntlrInterface::getElementBuilder)
                .toImmutable();

        this.klassBuilder.setInterfaceBuilders(interfaceBuilders);

        Optional<KlassBuilder> superClassBuilder = this.superClassState.map(AntlrClass::getElementBuilder);
        this.klassBuilder.setSuperClassBuilder(superClassBuilder);
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
        this.parameterizedPropertyStates.forEachWith(AntlrNamedElement::reportNameErrors, compilerErrorHolder);
        this.associationEndStates.forEachWith(AntlrNamedElement::reportNameErrors, compilerErrorHolder);
        this.associationEndSignatureStates.forEachWith(AntlrNamedElement::reportNameErrors, compilerErrorHolder);
    }

    @Override
    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        super.reportErrors(compilerErrorHolder);

        if (this.isUser)
        {
            this.reportMultiplePropertiesWithModifiers(
                    compilerErrorHolder,
                    this.dataTypePropertyStates,
                    "key",
                    "userId");
        }
        this.reportMissingKeyProperty(compilerErrorHolder);
        this.reportSuperClassNotFound(compilerErrorHolder);
        this.reportExtendsConcrete(compilerErrorHolder);
        this.reportMultipleOppositesWithModifier(compilerErrorHolder, "version");
        this.reportOwnedByMultipleTypes(compilerErrorHolder);
        this.reportVersionErrors(compilerErrorHolder);
        this.reportTransientInheritance(compilerErrorHolder);
        this.reportTransientIdProperties(compilerErrorHolder);

        // TODO: parameterized properties
    }

    private void reportMultipleOppositesWithModifier(@Nonnull CompilerErrorState compilerErrorHolder, String modifier)
    {
        MutableList<AntlrAssociationEnd> associationEnds = this.associationEndStates
                .select(associationEnd -> associationEnd
                        .getOpposite()
                        .getModifiers()
                        .anySatisfyWith(AntlrModifier::is, modifier));
        if (associationEnds.size() <= 1)
        {
            return;
        }

        for (AntlrAssociationEnd associationEnd : associationEnds)
        {
            associationEnd.getOpposite().reportDuplicateOppositeWithModifier(compilerErrorHolder, this, modifier);
        }
    }

    private void reportOwnedByMultipleTypes(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        MutableList<AntlrAssociationEnd> associationEnds = this.associationEndStates
                .select(associationEnd -> associationEnd
                        .getOpposite()
                        .getModifiers()
                        .anySatisfy(AntlrModifier::isOwned))
                .collect(AntlrAssociationEnd::getOpposite);
        if (associationEnds.collect(AntlrClassReferenceProperty::getType).distinct().size() <= 1)
        {
            return;
        }

        for (AntlrAssociationEnd associationEnd : associationEnds)
        {
            associationEnd.reportDuplicateOppositeWithModifier(compilerErrorHolder, this, "owned");
        }
    }

    private void reportVersionErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        if (this.referencePropertyStates.anySatisfy(AntlrReferenceProperty::isVersion)
                && this.associationEndStates.anySatisfy(AntlrAssociationEnd::isVersioned))
        {
            String message = String.format("Class '%s' is a version and has a version.", this.getName());
            compilerErrorHolder.add("ERR_VER_VER", message, this);
        }
    }

    private void reportMissingKeyProperty(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        if (!this.hasKeyProperty()
                && !this.hasIDProperty()
                && this.inheritanceTypeRequiresKeyProperties()
                && !this.superClassShouldHaveKey())
        {
            String message = String.format("Class '%s' must have at least one key property.", this.getName());
            compilerErrorHolder.add("ERR_CLS_KEY", message, this);
        }
    }

    private boolean hasIDProperty()
    {
        return this.getDataTypeProperties().anySatisfy(AntlrDataTypeProperty::isId);
    }

    private boolean hasKeyProperty()
    {
        return this.getDataTypeProperties().anySatisfy(AntlrDataTypeProperty::isKey);
    }

    private boolean superClassShouldHaveKey()
    {
        return this.superClassState
                .map(AntlrClass::getInheritanceType)
                .equals(Optional.of(InheritanceType.TABLE_PER_CLASS));
    }

    private boolean inheritanceTypeRequiresKeyProperties()
    {
        return this.inheritanceType == InheritanceType.NONE || this.inheritanceType == InheritanceType.TABLE_PER_CLASS;
    }

    public void reportDuplicateUserClass(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        String message = String.format(
                "Only one 'user' class is allowed. Found '%s'.",
                this.getName());
        compilerErrorHolder.add("ERR_DUP_USR", message, this, this.nameContext);
    }

    private void reportSuperClassNotFound(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        if (this.superClassState.equals(Optional.of(AntlrClass.NOT_FOUND)))
        {
            ClassReferenceContext offendingToken = this
                    .getElementContext()
                    .classHeader()
                    .extendsDeclaration()
                    .classReference();
            String message = String.format(
                    "Cannot find class '%s'.",
                    offendingToken.getText());
            compilerErrorHolder.add("ERR_EXT_CLS", message, this, offendingToken);
        }
    }

    private void reportExtendsConcrete(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        if (this.superClassState.isEmpty()
                || this.superClassState.equals(Optional.of(NOT_FOUND)))
        {
            return;
        }

        if (!this.superClassState.get().isAbstract())
        {
            ClassReferenceContext offendingToken = this
                    .getElementContext()
                    .classHeader()
                    .extendsDeclaration()
                    .classReference();
            String message = String.format(
                    "Superclass must be abstract '%s'.",
                    offendingToken.getText());
            compilerErrorHolder.add("ERR_EXT_CCT", message, this, offendingToken);
        }
    }

    private void reportTransientInheritance(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        if (this.isTransient()
                || this.superClassState.isEmpty()
                || !this.superClassState.get().isTransient())
        {
            return;
        }

        ClassReferenceContext offendingToken = this
                .getElementContext()
                .classHeader()
                .extendsDeclaration()
                .classReference();
        String message = String.format(
                "Must be transient to inherit from transient superclass '%s'.",
                offendingToken.getText());
        compilerErrorHolder.add("ERR_EXT_TNS", message, this, offendingToken);
    }

    private void reportTransientIdProperties(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        if (!this.isTransient())
        {
            return;
        }

        this.dataTypePropertyStates
                .select(AntlrDataTypeProperty::isId)
                .forEachWith(AntlrDataTypeProperty::reportTransientIdProperties, compilerErrorHolder);
    }

    @Override
    protected void reportCircularInheritance(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        if (this.superClassState.isEmpty())
        {
            return;
        }
        if (this.superClassState.get().extendsClass(this, Sets.mutable.empty()))
        {
            ClassReferenceContext offendingToken = this
                    .getElementContext()
                    .classHeader()
                    .extendsDeclaration()
                    .classReference();
            String message = String.format(
                    "Circular inheritance '%s'.",
                    offendingToken.getText());
            compilerErrorHolder.add("ERR_EXT_SLF", message, this, offendingToken);
        }
    }

    private boolean extendsClass(@Nonnull AntlrClass antlrClass, @Nonnull MutableSet<AntlrClass> visitedClasses)
    {
        if (this.superClassState.isEmpty())
        {
            return false;
        }

        if (this.superClassState.equals(Optional.of(antlrClass)))
        {
            return true;
        }

        if (visitedClasses.contains(this))
        {
            return false;
        }

        visitedClasses.add(this);
        return this.superClassState.get().extendsClass(antlrClass, visitedClasses);
    }

    @Override
    protected boolean isInterfaceRedundant(int index, @Nonnull AntlrInterface interfaceState)
    {
        return this.superClassState.isPresent() && this.superClassState.get().implementsInterface(interfaceState)
                || this.interfaceNotAtIndexImplements(index, interfaceState);
    }

    @Override
    protected InterfaceReferenceContext getOffendingInterfaceReference(int index)
    {
        return this.getElementContext().classHeader().implementsDeclaration().interfaceReference().get(index);
    }

    @Override
    public ImmutableBag<String> getDuplicateMemberNames()
    {
        return this.getDeclaredMemberNames()
                .toBag()
                .selectByOccurrences(occurrences -> occurrences > 1)
                .toImmutable();
    }

    private ImmutableList<String> getDeclaredMemberNames()
    {
        MutableList<String> topLevelNames = Lists.mutable.empty();
        this.getDataTypeProperties().collect(AntlrProperty::getName, topLevelNames);
        this.parameterizedPropertyStates.collect(AntlrNamedElement::getName, topLevelNames);
        this.associationEndStates.collect(AntlrProperty::getName, topLevelNames);
        this.associationEndSignatureStates.collect(AntlrProperty::getName, topLevelNames);
        return topLevelNames.toImmutable();
    }

    @Override
    public boolean isSubClassOf(AntlrClassifier classifier)
    {
        if (this == classifier)
        {
            return false;
        }

        if (this.superClassState.isEmpty())
        {
            return false;
        }

        AntlrClass superClass = this.superClassState.get();
        if (superClass == classifier)
        {
            return true;
        }

        return superClass.isSubClassOf(classifier);
    }

    @Nonnull
    @Override
    public ClassDeclarationContext getElementContext()
    {
        return (ClassDeclarationContext) super.getElementContext();
    }

    @Override
    public Pair<Token, Token> getContextBefore()
    {
        return Tuples.pair(this.getElementContext().getStart(), this.getElementContext().classBody().getStart());
    }

    @Override
    public AntlrDataTypeProperty<?> getDataTypePropertyByName(String name)
    {
        if (this.dataTypePropertiesByName.containsKey(name))
        {
            return this.dataTypePropertiesByName.get(name);
        }

        if (this.superClassState.isPresent())
        {
            AntlrDataTypeProperty<?> superClassProperty = this.superClassState.get().getDataTypePropertyByName(name);
            if (superClassProperty != AntlrEnumerationProperty.NOT_FOUND)
            {
                return superClassProperty;
            }
        }

        return this.getInterfaceDataTypePropertyByName(name);
    }

    public AntlrAssociationEnd getAssociationEndByName(String name)
    {
        if (this.associationEndsByName.containsKey(name))
        {
            return this.associationEndsByName.get(name);
        }

        return this.superClassState
                .map(superClass -> superClass.getAssociationEndByName(name))
                .orElse(AntlrAssociationEnd.NOT_FOUND);
    }

    public AntlrModifier getModifierByName(String name)
    {
        if (this.modifiersByName.containsKey(name))
        {
            return this.modifiersByName.get(name);
        }

        if (this.superClassState.isPresent())
        {
            AntlrModifier superClassProperty = this.superClassState.get().getModifierByName(name);
            if (superClassProperty != AntlrModifier.NOT_FOUND)
            {
                return superClassProperty;
            }
        }

        return this.getInterfaceClassifierModifierByName(name);
    }

    @Nonnull
    @Override
    public KlassBuilder getTypeGetter()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getTypeBuilder() not implemented yet");
    }

    public boolean isVersion()
    {
        return this.dataTypePropertyStates.anySatisfy(AntlrDataTypeProperty::isVersion);
    }
}
