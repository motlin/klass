package cool.klass.serialization.jackson.response.reladomo;

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
import cool.klass.model.meta.domain.api.projection.Projection;
import cool.klass.model.meta.domain.api.projection.ProjectionChild;
import cool.klass.model.meta.domain.api.projection.ProjectionDataTypeProperty;
import cool.klass.model.meta.domain.api.projection.ProjectionElement;
import cool.klass.model.meta.domain.api.projection.ProjectionParent;
import cool.klass.model.meta.domain.api.projection.ProjectionWithAssociationEnd;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.visitor.PrimitiveTypeVisitor;
import cool.klass.serialization.jackson.jsonview.KlassJsonView;
import cool.klass.serialization.jackson.model.data.property.SerializeValueToJsonFieldPrimitiveTypeVisitor;
import cool.klass.serialization.jackson.response.KlassResponseMetadata;
import org.eclipse.collections.api.list.ImmutableList;

public class ReladomoContextJsonSerializer extends JsonSerializer<MithraObject>
{
    @Nonnull
    private final DataStore             dataStore;
    @Nonnull
    private final KlassResponseMetadata metadata;

    public ReladomoContextJsonSerializer(
            @Nonnull DataStore dataStore,
            KlassResponseMetadata metadata)
    {
        this.dataStore = Objects.requireNonNull(dataStore);
        this.metadata  = Objects.requireNonNull(metadata);
    }

    @Override
    public void serialize(
            @Nonnull MithraObject mithraObject,
            @Nonnull JsonGenerator jsonGenerator,
            @Nonnull SerializerProvider serializers) throws IOException
    {
        Class<?> activeViewClass = serializers.getActiveView();
        Objects.requireNonNull(activeViewClass);

        if (!KlassJsonView.class.isAssignableFrom(activeViewClass))
        {
            throw new IllegalStateException(activeViewClass.getCanonicalName());
        }
        KlassJsonView klassJsonView = this.instantiate(activeViewClass);
        Projection    projection    = klassJsonView.getProjection();

        // This would work if we consistently used the same DomainModel everywhere (instead of sometimes compiled and sometimes code generated).
        // Projection projection = this.domainModel.getProjections().selectInstancesOf(activeView).getOnly();
        this.serialize(mithraObject, jsonGenerator, projection);
    }

    @Nonnull
    private KlassJsonView instantiate(@Nonnull Class<?> activeViewClass)
    {
        try
        {
            return activeViewClass.asSubclass(KlassJsonView.class).newInstance();
        }
        catch (ReflectiveOperationException e)
        {
            throw new RuntimeException(e);
        }
    }

    private void serialize(
            @Nonnull MithraObject mithraObject,
            @Nonnull JsonGenerator jsonGenerator,
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
            ImmutableList<? extends ProjectionChild> children = projectionParent.getChildren();
            for (ProjectionElement projectionElement : children)
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

    private void handleProjectionPrimitiveMember(
            @Nonnull JsonGenerator jsonGenerator,
            MithraObject mithraObject,
            @Nonnull ProjectionDataTypeProperty projectionPrimitiveMember) throws IOException
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
            @Nonnull ProjectionWithAssociationEnd projectionWithAssociationEnd) throws IOException
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

            // TODO: Add configuration to disable serialization of empty lists
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
        else
        {
            Object value = this.dataStore.getToOne(mithraObject, associationEnd);
            // TODO: Add configuration to disable serialization of null values
            if (value == null)
            {
                // Should only happen for to-one optional relationships
                jsonGenerator.writeNullField(associationEndName);
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
