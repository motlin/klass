package cool.klass.model.meta.domain.json.dto;

import org.eclipse.collections.api.list.ImmutableList;

public class EnumerationPropertyDTO
        extends DataTypePropertyDTO
{
    private final ReferenceDTO enumeration;

    public EnumerationPropertyDTO(
            String name,
            ReferenceDTO enumeration,
            Boolean optional,
            ImmutableList<ModifierDTO> propertyModifiers,
            ValidationDTO minLengthValidation,
            ValidationDTO maxLengthValidation)
    {
        super(name, optional, propertyModifiers, minLengthValidation, maxLengthValidation);
        this.enumeration = enumeration;
    }

    public ReferenceDTO getEnumeration()
    {
        return this.enumeration;
    }
}
