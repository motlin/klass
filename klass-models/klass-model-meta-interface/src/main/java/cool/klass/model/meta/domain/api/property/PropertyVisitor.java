package cool.klass.model.meta.domain.api.property;

public interface PropertyVisitor
{
    void visitPrimitiveProperty(PrimitiveProperty primitiveProperty);

    void visitEnumerationProperty(EnumerationProperty primitiveProperty);

    void visitAssociationEnd(AssociationEnd primitiveProperty);

    void visitParameterizedProperty(ParameterizedProperty parameterizedProperty);
}
