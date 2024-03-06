package cool.klass.serializer.json;

import java.io.IOException;
import java.util.Objects;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
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
import cool.klass.model.meta.domain.api.projection.ProjectionChild;
import cool.klass.model.meta.domain.api.projection.ProjectionDataTypeProperty;
import cool.klass.model.meta.domain.api.projection.ProjectionElement;
import cool.klass.model.meta.domain.api.projection.ProjectionWithAssociationEnd;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.visitor.PrimitiveTypeVisitor;
import org.eclipse.collections.api.list.ImmutableList;

// TODO: Refactor this to use DataStore
public class ReladomoJsonTree implements JsonSerializable
{
    private final DataStore    dataStore;
    private final MithraObject mithraObject;

    private final ImmutableList<? extends ProjectionElement> projectionElements;

    public ReladomoJsonTree(
            DataStore dataStore,
            MithraObject mithraObject,
            @Nonnull ImmutableList<? extends ProjectionElement> projectionElements)
    {
        this.dataStore = dataStore;
        this.mithraObject = Objects.requireNonNull(mithraObject);
        this.projectionElements = Objects.requireNonNull(projectionElements);
        if (projectionElements.isEmpty())
        {
            throw new AssertionError();
        }
    }

    public void serialize(
            JsonGenerator jsonGenerator,
            MithraObject mithraObject,
            @Nonnull ImmutableList<? extends ProjectionElement> projectionElements) throws IOException
    {
        jsonGenerator.writeStartObject();
        try
        {
            // TODO: Use listener?
            for (ProjectionElement projectionElement : projectionElements)
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
            jsonGenerator.writeNullField(propertyName);
        }
        else if (dataType instanceof Enumeration)
        {
            EnumerationLiteral enumerationLiteral = (EnumerationLiteral) dataTypeValue;
            jsonGenerator.writeStringField(propertyName, enumerationLiteral.getPrettyName());
        }
        else
        {
            PrimitiveType primitiveType = (PrimitiveType) dataType;
            PrimitiveTypeVisitor visitor =
                    new SerializeValueToJsonFieldPrimitiveTypeVisitor(jsonGenerator, propertyName, dataTypeValue);
            primitiveType.visit(visitor);
        }
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

        ImmutableList<? extends ProjectionChild> children = projectionWithAssociationEnd.getChildren();
        if (multiplicity.isToMany())
        {
            Object                   value      = this.dataStore.getToMany(mithraObject, associationEnd);
            MithraList<MithraObject> mithraList = (MithraList<MithraObject>) Objects.requireNonNull(value);

            jsonGenerator.writeArrayFieldStart(associationEndName);
            try
            {
                mithraList.forEachWithCursor(eachChildValue ->
                        this.recurse(jsonGenerator, children, (MithraObject) eachChildValue));
            }
            finally
            {
                jsonGenerator.writeEndArray();
            }
        }
        else
        {
            Object value = this.dataStore.getToOne(mithraObject, associationEnd);
            if (value == null)
            {
                // Should only happen for to-one optional relationships
                return;
            }

            jsonGenerator.writeFieldName(associationEndName);
            this.recurse(jsonGenerator, children, (MithraObject) value);
        }
    }

    public boolean recurse(
            @Nonnull JsonGenerator jsonGenerator,
            @Nonnull ImmutableList<? extends ProjectionElement> children,
            @Nonnull MithraObject eachChildValue)
    {
        try
        {
            this.serialize(jsonGenerator, eachChildValue, children);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        return true;
    }

    @Override
    public void serialize(@Nonnull JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
    {
        try
        {
            this.serialize(jsonGenerator, this.mithraObject, this.projectionElements);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void serializeWithType(
            JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider,
            TypeSerializer typeSer)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".serializeWithType() not implemented yet");
    }
}
