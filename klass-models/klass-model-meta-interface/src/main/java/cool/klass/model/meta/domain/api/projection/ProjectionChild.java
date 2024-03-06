package cool.klass.model.meta.domain.api.projection;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.property.Property;

public interface ProjectionChild
        extends ProjectionElement
{
    @Nonnull
    Property getProperty();

    default boolean isPolymorphic()
    {
        Classifier projectionParentClassifier = this.getParent().get().getClassifier();
        Classifier propertyOwner              = this.getProperty().getOwningClassifier();
        if (projectionParentClassifier == propertyOwner)
        {
            return false;
        }

        return !projectionParentClassifier.isStrictSubTypeOf(propertyOwner);
    }
}
