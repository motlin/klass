package cool.klass.jackson.response;

import java.time.Instant;
import java.util.Objects;

import javax.annotation.Nonnull;

public class KlassResponseMetadata
{
    @Nonnull
    private final Instant transactionTimestamp;

    public KlassResponseMetadata(@Nonnull Instant transactionTimestamp)
    {
        this.transactionTimestamp = Objects.requireNonNull(transactionTimestamp);
    }

    @Nonnull
    public Instant getTransactionTimestamp()
    {
        return this.transactionTimestamp;
    }
}
