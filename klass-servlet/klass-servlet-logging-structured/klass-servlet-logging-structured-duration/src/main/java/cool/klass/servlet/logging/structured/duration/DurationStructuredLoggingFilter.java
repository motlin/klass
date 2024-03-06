package cool.klass.servlet.logging.structured.duration;

import java.io.IOException;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Priority;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.ws.rs.Priorities;
import javax.ws.rs.ext.Provider;

@Provider
@Priority(Priorities.USER - 1)
public class DurationStructuredLoggingFilter implements Filter
{
    private static final Pattern DURATION_PATTERN = Pattern.compile("(\\d[HMS])(?!$)");

    private final Clock clock;

    public DurationStructuredLoggingFilter(Clock clock)
    {
        this.clock = clock;
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
        try
        {
            this.before(request);
            chain.doFilter(request, response);
        }
        finally
        {
            this.after(request);
        }
    }

    private void before(ServletRequest servletRequest)
    {
        Instant startTime = this.clock.instant();

        Object structuredArguments = servletRequest.getAttribute("structuredArguments");
        Objects.requireNonNull(structuredArguments);
        Map<String, Object> structuredArgumentsMap = (Map<String, Object>) structuredArguments;

        structuredArgumentsMap.put("klass.time.startTime", startTime);
    }

    private void after(ServletRequest servletRequest)
    {
        Object structuredArguments = servletRequest.getAttribute("structuredArguments");
        Objects.requireNonNull(structuredArguments);
        Map<String, Object> structuredArgumentsMap = (Map<String, Object>) structuredArguments;

        Instant  startTime      = (Instant) structuredArgumentsMap.get("klass.time.startTime");
        Instant  endTime        = this.clock.instant();
        Duration duration       = Duration.between(startTime, endTime);
        String   prettyDuration = prettyPrintDuration(duration);

        structuredArgumentsMap.put("klass.time.endTime", endTime);
        structuredArgumentsMap.put("klass.time.duration.pretty", prettyDuration);
        structuredArgumentsMap.put("klass.time.duration.ms", duration.toMillis());
        structuredArgumentsMap.put("klass.time.duration.ns", duration.toNanos());
    }

    private static String prettyPrintDuration(Duration duration)
    {
        String  trimmedString = duration.toString().substring(2);
        Matcher matcher       = DURATION_PATTERN.matcher(trimmedString);
        return matcher.replaceAll("$1 ").toLowerCase();
    }
}
