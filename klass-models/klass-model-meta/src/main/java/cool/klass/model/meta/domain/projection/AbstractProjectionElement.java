package cool.klass.model.meta.domain.projection;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.projection.ProjectionChild;
import cool.klass.model.meta.domain.api.projection.ProjectionElement;

public interface AbstractProjectionElement extends ProjectionElement
{
    interface ProjectionElementBuilder
    {
        @Nonnull
        ProjectionElement build();

        void build2();
    }

    interface ProjectionChildBuilder extends ProjectionElementBuilder
    {
        @Override
        @Nonnull
        ProjectionChild build();
    }
}
