package cool.klass.model.converter.compiler.state.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

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
import org.eclipse.collections.api.bag.MutableBag;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.ListIterable;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.multimap.list.ImmutableListMultimap;
import org.eclipse.collections.api.multimap.list.MutableListMultimap;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Multimaps;
import org.eclipse.collections.impl.utility.Iterate;

public abstract class AntlrDataTypeProperty<T extends DataType> extends AntlrProperty<T>
{
    protected final boolean                              isOptional;
    @Nonnull
    protected final ImmutableList<AntlrPropertyModifier> modifierStates;
    @Nonnull
    protected final AntlrClassifier                      owningClassifierState;

    protected final MutableList<AntlrMinLengthPropertyValidation> minLengthValidationStates = Lists.mutable.empty();
    protected final MutableList<AntlrMaxLengthPropertyValidation> maxLengthValidationStates = Lists.mutable.empty();
    protected final MutableList<AntlrMinPropertyValidation>       minValidationStates       = Lists.mutable.empty();
    protected final MutableList<AntlrMaxPropertyValidation>       maxValidationStates       = Lists.mutable.empty();

    private final MutableListMultimap<AntlrAssociationEnd, AntlrDataTypeProperty<?>> keyBuildersMatchingThisForeignKey = Multimaps.mutable.list.empty();
    private final MutableListMultimap<AntlrAssociationEnd, AntlrDataTypeProperty<?>> foreignKeyBuildersMatchingThisKey = Multimaps.mutable.list.empty();

    protected AntlrDataTypeProperty(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            AntlrClassifier owningClassifierState,
            @Nonnull ImmutableList<AntlrPropertyModifier> modifierStates,
            boolean isOptional)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, ordinal);
        this.isOptional = isOptional;
        this.modifierStates = Objects.requireNonNull(modifierStates);
        this.owningClassifierState = Objects.requireNonNull(owningClassifierState);
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.owningClassifierState);
    }

    @Nonnull
    public ImmutableList<AntlrPropertyModifier> getPropertyModifiers()
    {
        return this.modifierStates;
    }

    public boolean isKey()
    {
        return this.modifierStates.anySatisfy(AntlrPropertyModifier::isKey);
    }

    public boolean isID()
    {
        return this.modifierStates.anySatisfy(AntlrPropertyModifier::isID);
    }

    public boolean isAudit()
    {
        return this.modifierStates.anySatisfy(AntlrPropertyModifier::isAudit);
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

        this.getPropertyBuilder().setMinLengthPropertyValidationBuilder(minLengthPropertyValidationBuilders);
        this.getPropertyBuilder().setMaxLengthPropertyValidationBuilder(maxLengthPropertyValidationBuilders);
        this.getPropertyBuilder().setMinPropertyValidationBuilder(minPropertyValidationBuilders);
        this.getPropertyBuilder().setMaxPropertyValidationBuilder(maxPropertyValidationBuilders);
    }

    public void build2()
    {
        ImmutableListMultimap<AssociationEndBuilder, DataTypePropertyBuilder<?, ?, ?>> keysMatchingThisForeignKey =
                AntlrDataTypeProperty.collectKeyMultiValues(
                        this.keyBuildersMatchingThisForeignKey,
                        AntlrAssociationEnd::getElementBuilder,
                        AntlrDataTypeProperty::getPropertyBuilder);
        this.getPropertyBuilder().setKeyBuildersMatchingThisForeignKey(keysMatchingThisForeignKey);

        ImmutableListMultimap<AssociationEndBuilder, DataTypePropertyBuilder<?, ?, ?>> foreignKeysMatchingThisKey =
                AntlrDataTypeProperty.collectKeyMultiValues(
                        this.foreignKeyBuildersMatchingThisKey,
                        AntlrAssociationEnd::getElementBuilder,
                        AntlrDataTypeProperty::getPropertyBuilder);
        this.getPropertyBuilder().setForeignKeyBuildersMatchingThisKey(foreignKeysMatchingThisKey);
    }

    public static <InputKey, InputValue, OutputKey, OutputValue> ImmutableListMultimap<OutputKey, OutputValue> collectKeyMultiValues(
            MutableListMultimap<InputKey, InputValue> multimap,
            Function<? super InputKey, ? extends OutputKey> keyFunction,
            Function<? super InputValue, ? extends OutputValue> valueFunction)
    {
        MutableListMultimap<OutputKey, OutputValue> result = Multimaps.mutable.list.empty();
        multimap.forEachKeyMultiValues((key, multiValues) ->
                result.putAll(
                        keyFunction.valueOf(key),
                        Iterate.collect(multiValues, valueFunction)));
        return result.toImmutable();
    }

    @Override
    public void reportErrors(CompilerErrorState compilerErrorHolder)
    {
        this.reportDuplicateModifiers(compilerErrorHolder);
        this.reportDuplicateValidations(compilerErrorHolder);

        // TODO: ☑ Check for nullable key properties
        // TODO: ☑ Check that ID properties are key properties
        // TODO: ☑ Only Integer and Long may be ID (no enums either)
    }

    private void reportDuplicateModifiers(CompilerErrorState compilerErrorHolder)
    {
        MutableBag<String> duplicateModifiers = this.modifierStates
                .asLazy()
                .collect(AntlrNamedElement::getName)
                .toBag()
                .selectDuplicates();

        for (AntlrPropertyModifier modifierState : this.modifierStates)
        {
            if (duplicateModifiers.contains(modifierState.getName()))
            {
                ParserRuleContext offendingToken = modifierState.getElementContext();
                String message = String.format(
                        "ERR_DUP_MOD: Duplicate modifier '%s'.",
                        offendingToken.getText());
                compilerErrorHolder.add(message, this, offendingToken);
            }
        }
    }

    private void reportDuplicateValidations(CompilerErrorState compilerErrorHolder)
    {
        this.reportDuplicateValidations(compilerErrorHolder, this.minLengthValidationStates);
        this.reportDuplicateValidations(compilerErrorHolder, this.maxLengthValidationStates);
        this.reportDuplicateValidations(compilerErrorHolder, this.minValidationStates);
        this.reportDuplicateValidations(compilerErrorHolder, this.maxValidationStates);
    }

    private void reportDuplicateValidations(
            CompilerErrorState compilerErrorHolder,
            ListIterable<? extends AbstractAntlrPropertyValidation> validationStates)
    {
        if (validationStates.size() > 1)
        {
            for (AbstractAntlrPropertyValidation minLengthValidationState : validationStates)
            {
                ParserRuleContext offendingToken = minLengthValidationState.getElementContext();
                String message = String.format(
                        "ERR_DUP_VAL: Duplicate validation '%s'.",
                        offendingToken.getText());
                compilerErrorHolder.add(message, this, offendingToken);
            }
        }
    }

    @Nonnull
    @Override
    protected AntlrClassifier getOwningClassifierState()
    {
        return this.owningClassifierState;
    }

    @Nonnull
    public abstract DataTypePropertyBuilder<T, ?, ?> getPropertyBuilder();

    @Override
    public void getParserRuleContexts(@Nonnull MutableList<ParserRuleContext> parserRuleContexts)
    {
        if (this.elementContext instanceof ClassModifierContext)
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
                this.getType().toString(),
                this.modifierStates.collect(AntlrNamedElement::getName).makeString(" "));
    }

    @Override
    public String toString()
    {
        return String.format(
                "%s.%s: %s %s",
                this.getOwningClassifierState().getName(),
                this.getName(),
                this.getType().toString(),
                this.modifierStates.collect(AntlrNamedElement::getName).makeString(" "));
    }
}
