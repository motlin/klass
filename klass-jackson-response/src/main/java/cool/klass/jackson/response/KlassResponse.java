package cool.klass.jackson.response;

import java.util.Objects;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"_metadata", "_data"})
public class KlassResponse
{
    @Nonnull
    private final KlassResponseMetadata metadata;
    @Nonnull
    private final Object                data;

    @JsonCreator
    public KlassResponse(@Nonnull Object data, @Nonnull KlassResponseMetadata metadata)
    {
        this.metadata = Objects.requireNonNull(metadata);
        this.data = Objects.requireNonNull(data);
    }

    @Nonnull
    @JsonProperty("_metadata")
    public KlassResponseMetadata getMetadata()
    {
        return this.metadata;
    }

    @Nonnull
    @JsonProperty("_data")
    public Object getData()
    {
        return this.data;
    }
}
