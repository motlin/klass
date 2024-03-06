package cool.klass.model.meta.domain.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.AbstractClassifier;
import cool.klass.model.meta.domain.AbstractClassifier.ClassifierBuilder;
import cool.klass.model.meta.domain.api.DataType;
import cool.klass.model.meta.domain.api.DataType.DataTypeGetter;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.modifier.Modifier;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.property.validation.MaxLengthPropertyValidation;
import cool.klass.model.meta.domain.api.property.validation.MaxPropertyValidation;
import cool.klass.model.meta.domain.api.property.validation.MinLengthPropertyValidation;
import cool.klass.model.meta.domain.api.property.validation.MinPropertyValidation;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.api.source.property.DataTypePropertyWithSourceCode;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import cool.klass.model.meta.domain.property.ModifierImpl.ModifierBuilder;
import cool.klass.model.meta.domain.property.validation.MaxLengthPropertyValidationImpl.MaxLengthPropertyValidationBuilder;
import cool.klass.model.meta.domain.property.validation.MaxPropertyValidationImpl.MaxPropertyValidationBuilder;
import cool.klass.model.meta.domain.property.validation.MinLengthPropertyValidationImpl.MinLengthPropertyValidationBuilder;
import cool.klass.model.meta.domain.property.validation.MinPropertyValidationImpl.MinPropertyValidationBuilder;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.api.map.OrderedMap;
import org.eclipse.collections.api.multimap.list.ImmutableListMultimap;
import org.eclipse.collections.api.multimap.list.ListMultimap;
import org.eclipse.collections.api.multimap.list.MutableListMultimap;
import org.eclipse.collections.impl.factory.Multimaps;
import org.eclipse.collections.impl.tuple.Tuples;
import org.eclipse.collections.impl.utility.Iterate;

// TODO: The generic type here is inconvenient. Replace it with a bunch of overrides of the getType method
public abstract class AbstractDataTypeProperty<T extends DataType>
        extends AbstractProperty<T>
        implements DataTypePropertyWithSourceCode
{
    private final boolean optional;

    private ImmutableList<Modifier> modifiers;

    private OrderedMap<AssociationEnd, ImmutableList<DataTypeProperty>> keysMatchingThisForeignKey;
    private OrderedMap<AssociationEnd, ImmutableList<DataTypeProperty>> foreignKeysMatchingThisKey;

    @Nonnull
    private Optional<MinLengthPropertyValidation> minLengthPropertyValidation = Optional.empty();
    @Nonnull
    private Optional<MaxLengthPropertyValidation> maxLengthPropertyValidation = Optional.empty();
    @Nonnull
    private Optional<MinPropertyValidation>       minPropertyValidation       = Optional.empty();
    @Nonnull
    private Optional<MaxPropertyValidation>       maxPropertyValidation       = Optional.empty();

    protected AbstractDataTypeProperty(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            int ordinal,
            @Nonnull IdentifierContext nameContext,
            @Nonnull T dataType,
            @Nonnull AbstractClassifier owningClassifier,
            boolean isOptional)
    {
        super(elementContext, macroElement, sourceCode, ordinal, nameContext, dataType, owningClassifier);
        this.optional = isOptional;
    }

    @Override
    @Nonnull
    public ImmutableList<Modifier> getModifiers()
    {
        return Objects.requireNonNull(this.modifiers);
    }

    private void setModifiers(ImmutableList<Modifier> modifiers)
    {
        if (this.modifiers != null)
        {
            throw new IllegalStateException();
        }
        this.modifiers = Objects.requireNonNull(modifiers);
    }

    @Override
    public Optional<MinLengthPropertyValidation> getMinLengthPropertyValidation()
    {
        return Objects.requireNonNull(this.minLengthPropertyValidation);
    }

    @Override
    public Optional<MaxLengthPropertyValidation> getMaxLengthPropertyValidation()
    {
        return Objects.requireNonNull(this.maxLengthPropertyValidation);
    }

    @Override
    public Optional<MinPropertyValidation> getMinPropertyValidation()
    {
        return Objects.requireNonNull(this.minPropertyValidation);
    }

    @Override
    public Optional<MaxPropertyValidation> getMaxPropertyValidation()
    {
        return Objects.requireNonNull(this.maxPropertyValidation);
    }

    private void setMinLengthPropertyValidation(Optional<MinLengthPropertyValidation> minLengthPropertyValidations)
    {
        if (this.minLengthPropertyValidation.isPresent())
        {
            throw new IllegalStateException();
        }
        this.minLengthPropertyValidation = Objects.requireNonNull(minLengthPropertyValidations);
    }

    private void setMaxLengthPropertyValidation(Optional<MaxLengthPropertyValidation> maxLengthPropertyValidations)
    {
        if (this.maxLengthPropertyValidation.isPresent())
        {
            throw new IllegalStateException();
        }
        this.maxLengthPropertyValidation = Objects.requireNonNull(maxLengthPropertyValidations);
    }

    private void setMinPropertyValidation(Optional<MinPropertyValidation> minPropertyValidations)
    {
        if (this.minPropertyValidation.isPresent())
        {
            throw new IllegalStateException();
        }
        this.minPropertyValidation = Objects.requireNonNull(minPropertyValidations);
    }

    private void setMaxPropertyValidation(Optional<MaxPropertyValidation> maxPropertyValidations)
    {
        if (this.maxPropertyValidation.isPresent())
        {
            throw new IllegalStateException();
        }
        this.maxPropertyValidation = Objects.requireNonNull(maxPropertyValidations);
    }

    @Override
    public boolean isOptional()
    {
        return this.optional;
    }

    @Override
    public boolean isForeignKey()
    {
        return this.keysMatchingThisForeignKey.notEmpty();
    }

    @Override
    public OrderedMap<AssociationEnd, ImmutableList<DataTypeProperty>> getKeysMatchingThisForeignKey()
    {
        return Objects.requireNonNull(this.keysMatchingThisForeignKey);
    }

    private void setKeysMatchingThisForeignKey(OrderedMap<AssociationEnd, ImmutableList<DataTypeProperty>> keysMatchingThisForeignKey)
    {
        if (this.keysMatchingThisForeignKey != null)
        {
            throw new IllegalStateException();
        }
        this.keysMatchingThisForeignKey = Objects.requireNonNull(keysMatchingThisForeignKey);
    }

    @Override
    public OrderedMap<AssociationEnd, ImmutableList<DataTypeProperty>> getForeignKeysMatchingThisKey()
    {
        return Objects.requireNonNull(this.foreignKeysMatchingThisKey);
    }

    private void setForeignKeysMatchingThisKey(OrderedMap<AssociationEnd, ImmutableList<DataTypeProperty>> foreignKeysMatchingThisKey)
    {
        if (this.foreignKeysMatchingThisKey != null)
        {
            throw new IllegalStateException();
        }
        this.foreignKeysMatchingThisKey = Objects.requireNonNull(foreignKeysMatchingThisKey);
    }

    @Override
    public String toString()
    {
        String isOptionalString = this.optional ? "?" : "";
        String propertyModifiersString = this.getModifiers().isEmpty()
                ? ""
                : this.getModifiers().collect(Modifier::getKeyword).makeString(" ", " ", "");
        return String.format(
                "%s: %s%s%s",
                this.getName(),
                this.getType(),
                isOptionalString,
                propertyModifiersString);
    }

    public abstract static class DataTypePropertyBuilder<T extends DataType, TG extends DataTypeGetter, BuiltElement extends AbstractDataTypeProperty<T>>
            extends PropertyBuilder<T, TG, BuiltElement>
    {
        protected final boolean isOptional;

        protected MutableOrderedMap<AssociationEndBuilder, ImmutableList<DataTypePropertyBuilder<?, ?, ?>>> keyBuildersMatchingThisForeignKey;
        protected MutableOrderedMap<AssociationEndBuilder, ImmutableList<DataTypePropertyBuilder<?, ?, ?>>> foreignKeyBuildersMatchingThisKey;

        protected ImmutableList<ModifierBuilder> modifierBuilders;

        private Optional<MinLengthPropertyValidationBuilder> minLengthPropertyValidationBuilder;
        private Optional<MaxLengthPropertyValidationBuilder> maxLengthPropertyValidationBuilder;
        private Optional<MinPropertyValidationBuilder>       minPropertyValidationBuilder;
        private Optional<MaxPropertyValidationBuilder>       maxPropertyValidationBuilder;

        protected DataTypePropertyBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                int ordinal,
                @Nonnull IdentifierContext nameContext,
                @Nonnull TG typeBuilder,
                @Nonnull ClassifierBuilder<?> owningClassifierBuilder,
                boolean isOptional)
        {
            super(
                    elementContext,
                    macroElement,
                    sourceCode,
                    ordinal,
                    nameContext,
                    typeBuilder,
                    owningClassifierBuilder);
            this.isOptional = isOptional;
        }

        public void setKeyBuildersMatchingThisForeignKey(MutableOrderedMap<AssociationEndBuilder, ImmutableList<DataTypePropertyBuilder<?, ?, ?>>> keyBuildersMatchingThisForeignKey)
        {
            if (this.keyBuildersMatchingThisForeignKey != null)
            {
                throw new IllegalStateException();
            }
            this.keyBuildersMatchingThisForeignKey = Objects.requireNonNull(keyBuildersMatchingThisForeignKey);
        }

        public void setForeignKeyBuildersMatchingThisKey(MutableOrderedMap<AssociationEndBuilder, ImmutableList<DataTypePropertyBuilder<?, ?, ?>>> foreignKeyBuildersMatchingThisKey)
        {
            if (this.foreignKeyBuildersMatchingThisKey != null)
            {
                throw new IllegalStateException();
            }
            this.foreignKeyBuildersMatchingThisKey = Objects.requireNonNull(foreignKeyBuildersMatchingThisKey);
        }

        public void setMinLengthPropertyValidationBuilder(Optional<MinLengthPropertyValidationBuilder> minLengthPropertyValidationBuilder)
        {
            this.minLengthPropertyValidationBuilder = Objects.requireNonNull(minLengthPropertyValidationBuilder);
        }

        public void setMaxLengthPropertyValidationBuilder(Optional<MaxLengthPropertyValidationBuilder> maxLengthPropertyValidationBuilder)
        {
            this.maxLengthPropertyValidationBuilder = Objects.requireNonNull(maxLengthPropertyValidationBuilder);
        }

        public void setMinPropertyValidationBuilder(Optional<MinPropertyValidationBuilder> minPropertyValidationBuilder)
        {
            this.minPropertyValidationBuilder = Objects.requireNonNull(minPropertyValidationBuilder);
        }

        public void setMaxPropertyValidationBuilder(Optional<MaxPropertyValidationBuilder> maxPropertyValidationBuilder)
        {
            this.maxPropertyValidationBuilder = Objects.requireNonNull(maxPropertyValidationBuilder);
        }

        public void setModifierBuilders(ImmutableList<ModifierBuilder> modifierBuilders)
        {
            this.modifierBuilders = modifierBuilders;
        }

        @Override
        protected void buildChildren()
        {
            AbstractDataTypeProperty<T> property = this.getElement();

            ImmutableList<Modifier> modifiers =
                    this.modifierBuilders.collect(ModifierBuilder::build);
            property.setModifiers(modifiers);

            Optional<MinLengthPropertyValidation> minLengthPropertyValidation =
                    this.minLengthPropertyValidationBuilder.map(ElementBuilder::build);
            Optional<MaxLengthPropertyValidation> maxLengthPropertyValidation =
                    this.maxLengthPropertyValidationBuilder.map(ElementBuilder::build);
            Optional<MinPropertyValidation> minPropertyValidation =
                    this.minPropertyValidationBuilder.map(ElementBuilder::build);
            Optional<MaxPropertyValidation> maxPropertyValidation =
                    this.maxPropertyValidationBuilder.map(ElementBuilder::build);

            property.setMinLengthPropertyValidation(minLengthPropertyValidation);
            property.setMaxLengthPropertyValidation(maxLengthPropertyValidation);
            property.setMinPropertyValidation(minPropertyValidation);
            property.setMaxPropertyValidation(maxPropertyValidation);
        }

        public final void build2()
        {
            MutableOrderedMap<AssociationEnd, ImmutableList<DataTypeProperty>> keysMatchingThisForeignKey =
                    this.keyBuildersMatchingThisForeignKey.collect((associationEndBuilder, dataTypePropertyBuilders) -> Tuples.pair(
                            associationEndBuilder.getElement(),
                            dataTypePropertyBuilders.collect(dataTypePropertyBuilder -> dataTypePropertyBuilder.getElement())));

            MutableOrderedMap<AssociationEnd, ImmutableList<DataTypeProperty>> foreignKeysMatchingThisKey =
                    this.foreignKeyBuildersMatchingThisKey.collect((associationEndBuilder, dataTypePropertyBuilders) -> Tuples.pair(
                            associationEndBuilder.getElement(),
                            dataTypePropertyBuilders.collect(dataTypePropertyBuilder -> dataTypePropertyBuilder.getElement())));

            AbstractDataTypeProperty<T> property = this.getElement();
            property.setKeysMatchingThisForeignKey(keysMatchingThisForeignKey.asUnmodifiable());
            property.setForeignKeysMatchingThisKey(foreignKeysMatchingThisKey.asUnmodifiable());
        }

        public static <InputKey, InputValue, OutputKey, OutputValue> ImmutableListMultimap<OutputKey, OutputValue> collectKeyMultiValues(
                @Nonnull ListMultimap<InputKey, InputValue> multimap,
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
    }
}
