package cool.klass.model.converter.compiler.state;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEndSignature;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.property.AntlrEnumerationProperty;
import cool.klass.model.converter.compiler.state.property.AntlrParameterizedProperty;
import cool.klass.model.converter.compiler.state.property.AntlrProperty;
import cool.klass.model.converter.compiler.state.property.AntlrReferenceTypeProperty;
import cool.klass.model.meta.domain.ClassifierModifierImpl.ClassifierModifierBuilder;
import cool.klass.model.meta.domain.InterfaceImpl.InterfaceBuilder;
import cool.klass.model.meta.domain.KlassImpl.KlassBuilder;
import cool.klass.model.meta.domain.api.InheritanceType;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty.DataTypePropertyBuilder;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import cool.klass.model.meta.domain.property.AssociationEndSignatureImpl.AssociationEndSignatureBuilder;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.InterfaceReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterizedPropertyContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Sets;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public class AntlrClass
        extends AntlrClassifier
{
    @Nonnull
    public static final AntlrClass AMBIGUOUS = new AntlrClass(
            new ClassDeclarationContext(null, -1),
            Optional.empty(),
            new ParserRuleContext(),
            "ambiguous class",
            -1,
            new ParserRuleContext(),
            "klass.meta",
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
            new ParserRuleContext(),
            "not found class",
            -1,
            new ParserRuleContext(),
            "klass.meta",
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
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull ParserRuleContext packageContext,
            @Nonnull String packageName,
            boolean isUser)
    {
        super(elementContext, compilationUnit, nameContext, name, ordinal, packageContext, packageName);
        this.isUser = isUser;
    }

    public boolean isUser()
    {
        return this.isUser;
    }

    @Override
    protected ImmutableList<AntlrDataTypeProperty<?>> getInheritedProperties(@Nonnull MutableList<AntlrClassifier> visited)
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
    protected ImmutableList<AntlrClassifierModifier> getInheritedModifiers(@Nonnull MutableList<AntlrClassifier> visited)
    {
        ImmutableList<AntlrClassifierModifier> superClassModifiers = this.superClassState
                .map(antlrClass -> antlrClass.getClassifierModifiers(visited)).orElseGet(Lists.immutable::empty);

        ImmutableList<AntlrClassifierModifier> interfaceModifiers = this.interfaceStates
                .flatCollectWith(AntlrClassifier::getClassifierModifiers, visited)
                .toImmutable();

        return superClassModifiers.newWithAll(interfaceModifiers).distinctBy(AntlrNamedElement::getName);
    }

    @Override
    public int getNumMembers()
    {
        return this.getDataTypeProperties().size()
                + this.parameterizedPropertyStates.size()
                + this.associationEndStates.size()
                + this.associationEndSignatureStates.size();
    }

    public AntlrReferenceTypeProperty<?> getReferenceTypePropertyByName(@Nonnull String name)
    {
        AntlrReferenceTypeProperty<?> declaredProperty = this.referenceTypePropertiesByName.get(name);
        if (declaredProperty != null)
        {
            return declaredProperty;
        }

        Optional<AntlrReferenceTypeProperty<?>> superClassProperty = this.superClassState
                .map(superClass -> superClass.getReferenceTypePropertyByName(name));
        if (superClassProperty.isPresent())
        {
            return superClassProperty.get();
        }

        return this.interfaceStates
                .asLazy()
                .collectWith(AntlrInterface::getReferenceTypePropertyByName, name)
                .detect(Objects::nonNull);
    }

    public void enterAssociationEnd(@Nonnull AntlrAssociationEnd antlrAssociationEnd)
    {
        this.associationEndStates.add(antlrAssociationEnd);
        this.associationEndsByName.compute(
                antlrAssociationEnd.getName(),
                (name, builder) -> builder == null
                        ? antlrAssociationEnd
                        : AntlrAssociationEnd.AMBIGUOUS);

        this.referenceTypePropertyStates.add(antlrAssociationEnd);
        this.referenceTypePropertiesByName.compute(
                antlrAssociationEnd.getName(),
                (name, builder) -> builder == null
                        ? antlrAssociationEnd
                        : AntlrAssociationEnd.AMBIGUOUS);
        AntlrReferenceTypeProperty duplicate2 = this.referenceTypePropertiesByContext.put(
                antlrAssociationEnd.getElementContext(),
                antlrAssociationEnd);
        if (duplicate2 != null)
        {
            throw new AssertionError();
        }
    }

    public void enterParameterizedProperty(@Nonnull AntlrParameterizedProperty parameterizedPropertyState)
    {
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

        this.referenceTypePropertyStates.add(parameterizedPropertyState);
        this.referenceTypePropertiesByName.compute(
                parameterizedPropertyState.getName(),
                (name, builder) -> builder == null
                        ? parameterizedPropertyState
                        : AntlrParameterizedProperty.AMBIGUOUS);
        AntlrReferenceTypeProperty duplicate2 = this.referenceTypePropertiesByContext.put(
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
                this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.nameContext,
                this.name,
                this.ordinal,
                this.packageName,
                this.inheritanceType,
                this.isUser,
                this.isTransient());

        ImmutableList<ClassifierModifierBuilder> classifierModifierBuilders = this.classifierModifierStates
                .collect(AntlrClassifierModifier::build)
                .toImmutable();
        this.klassBuilder.setClassifierModifierBuilders(classifierModifierBuilders);

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

        this.reportDuplicateParameterizedPropertyNames(compilerErrorHolder);
        this.reportDuplicateAssociationEndNames(compilerErrorHolder);
        this.reportVersionErrors(compilerErrorHolder);
        this.reportMissingKeyProperty(compilerErrorHolder);
        this.reportSuperClassNotFound(compilerErrorHolder);
        this.reportExtendsConcrete(compilerErrorHolder);
        this.reportTransientInheritance(compilerErrorHolder);
        this.reportTransientIdProperties(compilerErrorHolder);

        // TODO: parameterized properties
    }

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

    private void reportVersionErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        MutableList<AntlrAssociationEnd> versionAssociationEnds =
                this.associationEndStates.select(AntlrAssociationEnd::isVersion);
        if (versionAssociationEnds.size() > 1)
        {
            for (AntlrAssociationEnd antlrAssociationEnd : versionAssociationEnds)
            {
                antlrAssociationEnd.reportDuplicateVersionProperty(compilerErrorHolder, this);
            }
        }

        MutableList<AntlrAssociationEnd> versionedAssociationEnds =
                this.associationEndStates.select(AntlrAssociationEnd::isVersioned);
        if (versionedAssociationEnds.size() > 1)
        {
            for (AntlrAssociationEnd versionedAssociationEnd : versionedAssociationEnds)
            {
                versionedAssociationEnd.reportDuplicateVersionedProperty(compilerErrorHolder, this);
            }
        }

        if (versionAssociationEnds.notEmpty() && versionedAssociationEnds.notEmpty())
        {
            String message = String.format("Class '%s' is a version and has a version.", this.name);
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
            String message = String.format("Class '%s' must have at least one key property.", this.name);
            compilerErrorHolder.add("ERR_CLS_KEY", message, this);
        }
    }

    private boolean hasIDProperty()
    {
        return this.getDataTypeProperties().anySatisfy(AntlrDataTypeProperty::isID);
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
        IdentifierContext offendingToken = this.getElementContext().classHeader().identifier();
        String message = String.format(
                "Only one 'user' class is allowed. Found '%s'.",
                offendingToken.getText());
        compilerErrorHolder.add("ERR_DUP_USR", message, this, offendingToken);
    }

    public void reportDuplicateUserProperties(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        MutableList<AntlrDataTypeProperty<?>> userIdProperties =
                this.dataTypePropertyStates.select(AntlrDataTypeProperty::isUserId);
        if (userIdProperties.size() > 1)
        {
            for (AntlrDataTypeProperty<?> userIdProperty : userIdProperties)
            {
                userIdProperty.reportDuplicateUserProperty(compilerErrorHolder);
            }
        }
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
        if (!this.superClassState.isPresent()
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
                || !this.superClassState.isPresent()
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
        if (!this.isTransient()
                || this.getDataTypeProperties().noneSatisfy(AntlrDataTypeProperty::isID))
        {
            return;
        }

        IdentifierContext offendingToken = this.getElementContext().classHeader().identifier();
        String message = String.format(
                "Transient class '%s' may not have id properties.",
                offendingToken.getText());
        compilerErrorHolder.add("ERR_TNS_IDP", message, this, offendingToken);
    }

    @Override
    protected void reportCircularInheritance(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        if (!this.superClassState.isPresent())
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
        if (!this.superClassState.isPresent())
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
    protected boolean isInterfaceRedundant(int index, AntlrInterface interfaceState)
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

    @Override
    protected ImmutableList<String> getDeclaredMemberNames()
    {
        MutableList<String> topLevelNames = Lists.mutable.empty();
        this.getDataTypeProperties().collect(AntlrProperty::getName, topLevelNames);
        this.parameterizedPropertyStates.collect(AntlrNamedElement::getName, topLevelNames);
        this.associationEndStates.collect(AntlrProperty::getName, topLevelNames);
        this.associationEndSignatureStates.collect(AntlrProperty::getName, topLevelNames);
        return topLevelNames.toImmutable();
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public void reportAuditErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        super.reportAuditErrors(compilerErrorHolder);

        this.parameterizedPropertyStates.each(each -> each.reportAuditErrors(compilerErrorHolder));
    }

    public boolean isSubClassOf(AntlrClassifier classifier)
    {
        if (this == classifier)
        {
            return false;
        }

        if (!this.superClassState.isPresent())
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

    @Override
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

    public MutableList<AntlrAssociationEnd> getAssociationEndStates()
    {
        return this.associationEndStates.asUnmodifiable();
    }

    @Override
    public AntlrClassifierModifier getClassifierModifierByName(String name)
    {
        if (this.classifierModifiersByName.containsKey(name))
        {
            return this.classifierModifiersByName.get(name);
        }

        if (this.superClassState.isPresent())
        {
            AntlrClassifierModifier superClassProperty = this.superClassState.get().getClassifierModifierByName(name);
            if (superClassProperty != AntlrClassifierModifier.NOT_FOUND)
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

    public boolean hasVersion()
    {
        return this.associationEndStates.anySatisfy(AntlrAssociationEnd::isVersion);
    }
}
