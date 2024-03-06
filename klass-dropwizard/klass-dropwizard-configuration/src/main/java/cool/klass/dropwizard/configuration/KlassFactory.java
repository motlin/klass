package cool.klass.dropwizard.configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import cool.klass.dropwizard.configuration.clock.ClockFactory;

public class KlassFactory
{
    private @NotNull @Valid ClockFactory clockFactory;

    public ClockFactory getClockFactory()
    {
        return clockFactory;
    }

    @JsonProperty("clock")
    public void setClockFactory(ClockFactory clockFactory)
    {
        this.clockFactory = clockFactory;
    }
}
