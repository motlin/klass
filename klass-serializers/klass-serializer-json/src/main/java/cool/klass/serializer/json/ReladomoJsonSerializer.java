package cool.klass.serializer.json;

import java.io.IOException;
import java.util.Objects;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.gs.fw.common.mithra.MithraList;
import com.gs.fw.common.mithra.MithraObject;
import cool.klass.data.store.DataStore;
import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.DataType;
import cool.klass.model.meta.domain.api.Enumeration;
import cool.klass.model.meta.domain.api.EnumerationLiteral;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.domain.api.projection.ProjectionDataTypeProperty;
import cool.klass.model.meta.domain.api.projection.ProjectionElement;
import cool.klass.model.meta.domain.api.projection.ProjectionParent;
import cool.klass.model.meta.domain.api.projection.ProjectionWithAssociationEnd;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.visitor.PrimitiveTypeVisitor;

public class ReladomoJsonSerializer extends JsonSerializer<MithraObject>
{
    private final DataStore        dataStore;
    private final ProjectionParent projectionParent;

    public ReladomoJsonSerializer(
            DataStore dataStore,
            ProjectionParent projectionParent)
    {
        this.dataStore = Objects.requireNonNull(dataStore);
        this.projectionParent = Objects.requireNonNull(projectionParent);
        if (projectionParent.getChildren().isEmpty())
        {
            throw new AssertionError();
        }
    }

    @Override
    public void serialize(
            MithraObject mithraObject,
            JsonGenerator jsonGenerator,
            SerializerProvider serializers) throws IOException
    {
        this.serialize(mithraObject, jsonGenerator, this.projectionParent);
    }

    public void serialize(
            MithraObject mithraObject,
            JsonGenerator jsonGenerator,
            @Nonnull ProjectionParent projectionParent) throws IOException
    {
        jsonGenerator.writeStartObject();
        try
        {
            if (projectionParent.hasPolymorphicChildren())
            {
                jsonGenerator.writeStringField("_type", mithraObject.getClass().getSimpleName());
            }

            // TODO: Use listener?
            for (ProjectionElement projectionElement : projectionParent.getChildren())
            {
                if (projectionElement instanceof ProjectionDataTypeProperty)
                {
                    this.handleProjectionPrimitiveMember(
                            jsonGenerator,
                            mithraObject,
                            (ProjectionDataTypeProperty) projectionElement);
                }
                else if (projectionElement instanceof ProjectionWithAssociationEnd)
                {
                    this.handleProjectionWithAssociationEnd(
                            jsonGenerator,
                            mithraObject,
                            (ProjectionWithAssociationEnd) projectionElement);
                }
                else
                {
                    throw new AssertionError(projectionElement.getClass().getSimpleName());
                }
            }
        }
        finally
        {
            jsonGenerator.writeEndObject();
        }
    }

    public void handleProjectionPrimitiveMember(
            @Nonnull JsonGenerator jsonGenerator,
            MithraObject mithraObject,
            ProjectionDataTypeProperty projectionPrimitiveMember) throws IOException
    {
        if (projectionPrimitiveMember.isPolymorphic())
        {
            Classifier classifier = projectionPrimitiveMember.getProperty().getOwningClassifier();
            if (!this.dataStore.isInstanceOf(mithraObject, classifier))
            {
                return;
            }
        }

        DataTypeProperty property     = projectionPrimitiveMember.getProperty();
        String           propertyName = property.getName();
        DataType         dataType     = property.getType();

        Object dataTypeValue = this.dataStore.getDataTypeProperty(mithraObject, property);
        if (dataTypeValue == null)
        {
            // TODO: Make this configurable
            jsonGenerator.writeNullField(propertyName);
            return;
        }

        if (dataType instanceof Enumeration)
        {
            EnumerationLiteral enumerationLiteral = (EnumerationLiteral) dataTypeValue;
            jsonGenerator.writeStringField(propertyName, enumerationLiteral.getPrettyName());
            return;
        }

        if (dataType instanceof PrimitiveType)
        {
            PrimitiveType primitiveType = (PrimitiveType) dataType;
            PrimitiveTypeVisitor visitor =
                    new SerializeValueToJsonFieldPrimitiveTypeVisitor(jsonGenerator, propertyName, dataTypeValue);
            primitiveType.visit(visitor);
            return;
        }

        throw new AssertionError();
    }

    public void handleProjectionWithAssociationEnd(
            @Nonnull JsonGenerator jsonGenerator,
            MithraObject mithraObject,
            ProjectionWithAssociationEnd projectionWithAssociationEnd) throws IOException
    {
        if (projectionWithAssociationEnd.isPolymorphic())
        {
            Klass klass = projectionWithAssociationEnd.getProperty().getOwningClassifier();
            if (!this.dataStore.isInstanceOf(mithraObject, klass))
            {
                return;
            }
        }

        AssociationEnd associationEnd     = projectionWithAssociationEnd.getProperty();
        Multiplicity   multiplicity       = associationEnd.getMultiplicity();
        String         associationEndName = associationEnd.getName();

        if (multiplicity.isToMany())
        {
            Object                   value      = this.dataStore.getToMany(mithraObject, associationEnd);
            MithraList<MithraObject> mithraList = (MithraList<MithraObject>) Objects.requireNonNull(value);

            // TODO: Make this configurable
            if (mithraList.notEmpty())
            {
                jsonGenerator.writeArrayFieldStart(associationEndName);
                try
                {
                    mithraList.forEachWithCursor(eachChildValue ->
                            this.recurse((MithraObject) eachChildValue, jsonGenerator, projectionWithAssociationEnd));
                }
                finally
                {
                    jsonGenerator.writeEndArray();
                }
            }
        }
        else
        {
            Object value = this.dataStore.getToOne(mithraObject, associationEnd);
            // TODO: Make this configurable
            if (value == null)
            {
                // Should only happen for to-one optional relationships
                return;
            }

            jsonGenerator.writeFieldName(associationEndName);
            this.recurse((MithraObject) value, jsonGenerator, projectionWithAssociationEnd);
        }
    }

    public boolean recurse(
            @Nonnull MithraObject eachChildValue,
            @Nonnull JsonGenerator jsonGenerator,
            @Nonnull ProjectionParent projectionParent)
    {
        try
        {
            this.serialize(eachChildValue, jsonGenerator, projectionParent);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        return true;
    }
}
