package cool.klass.model.meta.domain.api.source.service;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.service.ServiceProjectionDispatch;
import cool.klass.model.meta.domain.api.source.ElementWithSourceCode;
import cool.klass.model.meta.domain.api.source.projection.ProjectionWithSourceCode;
import cool.klass.model.meta.grammar.KlassParser.ServiceProjectionDispatchContext;

public interface ServiceProjectionDispatchWithSourceCode
        extends ServiceProjectionDispatch, ElementWithSourceCode
{
    @Override
    ServiceProjectionDispatchContext getElementContext();

    @Nonnull
    @Override
    ProjectionWithSourceCode getProjection();
}
