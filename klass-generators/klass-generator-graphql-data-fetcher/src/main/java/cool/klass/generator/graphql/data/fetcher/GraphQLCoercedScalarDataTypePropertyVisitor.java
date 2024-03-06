package cool.klass.generator.graphql.data.fetcher;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.AssociationEndSignature;
import cool.klass.model.meta.domain.api.property.EnumerationProperty;
import cool.klass.model.meta.domain.api.property.ParameterizedProperty;
import cool.klass.model.meta.domain.api.property.PrimitiveProperty;
import cool.klass.model.meta.domain.api.property.PropertyVisitor;

// TODO fix this for types coerced from Strings like Instant and LocalDate
public final class GraphQLCoercedScalarDataTypePropertyVisitor
        implements PropertyVisitor
{
    private final String propertyName;

    private String sourceCode;

    public GraphQLCoercedScalarDataTypePropertyVisitor(String propertyName)
    {
        this.propertyName = Objects.requireNonNull(propertyName);
    }

    public String getSourceCode()
    {
        return this.sourceCode;
    }

    @Override
    public void visitPrimitiveProperty(@Nonnull PrimitiveProperty primitiveProperty)
    {
        PrimitiveType primitiveType = primitiveProperty.getType();
        var           visitor       = new GraphCoercedQLScalarPrimitiveTypeVisitor(this.propertyName);
        primitiveType.visit(visitor);
        this.sourceCode = visitor.getSourceCode();
    }

    @Override
    public void visitEnumerationProperty(EnumerationProperty enumerationProperty)
    {
        this.sourceCode = this.propertyName;
    }

    @Override
    public void visitAssociationEndSignature(AssociationEndSignature associationEndSignature)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitAssociationEndSignature() not implemented yet");
    }

    @Override
    public void visitAssociationEnd(AssociationEnd associationEnd)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitAssociationEnd() not implemented yet");
    }

    @Override
    public void visitParameterizedProperty(ParameterizedProperty parameterizedProperty)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitParameterizedProperty() not implemented yet");
    }
}
