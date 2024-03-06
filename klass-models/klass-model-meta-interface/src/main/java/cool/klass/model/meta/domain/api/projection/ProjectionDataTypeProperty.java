package cool.klass.model.meta.domain.api.projection;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public interface ProjectionDataTypeProperty extends ProjectionElement
{
    @Override
    default ImmutableList<ProjectionElement> getChildren()
    {
        return Lists.immutable.empty();
    }

    @Override
    default void enter(@Nonnull ProjectionListener listener)
    {
        listener.enterProjectionDataTypeProperty(this);
    }

    @Override
    default void exit(@Nonnull ProjectionListener listener)
    {
        listener.exitProjectionDataTypeProperty(this);
    }

    @Nonnull
    String getHeaderText();

    @Nonnull
    DataTypeProperty getProperty();
}
