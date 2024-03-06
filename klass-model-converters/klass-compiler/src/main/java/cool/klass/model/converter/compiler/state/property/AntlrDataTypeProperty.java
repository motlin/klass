package cool.klass.model.converter.compiler.state.property;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.property.validation.AbstractAntlrPropertyValidation;
import cool.klass.model.converter.compiler.state.property.validation.AntlrMaxLengthPropertyValidation;
import cool.klass.model.converter.compiler.state.property.validation.AntlrMaxPropertyValidation;
import cool.klass.model.converter.compiler.state.property.validation.AntlrMinLengthPropertyValidation;
import cool.klass.model.converter.compiler.state.property.validation.AntlrMinPropertyValidation;
import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.api.DataType;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty.DataTypePropertyBuilder;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import cool.klass.model.meta.domain.property.validation.MaxLengthPropertyValidationImpl.MaxLengthPropertyValidationBuilder;
import cool.klass.model.meta.domain.property.validation.MaxPropertyValidationImpl.MaxPropertyValidationBuilder;
import cool.klass.model.meta.domain.property.validation.MinLengthPropertyValidationImpl.MinLengthPropertyValidationBuilder;
import cool.klass.model.meta.domain.property.validation.MinPropertyValidationImpl.MinPropertyValidationBuilder;
import cool.klass.model.meta.grammar.KlassParser.ClassifierModifierContext;
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
            new ParserRuleContext(null, -1),
            Optional.empty(),
            AbstractElement.NO_CONTEXT,
            "ambiguous data type property name",
            -1,
            AntlrClassifier.AMBIGUOUS,
            false)
    {
        @Override
        public boolean isSystem()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".isSystem() not implemented yet");
        }

        @Override
        public boolean isValid()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".isValid() not implemented yet");
        }

        @Override
        public boolean isTemporal()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".isTemporal() not implemented yet");
        }

        @Nonnull
        @Override
        public AntlrType getType()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".getType() not implemented yet");
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
        protected void reportInvalidIdProperties(@Nonnull CompilerErrorState compilerErrorHolder)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".reportInvalidIdProperties() not implemented yet");
        }
    };

    @Nonnull
    public static final AntlrDataTypeProperty NOT_FOUND = new AntlrDataTypeProperty(
            new ParserRuleContext(null, -1),
            Optional.empty(),
            AbstractElement.NO_CONTEXT,
            "not found data type property name",
            -1,
            AntlrClassifier.NOT_FOUND,
            false)
    {
        @Override
        public boolean isSystem()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".isSystem() not implemented yet");
        }

        @Override
        public boolean isValid()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".isValid() not implemented yet");
        }

        @Override
        public boolean isTemporal()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".isTemporal() not implemented yet");
        }

        @Nonnull
        @Override
        public AntlrType getType()
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".getType() not implemented yet");
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
        protected void reportInvalidIdProperties(@Nonnull CompilerErrorState compilerErrorHolder)
        {
            throw new UnsupportedOperationException(this.getClass().getSimpleName()
                    + ".reportInvalidIdProperties() not implemented yet");
        }
    };

    protected final boolean isOptional;

    @Nonnull
    protected final AntlrClassifier owningClassifierState;

    protected final MutableList<AntlrMinLengthPropertyValidation> minLengthValidationStates = Lists.mutable.empty();
    protected final MutableList<AntlrMaxLengthPropertyValidation> maxLengthValidationStates = Lists.mutable.empty();
    protected final MutableList<AntlrMinPropertyValidation>       minValidationStates       = Lists.mutable.empty();
    protected final MutableList<AntlrMaxPropertyValidation>       maxValidationStates       = Lists.mutable.empty();

    private final MutableOrderedMap<AntlrAssociationEnd, MutableList<AntlrDataTypeProperty<?>>> keyBuildersMatchingThisForeignKey = OrderedMapAdapter.adapt(new LinkedHashMap<>());
    private final MutableOrderedMap<AntlrAssociationEnd, MutableList<AntlrDataTypeProperty<?>>> foreignKeyBuildersMatchingThisKey = OrderedMapAdapter.adapt(new LinkedHashMap<>());

    protected AntlrDataTypeProperty(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AntlrClassifier owningClassifierState,
            boolean isOptional)
    {
        super(elementContext, compilationUnit, nameContext, name, ordinal);
        this.isOptional            = isOptional;
        this.owningClassifierState = Objects.requireNonNull(owningClassifierState);
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.owningClassifierState);
    }

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

    public boolean isAudit()
    {
        return this.getModifiers().anySatisfy(AntlrModifier::isAudit);
    }

    public boolean isDerived()
    {
        return this.getModifiers().anySatisfy(AntlrModifier::isDerived);
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

    public abstract boolean isTemporal();

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
        this.minLengthValidationStates.add(minLengthValidationState);
    }

    public void addMaxLengthValidationState(AntlrMaxLengthPropertyValidation maxLengthValidationState)
    {
        this.maxLengthValidationStates.add(maxLengthValidationState);
    }

    public void addMinValidationState(AntlrMinPropertyValidation minValidationState)
    {
        this.minValidationStates.add(minValidationState);
    }

    public void addMaxValidationState(AntlrMaxPropertyValidation maxValidationState)
    {
        this.maxValidationStates.add(maxValidationState);
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

        this.getElementBuilder().setMinLengthPropertyValidationBuilder(minLengthPropertyValidationBuilders);
        this.getElementBuilder().setMaxLengthPropertyValidationBuilder(maxLengthPropertyValidationBuilders);
        this.getElementBuilder().setMinPropertyValidationBuilder(minPropertyValidationBuilders);
        this.getElementBuilder().setMaxPropertyValidationBuilder(maxPropertyValidationBuilders);
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
        if (validationStates.size() > 1)
        {
            for (AbstractAntlrPropertyValidation minLengthValidationState : validationStates)
            {
                ParserRuleContext offendingToken = minLengthValidationState.getElementContext();
                String message = String.format(
                        "Duplicate validation '%s'.",
                        offendingToken.getText());
                compilerErrorHolder.add("ERR_DUP_VAL", message, this, offendingToken);
            }
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

        if (this.isOptional() != associationEnd.isToOneOptional())
        {
            String message  = String.format(
                    "Association end '%s.%s' has multiplicity [%s] but foreign key '%s.%s' is %srequired.",
                    associationEnd.getOwningClassifierState().getName(),
                    associationEnd.getName(),
                    associationEnd.getMultiplicity().getMultiplicity().getPrettyName(),
                    this.getOwningClassifierState(),
                    this.getName(),
                    this.isOptional ? "not " : "");
            compilerErrorHolder.add("ERR_FOR_MUL", message, this);
        }
    }

    @Nonnull
    @Override
    public AntlrClassifier getOwningClassifierState()
    {
        return this.owningClassifierState;
    }

    @Override
    public void getParserRuleContexts(@Nonnull MutableList<ParserRuleContext> parserRuleContexts)
    {
        if (this.elementContext instanceof ClassifierModifierContext)
        {
            return;
        }
        this.owningClassifierState.getParserRuleContexts(parserRuleContexts);
    }

    public String getShortString()
    {
        return String.format(
                "%s: %s %s",
                this.getName(),
                this.getType(),
                this.getModifiers().collect(AntlrNamedElement::getName).makeString(" "));
    }
}
