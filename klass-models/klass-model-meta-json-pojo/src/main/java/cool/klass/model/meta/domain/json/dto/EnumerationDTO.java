package cool.klass.model.meta.domain.json.dto;

import org.eclipse.collections.api.list.ImmutableList;

public class EnumerationDTO
        extends PackageableElementDTO
{
    private final ImmutableList<EnumerationLiteralDTO> enumerationLiterals;

    public EnumerationDTO(
            String name,
            String packageName,
            ImmutableList<EnumerationLiteralDTO> enumerationLiterals)
    {
        super(name, packageName);
        this.enumerationLiterals = enumerationLiterals;
    }

    public ImmutableList<EnumerationLiteralDTO> getEnumerationLiterals()
    {
        return this.enumerationLiterals;
    }
}
