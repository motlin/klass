package cool.klass.model.meta.domain.api.projection;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Klass;
import org.eclipse.collections.api.list.ImmutableList;

public interface ProjectionParent extends ProjectionElement
{
    @Nonnull
    Klass getKlass();

    @Override
    ImmutableList<ProjectionElement> getChildren();
}
