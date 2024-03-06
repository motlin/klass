package cool.klass.model.meta.domain.json.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME, property = "__typename")
@JsonSubTypes({
        @Type(value = AndCriteriaDTO.class, name = "klass.model.meta.domain.AndCriteria"),
        @Type(value = OrCriteriaDTO.class, name = "klass.model.meta.domain.OrCriteria"),
        @Type(value = OperatorCriteriaDTO.class, name = "klass.model.meta.domain.OperatorCriteria"),
        @Type(value = AllCriteriaDTO.class, name = "klass.model.meta.domain.AllCriteria"),
})
public interface CriteriaDTO
{
}
