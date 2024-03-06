package cool.klass.servlet.filter.mdc;

import java.io.IOException;
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

import org.eclipse.collections.api.list.ImmutableList;
import org.slf4j.MDC;

@Provider
@Priority(Priorities.USER - 3)
public class ClearMDCFilter implements Filter
{
    private final ImmutableList<String> mdcKeys;

    public ClearMDCFilter(ImmutableList<String> mdcKeys)
    {
        this.mdcKeys = Objects.requireNonNull(mdcKeys);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {
    }

    @Override
    public void destroy()
    {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException
    {
        try
        {
            chain.doFilter(request, response);
        }
        finally
        {
            for (String mdcKey : this.mdcKeys)
            {
                MDC.remove(mdcKey);
            }
        }
    }
}
