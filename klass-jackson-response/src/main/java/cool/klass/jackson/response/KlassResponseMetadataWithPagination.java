package cool.klass.jackson.response;

import java.time.Instant;
import java.util.Objects;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonProperty
    @Nonnull
    public KlassResponsePagination getPagination()
    {
        return this.pagination;
    }

    @JsonProperty
    @Nonnull
    public Instant getTransactionTimestamp()
    {
        return this.transactionTimestamp;
    }
}
