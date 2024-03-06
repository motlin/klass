package cool.klass.logging;

import java.util.Objects;

import org.slf4j.MDC;

public class MDCResource implements AutoCloseable
{
    private final String key;

    public MDCResource(String key, String value)
    {
        this.key = Objects.requireNonNull(key);
        MDC.put(key, value);
    }

    @Override
    public void close()
    {
        MDC.remove(this.key);
    }
}
