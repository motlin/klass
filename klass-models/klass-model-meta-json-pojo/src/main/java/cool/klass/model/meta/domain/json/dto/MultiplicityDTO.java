package cool.klass.model.meta.domain.json.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum MultiplicityDTO
{
    @JsonProperty("0..1")
    ZERO_TO_ONE,
    @JsonProperty("1..1")
    ONE_TO_ONE,
    @JsonProperty("0..*")
    ZERO_TO_MANY,
    @JsonProperty("1..*")
    ONE_TO_MANY,
}
