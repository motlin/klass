package cool.klass.servlet.filter.mdc.response;

import java.lang.reflect.Type;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import cool.klass.logging.context.MDCCloseable;
import cool.klass.serialization.jackson.response.KlassResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

@Provider
@Priority(Priorities.USER + 1)
public class KlassResponseFilter implements ContainerRequestFilter, ContainerResponseFilter
{
    private static final Logger LOGGER = LoggerFactory.getLogger(KlassResponseFilter.class);
    private static final Marker MARKER = MarkerFactory.getMarker("response");

    private static final Pattern DURATION_PATTERN = Pattern.compile("(\\d[HMS])(?!$)");

    private final Clock clock;

    public KlassResponseFilter(@Nonnull @Context Clock clock)
    {
        this.clock = Objects.requireNonNull(clock);
    }

    @Override
    public void filter(@Nonnull ContainerRequestContext requestContext)
    {
        Instant startTime = this.clock.instant();
        requestContext.setProperty("time.start", startTime);

        // TODO: Split Reladomo stuff into a separate Filter
        int remoteRetrieveCount   = MithraManagerProvider.getMithraManager().getRemoteRetrieveCount();
        int databaseRetrieveCount = MithraManagerProvider.getMithraManager().getDatabaseRetrieveCount();
        requestContext.setProperty("remoteRetrieveCount", remoteRetrieveCount);
        requestContext.setProperty("databaseRetrieveCount", databaseRetrieveCount);
    }

    @Override
    public void filter(
            ContainerRequestContext requestContext,
            ContainerResponseContext responseContext)
    {
        try (MDCCloseable mdc = new MDCCloseable())
        {
            this.filterWithMDC(mdc, requestContext, responseContext);
        }
    }

    private void filterWithMDC(
            MDCCloseable mdc,
            ContainerRequestContext requestContext,
            ContainerResponseContext responseContext)
    {
        int        status     = responseContext.getStatus();
        StatusType statusInfo = responseContext.getStatusInfo();
        Type       entityType = responseContext.getEntityType();
        Object     entity     = responseContext.getEntity();

        mdc.put("klass.response.http.status", String.valueOf(status));
        mdc.put("klass.response.http.statusInfo", String.valueOf(statusInfo));
        mdc.put("klass.response.entityType", entityType.getTypeName());

        Instant timeStart = (Instant) requestContext.getProperty("time.start");
        if (timeStart != null)
        {
            Instant  timeEnd  = this.clock.instant();
            Duration duration = Duration.between(timeStart, timeEnd);
            mdc.put("time.start", String.valueOf(timeStart));
            mdc.put("time.end", String.valueOf(timeEnd));
            mdc.put("time.duration.pretty", KlassResponseFilter.prettyPrintDuration(duration));
            mdc.put("time.duration.ms", String.valueOf(duration.toMillis()));
            mdc.put("time.duration.ns", String.valueOf(duration.toNanos()));
        }

        Object remoteRetrieveCountBeforeObject = requestContext.getProperty("remoteRetrieveCount");
        if (remoteRetrieveCountBeforeObject != null)
        {
            int remoteRetrieveCountBefore = (int) remoteRetrieveCountBeforeObject;
            int remoteRetrieveCountAfter  = MithraManagerProvider.getMithraManager().getRemoteRetrieveCount();
            int remoteRetrieveCount       = remoteRetrieveCountAfter - remoteRetrieveCountBefore;
            mdc.put("klass.response.reladomo.remoteRetrieveCount", String.valueOf(remoteRetrieveCount));
        }

        Object databaseRetrieveCountBeforeObject = requestContext.getProperty("databaseRetrieveCount");
        if (databaseRetrieveCountBeforeObject != null)
        {
            int databaseRetrieveCountBefore = (int) databaseRetrieveCountBeforeObject;
            int databaseRetrieveCountAfter  = MithraManagerProvider.getMithraManager().getDatabaseRetrieveCount();
            int databaseRetrieveCount       = databaseRetrieveCountAfter - databaseRetrieveCountBefore;
            mdc.put("klass.response.reladomo.databaseRetrieveCount", String.valueOf(databaseRetrieveCount));
        }

        if (entity instanceof KlassResponse)
        {
            KlassResponse klassResponse = (KlassResponse) entity;
            klassResponse.withMDC(mdc);
        }

        LOGGER.info(
                MARKER,
                "Sending response.");
    }

    private static String prettyPrintDuration(Duration duration)
    {
        String  trimmedString = duration.toString().substring(2);
        Matcher matcher       = DURATION_PATTERN.matcher(trimmedString);
        return matcher
                .replaceAll("$1 ")
                .toLowerCase();
    }
}
