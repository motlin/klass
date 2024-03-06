package cool.klass.serialization.jackson.module.meta.model.domain;

import java.io.IOException;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import cool.klass.model.meta.domain.api.projection.Projection;

public class ProjectionSerializer extends JsonSerializer<Projection>
{
    @Override
    public void serialize(
            @Nonnull Projection projection,
            @Nonnull JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider) throws IOException
    {
        jsonGenerator.writeString(projection.getFullyQualifiedName());
    }
}
