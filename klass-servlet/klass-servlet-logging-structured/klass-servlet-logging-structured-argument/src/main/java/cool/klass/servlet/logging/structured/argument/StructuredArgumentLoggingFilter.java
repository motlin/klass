package cool.klass.servlet.logging.structured.argument;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

import javax.annotation.Priority;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.ws.rs.Priorities;
import javax.ws.rs.ext.Provider;

import net.logstash.logback.marker.Markers;
import org.eclipse.collections.api.factory.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Provider
@Priority(Priorities.USER - 2)
public class StructuredArgumentLoggingFilter implements Filter
{
    private static final Logger LOGGER = LoggerFactory.getLogger(StructuredArgumentLoggingFilter.class);

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
            this.initialize(request);
            chain.doFilter(request, response);
        }
        finally
        {
            this.log(request);
        }
    }

    private void initialize(ServletRequest servletRequest)
    {
        servletRequest.setAttribute("structuredArguments", Maps.mutable.empty());
    }

    private void log(ServletRequest servletRequest)
    {
        Object structuredArguments = servletRequest.getAttribute("structuredArguments");
        Objects.requireNonNull(structuredArguments);
        Map<String, Object> structuredArgumentsMap = (Map<String, Object>) structuredArguments;

        LOGGER.info(Markers.appendEntries(structuredArgumentsMap), "structured logging");
    }
}
