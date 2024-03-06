package cool.klass.dropwizard.configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import cool.klass.dropwizard.configuration.clock.ClockFactory;
import cool.klass.dropwizard.configuration.uuid.UUIDSupplierFactory;

public class KlassFactory
{
    private @NotNull @Valid ClockFactory        clockFactory;
    private @NotNull @Valid UUIDSupplierFactory uuidFactory;

    public ClockFactory getClockFactory()
    {
        return this.clockFactory;
    }

    @JsonProperty("clock")
    public void setClockFactory(ClockFactory clockFactory)
    {
        this.clockFactory = clockFactory;
    }

    public UUIDSupplierFactory getUuidFactory()
    {
        return this.uuidFactory;
    }

    @JsonProperty("uuid")
    public void setUuidFactory(UUIDSupplierFactory uuidFactory)
    {
        this.uuidFactory = uuidFactory;
    }
}
