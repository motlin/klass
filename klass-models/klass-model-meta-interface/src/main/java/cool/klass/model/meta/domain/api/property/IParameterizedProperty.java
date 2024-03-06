package cool.klass.model.meta.domain.api.property;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.IKlass;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.order.IOrderBy;

// TODO: Super class for reference-type-property?
public interface IParameterizedProperty extends IProperty<IKlass>
{
    @Nonnull
    Multiplicity getMultiplicity();

    @Nonnull
    Optional<IOrderBy> getOrderBy();
}
