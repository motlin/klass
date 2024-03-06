package cool.klass.reladomo.persistent.writer.context;

import java.util.Objects;

import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.tuple.Pair;

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

    @Override
    public String toString()
    {
        ImmutableList<DataTypeProperty> keyProperties = this.klass.getKeyProperties();
        if (keyProperties.size() != this.keys.size())
        {
            throw new AssertionError();
        }

        String keysContext = keyProperties
                .asLazy()
                .zip(this.keys)
                .collect(KlassErrorContext::toString)
                .makeString();

        return String.format(
                "%s{%s}",
                this.klass.getName(),
                keysContext);
    }

    private static String toString(Pair<DataTypeProperty, Object> pair)
    {
        DataTypeProperty dataTypeProperty = pair.getOne();
        Object           key              = pair.getTwo();
        return String.format("%s=%s", dataTypeProperty.getName(), key);
    }
}
