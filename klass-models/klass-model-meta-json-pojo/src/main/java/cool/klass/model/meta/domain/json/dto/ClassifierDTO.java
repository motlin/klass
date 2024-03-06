package cool.klass.model.meta.domain.json.dto;

import org.eclipse.collections.api.list.ImmutableList;

public abstract class ClassifierDTO
        extends PackageableElementDTO
{
    private final ImmutableList<SuperInterfaceMappingDTO> superInterfaces;
    private final ImmutableList<ModifierDTO>              classifierModifiers;
    private final ImmutableList<PrimitivePropertyDTO>     primitiveProperties;
    private final ImmutableList<EnumerationPropertyDTO>   enumerationProperties;

    protected ClassifierDTO(
            String name,
            String packageName,
            ImmutableList<SuperInterfaceMappingDTO> superInterfaces,
            ImmutableList<ModifierDTO> classifierModifiers,
            ImmutableList<PrimitivePropertyDTO> primitiveProperties,
            ImmutableList<EnumerationPropertyDTO> enumerationProperties)
    {
        super(name, packageName);
        this.superInterfaces       = superInterfaces;
        this.classifierModifiers   = classifierModifiers;
        this.primitiveProperties   = primitiveProperties;
        this.enumerationProperties = enumerationProperties;
    }

    public ImmutableList<SuperInterfaceMappingDTO> getSuperInterfaces()
    {
        return this.superInterfaces;
    }

    public ImmutableList<ModifierDTO> getClassifierModifiers()
    {
        return this.classifierModifiers;
    }

    public ImmutableList<PrimitivePropertyDTO> getPrimitiveProperties()
    {
        return this.primitiveProperties;
    }

    public ImmutableList<EnumerationPropertyDTO> getEnumerationProperties()
    {
        return this.enumerationProperties;
    }
}
