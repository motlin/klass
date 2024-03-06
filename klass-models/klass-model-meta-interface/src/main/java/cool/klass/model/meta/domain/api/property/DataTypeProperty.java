package cool.klass.model.meta.domain.api.property;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.DataType;
import cool.klass.model.meta.domain.api.modifier.DataTypePropertyModifier;
import cool.klass.model.meta.domain.api.property.validation.MaxLengthPropertyValidation;
import cool.klass.model.meta.domain.api.property.validation.MaxPropertyValidation;
import cool.klass.model.meta.domain.api.property.validation.MinLengthPropertyValidation;
import cool.klass.model.meta.domain.api.property.validation.MinPropertyValidation;
import cool.klass.model.meta.domain.api.visitor.DataTypePropertyVisitor;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.multimap.list.ImmutableListMultimap;

public interface DataTypeProperty extends Property
{
    void visit(@Nonnull DataTypePropertyVisitor visitor);

    @Nonnull
    @Override
    DataType getType();

    @Nonnull
    ImmutableList<DataTypePropertyModifier> getPropertyModifiers();

    Optional<MinLengthPropertyValidation> getMinLengthPropertyValidation();

    Optional<MaxLengthPropertyValidation> getMaxLengthPropertyValidation();

    Optional<MinPropertyValidation> getMinPropertyValidation();

    Optional<MaxPropertyValidation> getMaxPropertyValidation();

    // TODO: Should this be a Map, rather than multimap?
    ImmutableListMultimap<AssociationEnd, DataTypeProperty> getKeysMatchingThisForeignKey();

    ImmutableListMultimap<AssociationEnd, DataTypeProperty> getForeignKeysMatchingThisKey();

    default boolean isKey()
    {
        return this.getPropertyModifiers().anySatisfy(DataTypePropertyModifier::isKey);
    }

    boolean isID();

    default boolean isAudit()
    {
        return this.getPropertyModifiers().anySatisfy(DataTypePropertyModifier::isAudit);
    }

    default boolean isCreatedBy()
    {
        return this.getPropertyModifiers().anySatisfy(DataTypePropertyModifier::isCreatedBy);
    }

    default boolean isCreatedOn()
    {
        return this.getPropertyModifiers().anySatisfy(DataTypePropertyModifier::isCreatedOn);
    }

    default boolean isLastUpdatedBy()
    {
        return this.getPropertyModifiers().anySatisfy(DataTypePropertyModifier::isLastUpdatedBy);
    }

    default boolean isValid()
    {
        return this.getPropertyModifiers().anySatisfy(DataTypePropertyModifier::isValid);
    }

    default boolean isSystem()
    {
        return this.getPropertyModifiers().anySatisfy(DataTypePropertyModifier::isSystem);
    }

    default boolean isFrom()
    {
        return this.getPropertyModifiers().anySatisfy(DataTypePropertyModifier::isFrom);
    }

    default boolean isTo()
    {
        return this.getPropertyModifiers().anySatisfy(DataTypePropertyModifier::isTo);
    }

    default boolean isFinal()
    {
        return this.getPropertyModifiers().anySatisfy(DataTypePropertyModifier::isFinal);
    }

    default boolean isPrivate()
    {
        return this.getPropertyModifiers().anySatisfy(DataTypePropertyModifier::isPrivate);
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

    default boolean isDerived()
    {
        return this.getPropertyModifiers().anySatisfy(DataTypePropertyModifier::isDerived);
    }
}
