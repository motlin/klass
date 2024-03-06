package cool.klass.jackson.response;

import java.security.Principal;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.annotation.JsonProperty;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.projection.Projection;

public class KlassResponseMetadata
{
    @Nonnull
    private final Projection                        projection;
    @Nonnull
    private final Multiplicity                      multiplicity;
    @Nonnull
    private final Instant                           transactionTimestamp;
    @Nonnull
    private final Optional<KlassResponsePagination> pagination;
    @Nonnull
    private final Optional<? extends Principal>               principal;

    public KlassResponseMetadata(
            @Nonnull Projection projection,
            @Nonnull Multiplicity multiplicity,
            @Nonnull Instant transactionTimestamp,
            @Nonnull Optional<KlassResponsePagination> pagination,
            @Nonnull Optional<? extends Principal> principal)
    {
        this.projection = Objects.requireNonNull(projection);
        this.multiplicity = Objects.requireNonNull(multiplicity);
        this.pagination = Objects.requireNonNull(pagination);
        this.transactionTimestamp = Objects.requireNonNull(transactionTimestamp);
        this.principal = Objects.requireNonNull(principal);
    }

    @JsonProperty
    @Nonnull
    public Projection getProjection()
    {
        return this.projection;
    }

    @JsonProperty
    @Nonnull
    public Multiplicity getMultiplicity()
    {
        return this.multiplicity;
    }

    @JsonProperty
    @Nonnull
    public Optional<KlassResponsePagination> getPagination()
    {
        return this.pagination;
    }

    @JsonProperty
    @Nonnull
    public Instant getTransactionTimestamp()
    {
        return this.transactionTimestamp;
    }

    @JsonProperty
    @Nonnull
    public Optional<? extends Principal> getPrincipal()
    {
        return this.principal;
    }
}
