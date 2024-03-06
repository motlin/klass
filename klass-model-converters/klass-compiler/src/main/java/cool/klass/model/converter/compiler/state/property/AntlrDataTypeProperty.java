package cool.klass.model.converter.compiler.state.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.property.validation.AbstractAntlrPropertyValidation;
import cool.klass.model.converter.compiler.state.property.validation.AntlrMaxLengthPropertyValidation;
import cool.klass.model.converter.compiler.state.property.validation.AntlrMaxPropertyValidation;
import cool.klass.model.converter.compiler.state.property.validation.AntlrMinLengthPropertyValidation;
import cool.klass.model.converter.compiler.state.property.validation.AntlrMinPropertyValidation;
import cool.klass.model.meta.domain.api.DataType;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty.DataTypePropertyBuilder;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import cool.klass.model.meta.domain.property.validation.MaxLengthPropertyValidationImpl.MaxLengthPropertyValidationBuilder;
import cool.klass.model.meta.domain.property.validation.MaxPropertyValidationImpl.MaxPropertyValidationBuilder;
import cool.klass.model.meta.domain.property.validation.MinLengthPropertyValidationImpl.MinLengthPropertyValidationBuilder;
import cool.klass.model.meta.domain.property.validation.MinPropertyValidationImpl.MinPropertyValidationBuilder;
import cool.klass.model.meta.grammar.KlassParser.ClassModifierContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.list.ListIterable;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.multimap.list.ImmutableListMultimap;
import org.eclipse.collections.api.multimap.list.MutableListMultimap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Multimaps;
import org.eclipse.collections.impl.utility.Iterate;

public abstract class AntlrDataTypeProperty<T extends DataType>
        extends AntlrProperty<T>
{
    protected final boolean isOptional;

    @Nonnull
    protected final AntlrClassifier owningClassifierState;

    protected final MutableList<AntlrMinLengthPropertyValidation> minLengthValidationStates = Lists.mutable.empty();
    protected final MutableList<AntlrMaxLengthPropertyValidation> maxLengthValidationStates = Lists.mutable.empty();
    protected final MutableList<AntlrMinPropertyValidation>       minValidationStates       = Lists.mutable.empty();
    protected final MutableList<AntlrMaxPropertyValidation>       maxValidationStates       = Lists.mutable.empty();

    private final MutableListMultimap<AntlrAssociationEnd, AntlrDataTypeProperty<?>> keyBuildersMatchingThisForeignKey = Multimaps.mutable.list.empty();
    private final MutableListMultimap<AntlrAssociationEnd, AntlrDataTypeProperty<?>> foreignKeyBuildersMatchingThisKey = Multimaps.mutable.list.empty();

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
        return this.getDataTypePropertyModifiers().anySatisfy(AntlrDataTypePropertyModifier::isKey);
    }

    public boolean isID()
    {
        return this.getDataTypePropertyModifiers().anySatisfy(AntlrDataTypePropertyModifier::isID);
    }

    public boolean isUserId()
    {
        return this.getDataTypePropertyModifiers().anySatisfy(AntlrDataTypePropertyModifier::isUserId);
    }

    public boolean isAudit()
    {
        return this.getDataTypePropertyModifiers().anySatisfy(AntlrDataTypePropertyModifier::isAudit);
    }

    public boolean isDerived()
    {
        return this.getDataTypePropertyModifiers().anySatisfy(AntlrDataTypePropertyModifier::isDerived);
    }

    public boolean isOptional()
    {
        return this.isOptional;
    }

    public abstract boolean isSystem();

    public abstract boolean isValid();

    public abstract boolean isTemporal();

    public void setKeyMatchingThisForeignKey(
            AntlrAssociationEnd associationEnd,
            AntlrDataTypeProperty<?> keyProperty)
    {
        this.keyBuildersMatchingThisForeignKey.put(associationEnd, keyProperty);
    }

    public void setForeignKeyMatchingThisKey(
            AntlrAssociationEnd associationEnd,
            AntlrDataTypeProperty<?> foreignKeyProperty)
    {
        this.foreignKeyBuildersMatchingThisKey.put(associationEnd, foreignKeyProperty);
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
        ImmutableListMultimap<AssociationEndBuilder, DataTypePropertyBuilder<?, ?, ?>> keysMatchingThisForeignKey =
                AntlrDataTypeProperty.collectKeyMultiValues(
                        this.keyBuildersMatchingThisForeignKey,
                        AntlrAssociationEnd::getElementBuilder,
                        AntlrDataTypeProperty::getElementBuilder);
        this.getElementBuilder().setKeyBuildersMatchingThisForeignKey(keysMatchingThisForeignKey);

        ImmutableListMultimap<AssociationEndBuilder, DataTypePropertyBuilder<?, ?, ?>> foreignKeysMatchingThisKey =
                AntlrDataTypeProperty.collectKeyMultiValues(
                        this.foreignKeyBuildersMatchingThisKey,
                        AntlrAssociationEnd::getElementBuilder,
                        AntlrDataTypeProperty::getElementBuilder);
        this.getElementBuilder().setForeignKeyBuildersMatchingThisKey(foreignKeysMatchingThisKey);
    }

    public static <InputKey, InputValue, OutputKey, OutputValue> ImmutableListMultimap<OutputKey, OutputValue> collectKeyMultiValues(
            @Nonnull MutableListMultimap<InputKey, InputValue> multimap,
            @Nonnull Function<? super InputKey, ? extends OutputKey> keyFunction,
            Function<? super InputValue, ? extends OutputValue> valueFunction)
    {
        MutableListMultimap<OutputKey, OutputValue> result = Multimaps.mutable.list.empty();
        multimap.forEachKeyMultiValues((key, multiValues) ->
                result.putAll(
                        keyFunction.valueOf(key),
                        Iterate.collect(multiValues, valueFunction)));
        return result.toImmutable();
    }

    @OverridingMethodsMustInvokeSuper
    @Override
    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        super.reportErrors(compilerErrorHolder);

        this.reportDuplicateValidations(compilerErrorHolder);
        this.reportInvalidIdProperties(compilerErrorHolder);

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

    @Nonnull
    @Override
    protected AntlrClassifier getOwningClassifierState()
    {
        return this.owningClassifierState;
    }

    @Override
    public void getParserRuleContexts(@Nonnull MutableList<ParserRuleContext> parserRuleContexts)
    {
        if (this.elementContext instanceof ClassModifierContext)
        {
            return;
        }
        this.owningClassifierState.getParserRuleContexts(parserRuleContexts);
    }

    private ListIterable<AntlrDataTypePropertyModifier> getDataTypePropertyModifiers()
    {
        // TODO: Consider generics instead of cast
        return this.getModifiers()
                .collect(AntlrDataTypePropertyModifier.class::cast);
    }

    public String getShortString()
    {
        return String.format(
                "%s: %s %s",
                this.getName(),
                this.getType(),
                this.getModifiers().collect(AntlrNamedElement::getName).makeString(" "));
    }

    @Override
    public String toString()
    {
        return String.format(
                "%s.%s: %s %s",
                this.getOwningClassifierState().getName(),
                this.getName(),
                this.getType(),
                this.getModifiers().collect(AntlrNamedElement::getName).makeString(" "));
    }
}
