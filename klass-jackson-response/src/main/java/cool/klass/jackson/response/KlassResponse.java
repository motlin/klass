package cool.klass.jackson.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class KlassResponse
{
    private final KlassMetadata metadata;
    private final Object        data;

    @JsonCreator
    public KlassResponse(Object data, KlassMetadata metadata)
    {
        this.metadata = metadata;
        this.data = data;
    }

    @JsonProperty("_metadata")
    public KlassMetadata getMetadata()
    {
        return this.metadata;
    }

    @JsonProperty("_data")
    public Object getData()
    {
        return this.data;
    }
}
