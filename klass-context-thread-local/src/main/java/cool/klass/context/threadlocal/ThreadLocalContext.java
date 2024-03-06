package cool.klass.context.threadlocal;

public class ThreadLocalContext<T> extends InheritableThreadLocal<T>
{
    public ThreadLocalCloseable withContext(T value)
    {
        T oldValue = this.get();
        if (oldValue != null)
        {
            throw new AssertionError(oldValue);
        }
        this.set(value);
        return new ThreadLocalCloseable();
    }

    public class ThreadLocalCloseable implements AutoCloseable
    {
        @Override
        public void close()
        {
            ThreadLocalContext.this.remove();
        }
    }
}
