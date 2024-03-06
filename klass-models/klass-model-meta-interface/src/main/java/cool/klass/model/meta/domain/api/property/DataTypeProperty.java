package cool.klass.model.meta.domain.api.property;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.DataType;
import cool.klass.model.meta.domain.api.modifier.Modifier;
import cool.klass.model.meta.domain.api.property.validation.MaxLengthPropertyValidation;
import cool.klass.model.meta.domain.api.property.validation.MaxPropertyValidation;
import cool.klass.model.meta.domain.api.property.validation.MinLengthPropertyValidation;
import cool.klass.model.meta.domain.api.property.validation.MinPropertyValidation;
import cool.klass.model.meta.domain.api.visitor.DataTypePropertyVisitor;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.OrderedMap;

public interface DataTypeProperty
        extends Property
{
    void visit(@Nonnull DataTypePropertyVisitor visitor);

    @Nonnull
    @Override
    DataType getType();

    @Nonnull
    ImmutableList<Modifier> getModifiers();

    Optional<MinLengthPropertyValidation> getMinLengthPropertyValidation();

    Optional<MaxLengthPropertyValidation> getMaxLengthPropertyValidation();

    Optional<MinPropertyValidation> getMinPropertyValidation();

    Optional<MaxPropertyValidation> getMaxPropertyValidation();

    OrderedMap<AssociationEnd, DataTypeProperty> getKeysMatchingThisForeignKey();

    OrderedMap<AssociationEnd, DataTypeProperty> getForeignKeysMatchingThisKey();

    default boolean isKey()
    {
        return this.getModifiers().anySatisfy(Modifier::isKey);
    }

    boolean isID();

    default boolean isAudit()
    {
        return this.getModifiers().anySatisfy(Modifier::isAudit);
    }

    default boolean isCreatedBy()
    {
        return this.getModifiers().anySatisfy(Modifier::isCreatedBy);
    }

    default boolean isCreatedOn()
    {
        return this.getModifiers().anySatisfy(Modifier::isCreatedOn);
    }

    default boolean isLastUpdatedBy()
    {
        return this.getModifiers().anySatisfy(Modifier::isLastUpdatedBy);
    }

    default boolean isValid()
    {
        return this.getModifiers().anySatisfy(Modifier::isValid);
    }

    default boolean isValidFrom()
    {
        return this.isValid() && this.isFrom();
    }

    default boolean isValidTo()
    {
        return this.isValid() && this.isTo();
    }

    default boolean isValidRange()
    {
        return this.isValid() && this.isTemporalRange();
    }

    default boolean isSystem()
    {
        return this.getModifiers().anySatisfy(Modifier::isSystem);
    }

    default boolean isSystemFrom()
    {
        return this.isSystem() && this.isFrom();
    }

    default boolean isSystemTo()
    {
        return this.isSystem() && this.isTo();
    }

    default boolean isSystemRange()
    {
        return this.isSystem() && this.isTemporalRange();
    }

    default boolean isFrom()
    {
        return this.getModifiers().anySatisfy(Modifier::isFrom);
    }

    default boolean isTo()
    {
        return this.getModifiers().anySatisfy(Modifier::isTo);
    }

    default boolean isFinal()
    {
        return this.getModifiers().anySatisfy(Modifier::isFinal);
    }

    default boolean isPrivate()
    {
        return this.getModifiers().anySatisfy(Modifier::isPrivate);
    }

    default boolean isValidTemporal()
    {
        return this.isValid() && this.isTemporalRange();
    }

    default boolean isSystemTemporal()
    {
        return this.isSystem() && this.isTemporalRange();
    }

    boolean isOptional();

    @Override
    default boolean isRequired()
    {
        return !this.isOptional();
    }

    boolean isTemporalRange();

    boolean isTemporalInstant();

    boolean isTemporal();

    boolean isForeignKey();

    boolean isVersion();

    @Override
    default boolean isDerived()
    {
        return this.getModifiers().anySatisfy(Modifier::isDerived);
    }

    default boolean isForeignKeyWithOpposite()
    {
        OrderedMap<AssociationEnd, DataTypeProperty> keysMatchingThisForeignKey = this.getKeysMatchingThisForeignKey();
        ImmutableList<DataTypeProperty> dataTypeProperties = keysMatchingThisForeignKey
                .valuesView()
                .toList()
                .toImmutable();
        return dataTypeProperties
                .anySatisfyWith((dataTypeProperty, keyProperty1) -> keyProperty1.isOppositeKey(dataTypeProperty), this);
    }

    default boolean isOppositeKey(
            @Nonnull DataTypeProperty dataTypeProperty)
    {
        return dataTypeProperty
                .getForeignKeysMatchingThisKey()
                .containsValue(Lists.immutable.with(this));
    }

    default boolean isForeignKeyMatchingKeyOnPath(AssociationEnd pathHere)
    {
        var opposite = pathHere.getOpposite();
        return this.getKeysMatchingThisForeignKey().containsKey(opposite);
    }
}
