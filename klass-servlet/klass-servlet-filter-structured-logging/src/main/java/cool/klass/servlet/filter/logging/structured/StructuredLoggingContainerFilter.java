package cool.klass.servlet.filter.logging.structured;

import java.lang.reflect.Type;
import java.time.Clock;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.ext.Provider;

import cool.klass.serialization.jackson.response.KlassResponse;

@Provider
@Priority(Priorities.USER - 1)
public class StructuredLoggingContainerFilter implements ContainerResponseFilter
{
    @Override
    public void filter(
            ContainerRequestContext requestContext,
            ContainerResponseContext responseContext)
    {
        KlassStructuredArguments structuredArguments =
                (KlassStructuredArguments) requestContext.getProperty("structuredArguments");

        StatusType statusInfo = responseContext.getStatusInfo();
        if (statusInfo.getStatusCode() != responseContext.getStatus())
        {
            throw new AssertionError();
        }

        structuredArguments.setStatusType(statusInfo);

        Type entityType = responseContext.getEntityType();
        structuredArguments.setEntityType(entityType);

        Object entity = responseContext.getEntity();

        if (entity instanceof KlassResponse)
        {
            KlassResponse klassResponse = (KlassResponse) entity;
            structuredArguments.setKlassResponse(klassResponse);
        }
    }
}
