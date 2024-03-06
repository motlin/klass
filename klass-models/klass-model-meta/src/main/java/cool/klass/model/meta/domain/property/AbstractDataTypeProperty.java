package cool.klass.model.meta.domain.property;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractClassifier;
import cool.klass.model.meta.domain.AbstractClassifier.ClassifierBuilder;
import cool.klass.model.meta.domain.api.DataType;
import cool.klass.model.meta.domain.api.DataType.DataTypeGetter;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.property.PropertyModifier;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import cool.klass.model.meta.domain.property.PropertyModifierImpl.PropertyModifierBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.block.function.Function;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.multimap.list.ImmutableListMultimap;
import org.eclipse.collections.api.multimap.list.ListMultimap;
import org.eclipse.collections.api.multimap.list.MutableListMultimap;
import org.eclipse.collections.impl.factory.Multimaps;
import org.eclipse.collections.impl.utility.Iterate;

// TODO: The generic type here is inconvenient. Replace it with a bunch of overrides of the getType method
public abstract class AbstractDataTypeProperty<T extends DataType> extends AbstractProperty<T> implements DataTypeProperty
{
    private final boolean optional;

    private ImmutableList<PropertyModifier> propertyModifiers;

    private ImmutableListMultimap<AssociationEnd, DataTypeProperty> keysMatchingThisForeignKey;
    private ImmutableListMultimap<AssociationEnd, DataTypeProperty> foreignKeysMatchingThisKey;

    protected AbstractDataTypeProperty(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull T dataType,
            @Nonnull AbstractClassifier owningClassifier,
            boolean isOptional)
    {
        super(elementContext, inferred, nameContext, name, ordinal, dataType, owningClassifier);
        this.optional = isOptional;
    }

    @Override
    @Nonnull
    public ImmutableList<PropertyModifier> getPropertyModifiers()
    {
        return Objects.requireNonNull(this.propertyModifiers);
    }

    protected void setPropertyModifiers(ImmutableList<PropertyModifier> propertyModifiers)
    {
        if (this.propertyModifiers != null)
        {
            throw new IllegalStateException();
        }
        this.propertyModifiers = Objects.requireNonNull(propertyModifiers);
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

    public ImmutableListMultimap<AssociationEnd, DataTypeProperty> getKeysMatchingThisForeignKey()
    {
        return Objects.requireNonNull(this.keysMatchingThisForeignKey);
    }

    protected void setKeysMatchingThisForeignKey(ImmutableListMultimap<AssociationEnd, DataTypeProperty> keysMatchingThisForeignKey)
    {
        if (this.keysMatchingThisForeignKey != null)
        {
            throw new IllegalStateException();
        }
        this.keysMatchingThisForeignKey = Objects.requireNonNull(keysMatchingThisForeignKey);
    }

    public ImmutableListMultimap<AssociationEnd, DataTypeProperty> getForeignKeysMatchingThisKey()
    {
        return Objects.requireNonNull(this.foreignKeysMatchingThisKey);
    }

    protected void setForeignKeysMatchingThisKey(ImmutableListMultimap<AssociationEnd, DataTypeProperty> foreignKeysMatchingThisKey)
    {
        if (this.foreignKeysMatchingThisKey != null)
        {
            throw new IllegalStateException();
        }
        this.foreignKeysMatchingThisKey = Objects.requireNonNull(foreignKeysMatchingThisKey);
    }

    public abstract static class DataTypePropertyBuilder<T extends DataType, TG extends DataTypeGetter, BuiltElement extends AbstractDataTypeProperty<T>>
            extends PropertyBuilder<T, TG, BuiltElement>
    {
        protected final ImmutableList<PropertyModifierBuilder> propertyModifierBuilders;
        protected final boolean                                isOptional;

        protected ImmutableListMultimap<AssociationEndBuilder, DataTypePropertyBuilder<?, ?, ?>> keyBuildersMatchingThisForeignKey;
        protected ImmutableListMultimap<AssociationEndBuilder, DataTypePropertyBuilder<?, ?, ?>> foreignKeyBuildersMatchingThisKey;

        protected DataTypePropertyBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull TG typeBuilder,
                @Nonnull ClassifierBuilder<?> owningClassifierBuilder,
                ImmutableList<PropertyModifierBuilder> propertyModifierBuilders,
                boolean isOptional)
        {
            super(elementContext, inferred, nameContext, name, ordinal, typeBuilder, owningClassifierBuilder);
            this.propertyModifierBuilders = Objects.requireNonNull(propertyModifierBuilders);
            this.isOptional = isOptional;
        }

        public void setKeyBuildersMatchingThisForeignKey(ImmutableListMultimap<AssociationEndBuilder, DataTypePropertyBuilder<?, ?, ?>> keyBuildersMatchingThisForeignKey)
        {
            if (this.keyBuildersMatchingThisForeignKey != null)
            {
                throw new IllegalStateException();
            }
            this.keyBuildersMatchingThisForeignKey = Objects.requireNonNull(keyBuildersMatchingThisForeignKey);
        }

        public void setForeignKeyBuildersMatchingThisKey(ImmutableListMultimap<AssociationEndBuilder, DataTypePropertyBuilder<?, ?, ?>> foreignKeyBuildersMatchingThisKey)
        {
            if (this.foreignKeyBuildersMatchingThisKey != null)
            {
                throw new IllegalStateException();
            }
            this.foreignKeyBuildersMatchingThisKey = Objects.requireNonNull(foreignKeyBuildersMatchingThisKey);
        }

        public final void build2()
        {
            ImmutableListMultimap<AssociationEnd, DataTypeProperty> keysMatchingThisForeignKey = DataTypePropertyBuilder.collectKeyMultiValues(
                    this.keyBuildersMatchingThisForeignKey,
                    ElementBuilder::getElement,
                    (DataTypePropertyBuilder<?, ?, ?> dataTypePropertyBuilder) -> dataTypePropertyBuilder.getElement());

            ImmutableListMultimap<AssociationEnd, DataTypeProperty> foreignKeysMatchingThisKey = DataTypePropertyBuilder.collectKeyMultiValues(
                    this.foreignKeyBuildersMatchingThisKey,
                    ElementBuilder::getElement,
                    (DataTypePropertyBuilder<?, ?, ?> dataTypePropertyBuilder) -> dataTypePropertyBuilder.getElement());

            this.element.setKeysMatchingThisForeignKey(keysMatchingThisForeignKey);
            this.element.setForeignKeysMatchingThisKey(foreignKeysMatchingThisKey);
        }

        public static <InputKey, InputValue, OutputKey, OutputValue> ImmutableListMultimap<OutputKey, OutputValue> collectKeyMultiValues(
                ListMultimap<InputKey, InputValue> multimap,
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
    }
}
