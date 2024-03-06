package cool.klass.model.meta.domain.json.dto;

import java.util.Optional;

import org.eclipse.collections.api.list.ImmutableList;

public class ClassDTO
        extends ClassifierDTO
{
    private final InheritanceTypeDTO     inheritanceType;
    private final int                    ordinal;
    private final Optional<ReferenceDTO> superClass;

    public ClassDTO(
            String name,
            String packageName,
            InheritanceTypeDTO inheritanceType,
            int ordinal,
            Optional<ReferenceDTO> superClass,
            ImmutableList<SuperInterfaceMappingDTO> superInterfaces,
            ImmutableList<ModifierDTO> classifierModifiers,
            ImmutableList<PrimitivePropertyDTO> primitiveProperties,
            ImmutableList<EnumerationPropertyDTO> enumerationProperties)
    {
        super(name, packageName, superInterfaces, classifierModifiers, primitiveProperties, enumerationProperties);
        this.inheritanceType = inheritanceType;
        this.ordinal         = ordinal;
        this.superClass      = superClass;
    }

    public InheritanceTypeDTO getInheritanceType()
    {
        return this.inheritanceType;
    }

    public int getOrdinal()
    {
        return this.ordinal;
    }

    public Optional<ReferenceDTO> getSuperClass()
    {
        return this.superClass;
    }
}
