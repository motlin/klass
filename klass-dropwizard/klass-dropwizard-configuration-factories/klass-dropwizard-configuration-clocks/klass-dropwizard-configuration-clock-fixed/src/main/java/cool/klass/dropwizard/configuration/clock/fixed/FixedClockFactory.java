package cool.klass.dropwizard.configuration.clock.fixed;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import javax.annotation.Nonnull;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.service.AutoService;
import cool.klass.dropwizard.configuration.clock.ClockFactory;

@JsonTypeName("fixed")
@AutoService(ClockFactory.class)
public class FixedClockFactory implements ClockFactory
{
    private @Valid @NotNull Instant instant;

    @Nonnull
    @Override
    public Clock getClock()
    {
        return Clock.fixed(this.instant, ZoneOffset.UTC.normalized());
    }

    @JsonProperty
    public void setInstant(Instant instant)
    {
        this.instant = instant;
    }
}
