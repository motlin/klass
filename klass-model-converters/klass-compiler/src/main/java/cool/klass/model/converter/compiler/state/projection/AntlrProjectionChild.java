package cool.klass.model.converter.compiler.state.projection;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.projection.AbstractProjectionElement.ProjectionChildBuilder;

public interface AntlrProjectionChild
        extends AntlrProjectionElement
{
    @Nonnull
    @Override
    ProjectionChildBuilder build();
}
