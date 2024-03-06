package cool.klass.deserializer.json.context;

import java.util.Objects;

import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.tuple.Pair;

public class AssociationEndErrorContext implements ErrorContext
{
    private final AssociationEnd        associationEnd;
    private final ImmutableList<Object> keys;

    public AssociationEndErrorContext(AssociationEnd associationEnd, ImmutableList<Object> keys)
    {
        this.associationEnd = Objects.requireNonNull(associationEnd);
        this.keys = Objects.requireNonNull(keys);
    }

    @Override
    public String toString()
    {
        ImmutableList<DataTypeProperty> keyProperties = this.associationEnd.getType().getKeyProperties();
        if (keyProperties.size() != this.keys.size())
        {
            throw new AssertionError();
        }

        String keysContext = keyProperties
                .asLazy()
                .zip(this.keys)
                .collect(AssociationEndErrorContext::toString)
                .makeString();

        String format = this.associationEnd.getMultiplicity().isToMany() ? "%s[%s]" : "%s{%s}";

        return String.format(
                format,
                this.associationEnd.getName(),
                keysContext);
    }

    private static String toString(Pair<DataTypeProperty, Object> pair)
    {
        DataTypeProperty dataTypeProperty = pair.getOne();
        Object           key              = pair.getTwo();
        return String.format("%s=%s", dataTypeProperty.getName(), key);
    }
}
