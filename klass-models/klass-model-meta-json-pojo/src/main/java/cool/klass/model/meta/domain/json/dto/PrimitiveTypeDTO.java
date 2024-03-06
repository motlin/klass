package cool.klass.model.meta.domain.json.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum PrimitiveTypeDTO
{
    @JsonProperty("Integer")
    INTEGER,
    @JsonProperty("Long")
    LONG,
    @JsonProperty("Double")
    DOUBLE,
    @JsonProperty("Float")
    FLOAT,
    @JsonProperty("Boolean")
    BOOLEAN,
    @JsonProperty("String")
    STRING,
    @JsonProperty("Instant")
    INSTANT,
    @JsonProperty("LocalDate")
    LOCAL_DATE,
    @JsonProperty("TemporalInstant")
    TEMPORAL_INSTANT,
    @JsonProperty("TemporalRange")
    TEMPORAL_RANGE,
}
