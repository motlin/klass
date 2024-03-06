package cool.klass.model.meta.domain.json.dto;

import org.eclipse.collections.api.list.ImmutableList;

public class ServiceUrlDTO
{
    private final String                    url;
    private final ImmutableList<ServiceDTO> services;

    public ServiceUrlDTO(
            String url,
            ImmutableList<ServiceDTO> services)
    {
        this.url      = url;
        this.services = services;
    }

    public String getUrl()
    {
        return this.url;
    }

    public ImmutableList<ServiceDTO> getServices()
    {
        return this.services;
    }
}
