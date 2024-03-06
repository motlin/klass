package cool.klass.model.meta.domain.api.service;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.projection.Projection;

public interface ServiceProjectionDispatch
        extends Element
{
    @Nonnull
    Projection getProjection();
}
