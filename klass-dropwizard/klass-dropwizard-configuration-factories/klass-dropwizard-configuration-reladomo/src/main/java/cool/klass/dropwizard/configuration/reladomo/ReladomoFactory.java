package cool.klass.dropwizard.configuration.reladomo;

import java.util.Arrays;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.util.Duration;

public class ReladomoFactory
{
    // Something like 30 seconds to 2 minutes makes sense in production
    private          Duration     transactionTimeout         = Duration.minutes(5);
    // reladomo-runtime-configuration/ReladomoRuntimeConfiguration.xml in production
    private @NotNull List<String> runtimeConfigurationPaths  = Arrays.asList(
            "reladomo-runtime-configuration/TestReladomoRuntimeConfiguration.xml");
    private          boolean      enableRetrieveCountMetrics = true;

    public Duration getTransactionTimeout()
    {
        return this.transactionTimeout;
    }

    @JsonProperty
    public void setTransactionTimeout(Duration transactionTimeout)
    {
        this.transactionTimeout = transactionTimeout;
    }

    public List<String> getRuntimeConfigurationPaths()
    {
        return this.runtimeConfigurationPaths;
    }

    @JsonProperty
    public void setRuntimeConfigurationPaths(List<String> runtimeConfigurationPaths)
    {
        this.runtimeConfigurationPaths = runtimeConfigurationPaths;
    }

    public boolean isEnableRetrieveCountMetrics()
    {
        return this.enableRetrieveCountMetrics;
    }

    @JsonProperty
    public void setEnableRetrieveCountMetrics(boolean enableRetrieveCountMetrics)
    {
        this.enableRetrieveCountMetrics = enableRetrieveCountMetrics;
    }
}
