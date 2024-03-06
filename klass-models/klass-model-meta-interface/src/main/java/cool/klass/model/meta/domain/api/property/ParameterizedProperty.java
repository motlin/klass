package cool.klass.model.meta.domain.api.property;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Klass;

public interface ParameterizedProperty extends ReferenceProperty
{
    @Nonnull
    @Override
    Klass getOwningClassifier();

    @Override
    default void visit(@Nonnull PropertyVisitor visitor)
    {
        visitor.visitParameterizedProperty(this);
    }
}
