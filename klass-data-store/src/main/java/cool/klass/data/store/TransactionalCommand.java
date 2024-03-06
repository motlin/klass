package cool.klass.data.store;

import javax.annotation.Nonnull;

public interface TransactionalCommand<Result>
{
    @Nonnull
    Result run(Transaction transaction);
}
