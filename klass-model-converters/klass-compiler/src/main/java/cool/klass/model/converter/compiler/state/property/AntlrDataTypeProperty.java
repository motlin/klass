package cool.klass.model.converter.compiler.state.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.AntlrNamedElement;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.meta.domain.api.DataType;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty.DataTypePropertyBuilder;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import cool.klass.model.meta.grammar.KlassParser.ClassModifierContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.multimap.list.ImmutableListMultimap;
import org.eclipse.collections.api.multimap.list.MutableListMultimap;
import org.eclipse.collections.impl.factory.Multimaps;
import org.eclipse.collections.impl.utility.Iterate;

public abstract class AntlrDataTypeProperty<T extends DataType> extends AntlrProperty<T>
{
    protected final boolean                              isOptional;
    @Nonnull
    protected final ImmutableList<AntlrPropertyModifier> propertyModifierStates;
    @Nonnull
    protected final AntlrClassifier                      owningClassifierState;

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
            @Nonnull ImmutableList<AntlrPropertyModifier> propertyModifierStates,
            boolean isOptional)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, ordinal);
        this.isOptional = isOptional;
        this.propertyModifierStates = Objects.requireNonNull(propertyModifierStates);
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
        return this.propertyModifierStates;
    }

    public boolean isKey()
    {
        return this.propertyModifierStates.anySatisfy(AntlrPropertyModifier::isKey);
    }

    public boolean isID()
    {
        return this.propertyModifierStates.anySatisfy(AntlrPropertyModifier::isID);
    }

    public boolean isAudit()
    {
        return this.propertyModifierStates.anySatisfy(AntlrPropertyModifier::isAudit);
    }

    public boolean isOptional()
    {
        return this.isOptional;
    }

    public abstract boolean isSystem();

    public abstract boolean isValid();

    public abstract boolean isTemporal();

    @Nonnull
    @Override
    public abstract DataTypePropertyBuilder<T, ?, ?> build();

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
        // TODO: ☑ Check for duplicate modifiers
        // TODO: ☑ Check for nullable key properties
        // TODO: ☑ Check that ID properties are key properties
        // TODO: ☑ Only Integer and Long may be ID (no enums either)
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

    public String getShortString()
    {
        return String.format(
                "%s: %s %s",
                this.getName(),
                this.getType().toString(),
                this.propertyModifierStates.collect(AntlrNamedElement::getName).makeString(" "));
    }

    @Override
    public String toString()
    {
        return String.format(
                "%s.%s: %s %s",
                this.getOwningClassifierState().getName(),
                this.getName(),
                this.getType().toString(),
                this.propertyModifierStates.collect(AntlrNamedElement::getName).makeString(" "));
    }
}
