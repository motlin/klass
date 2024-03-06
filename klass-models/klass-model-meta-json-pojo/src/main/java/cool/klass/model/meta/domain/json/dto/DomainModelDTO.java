package cool.klass.model.meta.domain.json.dto;

import org.eclipse.collections.api.list.ImmutableList;

public class DomainModelDTO
{
    private final ImmutableList<EnumerationDTO>  enumerations;
    private final ImmutableList<InterfaceDTO>    interfaces;
    private final ImmutableList<ClassDTO>        classes;
    private final ImmutableList<AssociationDTO>  associations;
    private final ImmutableList<ProjectionDTO>   projections;
    private final ImmutableList<ServiceGroupDTO> services;

    public DomainModelDTO(
            ImmutableList<EnumerationDTO> enumerations,
            ImmutableList<InterfaceDTO> interfaces,
            ImmutableList<ClassDTO> classes,
            ImmutableList<AssociationDTO> associations,
            ImmutableList<ProjectionDTO> projections,
            ImmutableList<ServiceGroupDTO> services)
    {
        this.enumerations = enumerations;
        this.interfaces   = interfaces;
        this.classes      = classes;
        this.associations = associations;
        this.projections  = projections;
        this.services     = services;
    }

    public ImmutableList<EnumerationDTO> getEnumerations()
    {
        return this.enumerations;
    }

    public ImmutableList<InterfaceDTO> getInterfaces()
    {
        return this.interfaces;
    }

    public ImmutableList<ClassDTO> getClasses()
    {
        return this.classes;
    }

    public ImmutableList<AssociationDTO> getAssociations()
    {
        return this.associations;
    }

    public ImmutableList<ProjectionDTO> getProjections()
    {
        return this.projections;
    }

    public ImmutableList<ServiceGroupDTO> getServices()
    {
        return this.services;
    }
}
