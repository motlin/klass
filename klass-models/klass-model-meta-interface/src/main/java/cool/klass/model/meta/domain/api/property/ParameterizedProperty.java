package cool.klass.model.meta.domain.api.property;

public interface ParameterizedProperty extends ReferenceProperty
{
    @Override
    default void visit(PropertyVisitor visitor)
    {
        visitor.visitParameterizedProperty(this);
    }
}
