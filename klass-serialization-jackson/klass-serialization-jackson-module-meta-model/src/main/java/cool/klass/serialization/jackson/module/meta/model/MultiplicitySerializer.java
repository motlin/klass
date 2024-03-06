package cool.klass.serialization.jackson.module.meta.model;

import java.io.IOException;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import cool.klass.model.meta.domain.api.Multiplicity;

public class MultiplicitySerializer extends JsonSerializer<Multiplicity>
{
    @Override
    public void serialize(
            @Nonnull Multiplicity multiplicity,
            @Nonnull JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider) throws IOException
    {
        jsonGenerator.writeString(multiplicity.getPrettyName());
    }
}
