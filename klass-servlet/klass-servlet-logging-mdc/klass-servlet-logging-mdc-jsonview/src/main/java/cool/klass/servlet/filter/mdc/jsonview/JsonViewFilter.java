package cool.klass.servlet.filter.mdc.jsonview;

import java.util.Objects;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

import cool.klass.model.meta.domain.api.projection.Projection;
import org.slf4j.MDC;

@Provider
@Priority(Priorities.USER + 1)
public class JsonViewFilter implements ContainerRequestFilter, ContainerResponseFilter
{
    private final Projection projection;

    public JsonViewFilter(Projection projection)
    {
        this.projection = Objects.requireNonNull(projection);
    }

    @Override
    public void filter(ContainerRequestContext requestContext)
    {
        MDC.put("klass.jsonView.projectionName", String.valueOf(this.projection));
        MDC.put("klass.jsonView.projectionClass", this.projection.getKlass().getFullyQualifiedName());
    }

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
    {
        MDC.remove("klass.jsonView.projectionName");
        MDC.remove("klass.jsonView.projectionClass");
    }
}
