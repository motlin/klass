package cool.klass.model.meta.domain.json.dto;

import org.eclipse.collections.api.list.ImmutableList;

public class UrlDTO
{
    private final String                         url;
    private final ImmutableList<ServiceGroupDTO> serviceGroups;

    public UrlDTO(
            String url,
            ImmutableList<ServiceGroupDTO> serviceGroups)
    {
        this.url           = url;
        this.serviceGroups = serviceGroups;
    }

    public String getUrl()
    {
        return this.url;
    }

    public ImmutableList<ServiceGroupDTO> getServiceGroups()
    {
        return this.serviceGroups;
    }
}
