package cool.klass.jackson.response;

import java.util.Objects;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class KlassResponseWithPagination
{
    @Nonnull
    private final Object                              data;
    @Nonnull
    private final KlassResponseMetadataWithPagination metadata;

    @JsonCreator
    public KlassResponseWithPagination(@Nonnull Object data, @Nonnull KlassResponseMetadataWithPagination metadata)
    {
        this.metadata = Objects.requireNonNull(metadata);
        this.data = Objects.requireNonNull(data);
    }

    @Nonnull
    @JsonProperty("_metadata")
    public KlassResponseMetadataWithPagination getMetadata()
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
