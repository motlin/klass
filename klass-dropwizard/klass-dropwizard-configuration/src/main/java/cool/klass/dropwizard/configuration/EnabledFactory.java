package cool.klass.dropwizard.configuration;

import com.fasterxml.jackson.annotation.JsonProperty;

public class EnabledFactory
{
    private boolean enabled = false;

    public boolean isEnabled()
    {
        return this.enabled;
    }

    @JsonProperty
    public void setEnabled(boolean enabled)
    {
        this.enabled = enabled;
    }
}
