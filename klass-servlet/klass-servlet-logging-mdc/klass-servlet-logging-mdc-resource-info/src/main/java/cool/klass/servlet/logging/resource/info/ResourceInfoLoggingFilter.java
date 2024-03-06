package cool.klass.servlet.logging.resource.info;

import javax.annotation.Nonnull;
import javax.annotation.Priority;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.ResourceInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.Provider;

import org.eclipse.collections.impl.list.mutable.ListAdapter;
import org.glassfish.jersey.server.ContainerRequest;
import org.slf4j.MDC;

@Provider
@Priority(Priorities.USER)
public class ResourceInfoLoggingFilter implements ContainerRequestFilter, ContainerResponseFilter
{
    @Context
    private ResourceInfo resourceInfo;

    @Override
    public void filter(@Nonnull ContainerRequestContext requestContext)
    {
        UriInfo uriInfo = requestContext.getUriInfo();

        String httpPath           = uriInfo.getPath();
        String httpMethod         = requestContext.getMethod();
        String resourceClassName  = this.resourceInfo.getResourceClass().getCanonicalName();
        String resourceMethodName = this.resourceInfo.getResourceMethod().getName();

        MDC.put("klass.request.httpPath", httpPath);
        MDC.put("klass.request.httpMethod", httpMethod);
        MDC.put("klass.request.resourceClassName", resourceClassName);
        MDC.put("klass.request.resourceMethodName", resourceMethodName);

        uriInfo.getQueryParameters().forEach((parameterName, parameterValues) ->
        {
            String key   = "klass.request.parameter.query." + parameterName;
            String value = ListAdapter.adapt(parameterValues).makeString();
            MDC.put(key, value);
        });

        uriInfo.getPathParameters().forEach((parameterName, parameterValues) ->
        {
            String key   = "klass.request.parameter.path." + parameterName;
            String value = ListAdapter.adapt(parameterValues).makeString();
            MDC.put(key, value);
        });

        if (requestContext instanceof ContainerRequest)
        {
            ContainerRequest containerRequest = (ContainerRequest) requestContext;
            String           pathTemplate     = containerRequest.getUriInfo().getMatchedModelResource().getPath();
            MDC.put("klass.request.httpPathTemplate", pathTemplate);
        }
    }

    @Override
    public void filter(
            ContainerRequestContext requestContext,
            ContainerResponseContext responseContext)
    {
        MDC.remove("klass.request.httpPath");
        MDC.remove("klass.request.httpMethod");
        MDC.remove("klass.request.httpPathTemplate");
        MDC.remove("klass.request.resourceClassName");
        MDC.remove("klass.request.resourceMethodName");

        UriInfo uriInfo = requestContext.getUriInfo();

        uriInfo
                .getQueryParameters()
                .keySet()
                .stream()
                .map(parameterName -> "klass.request.parameter.query." + parameterName)
                .forEach(MDC::remove);

        uriInfo
                .getPathParameters()
                .keySet()
                .stream()
                .map(parameterName -> "klass.request.parameter.path." + parameterName)
                .forEach(MDC::remove);
    }
}
