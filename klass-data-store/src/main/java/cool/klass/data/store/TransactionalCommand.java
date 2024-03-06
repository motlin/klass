package cool.klass.data.store;

public interface TransactionalCommand
{
    void run(Transaction transaction);
}
