package cool.klass.model.meta.domain.json.dto;

import org.eclipse.collections.api.list.ImmutableList;

public abstract class DataTypePropertyDTO
        extends NamedElementDTO
{
    private final boolean                    optional;
    private final ImmutableList<ModifierDTO> propertyModifiers;
    private final ValidationDTO              minLengthValidation;
    private final ValidationDTO              maxLengthValidation;

    protected DataTypePropertyDTO(
            String name,
            boolean optional,
            ImmutableList<ModifierDTO> propertyModifiers,
            ValidationDTO minLengthValidation, ValidationDTO maxLengthValidation)
    {
        super(name);
        this.optional            = optional;
        this.propertyModifiers   = propertyModifiers;
        this.minLengthValidation = minLengthValidation;
        this.maxLengthValidation = maxLengthValidation;
    }

    public boolean getOptional()
    {
        return this.optional;
    }

    public ImmutableList<ModifierDTO> getPropertyModifiers()
    {
        return this.propertyModifiers;
    }

    public ValidationDTO getMinLengthValidation()
    {
        return this.minLengthValidation;
    }

    public ValidationDTO getMaxLengthValidation()
    {
        return this.maxLengthValidation;
    }
}
