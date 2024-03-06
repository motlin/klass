package cool.klass.model.meta.domain.api;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.modifier.Modifier;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.property.PrimitiveProperty;
import cool.klass.model.meta.domain.api.property.Property;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public interface Klass
        extends Classifier
{
    @Override
    default void visit(TopLevelElementVisitor visitor)
    {
        visitor.visitKlass(this);
    }

    @Override
    default ImmutableList<Modifier> getInheritedModifiers()
    {
        ImmutableList<Modifier> superClassModifiers = this.getSuperClass()
                .map(Classifier::getModifiers)
                .orElseGet(Lists.immutable::empty);

        ImmutableList<Modifier> interfaceModifiers = Classifier.super.getInheritedModifiers();

        ImmutableList<Modifier> allModifiers = superClassModifiers.newWithAll(interfaceModifiers);
        return allModifiers.distinctBy(Modifier::getKeyword);
    }

    @Override
    default ImmutableList<DataTypeProperty> getInheritedDataTypeProperties()
    {
        ImmutableList<DataTypeProperty> interfaceProperties = this.getInterfaces()
                .flatCollect(Classifier::getDataTypeProperties)
                .toImmutable();

        ImmutableList<DataTypeProperty> superClassProperties = this.getSuperClass()
                .map(Classifier::getDataTypeProperties)
                .orElseGet(Lists.immutable::empty);

        ImmutableList<DataTypeProperty> allDataTypeProperties = this
                .getDeclaredDataTypeProperties()
                .newWithAll(superClassProperties)
                .newWithAll(interfaceProperties);
        ImmutableList<DataTypeProperty> result = allDataTypeProperties
                .distinctBy(NamedElement::getName)
                .newWithoutAll(this.getDeclaredDataTypeProperties());
        return result;
    }

    @Override
    default DataTypeProperty getDataTypePropertyByName(String name)
    {
        DataTypeProperty declaredDataTypePropertyByName = this.getDeclaredDataTypePropertyByName(name);
        if (declaredDataTypePropertyByName != null)
        {
            return declaredDataTypePropertyByName;
        }

        DataTypeProperty superClassDataTypeProperty = this
                .getSuperClass()
                .map(superClass -> superClass.getDataTypePropertyByName(name))
                .orElse(null);

        if (superClassDataTypeProperty != null)
        {
            return superClassDataTypeProperty;
        }

        return this.getInterfaces()
                .asLazy()
                .collectWith(Classifier::getDataTypePropertyByName, name)
                .detect(Objects::nonNull);
    }

    default AssociationEnd getAssociationEndByName(String name)
    {
        AssociationEnd declaredAssociationEndByName = this.getDeclaredAssociationEndByName(name)
                .orElse(null);
        if (declaredAssociationEndByName != null)
        {
            return declaredAssociationEndByName;
        }

        AssociationEnd superClassAssociationEnd = this
                .getSuperClass()
                .map(superClass -> superClass.getAssociationEndByName(name))
                .orElse(null);

        return superClassAssociationEnd;
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

    Optional<AssociationEnd> getDeclaredAssociationEndByName(String associationEndName);

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

    @Nonnull
    default Optional<Property> getPropertyByName(String name)
    {
        DataTypeProperty dataTypeProperty   = this.getDataTypePropertyByName(name);
        AssociationEnd   associationEnd     = this.getAssociationEndByName(name);

        if (dataTypeProperty != null && associationEnd != null)
        {
            String detailMessage = "Property " + name + " is both a data type property and an association end.";
            throw new AssertionError(detailMessage);
        }

        if (dataTypeProperty != null)
        {
            return Optional.of(dataTypeProperty);
        }

        if (associationEnd != null)
        {
            return Optional.of(associationEnd);
        }

        return Optional.empty();
    }

    @Nonnull
    Optional<AssociationEnd> getVersionProperty();

    @Nonnull
    Optional<AssociationEnd> getVersionedProperty();

    default Optional<DataTypeProperty> getVersionNumberProperty()
    {
        ImmutableList<DataTypeProperty> versionProperties = this
                .getDataTypeProperties()
                .select(DataTypeProperty::isVersion);
        if (versionProperties.size() > 1)
        {
            throw new AssertionError();
        }
        if (versionProperties.isEmpty())
        {
            return Optional.empty();
        }
        return Optional.of(versionProperties.getOnly());
    }

    @Nonnull
    Optional<Klass> getSuperClass();

    ImmutableList<Klass> getSubClasses();

    boolean isUser();

    @Override
    default boolean isAbstract()
    {
        return this.getInheritanceType() != InheritanceType.NONE;
    }

    InheritanceType getInheritanceType();

    boolean isTransient();

    default boolean isVersioned()
    {
        return this.getVersionProperty().isPresent();
    }

    default boolean isAudited()
    {
        return this.getDataTypeProperties().anySatisfy(DataTypeProperty::isAudit);
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
        if (optionalSuperClass.isEmpty())
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
        if (optionalSuperClass.isEmpty())
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

    default Optional<DataTypeProperty> getSystemFromProperty()
    {
        return this.getDataTypeProperties()
                .select(DataTypeProperty::isSystem)
                .detectOptional(DataTypeProperty::isFrom);
    }

    default Optional<DataTypeProperty> getSystemToProperty()
    {
        return this.getDataTypeProperties()
                .select(DataTypeProperty::isSystem)
                .detectOptional(DataTypeProperty::isTo);
    }

    default ImmutableList<Klass> getSubClassChain()
    {
        return this.getSubClasses()
                .flatCollect(Klass::getSubClassChainWithThis)
                .toImmutable();
    }

    default ImmutableList<Klass> getSubClassChainWithThis()
    {
        return Lists.immutable.with(this).newWithAll(this.getSubClassChain());
    }

    default ImmutableList<Klass> getSuperClassChain()
    {
        return this.getSuperClass()
                .map(Klass::getSuperClassChainWithThis)
                .orElseGet(Lists.immutable::empty);
    }

    default ImmutableList<Klass> getSuperClassChainWithThis()
    {
        return Lists.immutable.with(this).newWithAll(this.getSuperClassChain());
    }
}
