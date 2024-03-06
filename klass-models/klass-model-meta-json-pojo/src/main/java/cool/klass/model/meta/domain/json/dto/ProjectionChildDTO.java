package cool.klass.model.meta.domain.json.dto;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

@JsonTypeInfo(use = Id.NAME, property = "@type")
@JsonSubTypes({
        @Type(
                value = ProjectionDataTypePropertyDTO.class,
                name = "klass.model.meta.domain.ProjectionDataTypeProperty"),
        @Type(
                value = ProjectionReferencePropertyDTO.class,
                name = "klass.model.meta.domain.ProjectionReferenceProperty"),
        @Type(
                value = ProjectionProjectionReferenceDTO.class,
                name = "klass.model.meta.domain.ProjectionProjectionReference"),
})
public abstract class ProjectionChildDTO
        extends NamedElementDTO
{
    protected ProjectionChildDTO(String name)
    {
        super(name);
    }
}
