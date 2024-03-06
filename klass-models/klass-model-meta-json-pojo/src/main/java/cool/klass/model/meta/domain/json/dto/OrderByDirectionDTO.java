package cool.klass.model.meta.domain.json.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum OrderByDirectionDTO
{
    @JsonProperty("ascending")
    ASCENDING,
    @JsonProperty("descending")
    DESCENDING,
}
