package cool.klass.dropwizard.configuration.sample.data;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class SampleDataFactory
{
    private boolean enabled = false;

    private @Valid @NotNull Instant      dataInstant     = Instant.parse("1999-12-31T23:59:00Z");
    private @Valid @NotNull List<String> skippedPackages = Arrays.asList("klass.model.meta.domain");

    public boolean isEnabled()
    {
        return this.enabled;
    }

    @JsonProperty
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }

    @JsonProperty
    public Instant getDataInstant()
    {
        return this.dataInstant;
    }

    @JsonProperty
    public void setDataInstant(Instant dataInstant)
    {
        this.dataInstant = dataInstant;
    }

    @JsonProperty
    public ImmutableList<String> getSkippedPackages()
    {
        return Lists.immutable.withAll(this.skippedPackages);
    }

    @JsonProperty
    public void setSkippedPackages(List<String> skippedPackages)
    {
        this.skippedPackages = skippedPackages;
    }
}
