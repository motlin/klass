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
    private @Valid @NotNull Instant instant = Instant.parse("2000-12-31T23:59:59Z");

    @Nonnull
    @Override
    public Clock createClock()
    {
        return Clock.fixed(this.instant, ZoneOffset.UTC.normalized());
    }

    @JsonProperty
    public Instant getInstant()
    {
        return this.instant;
    }

    @JsonProperty
    public void setInstant(Instant instant)
    {
        this.instant = instant;
    }
}
