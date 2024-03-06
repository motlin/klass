package cool.klass.servlet.filter.mdc;

import java.util.Objects;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

import org.eclipse.collections.api.list.ImmutableList;
import org.slf4j.MDC;

public class ClearMDCContainerResponseFilter implements ContainerResponseFilter
{
    private final ImmutableList<String> mdcKeys;

    public ClearMDCContainerResponseFilter(ImmutableList<String> mdcKeys)
    {
        this.mdcKeys = Objects.requireNonNull(mdcKeys);
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
    {
        for (String mdcKey : this.mdcKeys)
        {
            MDC.remove(mdcKey);
        }
    }
}
