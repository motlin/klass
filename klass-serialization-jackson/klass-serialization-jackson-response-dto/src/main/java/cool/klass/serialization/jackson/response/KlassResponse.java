package cool.klass.serialization.jackson.response;

import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.liftwizard.logging.slf4j.mdc.MultiMDCCloseable;

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
        this.data     = data;

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

    public void withMDC(MultiMDCCloseable mdc)
    {
        this.metadata.withMDC(mdc);

        if (this.data instanceof List)
        {
            List<?> list = (List<?>) this.data;
            int     size = list.size();
            mdc.put("klass.response.data.size", String.valueOf(size));
        }
        else if (this.data != null)
        {
            mdc.put("klass.response.data.type", this.data.getClass().getCanonicalName());
        }
    }
}
