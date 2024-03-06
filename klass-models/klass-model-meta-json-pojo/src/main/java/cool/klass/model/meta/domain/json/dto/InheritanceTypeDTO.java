package cool.klass.model.meta.domain.json.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum InheritanceTypeDTO
{
    @JsonProperty("table-per-subclass")
    TABLE_PER_SUBCLASS,
    @JsonProperty("table-for-all-subclasses")
    TABLE_FOR_ALL_SUBCLASSES,
    @JsonProperty("table-per-class")
    TABLE_PER_CLASS,
    @JsonProperty("none")
    NONE,
}
