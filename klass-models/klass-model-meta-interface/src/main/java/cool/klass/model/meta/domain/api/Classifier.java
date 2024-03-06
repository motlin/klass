package cool.klass.model.meta.domain.api;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.property.Property;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.set.MutableSet;

public interface Classifier extends Type, PackageableElement
{
    @Nonnull
    ImmutableList<Interface> getInterfaces();

    @Nonnull
    default ImmutableList<ClassModifier> getClassModifiers()
    {
        Objects.requireNonNull(this.getDeclaredClassModifiers());

        MutableSet<String> propertyNames = this.getDeclaredClassModifiers().collect(NamedElement::getName).toSet();

        ImmutableList<ClassModifier> inheritedProperties = this.getInheritedClassModifiers()
                .reject(inheritedProperty -> propertyNames.contains(inheritedProperty.getName()));

        return this.getDeclaredClassModifiers().newWithAll(inheritedProperties);
    }

    default ImmutableList<ClassModifier> getInheritedClassModifiers()
    {
        return this.getInterfaces()
                .flatCollect(Classifier::getClassModifiers)
                .distinctBy(NamedElement::getName)
                .toImmutable();
    }

    @Nonnull
    ImmutableList<ClassModifier> getDeclaredClassModifiers();

    @Nonnull
    ImmutableList<Property> getProperties();

    @Nonnull
    default ImmutableList<DataTypeProperty> getDataTypeProperties()
    {
        Objects.requireNonNull(this.getDeclaredDataTypeProperties());

        MutableSet<String> propertyNames = this.getDeclaredDataTypeProperties().collect(NamedElement::getName).toSet();

        ImmutableList<DataTypeProperty> inheritedProperties = this.getInheritedDataTypeProperties()
                .reject(inheritedProperty -> propertyNames.contains(inheritedProperty.getName()));

        return this.getDeclaredDataTypeProperties().newWithAll(inheritedProperties);
    }

    default ImmutableList<DataTypeProperty> getInheritedDataTypeProperties()
    {
        return this.getInterfaces()
                .flatCollect(Classifier::getDataTypeProperties)
                .distinctBy(NamedElement::getName)
                .toImmutable();
    }

    @Nonnull
    ImmutableList<DataTypeProperty> getDeclaredDataTypeProperties();

    default boolean isBitemporal()
    {
        return this.isValidTemporal() && this.isSystemTemporal();
    }

    default boolean isValidTemporal()
    {
        return this.getDataTypeProperties().anySatisfy(DataTypeProperty::isValidTemporal);
    }

    default boolean isSystemTemporal()
    {
        return this.getDataTypeProperties().anySatisfy(DataTypeProperty::isSystemTemporal);
    }

    @Nonnull
    default String getImplementsSourceCode()
    {
        return this.getInterfaces().isEmpty()
                    ? ""
                    : "    implements " + this.getInterfaces().collect(NamedElement::getName).makeString() + "\n";
    }

    default String getPropertiesSourceCode()
    {
        return this.getProperties()
                    .collect(Element::getSourceCode)
                    .collect(each -> "    " + each + '\n')
                    .makeString("");
    }

    default String getModifiersSourceCode()
    {
        return this.getClassModifiers()
                    .collect(Element::getSourceCode)
                    .collect(each -> "    " + each + '\n')
                    .makeString("");
    }
}
