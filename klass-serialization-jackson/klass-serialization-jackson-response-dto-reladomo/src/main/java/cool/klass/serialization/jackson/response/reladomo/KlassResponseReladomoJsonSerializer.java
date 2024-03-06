package cool.klass.serialization.jackson.response.reladomo;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.gs.fw.common.mithra.MithraObject;
import cool.klass.data.store.DataStore;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.serialization.jackson.response.KlassResponse;
import cool.klass.serialization.jackson.response.KlassResponseMetadata;

// TODO: Split into one for non-null lists and one for nullable MithraObjects
public class KlassResponseReladomoJsonSerializer
        extends JsonSerializer<KlassResponse>
{
    @Nonnull
    private final DomainModel domainModel;
    @Nonnull
    private final DataStore   dataStore;

    public KlassResponseReladomoJsonSerializer(
            @Nonnull DomainModel domainModel,
            @Nonnull DataStore dataStore)
    {
        this.domainModel = Objects.requireNonNull(domainModel);
        this.dataStore   = Objects.requireNonNull(dataStore);
    }

    @Override
    public void serialize(
            @Nonnull KlassResponse klassResponse,
            @Nonnull JsonGenerator jsonGenerator,
            @Nonnull SerializerProvider serializerProvider) throws IOException
    {
        jsonGenerator.writeStartObject();

        try
        {
            KlassResponseMetadata metadata = klassResponse.getMetadata();
            jsonGenerator.writeObjectField("_metadata", metadata);
            this.serializeData(klassResponse, jsonGenerator, serializerProvider);
        }
        finally
        {
            jsonGenerator.writeEndObject();
        }
    }

    private void serializeData(
            @Nonnull KlassResponse klassResponse,
            @Nonnull JsonGenerator jsonGenerator,
            @Nonnull SerializerProvider serializerProvider) throws IOException
    {
        if (klassResponse.getMetadata().getMultiplicity().isToOne())
        {
            this.serializeDataOne(klassResponse, jsonGenerator, serializerProvider);
        }
        else
        {
            this.serializeDataMany(klassResponse, jsonGenerator, serializerProvider);
        }
    }

    private void serializeDataOne(
            @Nonnull KlassResponse klassResponse,
            @Nonnull JsonGenerator jsonGenerator,
            @Nonnull SerializerProvider serializerProvider) throws IOException
    {
        MithraObject mithraObject = (MithraObject) klassResponse.getData();

        if (mithraObject == null)
        {
            jsonGenerator.writeNullField("_data");
            return;
        }

        jsonGenerator.writeFieldName("_data");
        ReladomoContextJsonSerializer reladomoJsonSerializer = this.getReladomoContextJsonSerializer(klassResponse);
        reladomoJsonSerializer.serialize(mithraObject, jsonGenerator, serializerProvider);
    }

    private void serializeDataMany(
            @Nonnull KlassResponse klassResponse,
            @Nonnull JsonGenerator jsonGenerator,
            @Nonnull SerializerProvider serializerProvider) throws IOException
    {
        Object data = klassResponse.getData();
        if (!(data instanceof List<?>))
        {
            throw new ClassCastException(data.getClass().getCanonicalName()
                    + " cannot be cast to "
                    + List.class.getCanonicalName());
        }

        List<MithraObject> mithraList = (List<MithraObject>) data;
        jsonGenerator.writeArrayFieldStart("_data");
        try
        {
            ReladomoContextJsonSerializer reladomoJsonSerializer = this.getReladomoContextJsonSerializer(klassResponse);
            for (MithraObject eachMithraObject : mithraList)
            {
                reladomoJsonSerializer.serialize(
                        eachMithraObject,
                        jsonGenerator,
                        serializerProvider);
            }
        }
        finally
        {
            jsonGenerator.writeEndArray();
        }
    }

    @Nonnull
    private ReladomoContextJsonSerializer getReladomoContextJsonSerializer(@Nonnull KlassResponse klassResponse)
    {
        KlassResponseMetadata metadata = klassResponse.getMetadata();

        return new ReladomoContextJsonSerializer(
                this.domainModel,
                this.dataStore,
                metadata);
    }
}
