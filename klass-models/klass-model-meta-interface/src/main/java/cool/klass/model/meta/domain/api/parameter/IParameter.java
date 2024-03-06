package cool.klass.model.meta.domain.api.parameter;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.DataType;
import cool.klass.model.meta.domain.api.INamedElement;
import cool.klass.model.meta.domain.api.Multiplicity;

public interface IParameter extends INamedElement
{
    @Nonnull
    DataType getType();

    @Nonnull
    Multiplicity getMultiplicity();
}
