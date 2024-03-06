package cool.klass.serializer.json;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializable;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.gs.fw.common.mithra.MithraList;
import com.gs.fw.common.mithra.MithraObject;
import com.gs.fw.common.mithra.attribute.Attribute;
import com.gs.fw.common.mithra.attribute.TimestampAttribute;
import com.gs.fw.common.mithra.finder.AbstractRelatedFinder;
import com.gs.fw.common.mithra.finder.RelatedFinder;
import cool.klass.model.meta.domain.DataType;
import cool.klass.model.meta.domain.Enumeration;
import cool.klass.model.meta.domain.Multiplicity;
import cool.klass.model.meta.domain.projection.ProjectionAssociationEnd;
import cool.klass.model.meta.domain.projection.ProjectionDataTypeProperty;
import cool.klass.model.meta.domain.projection.ProjectionElement;
import cool.klass.model.meta.domain.property.AssociationEnd;
import cool.klass.model.meta.domain.property.DataTypeProperty;
import cool.klass.model.meta.domain.property.PrimitiveType;
import cool.klass.model.meta.domain.visitor.PrimitiveTypeVisitor;
import org.eclipse.collections.api.list.ImmutableList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReladomoJsonTree implements JsonSerializable
{
    private static final Logger LOGGER = LoggerFactory.getLogger(ReladomoJsonTree.class);

    private final MithraObject                     mithraObject;
    private final ImmutableList<ProjectionElement> projectionElements;

    public ReladomoJsonTree(
            MithraObject mithraObject,
            @Nonnull ImmutableList<ProjectionElement> projectionElements)
    {
        this.mithraObject = Objects.requireNonNull(mithraObject);
        this.projectionElements = Objects.requireNonNull(projectionElements);
        if (projectionElements.isEmpty())
        {
            throw new AssertionError();
        }
    }

    public static void serialize(
            JsonGenerator jsonGenerator,
            MithraObject mithraObject,
            @Nonnull ImmutableList<ProjectionElement> projectionElements) throws IOException
    {
        RelatedFinder finder = mithraObject.zGetPortal().getFinder();

        jsonGenerator.writeStartObject();
        try
        {
            // TODO: Use listener?
            for (ProjectionElement projectionElement : projectionElements)
            {
                if (projectionElement instanceof ProjectionDataTypeProperty)
                {
                    handleProjectionPrimitiveMember(
                            jsonGenerator,
                            mithraObject,
                            finder,
                            (ProjectionDataTypeProperty) projectionElement);
                }
                else if (projectionElement instanceof ProjectionAssociationEnd)
                {
                    handleProjectionAssociationEnd(
                            jsonGenerator,
                            mithraObject,
                            finder,
                            (ProjectionAssociationEnd) projectionElement);
                }
                else
                {
                    throw new AssertionError(projectionElement.getClass().getSimpleName());
                }
            }
        }
        catch (RuntimeException e)
        {
            LOGGER.info("", e);
            throw e;
        }
        finally
        {
            jsonGenerator.writeEndObject();
        }
    }

    public static void handleProjectionPrimitiveMember(
            @Nonnull JsonGenerator jsonGenerator,
            MithraObject mithraObject,
            @Nonnull RelatedFinder finder,
            ProjectionDataTypeProperty projectionPrimitiveMember) throws IOException
    {
        DataTypeProperty<?> property     = projectionPrimitiveMember.getProperty();
        String              propertyName = property.getName();
        DataType            dataType     = property.getType();

        Object dataTypeValue = ReladomoJsonTree.getDataTypeValue(mithraObject, finder, property);
        if (dataTypeValue == null)
        {
            jsonGenerator.writeNullField(propertyName);
        }
        else if (dataType instanceof Enumeration)
        {
            jsonGenerator.writeStringField(propertyName, (String) dataTypeValue);
        }
        else
        {
            PrimitiveType primitiveType = (PrimitiveType) dataType;
            PrimitiveTypeVisitor visitor =
                    new SerializeValueToJsonFieldPrimitiveTypeVisitor(jsonGenerator, propertyName, dataTypeValue);
            primitiveType.visit(visitor);
        }
    }

    public static void handleProjectionAssociationEnd(
            @Nonnull JsonGenerator jsonGenerator,
            MithraObject mithraObject,
            RelatedFinder finder,
            ProjectionAssociationEnd projectionAssociationEnd) throws IOException
    {
        ImmutableList<ProjectionElement> children = projectionAssociationEnd.getChildren();
        AssociationEnd                 associationEnd            = projectionAssociationEnd.getAssociationEnd();
        Multiplicity                   multiplicity              = associationEnd.getMultiplicity();

        String associationEndName = associationEnd.getName();

        AbstractRelatedFinder relationshipFinder =
                (AbstractRelatedFinder) finder.getRelationshipFinderByName(associationEndName);
        Object value = relationshipFinder.valueOf(mithraObject);
        if (value == null)
        {
            // Should only happen for to-one optional relationships
            return;
        }

        if (multiplicity.isToMany())
        {
            MithraList<MithraObject> mithraList = (MithraList<MithraObject>) Objects.requireNonNull(value);

            jsonGenerator.writeArrayFieldStart(associationEndName);
            try
            {
                mithraList.forEachWithCursor(eachChildValue ->
                        recurse(jsonGenerator, children, (MithraObject) eachChildValue));
            }
            finally
            {
                jsonGenerator.writeEndArray();
            }
        }
        else
        {
            jsonGenerator.writeFieldName(associationEndName);
            recurse(jsonGenerator, children, (MithraObject) value);
        }
    }

    public static boolean recurse(
            @Nonnull JsonGenerator jsonGenerator,
            @Nonnull ImmutableList<ProjectionElement> children,
            @Nonnull MithraObject eachChildValue)
    {
        try
        {
            ReladomoJsonTree.serialize(jsonGenerator, eachChildValue, children);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
        return true;
    }

    // TODO: Move this generic serialization logic to an abstract Reladomo serializer
    public static Object getDataTypeValue(
            MithraObject mithraObject,
            RelatedFinder finder,
            DataTypeProperty<?> dataTypeProperty)
    {
        Attribute attribute = finder.getAttributeByName(dataTypeProperty.getName());

        if (attribute == null)
        {
            throw new AssertionError(
                    "Domain model and generated code are out of sync. Try rerunning a full clean build.");
        }

        if (dataTypeProperty.isOptional() && attribute.isAttributeNull(mithraObject))
        {
            return null;
        }

        Object  result     = attribute.valueOf(mithraObject);
        boolean isTemporal = dataTypeProperty.isTemporal();
        if (isTemporal || dataTypeProperty.getType() == PrimitiveType.INSTANT)
        {
            Instant            instant            = ((Date) result).toInstant();
            TimestampAttribute timestampAttribute = (TimestampAttribute) attribute;
            if (isTemporal)
            {
                Timestamp infinity        = timestampAttribute.getAsOfAttributeInfinity();
                Instant   infinityInstant = infinity.toInstant();
                if (instant.equals(infinityInstant))
                {
                    return null;
                }
            }

            // TODO: Consider handling here the case where validTo == systemTo + 1 day, but really means infinity
            // TODO: Alternately, just enable future dated rows to turn off this optimization

            return instant.toString();
        }
        if (dataTypeProperty.getType() == PrimitiveType.LOCAL_DATE)
        {
            return ((java.sql.Date) result).toLocalDate();
        }
        return result;
    }

    @Override
    public void serialize(@Nonnull JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
    {
        try
        {
            ReladomoJsonTree.serialize(jsonGenerator, this.mithraObject, this.projectionElements);
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
