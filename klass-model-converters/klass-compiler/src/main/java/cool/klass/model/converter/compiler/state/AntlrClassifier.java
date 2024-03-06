package cool.klass.model.converter.compiler.state;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.annotation.CompilerAnnotationState;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEndSignature;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.property.AntlrEnumerationProperty;
import cool.klass.model.converter.compiler.state.property.AntlrModifier;
import cool.klass.model.converter.compiler.state.property.AntlrProperty;
import cool.klass.model.converter.compiler.state.property.AntlrReferenceProperty;
import cool.klass.model.meta.domain.AbstractClassifier.ClassifierBuilder;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndSignatureContext;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.InterfaceReferenceContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.bag.ImmutableBag;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.api.map.OrderedMap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Sets;
import org.eclipse.collections.impl.list.Interval;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public abstract class AntlrClassifier
        extends AntlrPackageableElement
        implements AntlrType, AntlrTopLevelElement
{
    //<editor-fold desc="AMBIGUOUS">
    public static final AntlrClassifier AMBIGUOUS = new AntlrClassifier(
            new ClassDeclarationContext(AMBIGUOUS_PARENT, -1),
            Optional.empty(),
            -1,
            AMBIGUOUS_IDENTIFIER_CONTEXT,
            AntlrCompilationUnit.AMBIGUOUS)
    {
        @Override
        public AntlrReferenceProperty<?> getReferencePropertyByName(@Nonnull String name)
        {
            return AntlrReferenceProperty.AMBIGUOUS;
        }

        @Override
        public AntlrDataTypeProperty<?> getDataTypePropertyByName(String name)
        {
            return AntlrDataTypeProperty.AMBIGUOUS;
        }

        @Override
        public String toString()
        {
            return AntlrClassifier.class.getSimpleName() + ".AMBIGUOUS";
        }
    };
    //</editor-fold>

    //<editor-fold desc="NOT_FOUND">
    public static final AntlrClassifier NOT_FOUND = new AntlrClassifier(
            new ClassDeclarationContext(NOT_FOUND_PARENT, -1),
            Optional.empty(),
            -1,
            NOT_FOUND_IDENTIFIER_CONTEXT,
            AntlrCompilationUnit.AMBIGUOUS)
    {
        @Override
        public AntlrReferenceProperty<?> getReferencePropertyByName(@Nonnull String name)
        {
            return AntlrReferenceProperty.NOT_FOUND;
        }

        @Override
        public AntlrDataTypeProperty<?> getDataTypePropertyByName(String name)
        {
            return AntlrDataTypeProperty.NOT_FOUND;
        }

        @Override
        public String toString()
        {
            return AntlrClassifier.class.getSimpleName() + ".NOT_FOUND";
        }
    };
    //</editor-fold>

    protected final MutableList<AntlrAssociationEndSignature>               associationEndSignatureStates  =
            Lists.mutable.empty();
    protected final MutableOrderedMap<String, AntlrAssociationEndSignature> associationEndSignaturesByName =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    protected final MutableOrderedMap<AssociationEndSignatureContext, AntlrAssociationEndSignature> associationEndSignaturesByContext =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    protected final MutableList<AntlrReferenceProperty<?>>               referencePropertyStates   =
            Lists.mutable.empty();
    protected final MutableOrderedMap<String, AntlrReferenceProperty<?>> referencePropertiesByName =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    protected final MutableOrderedMap<ParserRuleContext, AntlrReferenceProperty<?>> referencePropertiesByContext =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    protected final MutableList<AntlrModifier>            modifierStates         = Lists.mutable.empty();
    protected final MutableList<AntlrProperty>            propertyStates         = Lists.mutable.empty();
    protected final MutableList<AntlrDataTypeProperty<?>> dataTypePropertyStates = Lists.mutable.empty();
    protected final MutableList<AntlrInterface>           interfaceStates        = Lists.mutable.empty();

    protected final MutableOrderedMap<String, AntlrDataTypeProperty<?>> dataTypePropertiesByName =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    protected final MutableOrderedMap<String, AntlrModifier>            modifiersByName          =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());
    protected final MutableOrderedMap<ParserRuleContext, AntlrModifier> modifiersByContext       =
            OrderedMapAdapter.adapt(new LinkedHashMap<>());

    protected AntlrClassifier(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            int ordinal,
            @Nonnull IdentifierContext nameContext,
            @Nonnull AntlrCompilationUnit compilationUnitState)
    {
        super(elementContext, compilationUnit, ordinal, nameContext, compilationUnitState);
    }

    public abstract AntlrReferenceProperty<?> getReferencePropertyByName(@Nonnull String name);

    public abstract AntlrDataTypeProperty<?> getDataTypePropertyByName(String name);

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

    @Nonnull
    @Override
    public ClassifierBuilder<?> getTypeGetter()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getTypeGetter() not implemented yet");
    }

    public final ImmutableList<AntlrProperty> getProperties()
    {
        return this.getProperties(Lists.mutable.empty());
    }

    protected ImmutableList<AntlrProperty> getProperties(@Nonnull MutableList<AntlrClassifier> visited)
    {
        if (visited.contains(this))
        {
            return Lists.immutable.empty();
        }
        visited.add(this);

        MutableSet<String> propertyNames = this.propertyStates.collect(AntlrNamedElement::getName).toSet();

        ImmutableList<AntlrProperty> inheritedProperties = this.getInheritedProperties(visited)
                .reject(inheritedProperty -> propertyNames.contains(inheritedProperty.getName()));

        return this.propertyStates.toImmutable().newWithAll(inheritedProperties);
    }

    protected ImmutableList<AntlrProperty> getInheritedProperties(@Nonnull MutableList<AntlrClassifier> visited)
    {
        return this.interfaceStates
                .flatCollectWith(AntlrClassifier::getProperties, visited)
                .distinctBy(AntlrNamedElement::getName)
                .toImmutable();
    }

    public final ImmutableList<AntlrDataTypeProperty<?>> getDataTypeProperties()
    {
        return this.getDataTypeProperties(Lists.mutable.empty());
    }

    protected ImmutableList<AntlrDataTypeProperty<?>> getDataTypeProperties(@Nonnull MutableList<AntlrClassifier> visited)
    {
        if (visited.contains(this))
        {
            return Lists.immutable.empty();
        }
        visited.add(this);

        MutableSet<String> propertyNames = this.dataTypePropertyStates.collect(AntlrNamedElement::getName).toSet();

        ImmutableList<AntlrDataTypeProperty<?>> inheritedProperties = this.getInheritedDataTypeProperties(visited)
                .reject(inheritedProperty -> propertyNames.contains(inheritedProperty.getName()));

        return this.dataTypePropertyStates.toImmutable().newWithAll(inheritedProperties);
    }

    protected ImmutableList<AntlrDataTypeProperty<?>> getInheritedDataTypeProperties(@Nonnull MutableList<AntlrClassifier> visited)
    {
        return this.interfaceStates
                .flatCollectWith(AntlrClassifier::getDataTypeProperties, visited)
                .distinctBy(AntlrNamedElement::getName)
                .toImmutable();
    }

    private ImmutableList<AntlrModifier> getModifiers()
    {
        return this.getModifiers(Lists.mutable.empty());
    }

    protected ImmutableList<AntlrModifier> getModifiers(@Nonnull MutableList<AntlrClassifier> visited)
    {
        if (visited.contains(this))
        {
            return Lists.immutable.empty();
        }
        visited.add(this);

        MutableSet<String> modifierNames = this.modifierStates.collect(AntlrModifier::getKeyword).toSet();

        ImmutableList<AntlrModifier> inheritedModifiers = this.getInheritedModifiers(visited)
                .reject(inheritedProperty -> modifierNames.contains(inheritedProperty.getKeyword()));

        return this.modifierStates.toImmutable().newWithAll(inheritedModifiers);
    }

    protected ImmutableList<AntlrModifier> getInheritedModifiers(@Nonnull MutableList<AntlrClassifier> visited)
    {
        return this.interfaceStates
                .flatCollectWith(AntlrClassifier::getModifiers, visited)
                .distinctBy(AntlrModifier::getKeyword)
                .toImmutable();
    }

    public boolean isTransient()
    {
        return this.getModifiers().anySatisfy(AntlrModifier::isTransient);
    }

    public void enterDataTypeProperty(@Nonnull AntlrDataTypeProperty<?> antlrDataTypeProperty)
    {
        Objects.requireNonNull(antlrDataTypeProperty);
        this.propertyStates.add(antlrDataTypeProperty);
        this.dataTypePropertyStates.add(antlrDataTypeProperty);
        this.dataTypePropertiesByName.compute(
                antlrDataTypeProperty.getName(),
                (name, builder) -> builder == null
                        ? antlrDataTypeProperty
                        : AntlrDataTypeProperty.AMBIGUOUS);
    }

    public AntlrAssociationEndSignature getAssociationEndSignatureByContext(@Nonnull AssociationEndSignatureContext ctx)
    {
        Objects.requireNonNull(ctx);
        return this.associationEndSignaturesByContext.get(ctx);
    }

    public AntlrReferenceProperty<?> getReferencePropertyByContext(@Nonnull ParserRuleContext ctx)
    {
        Objects.requireNonNull(ctx);
        return this.referencePropertiesByContext.get(ctx);
    }

    public void enterAssociationEndSignature(@Nonnull AntlrAssociationEndSignature associationEndSignatureState)
    {
        Objects.requireNonNull(associationEndSignatureState);
        this.propertyStates.add(associationEndSignatureState);
        this.associationEndSignatureStates.add(associationEndSignatureState);
        this.associationEndSignaturesByName.compute(
                associationEndSignatureState.getName(),
                (name, builder) -> builder == null
                        ? associationEndSignatureState
                        : AntlrAssociationEndSignature.AMBIGUOUS);
        AntlrAssociationEndSignature duplicate1 = this.associationEndSignaturesByContext.put(
                associationEndSignatureState.getElementContext(),
                associationEndSignatureState);
        if (duplicate1 != null)
        {
            throw new AssertionError();
        }

        this.referencePropertyStates.add(associationEndSignatureState);
        this.referencePropertiesByName.compute(
                associationEndSignatureState.getName(),
                (name, builder) -> builder == null
                        ? associationEndSignatureState
                        : AntlrAssociationEndSignature.AMBIGUOUS);
        AntlrReferenceProperty<?> duplicate2 = this.referencePropertiesByContext.put(
                associationEndSignatureState.getElementContext(),
                associationEndSignatureState);
        if (duplicate2 != null)
        {
            throw new AssertionError();
        }
    }

    public void enterModifier(@Nonnull AntlrModifier modifierState)
    {
        Objects.requireNonNull(modifierState);
        this.modifierStates.add(modifierState);
        this.modifiersByName.compute(
                modifierState.getKeyword(),
                (name, builder) -> builder == null
                        ? modifierState
                        : AntlrModifier.AMBIGUOUS);

        AntlrModifier duplicate = this.modifiersByContext.put(
                modifierState.getElementContext(),
                modifierState);
        if (duplicate != null)
        {
            throw new AssertionError();
        }
    }

    public AntlrModifier getModifierByContext(@Nonnull ParserRuleContext modifierContext)
    {
        Objects.requireNonNull(modifierContext);
        return this.modifiersByContext.get(modifierContext);
    }

    public int getNumClassifierModifiers()
    {
        return this.modifierStates.size();
    }

    public void enterImplementsDeclaration(@Nonnull AntlrInterface interfaceState)
    {
        Objects.requireNonNull(interfaceState);
        this.interfaceStates.add(interfaceState);
    }

    @OverridingMethodsMustInvokeSuper
    protected boolean implementsInterface(AntlrInterface interfaceState)
    {
        return this.interfaceStates.contains(interfaceState)
                || this.interfaceStates.anySatisfyWith(AntlrClassifier::implementsInterface, interfaceState);
    }

    @Nonnull
    public MutableOrderedMap<AntlrAssociationEnd, MutableOrderedMap<AntlrDataTypeProperty<?>, AntlrDataTypeProperty<?>>> getForeignKeys()
    {
        MutableOrderedMap<AntlrAssociationEnd, MutableOrderedMap<AntlrDataTypeProperty<?>, AntlrDataTypeProperty<?>>> foreignKeyConstraints =
                OrderedMapAdapter.adapt(new LinkedHashMap<>());

        for (AntlrDataTypeProperty<?> foreignKey : this.getDataTypeProperties())
        {
            OrderedMap<AntlrAssociationEnd, MutableList<AntlrDataTypeProperty<?>>> keysMatchingThisForeignKey = foreignKey.getKeysMatchingThisForeignKey();

            keysMatchingThisForeignKey.forEachKeyValue((associationEnd, keys) ->
            {
                MutableOrderedMap<AntlrDataTypeProperty<?>, AntlrDataTypeProperty<?>> dataTypeProperties = foreignKeyConstraints.computeIfAbsent(
                        associationEnd,
                        ignored -> OrderedMapAdapter.adapt(new LinkedHashMap<>()));

                dataTypeProperties.put(foreignKey, keys.getOnly());
            });
        }

        return foreignKeyConstraints;
    }

    //<editor-fold desc="Report Compiler Errors">
    @OverridingMethodsMustInvokeSuper
    public void reportErrors(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        this.reportDuplicatePropertyNames(compilerAnnotationHolder);
        this.reportMultiplePropertiesWithModifiers(compilerAnnotationHolder, this.dataTypePropertyStates, "id");
        this.reportMultiplePropertiesWithModifiers(compilerAnnotationHolder, this.dataTypePropertyStates, "version");
        this.reportMultiplePropertiesWithModifiers(compilerAnnotationHolder, this.dataTypePropertyStates, "createdBy");
        this.reportMultiplePropertiesWithModifiers(compilerAnnotationHolder, this.dataTypePropertyStates, "lastUpdatedBy");
        this.reportMultiplePropertiesWithModifiers(compilerAnnotationHolder, this.referencePropertyStates, "version");
        this.reportMultiplePropertiesWithModifiers(compilerAnnotationHolder, this.referencePropertyStates, "createdBy");
        this.reportMultiplePropertiesWithModifiers(compilerAnnotationHolder, this.referencePropertyStates, "lastUpdatedBy");
        this.reportIdAndKeyProperties(compilerAnnotationHolder);
        this.reportInterfaceNotFound(compilerAnnotationHolder);
        this.reportRedundantInterface(compilerAnnotationHolder);
        this.reportCircularInheritance(compilerAnnotationHolder);
        this.reportPropertyDeclarationOrder(compilerAnnotationHolder);
        this.reportDuplicateAssociationEndSignatureNames(compilerAnnotationHolder);
    }

    private void reportDuplicatePropertyNames(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        ImmutableBag<String> duplicateMemberNames = this.getDuplicateMemberNames();
        for (AntlrProperty property : this.propertyStates)
        {
            if (duplicateMemberNames.contains(property.getName()))
            {
                property.reportDuplicateMemberName(compilerAnnotationHolder);
            }
            property.reportErrors(compilerAnnotationHolder);
        }
    }

    protected <T extends AntlrProperty> void reportMultiplePropertiesWithModifiers(
            @Nonnull CompilerAnnotationState compilerAnnotationHolder,
            MutableList<T> propertyStates,
            String... modifiersArray)
    {
        ImmutableList<String> modifiers = Lists.immutable.with(modifiersArray);
        MutableList<T> properties = propertyStates
                .select(property -> modifiers
                        .allSatisfy(modifier -> property.getModifiers().anySatisfyWith(AntlrModifier::is, modifier)));

        if (properties.size() <= 1)
        {
            return;
        }

        for (AntlrProperty property : properties)
        {
            property.reportDuplicatePropertyWithModifiers(compilerAnnotationHolder, modifiers);
        }
    }

    private void reportIdAndKeyProperties(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        MutableList<AntlrDataTypeProperty<?>> idProperties = this.dataTypePropertyStates
                .select(AntlrDataTypeProperty::isId);
        if (idProperties.isEmpty())
        {
            return;
        }

        ImmutableList<AntlrDataTypeProperty<?>> nonIdKeyProperties = this.getKeyProperties()
                .reject(AntlrDataTypeProperty::isId);
        if (nonIdKeyProperties.isEmpty())
        {
            return;
        }

        for (AntlrDataTypeProperty<?> idProperty : idProperties)
        {
            idProperty.reportIdPropertyWithKeyProperties(compilerAnnotationHolder);
        }

        for (AntlrDataTypeProperty<?> nonIdKeyProperty : nonIdKeyProperties)
        {
            nonIdKeyProperty.reportKeyPropertyWithIdProperties(compilerAnnotationHolder);
        }
    }

    private void reportInterfaceNotFound(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
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
                compilerAnnotationHolder.add("ERR_IMP_INT", message, this, offendingToken);
            }
        }
    }

    private void reportRedundantInterface(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
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
                compilerAnnotationHolder.add("ERR_DUP_INT", message, this, offendingToken);
            }

            if (this.isInterfaceRedundant(i, interfaceState))
            {
                InterfaceReferenceContext offendingToken = this.getOffendingInterfaceReference(i);
                String message = String.format(
                        "Redundant interface '%s'.",
                        offendingToken.getText());
                compilerAnnotationHolder.add("ERR_RED_INT", message, this, offendingToken);
            }

            visitedInterfaceStates.add(interfaceState);
        }
    }

    private void reportDuplicateAssociationEndSignatureNames(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        ImmutableBag<String> duplicateMemberNames = this.getDuplicateMemberNames();

        for (AntlrAssociationEndSignature associationEndSignatureState : this.associationEndSignatureStates)
        {
            if (duplicateMemberNames.contains(associationEndSignatureState.getName()))
            {
                associationEndSignatureState.reportDuplicateMemberName(compilerAnnotationHolder);
            }
            associationEndSignatureState.reportErrors(compilerAnnotationHolder);
        }
    }

    protected void reportCircularInheritance(CompilerAnnotationState compilerAnnotationHolder)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".reportCircularInheritance() not implemented yet");
    }

    protected void reportPropertyDeclarationOrder(CompilerAnnotationState compilerAnnotationHolder)
    {
        ImmutableList<AntlrDataTypeProperty<?>> dataTypeProperties = this.dataTypePropertyStates.reject(AntlrElement::hasMacro).toImmutable();

        MutableList<AntlrDataTypeProperty<?>> orderedDataTypeProperties = Lists.mutable.empty();

        ImmutableList<AntlrDataTypeProperty<?>> foreignKeys        = dataTypeProperties.select(AntlrDataTypeProperty::isForeignKey);

        ImmutableList<AntlrDataTypeProperty<?>> keysAndForeignKeys = foreignKeys.select(AntlrDataTypeProperty::isKey).reject(orderedDataTypeProperties::contains);
        orderedDataTypeProperties.addAllIterable(keysAndForeignKeys);
        ImmutableList<AntlrDataTypeProperty<?>> keys               = dataTypeProperties.select(AntlrDataTypeProperty::isKey).reject(AntlrDataTypeProperty::isForeignKey).reject(orderedDataTypeProperties::contains);
        orderedDataTypeProperties.addAllIterable(keys);
        ImmutableList<AntlrDataTypeProperty<?>> nonKeyForeignKeys  = foreignKeys.reject(AntlrDataTypeProperty::isKey).reject(AntlrDataTypeProperty::isCreatedBy).reject(AntlrDataTypeProperty::isLastUpdatedBy).reject(orderedDataTypeProperties::contains);
        orderedDataTypeProperties.addAllIterable(nonKeyForeignKeys);
        ImmutableList<AntlrDataTypeProperty<?>> system             = dataTypeProperties.select(AntlrDataTypeProperty::isSystemRange).reject(orderedDataTypeProperties::contains);
        orderedDataTypeProperties.addAllIterable(system);
        ImmutableList<AntlrDataTypeProperty<?>> systemFrom         = dataTypeProperties.select(AntlrDataTypeProperty::isSystemFrom).reject(orderedDataTypeProperties::contains);
        orderedDataTypeProperties.addAllIterable(systemFrom);
        ImmutableList<AntlrDataTypeProperty<?>> systemTo           = dataTypeProperties.select(AntlrDataTypeProperty::isSystemTo).reject(orderedDataTypeProperties::contains);
        orderedDataTypeProperties.addAllIterable(systemTo);
        ImmutableList<AntlrDataTypeProperty<?>> valid              = dataTypeProperties.select(AntlrDataTypeProperty::isValidRange).reject(orderedDataTypeProperties::contains);
        orderedDataTypeProperties.addAllIterable(valid);
        ImmutableList<AntlrDataTypeProperty<?>> validFrom          = dataTypeProperties.select(AntlrDataTypeProperty::isValidFrom).reject(orderedDataTypeProperties::contains);
        orderedDataTypeProperties.addAllIterable(validFrom);
        ImmutableList<AntlrDataTypeProperty<?>> validTo            = dataTypeProperties.select(AntlrDataTypeProperty::isValidTo).reject(orderedDataTypeProperties::contains);
        orderedDataTypeProperties.addAllIterable(validTo);
        ImmutableList<AntlrDataTypeProperty<?>> createdBy          = dataTypeProperties.select(AntlrDataTypeProperty::isCreatedBy).reject(AntlrDataTypeProperty::isKey).reject(orderedDataTypeProperties::contains);
        orderedDataTypeProperties.addAllIterable(createdBy);
        ImmutableList<AntlrDataTypeProperty<?>> createdOn          = dataTypeProperties.select(AntlrDataTypeProperty::isCreatedOn).reject(orderedDataTypeProperties::contains);
        orderedDataTypeProperties.addAllIterable(createdOn);
        ImmutableList<AntlrDataTypeProperty<?>> lastUpdatedBy      = dataTypeProperties.select(AntlrDataTypeProperty::isLastUpdatedBy).reject(AntlrDataTypeProperty::isKey).reject(orderedDataTypeProperties::contains);
        orderedDataTypeProperties.addAllIterable(lastUpdatedBy);
        ImmutableList<AntlrDataTypeProperty<?>> otherDataTypeProperties = dataTypeProperties.reject(orderedDataTypeProperties::contains);
        orderedDataTypeProperties.addAllIterable(otherDataTypeProperties);

        if (!orderedDataTypeProperties.equals(orderedDataTypeProperties.distinct()))
        {
            throw new AssertionError(orderedDataTypeProperties);
        }

        if (!dataTypeProperties.equals(orderedDataTypeProperties))
        {
            String allAdditionalContext = ""
                    + this.getContext("keys and foreign keys:     ", keysAndForeignKeys)
                    + this.getContext("keys:                      ", keys)
                    + this.getContext("foreign keys but not keys: ", nonKeyForeignKeys)
                    + this.getContext("system range:              ", system)
                    + this.getContext("system from:               ", systemFrom)
                    + this.getContext("system to:                 ", systemTo)
                    + this.getContext("valid range:               ", valid)
                    + this.getContext("valid from:                ", validFrom)
                    + this.getContext("valid to:                  ", validTo)
                    + this.getContext("created by:                ", createdBy)
                    + this.getContext("created on:                ", createdOn)
                    + this.getContext("last updated by:           ", lastUpdatedBy)
                    + this.getContext("Other:                     ", otherDataTypeProperties);

            String message = String.format(
                    "The properties of class '%s' are not declared in the correct order. Expected '%s' but found '%s'.%n%s",
                    this.getName(),
                    orderedDataTypeProperties.collect(AntlrNamedElement::getName),
                    dataTypeProperties.collect(AntlrNamedElement::getName),
                    allAdditionalContext);
            compilerAnnotationHolder.add("ERR_DTP_ORD", message, this);
        }
    }

    @Nonnull
    private String getContext(String description, ImmutableList<AntlrDataTypeProperty<?>> properties)
    {
        return properties.isEmpty() ? "" : description
                + properties.collect(AntlrNamedElement::getName) + ".\n";
    }

    @OverridingMethodsMustInvokeSuper
    public void reportAuditErrors(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        this.reportAuditErrors(compilerAnnotationHolder, this.modifierStates, this);
        this.dataTypePropertyStates.each(each -> each.reportAuditErrors(compilerAnnotationHolder));
    }

    protected void reportForwardReference(CompilerAnnotationState compilerAnnotationHolder)
    {
        for (int i = 0; i < this.interfaceStates.size(); i++)
        {
            AntlrInterface interfaceState = this.interfaceStates.get(i);
            if (this.isForwardReference(interfaceState))
            {
                String message = String.format(
                        "Class '%s' is declared on line %d and has a forward reference to implemented interface '%s' which is declared later in the source file '%s' on line %d.",
                        this.getName(),
                        this.getElementContext().getStart().getLine(),
                        interfaceState.getName(),
                        this.getCompilationUnit().get().getSourceName(),
                        interfaceState.getElementContext().getStart().getLine());
                compilerAnnotationHolder.add("ERR_FWD_REF", message, this, this.getOffendingInterfaceReference(i));
            }
        }
    }
    //</editor-fold>

    protected boolean isInterfaceRedundant(int index, @Nonnull AntlrInterface interfaceState)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".isInterfaceRedundant() not implemented yet");
    }

    protected boolean interfaceNotAtIndexImplements(int index, @Nonnull AntlrInterface interfaceState)
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

    protected ImmutableBag<String> getDuplicateMemberNames()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getDuplicateMemberNames() not implemented yet");
    }

    protected AntlrDataTypeProperty<?> getInterfaceDataTypePropertyByName(String name)
    {
        return this.interfaceStates
                .asLazy()
                .<String, AntlrDataTypeProperty<?>>collectWith(AntlrInterface::getDataTypePropertyByName, name)
                .detectOptional(interfaceProperty -> interfaceProperty != AntlrEnumerationProperty.NOT_FOUND)
                .orElse(AntlrEnumerationProperty.NOT_FOUND);
    }

    protected AntlrModifier getInterfaceClassifierModifierByName(String name)
    {
        return this.interfaceStates
                .asLazy()
                .collectWith(AntlrInterface::getModifierByName, name)
                .detectOptional(interfaceModifier -> interfaceModifier != AntlrModifier.NOT_FOUND)
                .orElse(AntlrModifier.NOT_FOUND);
    }

    public boolean isSubClassOf(AntlrClassifier classifier)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".isSubClassOf() not implemented yet");
    }

    public ImmutableList<AntlrDataTypeProperty<?>> getKeyProperties()
    {
        return this.getDataTypeProperties().select(AntlrDataTypeProperty::isKey);
    }

    public ImmutableList<AntlrDataTypeProperty<?>> getOverriddenDataTypeProperties(String name)
    {
        MutableList<AntlrDataTypeProperty<?>> overriddenProperties = Lists.mutable.empty();
        MutableSet<AntlrClassifier>           visited              = Sets.mutable.empty();
        this.getOverriddenDataTypeProperties(name, overriddenProperties, visited);
        return overriddenProperties.toImmutable();
    }

    protected void getOverriddenDataTypeProperties(
            String name,
            MutableList<AntlrDataTypeProperty<?>> overriddenProperties,
            MutableSet<AntlrClassifier> visited)
    {
        if (visited.contains(this))
        {
            return;
        }
        visited.add(this);

        AntlrDataTypeProperty<?> antlrDataTypeProperty = this.dataTypePropertiesByName.get(name);
        if (antlrDataTypeProperty != null)
        {
            overriddenProperties.add(antlrDataTypeProperty);
        }

        this
                .getSuperClass()
                .ifPresent(antlrClass -> antlrClass.getOverriddenDataTypeProperties(name, overriddenProperties,
                        visited));

        for (AntlrInterface interfaceState : this.interfaceStates)
        {
            interfaceState.getOverriddenDataTypeProperties(name, overriddenProperties, visited);
        }
    }

    public Optional<AntlrClass> getSuperClass()
    {
        return Optional.empty();
    }
}
