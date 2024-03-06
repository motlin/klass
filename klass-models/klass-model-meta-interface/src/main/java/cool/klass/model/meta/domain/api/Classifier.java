package cool.klass.model.meta.domain.api;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.modifier.Modifier;
import cool.klass.model.meta.domain.api.modifier.ModifierOwner;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.property.Property;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.set.MutableSet;

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
        Objects.requireNonNull(this.getDeclaredDataTypeProperties());

        MutableSet<String> propertyNames = this.getDeclaredDataTypeProperties().collect(NamedElement::getName).toSet();

        ImmutableList<DataTypeProperty> inheritedProperties = this.getInheritedDataTypeProperties()
                .reject(inheritedProperty -> propertyNames.contains(inheritedProperty.getName()));

        return inheritedProperties.newWithAll(this.getDeclaredDataTypeProperties());
    }

    default ImmutableList<DataTypeProperty> getInheritedDataTypeProperties()
    {
        // TODO: Factor in depth of declaration

        return this.getInterfaces()
                .flatCollect(Classifier::getDataTypeProperties)
                .distinctBy(NamedElement::getName)
                .toImmutable();
    }

    @Nonnull
    ImmutableList<DataTypeProperty> getDeclaredDataTypeProperties();

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
}
