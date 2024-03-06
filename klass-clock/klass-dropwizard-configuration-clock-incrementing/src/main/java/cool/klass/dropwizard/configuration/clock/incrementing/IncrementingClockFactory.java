package cool.klass.dropwizard.configuration.clock.incrementing;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nonnull;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.google.auto.service.AutoService;
import cool.klass.clock.incrementing.IncrementingClock;
import cool.klass.dropwizard.configuration.clock.ClockFactory;
import io.dropwizard.validation.MinDuration;

@JsonTypeName("incrementing")
@AutoService(ClockFactory.class)
public class IncrementingClockFactory implements ClockFactory
{
    private @Valid @NotNull Instant instant = Instant.parse("2000-12-31T23:59:59Z");

    @NotNull
    @MinDuration(value = 0, unit = TimeUnit.MILLISECONDS, inclusive = false)
    private io.dropwizard.util.Duration incrementAmount = io.dropwizard.util.Duration.seconds(1);

    // TODO: Allow configuring the time zone like io.dropwizard.logging.AbstractAppenderFactory.setTimeZone(java.lang.String)

    @Nonnull
    @Override
    public Clock createClock()
    {
        long     nanoseconds = this.incrementAmount.toNanoseconds();
        Duration duration    = Duration.ofNanos(nanoseconds);
        return new IncrementingClock(this.instant, ZoneOffset.UTC.normalized(), duration);
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

    @JsonProperty
    public io.dropwizard.util.Duration getIncrementAmount()
    {
        return this.incrementAmount;
    }

    @JsonProperty
    public void setIncrementAmount(io.dropwizard.util.Duration incrementAmount)
    {
        this.incrementAmount = incrementAmount;
    }
}
