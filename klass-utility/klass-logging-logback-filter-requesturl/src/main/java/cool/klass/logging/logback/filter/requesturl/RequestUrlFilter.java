package cool.klass.logging.logback.filter.requesturl;

import java.util.Objects;

import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.core.filter.Filter;
import ch.qos.logback.core.spi.FilterReply;
import org.eclipse.collections.api.list.ImmutableList;

public class RequestUrlFilter extends Filter<IAccessEvent>
{
    private final ImmutableList<String> bannedUrls;

    public RequestUrlFilter(ImmutableList<String> bannedUrls)
    {
        this.bannedUrls = Objects.requireNonNull(bannedUrls);
    }

    @Override
    public FilterReply decide(IAccessEvent event)
    {
        String url = event.getRequestURL();
        for (String bannedUrl : this.bannedUrls)
        {
            if (url.contains(bannedUrl))
            {
                return FilterReply.DENY;
            }
        }
        return FilterReply.NEUTRAL;
    }
}
