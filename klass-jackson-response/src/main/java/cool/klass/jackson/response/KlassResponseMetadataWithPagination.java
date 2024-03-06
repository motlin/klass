package cool.klass.jackson.response;

import java.time.Instant;
import java.util.Objects;

import javax.annotation.Nonnull;

public class KlassResponseMetadataWithPagination
{
    @Nonnull
    private final KlassResponsePagination pagination;
    @Nonnull
    private final Instant                 transactionTimestamp;

    public KlassResponseMetadataWithPagination(
            KlassResponsePagination pagination,
            Instant transactionTimestamp)
    {
        this.pagination = Objects.requireNonNull(pagination);
        this.transactionTimestamp = Objects.requireNonNull(transactionTimestamp);
    }

    @Nonnull
    public KlassResponsePagination getPagination()
    {
        return this.pagination;
    }

    @Nonnull
    public Instant getTransactionTimestamp()
    {
        return this.transactionTimestamp;
    }
}
