package cool.klass.servlet.filter.mdc.jsonview;

import java.util.Objects;

import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;

import cool.klass.logging.context.MDCCloseable;
import cool.klass.model.meta.domain.api.projection.Projection;

@Provider
@Priority(Priorities.USER + 1)
public class JsonViewFilter implements ContainerRequestFilter
{
    private final Projection projection;

    public JsonViewFilter(Projection projection)
    {
        this.projection = Objects.requireNonNull(projection);
    }

    @Override
    public void filter(ContainerRequestContext requestContext)
    {
        MDCCloseable mdc = (MDCCloseable) requestContext.getProperty("mdc");

        mdc.put("klass.jsonView.projectionName", String.valueOf(this.projection));
        mdc.put("klass.jsonView.projectionClass", this.projection.getKlass().getFullyQualifiedName());
    }
}
