package cool.klass.model.meta.domain.api.projection;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.INamedElement;
import cool.klass.model.meta.domain.api.property.IDataTypeProperty;
import org.eclipse.collections.api.list.ImmutableList;

public interface IProjectionDataTypeProperty extends INamedElement, ProjectionElement
{
    @Override
    ImmutableList<ProjectionElement> getChildren();

    @Override
    default void enter(ProjectionListener listener)
    {
        listener.enterProjectionDataTypeProperty(this);
    }

    @Override
    default void exit(ProjectionListener listener)
    {
        listener.exitProjectionDataTypeProperty(this);
    }

    @Nonnull
    String getHeaderText();

    @Nonnull
    IDataTypeProperty<?> getProperty();
}
