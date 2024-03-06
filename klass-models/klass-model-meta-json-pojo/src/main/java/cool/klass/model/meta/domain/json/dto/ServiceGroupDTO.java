package cool.klass.model.meta.domain.json.dto;

import org.eclipse.collections.api.list.ImmutableList;

public class ServiceGroupDTO
        extends PackageableElementDTO
{
    private final ImmutableList<ServiceUrlDTO> urls;

    public ServiceGroupDTO(
            String name,
            String packageName,
            ImmutableList<ServiceUrlDTO> urls)
    {
        super(name, packageName);
        this.urls = urls;
    }

    public ImmutableList<ServiceUrlDTO> getUrls()
    {
        return this.urls;
    }
}
