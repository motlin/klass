package cool.klass.model.meta.domain.projection;

import cool.klass.model.meta.domain.api.projection.ProjectionElement;

public interface AbstractProjectionElement extends ProjectionElement
{
    interface ProjectionElementBuilder
    {
        AbstractProjectionElement build();
    }
}
