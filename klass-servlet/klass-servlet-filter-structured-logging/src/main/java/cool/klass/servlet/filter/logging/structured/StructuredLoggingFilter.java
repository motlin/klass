package cool.klass.servlet.filter.logging.structured;

import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.util.Objects;

import javax.annotation.Priority;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Priorities;
import javax.ws.rs.ext.Provider;

import com.gs.fw.common.mithra.MithraManagerProvider;
import net.logstash.logback.argument.StructuredArgument;
import net.logstash.logback.argument.StructuredArguments;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

@Provider
@Priority(Priorities.USER - 1)
public class StructuredLoggingFilter implements Filter
{
    private static final Logger LOGGER = LoggerFactory.getLogger(StructuredLoggingFilter.class);
    private static final Marker MARKER = MarkerFactory.getMarker("structured logging");

    private final Clock clock;

    public StructuredLoggingFilter(Clock clock)
    {
        this.clock = Objects.requireNonNull(clock);
    }

    @Override
    public void init(FilterConfig filterConfig)
    {
    }

    @Override
    public void destroy()
    {
    }

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException
    {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        try
        {
            this.recordContext(httpServletRequest);
            chain.doFilter(request, response);
        }
        finally
        {
            this.log(httpServletRequest);
        }
    }

    private void recordContext(HttpServletRequest httpServletRequest)
    {
        Instant startTime = this.clock.instant();

        KlassStructuredArguments structuredArguments = new KlassStructuredArguments();
        httpServletRequest.setAttribute("structuredArguments", structuredArguments);

        structuredArguments.setStartTime(startTime);

        int remoteRetrieveCount   = MithraManagerProvider.getMithraManager().getRemoteRetrieveCount();
        int databaseRetrieveCount = MithraManagerProvider.getMithraManager().getDatabaseRetrieveCount();
        structuredArguments.setRemoteRetrieveCountBefore(remoteRetrieveCount);
        structuredArguments.setDatabaseRetrieveCountBefore(databaseRetrieveCount);
    }

    private void log(HttpServletRequest httpServletRequest)
    {
        KlassStructuredArguments structuredArguments =
                (KlassStructuredArguments) httpServletRequest.getAttribute("structuredArguments");

        Objects.requireNonNull(structuredArguments);

        int remoteRetrieveCountAfter   = MithraManagerProvider.getMithraManager().getRemoteRetrieveCount();
        int databaseRetrieveCountAfter = MithraManagerProvider.getMithraManager().getDatabaseRetrieveCount();
        structuredArguments.setRemoteRetrieveCountAfter(remoteRetrieveCountAfter);
        structuredArguments.setDatabaseRetrieveCountAfter(databaseRetrieveCountAfter);

        Instant timeEnd = this.clock.instant();
        structuredArguments.setEndTime(timeEnd);

        StructuredArgument structuredArgument = StructuredArguments.fields(structuredArguments);
        LOGGER.info(MARKER, "{}", structuredArgument);
    }
}
