package cool.klass.servlet.filter.logging.structured;

import java.lang.reflect.Type;
import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response.StatusType;
import javax.ws.rs.ext.Provider;

import com.gs.fw.common.mithra.MithraManagerProvider;
import cool.klass.serialization.jackson.response.KlassResponse;
import net.logstash.logback.argument.StructuredArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

@Provider
@Priority(Priorities.USER - 1)
public class StructuredLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter
{
    private static final Logger LOGGER = LoggerFactory.getLogger(StructuredLoggingFilter.class);
    private static final Marker MARKER = MarkerFactory.getMarker("structured logging");

    private final Clock clock;

    public StructuredLoggingFilter(@Nonnull @Context Clock clock)
    {
        this.clock = Objects.requireNonNull(clock);
    }

    @Override
    public void filter(@Nonnull ContainerRequestContext requestContext)
    {
        Instant startTime = this.clock.instant();

        KlassStructuredArguments structuredArguments = new KlassStructuredArguments();
        requestContext.setProperty("structuredArguments", structuredArguments);

        structuredArguments.setStartTime(startTime);

        int remoteRetrieveCount   = MithraManagerProvider.getMithraManager().getRemoteRetrieveCount();
        int databaseRetrieveCount = MithraManagerProvider.getMithraManager().getDatabaseRetrieveCount();
        structuredArguments.setRemoteRetrieveCountBefore(remoteRetrieveCount);
        structuredArguments.setDatabaseRetrieveCountBefore(databaseRetrieveCount);
    }

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

        int remoteRetrieveCountAfter   = MithraManagerProvider.getMithraManager().getRemoteRetrieveCount();
        int databaseRetrieveCountAfter = MithraManagerProvider.getMithraManager().getDatabaseRetrieveCount();
        structuredArguments.setRemoteRetrieveCountAfter(remoteRetrieveCountAfter);
        structuredArguments.setDatabaseRetrieveCountAfter(databaseRetrieveCountAfter);

        Instant timeEnd = this.clock.instant();
        structuredArguments.setEndTime(timeEnd);

        LOGGER.info(
                MARKER,
                "Sending response. {}",
                StructuredArguments.fields(structuredArguments));
    }
}
