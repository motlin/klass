package cool.klass.data.store;

@FunctionalInterface
public interface TransactionalCommand<Result>
{
    Result run(Transaction transaction);
}
