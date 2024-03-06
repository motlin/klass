package cool.klass.jackson.response;

import java.time.Instant;
import java.util.Objects;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KlassResponseMetadata
{
    @Nonnull
    private final Instant transactionTimestamp;

    public KlassResponseMetadata(@Nonnull Instant transactionTimestamp)
    {
        this.transactionTimestamp = Objects.requireNonNull(transactionTimestamp);
    }

    @JsonProperty
    @Nonnull
    public Instant getTransactionTimestamp()
    {
        return this.transactionTimestamp;
    }
}
