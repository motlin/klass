package cool.klass.model.converter.compiler.state.property;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveType;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.property.validation.AbstractAntlrPropertyValidation;
import cool.klass.model.converter.compiler.state.property.validation.AntlrMaxLengthPropertyValidation;
import cool.klass.model.converter.compiler.state.property.validation.AntlrMaxPropertyValidation;
import cool.klass.model.converter.compiler.state.property.validation.AntlrMinLengthPropertyValidation;
import cool.klass.model.converter.compiler.state.property.validation.AntlrMinPropertyValidation;
import cool.klass.model.meta.domain.api.DataType;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty.DataTypePropertyBuilder;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import cool.klass.model.meta.domain.property.validation.AbstractPropertyValidation.PropertyValidationBuilder;
import cool.klass.model.meta.domain.property.validation.MaxLengthPropertyValidationImpl.MaxLengthPropertyValidationBuilder;
import cool.klass.model.meta.domain.property.validation.MaxPropertyValidationImpl.MaxPropertyValidationBuilder;
import cool.klass.model.meta.domain.property.validation.MinLengthPropertyValidationImpl.MinLengthPropertyValidationBuilder;
import cool.klass.model.meta.domain.property.validation.MinPropertyValidationImpl.MinPropertyValidationBuilder;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.ListIterable;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;
import org.eclipse.collections.impl.tuple.Tuples;

public abstract class AntlrDataTypeProperty<T extends DataType>
        extends AntlrProperty
{
    @Nonnull
    public static final AntlrDataTypeProperty AMBIGUOUS = new AntlrDataTypeProperty(
            new ClassDeclarationContext(null, -1),
            Optional.empty(),
            -1,
            new IdentifierContext(null, -1),
            AntlrClassifier.AMBIGUOUS,
            false)
    {
        @Override
        protected ParserRuleContext getTypeParserRuleContext()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".getTypeParserRuleContext() not implemented yet");
        }

        @Nonnull
        @Override
        public AntlrType getType()
        {
            return AntlrEnumeration.AMBIGUOUS;
        }

        @Nonnull
        @Override
        public DataTypePropertyBuilder build()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".build() not implemented yet");
        }

        @Nonnull
        @Override
        public DataTypePropertyBuilder getElementBuilder()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".getElementBuilder() not implemented yet");
        }

        @Override
        public String getTypeName()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".getTypeName() not implemented yet");
        }

        @Override
        protected void reportInvalidIdProperties(@Nonnull CompilerErrorState compilerErrorHolder)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".reportInvalidIdProperties() not implemented yet");
        }
    };

    @Nonnull
    public static final AntlrDataTypeProperty NOT_FOUND = new AntlrDataTypeProperty(
            new ClassDeclarationContext(null, -1),
            Optional.empty(),
            -1,
            new IdentifierContext(null, -1),
            AntlrClassifier.NOT_FOUND,
            false)
    {
        @Override
        protected ParserRuleContext getTypeParserRuleContext()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".getTypeParserRuleContext() not implemented yet");
        }

        @Nonnull
        @Override
        public AntlrType getType()
        {
            return AntlrEnumeration.NOT_FOUND;
        }

        @Nonnull
        @Override
        public DataTypePropertyBuilder build()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".build() not implemented yet");
        }

        @Nonnull
        @Override
        public DataTypePropertyBuilder getElementBuilder()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".getElementBuilder() not implemented yet");
        }

        @Override
        public String getTypeName()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".getTypeName() not implemented yet");
        }

        @Override
        protected void reportInvalidIdProperties(@Nonnull CompilerErrorState compilerErrorHolder)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".reportInvalidIdProperties() not implemented yet");
        }
    };

    private static final ImmutableList<PrimitiveType> ALLOWED_VERSION_TYPES =
            Lists.immutable.with(
                    PrimitiveType.INTEGER,
                    PrimitiveType.LONG);

    protected final boolean isOptional;

    @Nonnull
    protected final AntlrClassifier owningClassifierState;

    protected final MutableList<AbstractAntlrPropertyValidation>  validationStates          = Lists.mutable.empty();
    protected final MutableList<AntlrMinLengthPropertyValidation> minLengthValidationStates = Lists.mutable.empty();
    protected final MutableList<AntlrMaxLengthPropertyValidation> maxLengthValidationStates = Lists.mutable.empty();
    protected final MutableList<AntlrMinPropertyValidation>       minValidationStates       = Lists.mutable.empty();
    protected final MutableList<AntlrMaxPropertyValidation>       maxValidationStates       = Lists.mutable.empty();

    private final MutableOrderedMap<AntlrAssociationEnd, MutableList<AntlrDataTypeProperty<?>>> keyBuildersMatchingThisForeignKey = OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<AntlrAssociationEnd, MutableList<AntlrDataTypeProperty<?>>> foreignKeyBuildersMatchingThisKey = OrderedMapAdapter.adapt(new LinkedHashMap<>());

    protected AntlrDataTypeProperty(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            int ordinal,
            @Nonnull IdentifierContext nameContext,
            @Nonnull AntlrClassifier owningClassifierState,
            boolean isOptional)
    {
        super(elementContext, compilationUnit, ordinal, nameContext);
        this.isOptional            = isOptional;
        this.owningClassifierState = Objects.requireNonNull(owningClassifierState);
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.owningClassifierState);
    }

    protected abstract ParserRuleContext getTypeParserRuleContext();

    public boolean isKey()
    {
        return this.getModifiers().anySatisfy(AntlrModifier::isKey);
    }

    public boolean isId()
    {
        return this.getModifiers().anySatisfy(AntlrModifier::isId);
    }

    public boolean isUserId()
    {
        return this.getModifiers().anySatisfy(AntlrModifier::isUserId);
    }

    public boolean isVersion()
    {
        return this.getModifiers().anySatisfy(AntlrModifier::isVersion);
    }

    public boolean isAudit()
    {
        return this.getModifiers().anySatisfy(AntlrModifier::isAudit);
    }

    public boolean isCreatedOn()
    {
        return this.getModifiers().anySatisfy(AntlrModifier::isCreatedOn);
    }

    public boolean isPrivate()
    {
        return this.getModifiers().anySatisfy(AntlrModifier::isPrivate);
    }

    public boolean isOptional()
    {
        return this.isOptional;
    }

    public boolean isSystem()
    {
        return this.getModifiers().anySatisfy(AntlrModifier::isSystem);
    }

    public boolean isValid()
    {
        return this.getModifiers().anySatisfy(AntlrModifier::isValid);
    }

    public boolean isValidRange()
    {
        return this.isValid() && !this.isFrom() && !this.isTo();
    }

    public boolean isValidFrom()
    {
        return this.isValid() && this.isFrom();
    }

    public boolean isValidTo()
    {
        return this.isValid() && this.isTo();
    }

    public boolean isSystemRange()
    {
        return this.isSystem() && !this.isFrom() && !this.isTo();
    }

    public boolean isSystemFrom()
    {
        return this.isSystem() && this.isFrom();
    }

    public boolean isSystemTo()
    {
        return this.isSystem() && this.isTo();
    }

    public boolean isFrom()
    {
        return this.getModifiers().anySatisfy(AntlrModifier::isFrom);
    }

    public boolean isTo()
    {
        return this.getModifiers().anySatisfy(AntlrModifier::isTo);
    }

    public void setKeyMatchingThisForeignKey(
            AntlrAssociationEnd associationEnd,
            AntlrDataTypeProperty<?> keyProperty)
    {
        this.keyBuildersMatchingThisForeignKey
                .computeIfAbsent(associationEnd, k -> Lists.mutable.empty())
                .add(keyProperty);
    }

    public void setForeignKeyMatchingThisKey(
            AntlrAssociationEnd associationEnd,
            AntlrDataTypeProperty<?> foreignKeyProperty)
    {
        this.foreignKeyBuildersMatchingThisKey
                .computeIfAbsent(associationEnd, k -> Lists.mutable.empty())
                .add(foreignKeyProperty);
    }

    public void addMinLengthValidationState(AntlrMinLengthPropertyValidation minLengthValidationState)
    {
        this.validationStates.add(minLengthValidationState);
        this.minLengthValidationStates.add(minLengthValidationState);
    }

    public void addMaxLengthValidationState(AntlrMaxLengthPropertyValidation maxLengthValidationState)
    {
        this.validationStates.add(maxLengthValidationState);
        this.maxLengthValidationStates.add(maxLengthValidationState);
    }

    public void addMinValidationState(AntlrMinPropertyValidation minValidationState)
    {
        this.validationStates.add(minValidationState);
        this.minValidationStates.add(minValidationState);
    }

    public void addMaxValidationState(AntlrMaxPropertyValidation maxValidationState)
    {
        this.validationStates.add(maxValidationState);
        this.maxValidationStates.add(maxValidationState);
    }

    public ListIterable<AbstractAntlrPropertyValidation> getValidationStates()
    {
        return this.validationStates;
    }

    public ListIterable<AntlrMinLengthPropertyValidation> getMinLengthValidationStates()
    {
        return this.minLengthValidationStates;
    }

    public ListIterable<AntlrMaxLengthPropertyValidation> getMaxLengthValidationStates()
    {
        return this.maxLengthValidationStates;
    }

    public ListIterable<AntlrMinPropertyValidation> getMinValidationStates()
    {
        return this.minValidationStates;
    }

    public ListIterable<AntlrMaxPropertyValidation> getMaxValidationStates()
    {
        return this.maxValidationStates;
    }

    @Nonnull
    @Override
    public abstract DataTypePropertyBuilder<T, ?, ?> build();

    @Nonnull
    @Override
    public abstract DataTypePropertyBuilder<T, ?, ?> getElementBuilder();

    protected void buildValidations()
    {
        Optional<MinLengthPropertyValidationBuilder> minLengthPropertyValidationBuilders = this.minLengthValidationStates
                .collect(AntlrMinLengthPropertyValidation::build)
                .detectOptional(x -> true);
        Optional<MaxLengthPropertyValidationBuilder> maxLengthPropertyValidationBuilders = this.maxLengthValidationStates
                .collect(AntlrMaxLengthPropertyValidation::build)
                .detectOptional(x -> true);
        Optional<MinPropertyValidationBuilder> minPropertyValidationBuilders = this.minValidationStates
                .collect(AntlrMinPropertyValidation::build)
                .detectOptional(x -> true);
        Optional<MaxPropertyValidationBuilder> maxPropertyValidationBuilders = this.maxValidationStates
                .collect(AntlrMaxPropertyValidation::build)
                .detectOptional(x -> true);

        MutableList<PropertyValidationBuilder<?>> propertyValidationBuilders = this.validationStates.collect(
                AbstractAntlrPropertyValidation::getElementBuilder);

        this.getElementBuilder().setMinLengthPropertyValidationBuilder(minLengthPropertyValidationBuilders);
        this.getElementBuilder().setMaxLengthPropertyValidationBuilder(maxLengthPropertyValidationBuilders);
        this.getElementBuilder().setMinPropertyValidationBuilder(minPropertyValidationBuilders);
        this.getElementBuilder().setMaxPropertyValidationBuilder(maxPropertyValidationBuilders);
        this.getElementBuilder().setPropertyValidationBuilders(propertyValidationBuilders.toImmutable());
    }

    public void build2()
    {
        MutableOrderedMap<AssociationEndBuilder, ImmutableList<DataTypePropertyBuilder<?, ?, ?>>> keysMatchingThisForeignKey =
                this.keyBuildersMatchingThisForeignKey.collect((associationEnd, dataTypeProperties) -> Tuples.pair(
                        associationEnd.getElementBuilder(),
                        dataTypeProperties.<DataTypePropertyBuilder<?, ?, ?>>collect(AntlrDataTypeProperty::getElementBuilder).toImmutable()));

        this.getElementBuilder().setKeyBuildersMatchingThisForeignKey(keysMatchingThisForeignKey.asUnmodifiable());

        MutableOrderedMap<AssociationEndBuilder, ImmutableList<DataTypePropertyBuilder<?, ?, ?>>> foreignKeysMatchingThisKey =
                this.foreignKeyBuildersMatchingThisKey.collect((associationEnd, dataTypeProperties) -> Tuples.pair(
                        associationEnd.getElementBuilder(),
                        dataTypeProperties.<DataTypePropertyBuilder<?, ?, ?>>collect(AntlrDataTypeProperty::getElementBuilder).toImmutable()));

        this.getElementBuilder().setForeignKeyBuildersMatchingThisKey(foreignKeysMatchingThisKey.asUnmodifiable());
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        super.reportErrors(compilerErrorHolder);

        this.reportDuplicateValidations(compilerErrorHolder);
        this.reportInvalidIdProperties(compilerErrorHolder);
        this.reportInvalidForeignKeyProperties(compilerErrorHolder);
        this.reportInvalidUserIdProperties(compilerErrorHolder);
        this.reportInvalidVersionProperties(compilerErrorHolder);
        this.reportInvalidTemporalProperties(compilerErrorHolder);
        this.reportInvalidAuditProperties(compilerErrorHolder);

        // TODO: ☑ Check for nullable key properties
    }

    private void reportDuplicateValidations(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        this.reportDuplicateValidations(compilerErrorHolder, this.minLengthValidationStates);
        this.reportDuplicateValidations(compilerErrorHolder, this.maxLengthValidationStates);
        this.reportDuplicateValidations(compilerErrorHolder, this.minValidationStates);
        this.reportDuplicateValidations(compilerErrorHolder, this.maxValidationStates);
    }

    private void reportDuplicateValidations(
            @Nonnull CompilerErrorState compilerErrorHolder,
            @Nonnull ListIterable<? extends AbstractAntlrPropertyValidation> validationStates)
    {
        if (validationStates.size() <= 1)
        {
            return;
        }

        for (AbstractAntlrPropertyValidation minLengthValidation : validationStates)
        {
            ParserRuleContext offendingToken = minLengthValidation.getElementContext();
            String message = String.format(
                    "Duplicate validation '%s'.",
                    offendingToken.getText());
            compilerErrorHolder.add("ERR_DUP_VAL", message, minLengthValidation, minLengthValidation.getKeywordToken());
        }
    }

    protected abstract void reportInvalidIdProperties(@Nonnull CompilerErrorState compilerErrorHolder);

    private void reportInvalidForeignKeyProperties(CompilerErrorState compilerErrorHolder)
    {
        this.keyBuildersMatchingThisForeignKey.forEach((associationEnd, keyBuilders) -> this.reportInvalidForeignKeyProperties(
                compilerErrorHolder,
                associationEnd,
                keyBuilders));
    }

    private void reportInvalidForeignKeyProperties(
            CompilerErrorState compilerErrorHolder,
            AntlrAssociationEnd associationEnd,
            ListIterable<AntlrDataTypeProperty<?>> keyBuilders)
    {
        if (keyBuilders.size() > 1)
        {
            throw new AssertionError("TODO: Is it sometimes valid to have a single foreign key relate to many different primary keys on different types?");
        }

        if (!associationEnd.isToOne())
        {
            throw new AssertionError(associationEnd);
        }

        if (this.isOptional() && associationEnd.isToOneRequired())
        {
            String message = String.format(
                    "Association end '%s.%s' has multiplicity [%s] so foreign key '%s.%s' ought to be required.",
                    associationEnd.getOwningClassifierState().getName(),
                    associationEnd.getName(),
                    associationEnd.getMultiplicity().getMultiplicity().getPrettyName(),
                    this.getOwningClassifierState().getName(),
                    this.getName());
            compilerErrorHolder.add("ERR_FOR_MUL", message, this, this.getTypeParserRuleContext());
            compilerErrorHolder.add("ERR_FOR_MUL", message, associationEnd.getMultiplicity());
        }

        if (!this.isOptional() && associationEnd.isToOneOptional())
        {
            // TODO: Possibly warn here. However, testing has showed legit examples where this warning fires, where the foreign key data includes references that sometimes cannot be resolved from the set of keys that we currently source.
            String message = String.format(
                    "Association end '%s.%s' has multiplicity [%s] but foreign key '%s.%s' is %srequired.",
                    associationEnd.getOwningClassifierState().getName(),
                    associationEnd.getName(),
                    associationEnd.getMultiplicity().getMultiplicity().getPrettyName(),
                    this.getOwningClassifierState().getName(),
                    this.getName(),
                    this.isOptional ? "not " : "");
            // compilerErrorHolder.add("ERR_FOR_MUL", message, this, this.getTypeParserRuleContext());
            // compilerErrorHolder.add("ERR_FOR_MUL", message, associationEnd.getMultiplicity());
        }
    }

    private void reportInvalidUserIdProperties(CompilerErrorState compilerErrorHolder)
    {
        if (!this.isUserId() || this.isCreatedBy() || this.isLastUpdatedBy())
        {
            return;
        }

        AntlrType antlrType = this.getType();
        if (antlrType instanceof AntlrPrimitiveType
                && ((AntlrPrimitiveType) antlrType).getPrimitiveType().equals(PrimitiveType.STRING))
        {
            return;
        }

        AntlrModifier modifier = this.getModifiers().detect(AntlrModifier::isUserId);
        String message = String.format(
                "Expected type '%s' but was '%s' for '%s' property '%s'.",
                PrimitiveType.STRING,
                antlrType.getName(),
                modifier.getKeyword(),
                this);
        compilerErrorHolder.add(
                "ERR_USR_DTP",
                message,
                this,
                Lists.immutable.with(modifier.getElementContext(), this.getTypeParserRuleContext()));
    }

    private void reportInvalidVersionProperties(CompilerErrorState compilerErrorHolder)
    {
        if (this.getModifiers().noneSatisfy(AntlrModifier::isVersion))
        {
            return;
        }

        AntlrType antlrType = this.getType();
        if (antlrType instanceof AntlrPrimitiveType
                && ALLOWED_VERSION_TYPES.contains(((AntlrPrimitiveType) antlrType).getPrimitiveType()))
        {
            return;
        }

        ParserRuleContext offendingToken = this.getTypeParserRuleContext();
        AntlrModifier     modifier       = this.getModifiers().detect(AntlrModifier::isVersion);
        String message = String.format(
                "Expected types %s but was '%s' for '%s' property '%s'.",
                ALLOWED_VERSION_TYPES,
                antlrType.getName(),
                modifier.getKeyword(),
                this);
        compilerErrorHolder.add(
                "ERR_VER_DTP",
                message,
                modifier,
                Lists.immutable.with(offendingToken, modifier.getElementContext()));
    }

    private void reportInvalidTemporalProperties(CompilerErrorState compilerErrorHolder)
    {
        if (this.isValidRange() || this.isSystemRange())
        {
            if (this.getType() != AntlrPrimitiveType.TEMPORAL_RANGE)
            {
                ParserRuleContext offendingToken = this.getTypeParserRuleContext();
                String message = String.format(
                        "Expected type '%s' for temporal property but found '%s'.",
                        AntlrPrimitiveType.TEMPORAL_RANGE,
                        offendingToken.getText());
                ListIterable<AntlrModifier> modifiers = this
                        .getModifiers()
                        .select(antlrModifier -> antlrModifier.isSystem() || antlrModifier.isVersion());
                ListIterable<ParserRuleContext> modifierContexts = modifiers
                        .collect(AntlrElement::getElementContext);
                compilerErrorHolder.add(
                        "ERR_TMP_RNG",
                        message,
                        this,
                        Lists.immutable
                                .with(offendingToken)
                                .newWithAll(modifierContexts));
            }
        }
        else if (this.isFrom() || this.isTo())
        {
            if (!this.isValid() && !this.isSystem())
            {
                ParserRuleContext offendingToken = this.getTypeParserRuleContext();
                String message = String.format(
                        "Expected type '%s' for temporal property but found '%s'.",
                        AntlrPrimitiveType.TEMPORAL_RANGE,
                        offendingToken.getText());
                ListIterable<AntlrModifier> modifiers = this
                        .getModifiers()
                        .select(antlrModifier -> antlrModifier.isSystem() || antlrModifier.isVersion());
                ListIterable<ParserRuleContext> modifierContexts = modifiers
                        .collect(AntlrElement::getElementContext);
                compilerErrorHolder.add(
                        "ERR_TMP_RNG",
                        message,
                        this,
                        Lists.immutable
                                .with(offendingToken)
                                .newWithAll(modifierContexts));
            }
            else if (this.getType() != AntlrPrimitiveType.TEMPORAL_INSTANT)
            {
                ParserRuleContext offendingToken = this.getTypeParserRuleContext();
                String message = String.format(
                        "Expected type '%s' for temporal property but found '%s'.",
                        AntlrPrimitiveType.TEMPORAL_INSTANT,
                        offendingToken.getText());
                ListIterable<AntlrModifier> modifiers = this
                        .getModifiers()
                        .select(modifier -> modifier.isSystem() || modifier.isVersion() || modifier.isFrom() || modifier.isTo());
                ListIterable<ParserRuleContext> modifierContexts = modifiers
                        .collect(AntlrElement::getElementContext);
                compilerErrorHolder.add(
                        "ERR_TMP_INS",
                        message,
                        this,
                        Lists.immutable
                                .with(offendingToken)
                                .newWithAll(modifierContexts));
            }
            else if (this.isFrom() && this.isTo())
            {
                ImmutableList<AntlrModifier> modifiers = this
                        .getModifiers()
                        .select(modifier -> modifier.isFrom() || modifier.isTo())
                        .toImmutable();
                ImmutableList<ParserRuleContext> modifierContexts = modifiers
                        .collect(AntlrElement::getElementContext);
                String message = "Property may not have both 'from' and to' modifiers.";
                compilerErrorHolder.add(
                        "ERR_TMP_FTO",
                        message,
                        this,
                        modifierContexts);
            }
        }
    }

    private void reportInvalidAuditProperties(CompilerErrorState compilerErrorHolder)
    {
        if (this.isCreatedBy() || this.isLastUpdatedBy())
        {
            AntlrType antlrType = this.getType();
            if (!(antlrType instanceof AntlrPrimitiveType)
                    || ((AntlrPrimitiveType) antlrType).getPrimitiveType() != PrimitiveType.STRING)
            {
                AntlrModifier modifier = this
                        .getModifiers()
                        .detect(antlrModifier -> antlrModifier.isCreatedBy() || antlrModifier.isLastUpdatedBy());
                String message = String.format(
                        "Expected type '%s' but was '%s' for '%s' property '%s'.",
                        PrimitiveType.STRING,
                        antlrType.getName(),
                        modifier.getKeyword(),
                        this);
                compilerErrorHolder.add(
                        "ERR_AUD_DTP",
                        message,
                        this,
                        Lists.immutable.with(modifier.getElementContext(), this.getTypeParserRuleContext()));
            }
        }

        if (this.isCreatedOn())
        {
            AntlrType antlrType = this.getType();
            if (!(antlrType instanceof AntlrPrimitiveType)
                    || ((AntlrPrimitiveType) antlrType).getPrimitiveType() != PrimitiveType.INSTANT)
            {
                AntlrModifier modifier = this
                        .getModifiers()
                        .detect(AntlrModifier::isCreatedOn);
                String message = String.format(
                        "Expected type '%s' but was '%s' for '%s' property '%s'.",
                        PrimitiveType.INSTANT,
                        antlrType.getName(),
                        modifier.getKeyword(),
                        this);
                compilerErrorHolder.add(
                        "ERR_AUD_DTP",
                        message,
                        this,
                        Lists.immutable.with(modifier.getElementContext(), this.getTypeParserRuleContext()));
            }
        }
    }

    @Nonnull
    @Override
    public AntlrClassifier getOwningClassifierState()
    {
        return this.owningClassifierState;
    }

    public void reportIdPropertyWithKeyProperties(CompilerErrorState compilerErrorHolder)
    {
        String message = String.format(
                "Class '%s' may have id properties or non-id key properties, but not both. Found id property: %s.",
                this.getOwningClassifierState().getName(),
                this);
        compilerErrorHolder.add("ERR_KEY_IDS", message, this);
    }

    public void reportKeyPropertyWithIdProperties(CompilerErrorState compilerErrorHolder)
    {
        String message = String.format(
                "Class '%s' may have id properties or non-id key properties, but not both. Found non-id key property: %s.",
                this.getOwningClassifierState().getName(),
                this);
        compilerErrorHolder.add("ERR_KEY_IDS", message, this);
    }

    public void reportTransientIdProperties(CompilerErrorState compilerErrorHolder)
    {
        ImmutableList<AntlrModifier> idModifiers = this.getModifiersByName("id");
        if (idModifiers.isEmpty())
        {
            return;
        }

        String message = String.format(
                "Transient class '%s' may not have id properties.",
                this.getOwningClassifierState().getName());
        compilerErrorHolder.add("ERR_TNS_IDP", message, this, idModifiers.collect(AntlrElement::getElementContext));
    }

    @Override
    public String toString()
    {
        return String.format(
                "%s.%s",
                this.owningClassifierState.getName(),
                this.getShortString());
    }

    @Override
    public String getShortString()
    {
        MutableList<String> sourceCodeStrings = org.eclipse.collections.api.factory.Lists.mutable.empty();

        String typeSourceCode = this.getType().getName();
        sourceCodeStrings.add(typeSourceCode);

        this
                .getModifiers()
                .asLazy()
                .collect(AntlrElement::toString)
                .into(sourceCodeStrings);

        this
                .getValidationStates()
                .asLazy()
                .collect(AntlrElement::toString)
                .into(sourceCodeStrings);

        return String.format(
                "%s: %s",
                this.getName(),
                sourceCodeStrings.makeString(" "));
    }
}
