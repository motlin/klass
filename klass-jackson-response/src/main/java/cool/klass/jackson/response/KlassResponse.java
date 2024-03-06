package cool.klass.jackson.response;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"_metadata", "_data"})
public class KlassResponse
{
    @Nonnull
    private final KlassResponseMetadata metadata;
    @Nullable
    private final Object                data;

    @JsonCreator
    public KlassResponse(@Nonnull KlassResponseMetadata metadata, Object data)
    {
        this.metadata = Objects.requireNonNull(metadata);
        this.data = data;

        if (metadata.getMultiplicity().isToMany() && !(data instanceof List))
        {
            throw new IllegalStateException(metadata.getCriteria().toString());
        }
    }

    @Nonnull
    @JsonProperty("_metadata")
    public KlassResponseMetadata getMetadata()
    {
        return this.metadata;
    }

    @Nullable
    @JsonProperty("_data")
    public Object getData()
    {
        return this.data;
    }

    @Override
    public String toString()
    {
        return String.format("{\"_metadata\":%s,\"_data\":%s}", this.metadata, this.data);
    }
}
