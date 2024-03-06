package cool.klass.model.meta.domain.json.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME, property = "@type")
@JsonSubTypes(
        {
                @Type(value = ThisMemberReferencePathDTO.class, name = "klass.model.meta.domain.ThisMemberReferencePath"),
                @Type(value = TypeMemberReferencePathDTO.class, name = "klass.model.meta.domain.TypeMemberReferencePath"),
                @Type(value = VariableReferenceDTO.class, name = "klass.model.meta.domain.VariableReference"),
        })
public interface ExpressionValueDTO
{
}
