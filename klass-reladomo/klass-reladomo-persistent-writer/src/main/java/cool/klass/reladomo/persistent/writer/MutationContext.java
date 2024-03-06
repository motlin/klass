package cool.klass.reladomo.persistent.writer;

import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

public class MutationContext
{
    private final Optional<String> userId;
    private final Instant          transactionTime;

    public MutationContext(Optional<String> userId, Instant transactionTime)
    {
        this.userId = Objects.requireNonNull(userId);
        this.transactionTime = Objects.requireNonNull(transactionTime);
    }

    public Optional<String> getUserId()
    {
        return this.userId;
    }

    public Instant getTransactionTime()
    {
        return this.transactionTime;
    }
}
