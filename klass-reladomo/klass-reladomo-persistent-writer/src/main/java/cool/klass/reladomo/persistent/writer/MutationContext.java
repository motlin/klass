package cool.klass.reladomo.persistent.writer;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import org.eclipse.collections.api.map.ImmutableMap;

public class MutationContext
{
    private final Optional<String>                       userId;
    private final Instant                                transactionTime;
    private final ImmutableMap<DataTypeProperty, Object> propertyDataFromUrl;

    public MutationContext(
            @Nonnull Optional<String> userId,
            @Nonnull Instant transactionTime,
            @Nonnull ImmutableMap<DataTypeProperty, Object> propertyDataFromUrl)
    {
        this.userId              = Objects.requireNonNull(userId);
        this.transactionTime     = Objects.requireNonNull(transactionTime);
        this.propertyDataFromUrl = Objects.requireNonNull(propertyDataFromUrl);
    }

    public Optional<String> getUserId()
    {
        return this.userId;
    }

    public Instant getTransactionTime()
    {
        return this.transactionTime;
    }

    public ImmutableMap<DataTypeProperty, Object> getPropertyDataFromUrl()
    {
        return this.propertyDataFromUrl;
    }
}
