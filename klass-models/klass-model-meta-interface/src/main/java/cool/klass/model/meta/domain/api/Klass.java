package cool.klass.model.meta.domain.api;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.property.PrimitiveProperty;
import cool.klass.model.meta.domain.api.property.Property;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public interface Klass extends Classifier
{
    @Override
    default ImmutableList<ClassModifier> getInheritedClassModifiers()
    {
        ImmutableList<ClassModifier> superClassProperties = this.getSuperClass()
                .map(Classifier::getClassModifiers)
                .orElseGet(Lists.immutable::empty);

        ImmutableList<ClassModifier> interfaceProperties = Classifier.super.getInheritedClassModifiers();

        return superClassProperties.newWithAll(interfaceProperties).distinctBy(NamedElement::getName);
    }

    @Override
    default ImmutableList<DataTypeProperty> getInheritedDataTypeProperties()
    {
        ImmutableList<DataTypeProperty> superClassProperties = this.getSuperClass()
                .map(Classifier::getDataTypeProperties)
                .orElseGet(Lists.immutable::empty);

        ImmutableList<DataTypeProperty> interfaceProperties = Classifier.super.getInheritedDataTypeProperties();

        return superClassProperties.newWithAll(interfaceProperties).distinctBy(NamedElement::getName);
    }

    // TODO: Replace with an implementation that preserves order
    @Nonnull
    @Override
    default ImmutableList<Property> getProperties()
    {
        return Lists.immutable.<Property>empty()
                .newWithAll(this.getDataTypeProperties())
                .newWithAll(this.getAssociationEnds());
    }

    @Override
    default ImmutableList<Property> getDeclaredProperties()
    {
        return Lists.immutable.<Property>empty()
                .newWithAll(this.getDeclaredDataTypeProperties())
                .newWithAll(this.getDeclaredAssociationEnds());
    }

    ImmutableList<AssociationEnd> getDeclaredAssociationEnds();

    default ImmutableList<AssociationEnd> getAssociationEnds()
    {
        ImmutableList<AssociationEnd> inheritedAssociationEnds = this.getSuperClass()
                .map(Klass::getAssociationEnds)
                .orElseGet(Lists.immutable::empty);

        return inheritedAssociationEnds
                .newWithAll(this.getDeclaredAssociationEnds())
                .toReversed()
                .distinctBy(NamedElement::getName)
                .toReversed();
    }

    // TODO: Override for efficiency?
    @Nonnull
    default Optional<Property> getPropertyByName(String name)
    {
        ImmutableList<DataTypeProperty> dataTypeProperties = this.getDataTypeProperties()
                .select(each -> each.getName().equals(name));
        ImmutableList<AssociationEnd> associationEnds = this.getAssociationEnds()
                .select(each -> each.getName().equals(name));

        if (dataTypeProperties.isEmpty() && associationEnds.isEmpty())
        {
            return Optional.empty();
        }

        if (dataTypeProperties.size() + associationEnds.size() > 1)
        {
            throw new AssertionError(name);
        }

        if (dataTypeProperties.notEmpty())
        {
            return Optional.of(dataTypeProperties.getOnly());
        }

        return Optional.of(associationEnds.getOnly());
    }

    default ImmutableList<DataTypeProperty> getKeyProperties()
    {
        return this.getDataTypeProperties().select(DataTypeProperty::isKey);
    }

    @Nonnull
    Optional<AssociationEnd> getVersionProperty();

    @Nonnull
    Optional<AssociationEnd> getVersionedProperty();

    @Nonnull
    Optional<Klass> getSuperClass();

    boolean isUser();

    default boolean isAbstract()
    {
        return this.getInheritanceType() != InheritanceType.NONE;
    }

    InheritanceType getInheritanceType();

    default boolean isTransient()
    {
        return this.getClassModifiers().anySatisfy(ClassModifier::isTransient);
    }

    @Nonnull
    @Override
    default String getSourceCodeWithInference()
    {
        String sourceCode = this.getSourceCode();
        if (this.getMacroElement().isPresent())
        {
            return sourceCode;
        }

        return ""
                + (this.isUser() ? "user" : "class") + ' ' + this.getName() + '\n'
                + this.getAbstractSourceCode()
                + this.getExtendsSourceCode()
                + this.getImplementsSourceCode()
                + this.getModifiersSourceCode()
                + "{\n"
                + this.getPropertiesSourceCode()
                + "}\n";
    }

    @Nonnull
    default String getAbstractSourceCode()
    {
        return this.getInheritanceType() == InheritanceType.NONE
                ? ""
                : "    abstract(" + this.getInheritanceType().getPrettyName() + ")\n";
    }

    @Nonnull
    default String getExtendsSourceCode()
    {
        return this.getSuperClass()
                .map(NamedElement::getName)
                .map(klassName -> "    extends " + klassName + "\n")
                .orElse("");
    }

    @Override
    default boolean isStrictSuperTypeOf(@Nonnull Classifier classifier)
    {
        if (Classifier.super.isStrictSuperTypeOf(classifier))
        {
            return true;
        }

        if (this == classifier)
        {
            return false;
        }

        if (classifier instanceof Interface)
        {
            return false;
        }

        Klass           klass              = (Klass) classifier;
        Optional<Klass> optionalSuperClass = klass.getSuperClass();
        if (!optionalSuperClass.isPresent())
        {
            return false;
        }

        Klass superClass = optionalSuperClass.get();
        if (this == superClass)
        {
            return true;
        }

        return this.isStrictSuperTypeOf(superClass);
    }

    @Override
    default boolean isStrictSubTypeOf(Classifier classifier)
    {
        if (Classifier.super.isStrictSubTypeOf(classifier))
        {
            return true;
        }

        if (this == classifier)
        {
            return false;
        }

        Optional<Klass> optionalSuperClass = this.getSuperClass();
        if (!optionalSuperClass.isPresent())
        {
            return false;
        }

        Klass superClass = optionalSuperClass.get();
        if (superClass == classifier)
        {
            return true;
        }

        return superClass.isStrictSubTypeOf(classifier);
    }

    default Optional<PrimitiveProperty> getCreatedByProperty()
    {
        return this.getDataTypeProperties()
                .asLazy()
                .selectInstancesOf(PrimitiveProperty.class)
                .detectOptional(DataTypeProperty::isCreatedBy);
    }

    default Optional<PrimitiveProperty> getCreatedOnProperty()
    {
        return this.getDataTypeProperties()
                .asLazy()
                .selectInstancesOf(PrimitiveProperty.class)
                .detectOptional(DataTypeProperty::isCreatedOn);
    }

    default Optional<PrimitiveProperty> getLastUpdatedByProperty()
    {
        return this.getDataTypeProperties()
                .asLazy()
                .selectInstancesOf(PrimitiveProperty.class)
                .detectOptional(DataTypeProperty::isLastUpdatedBy);
    }
}
