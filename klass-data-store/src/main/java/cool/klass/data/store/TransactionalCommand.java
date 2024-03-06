package cool.klass.data.store;

public interface TransactionalCommand<Result>
{
    Result run(Transaction transaction);
}
