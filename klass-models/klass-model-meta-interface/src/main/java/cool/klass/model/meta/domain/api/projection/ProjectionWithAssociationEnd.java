package cool.klass.model.meta.domain.api.projection;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.AssociationEnd;

public interface ProjectionWithAssociationEnd extends ProjectionParent, ProjectionChild
{
    @Override
    @Nonnull
    AssociationEnd getProperty();

    @Nonnull
    @Override
    default Klass getKlass()
    {
        return this.getProperty().getType();
    }

    default boolean isLeaf()
    {
        return this.getChildren()
                .asLazy()
                .selectInstancesOf(ProjectionWithAssociationEnd.class)
                .isEmpty();
    }
}
