package cool.klass.model.meta.domain.api.service;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.IElement;
import cool.klass.model.meta.domain.api.projection.IProjection;

public interface IServiceProjectionDispatch extends IElement
{
    @Nonnull
    IProjection getProjection();
}
