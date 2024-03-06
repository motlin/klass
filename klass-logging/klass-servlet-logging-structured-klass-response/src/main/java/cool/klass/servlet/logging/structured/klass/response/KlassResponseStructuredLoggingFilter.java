package cool.klass.servlet.logging.structured.klass.response;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

import cool.klass.serialization.jackson.response.KlassResponse;
import cool.klass.serialization.jackson.response.KlassResponseMetadata;
import cool.klass.serialization.jackson.response.KlassResponsePagination;

public class KlassResponseStructuredLoggingFilter implements ContainerResponseFilter
{
    @Override
    public void filter(
            ContainerRequestContext requestContext,
            ContainerResponseContext responseContext)
    {
        Object structuredArguments = requestContext.getProperty("structuredArguments");
        Objects.requireNonNull(structuredArguments);
        Map<String, Object> structuredArgumentsMap = (Map<String, Object>) structuredArguments;

        Object entity = responseContext.getEntity();
        if (entity instanceof KlassResponse)
        {
            KlassResponse klassResponse = (KlassResponse) entity;
            this.setKlassResponse(klassResponse, structuredArgumentsMap);
        }
    }

    private void setKlassResponse(
            KlassResponse klassResponse,
            Map<String, Object> structuredArgumentsMap)
    {
        Object data = klassResponse.getData();
        if (data instanceof List)
        {
            List<?> list = (List<?>) data;
            int     size = list.size();
            structuredArgumentsMap.put("klass.response.data.size", size);
        }
        else if (data != null)
        {
            structuredArgumentsMap.put("klass.response.data.type", data.getClass());
        }

        KlassResponseMetadata metadata = klassResponse.getMetadata();
        this.setMetadata(metadata, structuredArgumentsMap);
    }

    private void setMetadata(
            KlassResponseMetadata metadata,
            Map<String, Object> structuredArgumentsMap)
    {
        this.put(structuredArgumentsMap, "klass.model.criteria", metadata.getCriteria());
        this.put(structuredArgumentsMap, "klass.model.orderBy", metadata.getOrderBy());
        structuredArgumentsMap.put("klass.model.multiplicity", metadata.getMultiplicity());
        structuredArgumentsMap.put("klass.model.projection", metadata.getProjection());

        structuredArgumentsMap.put("klass.request.transactionTimestamp", metadata.getTransactionTimestamp());
        this.put(structuredArgumentsMap, "klass.request.principal", metadata.getPrincipal());

        metadata.getPagination()
                .ifPresent(pagination -> this.setPagination(pagination, structuredArgumentsMap));
    }

    private void put(Map<String, Object> structuredArgumentsMap, String key, Optional<?> optionalValue)
    {
        optionalValue.ifPresent(object -> structuredArgumentsMap.put(key, object));
    }

    private void setPagination(
            KlassResponsePagination pagination,
            Map<String, Object> structuredArgumentsMap)
    {
        structuredArgumentsMap.put("klass.response.pagination.pageSize", pagination.getPageSize());
        structuredArgumentsMap.put("klass.response.pagination.numberOfPages", pagination.getNumberOfPages());
        structuredArgumentsMap.put("klass.response.pagination.pageNumber", pagination.getPageNumber());
    }
}
