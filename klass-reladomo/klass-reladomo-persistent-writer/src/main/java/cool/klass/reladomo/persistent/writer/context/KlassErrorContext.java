package cool.klass.reladomo.persistent.writer.context;

import java.util.Objects;

import cool.klass.model.meta.domain.api.Klass;
import org.eclipse.collections.api.list.ImmutableList;

public class KlassErrorContext implements ErrorContext
{
    private final Klass                 klass;
    private final ImmutableList<Object> keys;

    public KlassErrorContext(Klass klass, ImmutableList<Object> keys)
    {
        this.klass = Objects.requireNonNull(klass);
        this.keys = Objects.requireNonNull(keys);
    }

    public Klass getKlass()
    {
        return this.klass;
    }

    public ImmutableList<Object> getKeys()
    {
        return this.keys;
    }

    @Override
    public String toString()
    {
        return String.format(
                "%s{%s}",
                this.klass.getName(),
                this.keys.makeString());
    }
}
