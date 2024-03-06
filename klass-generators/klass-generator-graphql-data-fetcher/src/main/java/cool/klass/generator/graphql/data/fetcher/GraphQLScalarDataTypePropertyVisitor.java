package cool.klass.generator.graphql.data.fetcher;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.AssociationEndSignature;
import cool.klass.model.meta.domain.api.property.EnumerationProperty;
import cool.klass.model.meta.domain.api.property.ParameterizedProperty;
import cool.klass.model.meta.domain.api.property.PrimitiveProperty;
import cool.klass.model.meta.domain.api.property.PropertyVisitor;

// TODO fix this for types coerced from Strings like Instant and LocalDate
public final class GraphQLScalarDataTypePropertyVisitor
        implements PropertyVisitor
{
    private String sourceCode;

    public String getSourceCode()
    {
        return this.sourceCode;
    }

    @Override
    public void visitPrimitiveProperty(@Nonnull PrimitiveProperty primitiveProperty)
    {
        this.sourceCode = primitiveProperty.getType().getJavaClass().getSimpleName();
    }

    @Override
    public void visitEnumerationProperty(EnumerationProperty enumerationProperty)
    {
        this.sourceCode = "String";
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
