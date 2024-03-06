package cool.klass.model.meta.domain.api.property;

public interface PropertyVisitor
{
    void visitPrimitiveProperty(PrimitiveProperty primitiveProperty);

    void visitEnumerationProperty(EnumerationProperty enumerationProperty);

    void visitAssociationEnd(AssociationEnd associationEnd);

    void visitAssociationEndSignature(AssociationEndSignature associationEndSignature);

    void visitParameterizedProperty(ParameterizedProperty parameterizedProperty);
}
