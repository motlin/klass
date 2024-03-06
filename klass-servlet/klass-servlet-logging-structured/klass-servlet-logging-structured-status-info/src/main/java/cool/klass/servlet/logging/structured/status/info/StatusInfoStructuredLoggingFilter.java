package cool.klass.servlet.logging.structured.status.info;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Response.StatusType;

public class StatusInfoStructuredLoggingFilter implements ContainerResponseFilter
{
    @Override
    public void filter(
            ContainerRequestContext requestContext,
            ContainerResponseContext responseContext) throws IOException
    {
        Object structuredArguments = requestContext.getProperty("structuredArguments");
        Objects.requireNonNull(structuredArguments);
        Map<String, Object> structuredArgumentsMap = (Map<String, Object>) structuredArguments;

        StatusType statusInfo = responseContext.getStatusInfo();
        if (statusInfo.getStatusCode() != responseContext.getStatus())
        {
            throw new AssertionError();
        }

        structuredArgumentsMap.put("klass.response.http.statusEnum", statusInfo.toEnum());
        structuredArgumentsMap.put("klass.response.http.statusCode", statusInfo.getStatusCode());
        structuredArgumentsMap.put("klass.response.http.statusFamily", statusInfo.getFamily());
        structuredArgumentsMap.put("klass.response.http.statusPhrase", statusInfo.getReasonPhrase());

        Type entityType = responseContext.getEntityType();
        if (entityType instanceof Class)
        {
            Class<?> aClass = (Class<?>) entityType;
            structuredArgumentsMap.put("klass.response.http.entityType", aClass.getCanonicalName());
        }
        else
        {
            throw new AssertionError(entityType.getClass().getCanonicalName());
        }
    }
}
