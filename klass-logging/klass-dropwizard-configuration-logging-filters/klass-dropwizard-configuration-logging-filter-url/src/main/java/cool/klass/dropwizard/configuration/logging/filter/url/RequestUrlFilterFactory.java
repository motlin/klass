package cool.klass.dropwizard.configuration.logging.filter.url;

import java.util.List;
import java.util.Objects;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import ch.qos.logback.access.spi.IAccessEvent;
import ch.qos.logback.core.filter.Filter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.service.AutoService;
import cool.klass.logging.logback.filter.requesturl.RequestUrlFilter;
import io.dropwizard.logging.filter.FilterFactory;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.list.ImmutableList;
import org.hibernate.validator.constraints.NotEmpty;

@JsonTypeName("url")
@AutoService(FilterFactory.class)
public class RequestUrlFilterFactory implements FilterFactory<IAccessEvent>
{
    @NotEmpty
    private @Valid @NotNull List<String> bannedUrls = Lists.mutable.empty();

    @Override
    public Filter<IAccessEvent> build()
    {
        ImmutableList<String> bannedUrls = Lists.immutable.withAll(this.bannedUrls);
        return new RequestUrlFilter(bannedUrls);
    }

    @JsonProperty
    public List<String> getBannedUrls()
    {
        return this.bannedUrls;
    }

    @JsonProperty
    public void setBannedUrls(List<String> bannedUrls)
    {
        this.bannedUrls = Objects.requireNonNull(bannedUrls);
    }
}
