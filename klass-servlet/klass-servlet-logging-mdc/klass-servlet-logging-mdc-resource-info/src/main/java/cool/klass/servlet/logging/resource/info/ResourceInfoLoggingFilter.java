package cool.klass.servlet.logging.resource.info;

import javax.annotation.Nonnull;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import cool.klass.logging.context.MDCCloseable;
import org.eclipse.collections.impl.list.mutable.ListAdapter;
import org.glassfish.jersey.server.ContainerRequest;

// Priority must be less than the priority of StructuredArgumentLoggingFilter
@Provider
@Priority(Priorities.USER - 20)
public class ResourceInfoLoggingFilter implements ContainerRequestFilter
{
    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(@Nonnull ContainerRequestContext requestContext)
    {
        MDCCloseable mdc = (MDCCloseable) requestContext.getProperty("mdc");

        UriInfo uriInfo = requestContext.getUriInfo();

        String httpPath           = uriInfo.getPath();
        String httpMethod         = requestContext.getMethod();
        String resourceClassName  = this.resourceInfo.getResourceClass().getCanonicalName();
        String resourceMethodName = this.resourceInfo.getResourceMethod().getName();

        mdc.put("klass.request.httpPath", httpPath);
        mdc.put("klass.request.httpMethod", httpMethod);
        mdc.put("klass.request.resourceClassName", resourceClassName);
        mdc.put("klass.request.resourceMethodName", resourceMethodName);

        uriInfo.getQueryParameters().forEach((parameterName, parameterValues) ->
        {
            String key   = "klass.request.parameter.query." + parameterName;
            String value = ListAdapter.adapt(parameterValues).makeString();
            mdc.put(key, value);
        });

        uriInfo.getPathParameters().forEach((parameterName, parameterValues) ->
        {
            String key   = "klass.request.parameter.path." + parameterName;
            String value = ListAdapter.adapt(parameterValues).makeString();
            mdc.put(key, value);
        });

        if (requestContext instanceof ContainerRequest)
        {
            ContainerRequest containerRequest = (ContainerRequest) requestContext;
            String           pathTemplate     = containerRequest.getUriInfo().getMatchedModelResource().getPath();
            mdc.put("klass.request.httpPathTemplate", pathTemplate);
        }
    }
}
