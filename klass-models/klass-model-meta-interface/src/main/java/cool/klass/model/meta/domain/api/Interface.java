package cool.klass.model.meta.domain.api;

import cool.klass.model.meta.domain.api.property.Property;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public interface Interface extends Classifier
{
    // TODO: Replace with an implementation that preserves order
    default ImmutableList<Property> getProperties()
    {
        return Lists.immutable.<Property>empty()
                .newWithAll(this.getDataTypeProperties());
    }
}
