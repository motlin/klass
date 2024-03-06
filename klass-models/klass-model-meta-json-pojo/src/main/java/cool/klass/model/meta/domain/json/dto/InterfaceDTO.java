package cool.klass.model.meta.domain.json.dto;

import org.eclipse.collections.api.list.ImmutableList;

public class InterfaceDTO
        extends ClassifierDTO
{
    public InterfaceDTO(
            String name,
            String packageName,
            ImmutableList<SuperInterfaceMappingDTO> superInterfaces,
            ImmutableList<ModifierDTO> classifierModifiers,
            ImmutableList<PrimitivePropertyDTO> primitiveProperties,
            ImmutableList<EnumerationPropertyDTO> enumerationProperties)
    {
        super(name, packageName, superInterfaces, classifierModifiers, primitiveProperties, enumerationProperties);
    }
}
