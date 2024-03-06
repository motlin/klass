package cool.klass.model.meta.domain.api;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.property.Property;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public interface Interface
        extends Classifier
{
    @Override
    default void visit(TopLevelElementVisitor visitor)
    {
        visitor.visitInterface(this);
    }

    @Override
    default boolean isAbstract()
    {
        return true;
    }

    // TODO: Replace with an implementation that preserves order
    @Override
    @Nonnull
    default ImmutableList<Property> getProperties()
    {
        return Lists.immutable.<Property>empty()
                .newWithAll(this.getDataTypeProperties());
    }

    @Override
    default ImmutableList<Property> getDeclaredProperties()
    {
        return Lists.immutable.<Property>empty()
                .newWithAll(this.getDeclaredDataTypeProperties());
    }

    @Override
    default DataTypeProperty getDataTypePropertyByName(String name)
    {
        DataTypeProperty declaredDataTypePropertyByName = this.getDeclaredDataTypePropertyByName(name);
        if (declaredDataTypePropertyByName != null)
        {
            return declaredDataTypePropertyByName;
        }

        return this.getInterfaces()
                .asLazy()
                .collectWith(Interface::getDataTypePropertyByName, name)
                .detect(Objects::nonNull);
    }
}
