package cool.klass.deserializer.json.context;

import java.util.Objects;

import cool.klass.model.meta.domain.api.property.AssociationEnd;
import org.eclipse.collections.api.list.ImmutableList;

public class AssociationEndWithIndexErrorContext implements ErrorContext
{
    private final AssociationEnd        associationEnd;
    private final int                   index;
    private final ImmutableList<Object> keys;

    public AssociationEndWithIndexErrorContext(
            AssociationEnd associationEnd,
            int index,
            ImmutableList<Object> keys)
    {
        this.associationEnd = Objects.requireNonNull(associationEnd);
        this.index = index;
        this.keys = keys;
    }

    public AssociationEnd getAssociationEnd()
    {
        return this.associationEnd;
    }

    public int getIndex()
    {
        return this.index;
    }

    public ImmutableList<Object> getKeys()
    {
        return this.keys;
    }

    @Override
    public String toString()
    {
        return String.format(
                "%s%s(%d)",
                this.associationEnd.getName(),
                this.keys,
                this.index);
    }
}
