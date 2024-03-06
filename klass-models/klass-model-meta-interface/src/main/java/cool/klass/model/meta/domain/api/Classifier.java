package cool.klass.model.meta.domain.api;

import java.util.LinkedHashMap;
import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.modifier.Modifier;
import cool.klass.model.meta.domain.api.modifier.ModifierOwner;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.property.Property;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.api.map.OrderedMap;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

public interface Classifier
        extends Type, ModifierOwner, TopLevelElement
{
    boolean isAbstract();

    @Nonnull
    ImmutableList<Interface> getInterfaces();

    @Nonnull
    default ImmutableList<Modifier> getModifiers()
    {
        Objects.requireNonNull(this.getDeclaredModifiers());

        MutableSet<String> propertyNames = this.getDeclaredModifiers().collect(Modifier::getKeyword).toSet();

        ImmutableList<Modifier> inheritedProperties = this.getInheritedModifiers()
                .reject(inheritedProperty -> propertyNames.contains(inheritedProperty.getKeyword()));

        return this.getDeclaredModifiers().newWithAll(inheritedProperties);
    }

    default ImmutableList<Modifier> getInheritedModifiers()
    {
        return this.getInterfaces()
                .flatCollect(Classifier::getModifiers)
                .distinctBy(Modifier::getKeyword)
                .toImmutable();
    }

    @Nonnull
    ImmutableList<Modifier> getDeclaredModifiers();

    @Nonnull
    ImmutableList<Property> getProperties();

    ImmutableList<Property> getDeclaredProperties();

    // TODO: Optimize Classifier.getKeyProperties()
    default ImmutableList<DataTypeProperty> getKeyProperties()
    {
        return this.getDataTypeProperties().select(DataTypeProperty::isKey);
    }

    @Nonnull
    default ImmutableList<DataTypeProperty> getDataTypeProperties()
    {
        ImmutableList<DataTypeProperty> inheritedDataTypeProperties = this.getInheritedDataTypeProperties();

        MutableSet<String> propertyNames = inheritedDataTypeProperties.collect(NamedElement::getName).toSet();

        ImmutableList<DataTypeProperty> declaredDataTypeProperties = this.getDeclaredDataTypeProperties()
                .reject(declaredProperty -> propertyNames.contains(declaredProperty.getName()));

        ImmutableList<DataTypeProperty> dataTypeProperties = inheritedDataTypeProperties.newWithAll(
                declaredDataTypeProperties);

        ImmutableList<DataTypeProperty> foreignKeys = dataTypeProperties.select(DataTypeProperty::isForeignKey);
        ImmutableList<DataTypeProperty> keysAndForeignKeys = foreignKeys.select(DataTypeProperty::isKey);
        ImmutableList<DataTypeProperty> keys = dataTypeProperties.select(DataTypeProperty::isKey).reject(DataTypeProperty::isForeignKey);
        ImmutableList<DataTypeProperty> nonKeyForeignKeys = foreignKeys.reject(DataTypeProperty::isKey).reject(DataTypeProperty::isCreatedBy).reject(DataTypeProperty::isLastUpdatedBy);
        ImmutableList<DataTypeProperty> system = dataTypeProperties.select(DataTypeProperty::isSystemRange);
        ImmutableList<DataTypeProperty> systemFrom = dataTypeProperties.select(DataTypeProperty::isSystemFrom);
        ImmutableList<DataTypeProperty> systemTo = dataTypeProperties.select(DataTypeProperty::isSystemTo);
        ImmutableList<DataTypeProperty> valid = dataTypeProperties.select(DataTypeProperty::isValidRange);
        ImmutableList<DataTypeProperty> validFrom = dataTypeProperties.select(DataTypeProperty::isValidFrom);
        ImmutableList<DataTypeProperty> validTo = dataTypeProperties.select(DataTypeProperty::isValidTo);
        ImmutableList<DataTypeProperty> createdBy = dataTypeProperties.select(DataTypeProperty::isCreatedBy).reject(DataTypeProperty::isKey);
        ImmutableList<DataTypeProperty> createdOn = dataTypeProperties.select(DataTypeProperty::isCreatedOn);
        ImmutableList<DataTypeProperty> lastUpdatedBy = dataTypeProperties.select(DataTypeProperty::isLastUpdatedBy).reject(DataTypeProperty::isKey);

        ImmutableList<DataTypeProperty> initialDataTypeProperties = Lists.immutable
                .withAll(keysAndForeignKeys)
                .newWithAll(keys)
                .newWithAll(nonKeyForeignKeys)
                .newWithAll(system)
                .newWithAll(systemFrom)
                .newWithAll(systemTo)
                .newWithAll(valid)
                .newWithAll(validFrom)
                .newWithAll(validTo)
                .newWithAll(createdBy)
                .newWithAll(createdOn)
                .newWithAll(lastUpdatedBy);

        ImmutableList<DataTypeProperty> otherDataTypeProperties = dataTypeProperties
                .reject(initialDataTypeProperties::contains);

        ImmutableList<DataTypeProperty> result = initialDataTypeProperties.newWithAll(otherDataTypeProperties);

        if (!result.equals(result.distinct()))
        {
            throw new AssertionError(result);
        }

        return result;
    }

    default ImmutableList<DataTypeProperty> getInheritedDataTypeProperties()
    {
        // TODO: Factor in depth of declaration

        ImmutableList<DataTypeProperty> inheritedDataTypeProperties = this.getInterfaces()
                .flatCollect(Classifier::getDataTypeProperties)
                .toImmutable();

        return this
                .getDeclaredDataTypeProperties()
                .newWithAll(inheritedDataTypeProperties)
                .distinctBy(NamedElement::getName)
                .newWithoutAll(this.getDeclaredDataTypeProperties());
    }

    @Nonnull
    ImmutableList<DataTypeProperty> getDeclaredDataTypeProperties();

    DataTypeProperty getDeclaredDataTypePropertyByName(String name);

    default boolean isTemporal()
    {
        return this.isSystemTemporal() || this.isValidTemporal();
    }

    default boolean isBitemporal()
    {
        return this.isSystemTemporal() && this.isValidTemporal();
    }

    default boolean isSystemTemporal()
    {
        return this.getDataTypeProperties().anySatisfy(DataTypeProperty::isSystemTemporal);
    }

    default boolean isValidTemporal()
    {
        return this.getDataTypeProperties().anySatisfy(DataTypeProperty::isValidTemporal);
    }

    default boolean isStrictSuperTypeOf(@Nonnull Classifier classifier)
    {
        if (this == classifier)
        {
            return false;
        }

        ImmutableList<Interface> superInterfaces = classifier.getInterfaces();
        if (superInterfaces.contains(this))
        {
            return true;
        }

        return superInterfaces.anySatisfy(this::isStrictSuperTypeOf);
    }

    default boolean isSubTypeOf(Classifier classifier)
    {
        if (this == classifier)
        {
            return true;
        }

        return this.isStrictSubTypeOf(classifier);
    }

    default boolean isStrictSubTypeOf(Classifier classifier)
    {
        if (this == classifier)
        {
            return false;
        }

        if (classifier instanceof Klass)
        {
            return false;
        }

        ImmutableList<Interface> superInterfaces = this.getInterfaces();
        if (superInterfaces.contains(classifier))
        {
            return true;
        }

        return superInterfaces.anySatisfyWith(Interface::isStrictSubTypeOf, classifier);
    }

    @Nonnull
    default MutableOrderedMap<AssociationEnd, MutableOrderedMap<DataTypeProperty, DataTypeProperty>> getForeignKeys()
    {
        MutableOrderedMap<AssociationEnd, MutableOrderedMap<DataTypeProperty, DataTypeProperty>> foreignKeyConstraints =
                OrderedMapAdapter.adapt(new LinkedHashMap<>());

        for (DataTypeProperty foreignKey : this.getDeclaredDataTypeProperties())
        {
            OrderedMap<AssociationEnd, DataTypeProperty> keysMatchingThisForeignKey = foreignKey.getKeysMatchingThisForeignKey();

            keysMatchingThisForeignKey.forEachKeyValue((associationEnd, key) ->
            {
                MutableOrderedMap<DataTypeProperty, DataTypeProperty> dataTypeProperties = foreignKeyConstraints.computeIfAbsent(
                        associationEnd,
                        ignored -> OrderedMapAdapter.adapt(new LinkedHashMap<>()));
                dataTypeProperties.put(foreignKey, key);
            });
        }

        return foreignKeyConstraints;
    }
}
