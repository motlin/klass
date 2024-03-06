package cool.klass.deserializer.json;

import java.util.Objects;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.databind.JsonNode;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.AssociationEndSignature;
import cool.klass.model.meta.domain.api.property.EnumerationProperty;
import cool.klass.model.meta.domain.api.property.ParameterizedProperty;
import cool.klass.model.meta.domain.api.property.PrimitiveProperty;
import cool.klass.model.meta.domain.api.property.PropertyVisitor;
import cool.klass.model.meta.domain.api.visitor.PrimitiveTypeVisitor;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.stack.MutableStack;

public class AssertValuesMatchPropertyVisitor
        implements PropertyVisitor
{
    @Nonnull
    private final JsonNode             jsonDataTypeValue;
    private final Object               value;
    @Nonnull
    private final String               propertyKind;
    @Nonnull
    private final MutableStack<String> contextStack;
    @Nonnull
    private final MutableList<String>  errors;

    public AssertValuesMatchPropertyVisitor(
            @Nonnull JsonNode jsonDataTypeValue,
            Object value,
            @Nonnull String propertyKind,
            @Nonnull MutableStack<String> contextStack,
            @Nonnull MutableList<String> errors)
    {
        this.jsonDataTypeValue = Objects.requireNonNull(jsonDataTypeValue);
        this.value             = value;
        this.propertyKind      = Objects.requireNonNull(propertyKind);
        this.contextStack      = Objects.requireNonNull(contextStack);
        this.errors            = Objects.requireNonNull(errors);
    }

    @Override
    public void visitPrimitiveProperty(@Nonnull PrimitiveProperty primitiveProperty)
    {
        PrimitiveTypeVisitor visitor = new AssertValuesMatchPrimitiveTypeVisitor(
                primitiveProperty,
                this.jsonDataTypeValue,
                this.value,
                this.propertyKind,
                this.contextStack,
                this.errors);
        primitiveProperty.getType().visit(visitor);
    }

    @Override
    public void visitEnumerationProperty(@Nonnull EnumerationProperty enumerationProperty)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitEnumerationProperty() not implemented yet");
    }

    @Override
    public void visitAssociationEndSignature(AssociationEndSignature associationEndSignature)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitAssociationEndSignature() not implemented yet");
    }

    @Override
    public void visitAssociationEnd(@Nonnull AssociationEnd associationEnd)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitAssociationEnd() not implemented yet");
    }

    @Override
    public void visitParameterizedProperty(@Nonnull ParameterizedProperty parameterizedProperty)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitParameterizedProperty() not implemented yet");
    }
}
