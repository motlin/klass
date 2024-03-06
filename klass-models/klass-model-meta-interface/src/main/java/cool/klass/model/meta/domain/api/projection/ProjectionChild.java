package cool.klass.model.meta.domain.api.projection;

import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.Property;

public interface ProjectionChild extends ProjectionElement
{
    Property getProperty();

    default boolean isPolymorphic()
    {
        Klass      projectionParentClass = this.getParent().get().getKlass();
        Classifier propertyOwner         = this.getProperty().getOwningClassifier();
        if (projectionParentClass == propertyOwner)
        {
            return false;
        }

        return !projectionParentClass.isStrictSubTypeOf(propertyOwner);
    }
}
