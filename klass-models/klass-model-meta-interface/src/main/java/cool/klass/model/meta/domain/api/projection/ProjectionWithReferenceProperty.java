package cool.klass.model.meta.domain.api.projection;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.property.ReferenceProperty;

public interface ProjectionWithReferenceProperty
        extends ProjectionParent, ProjectionChild
{
    @Override
    @Nonnull
    ReferenceProperty getProperty();

    @Nonnull
    @Override
    default Classifier getClassifier()
    {
        return this.getProperty().getType();
    }

    default boolean isLeaf()
    {
        return this.getChildren()
                .asLazy()
                .selectInstancesOf(ProjectionWithReferenceProperty.class)
                .isEmpty();
    }
}
