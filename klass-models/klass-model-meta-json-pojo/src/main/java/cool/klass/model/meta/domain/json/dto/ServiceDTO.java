package cool.klass.model.meta.domain.json.dto;

public class ServiceDTO
{
    private final String       verb;
    private final String       serviceMultiplicity;
    private final ReferenceDTO projection;
    private final CriteriaDTO  queryCriteria;

    public ServiceDTO(
            String verb,
            String serviceMultiplicity,
            ReferenceDTO projection,
            CriteriaDTO queryCriteria)
    {
        this.verb                = verb;
        this.serviceMultiplicity = serviceMultiplicity;
        this.projection          = projection;
        this.queryCriteria       = queryCriteria;
    }

    public String getVerb()
    {
        return this.verb;
    }

    public String getServiceMultiplicity()
    {
        return this.serviceMultiplicity;
    }

    public ReferenceDTO getProjection()
    {
        return this.projection;
    }

    public CriteriaDTO getQueryCriteria()
    {
        return this.queryCriteria;
    }
}
