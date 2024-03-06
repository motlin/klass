package cool.klass.jackson.response;

import java.security.Principal;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.projection.Projection;

public class KlassResponseBuilder
{
    @Nullable
    private final Object                            data;
    @Nonnull
    private final Projection                        projection;
    @Nonnull
    private final Multiplicity                      multiplicity;
    @Nonnull
    private final Instant                           transactionTimestamp;
    @Nonnull
    private       Optional<KlassResponsePagination> pagination = Optional.empty();
    @Nonnull
    private       Optional<? extends Principal>               principal  = Optional.empty();

    public KlassResponseBuilder(
            @Nullable Object data,
            @Nonnull Projection projection,
            @Nonnull Multiplicity multiplicity,
            @Nonnull Instant transactionTimestamp)
    {
        this.data = data;
        this.projection = Objects.requireNonNull(projection);
        this.multiplicity = Objects.requireNonNull(multiplicity);
        this.transactionTimestamp = Objects.requireNonNull(transactionTimestamp);
    }

    public KlassResponseBuilder setPagination(
            int pageSize,
            int numberOfPages,
            int pageNumber)
    {
        if (!this.multiplicity.isToMany())
        {
            throw new IllegalStateException();
        }

        KlassResponsePagination klassResponsePagination = new KlassResponsePagination(
                pageSize,
                numberOfPages,
                pageNumber);
        return this.setPagination(klassResponsePagination);
    }

    @Nonnull
    public KlassResponseBuilder setPagination(KlassResponsePagination klassResponsePagination)
    {
        this.pagination = Optional.of(klassResponsePagination);
        return this;
    }

    public KlassResponseBuilder setPrincipal(@Nonnull Principal principal)
    {
        return this.setPrincipal(Optional.of(principal));
    }

    public KlassResponseBuilder setPrincipal(@Nonnull Optional<? extends Principal> principal)
    {
        this.principal = Objects.requireNonNull(principal);
        return this;
    }

    public KlassResponse build()
    {
        KlassResponseMetadata metadata = new KlassResponseMetadata(
                this.projection,
                this.multiplicity,
                this.transactionTimestamp,
                this.pagination,
                this.principal);
        return new KlassResponse(metadata, this.data);
    }
}
