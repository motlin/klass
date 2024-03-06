package cool.klass.model.meta.domain.api.property;

import javax.annotation.Nonnull;

public interface ParameterizedProperty extends ReferenceProperty
{
    @Override
    default void visit(@Nonnull PropertyVisitor visitor)
    {
        visitor.visitParameterizedProperty(this);
    }
}
