package cool.klass.model.meta.domain.json.dto;

import org.eclipse.collections.api.list.ImmutableList;

public class PrimitivePropertyDTO
        extends DataTypePropertyDTO
{
    private final PrimitiveTypeDTO primitiveType;

    private final ValidationDTO minValidation;
    private final ValidationDTO maxValidation;

    public PrimitivePropertyDTO(
            String name,
            PrimitiveTypeDTO primitiveType,
            Boolean optional,
            ImmutableList<ModifierDTO> propertyModifiers,
            ValidationDTO minLengthValidation,
            ValidationDTO maxLengthValidation,
            ValidationDTO minValidation,
            ValidationDTO maxValidation)
    {
        super(name, optional, propertyModifiers, minLengthValidation, maxLengthValidation);
        this.primitiveType = primitiveType;
        this.minValidation = minValidation;
        this.maxValidation = maxValidation;
    }

    public PrimitiveTypeDTO getPrimitiveType()
    {
        return this.primitiveType;
    }

    public ValidationDTO getMinValidation()
    {
        return this.minValidation;
    }

    public ValidationDTO getMaxValidation()
    {
        return this.maxValidation;
    }
}
