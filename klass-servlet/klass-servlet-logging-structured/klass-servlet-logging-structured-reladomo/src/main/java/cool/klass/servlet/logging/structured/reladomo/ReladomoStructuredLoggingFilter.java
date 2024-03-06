package cool.klass.servlet.logging.structured.reladomo;

import java.util.Map;
import java.util.Objects;

import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

import com.gs.fw.common.mithra.MithraManagerProvider;

public class ReladomoStructuredLoggingFilter implements ClientRequestFilter, ContainerResponseFilter
{
    @Override
    public void filter(ClientRequestContext requestContext)
    {
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
        Object structuredArguments = requestContext.getProperty("structuredArguments");
        Objects.requireNonNull(structuredArguments);
        Map<String, Object> structuredArgumentsMap = (Map<String, Object>) structuredArguments;

        Object remoteRetrieveCountBeforeObject = requestContext.getProperty("remoteRetrieveCount");
        if (remoteRetrieveCountBeforeObject != null)
        {
            int remoteRetrieveCountBefore = (int) remoteRetrieveCountBeforeObject;
            int remoteRetrieveCountAfter  = MithraManagerProvider.getMithraManager().getRemoteRetrieveCount();
            int remoteRetrieveCount       = remoteRetrieveCountAfter - remoteRetrieveCountBefore;
            structuredArgumentsMap.put("klass.response.reladomo.remoteRetrieveCount", remoteRetrieveCount);
        }

        Object databaseRetrieveCountBeforeObject = requestContext.getProperty("databaseRetrieveCount");
        if (databaseRetrieveCountBeforeObject != null)
        {
            int databaseRetrieveCountBefore = (int) databaseRetrieveCountBeforeObject;
            int databaseRetrieveCountAfter  = MithraManagerProvider.getMithraManager().getDatabaseRetrieveCount();
            int databaseRetrieveCount       = databaseRetrieveCountAfter - databaseRetrieveCountBefore;
            structuredArgumentsMap.put("klass.response.reladomo.databaseRetrieveCount", databaseRetrieveCount);
        }
    }
}
