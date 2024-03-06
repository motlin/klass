package cool.klass.model.meta.domain.api.service.url;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.DataType;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.NamedElement;

public interface UrlParameter extends NamedElement
{
    @Nonnull
    DataType getType();

    @Nonnull
    Multiplicity getMultiplicity();
}
