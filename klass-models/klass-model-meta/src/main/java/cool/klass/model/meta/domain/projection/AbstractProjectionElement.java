package cool.klass.model.meta.domain.projection;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.projection.ProjectionElement;

public interface AbstractProjectionElement extends ProjectionElement
{
    interface ProjectionElementBuilder
    {
        @Nonnull
        AbstractProjectionElement build();
    }
}
