package cool.klass.reladomo.persistent.writer.context;

import java.util.Objects;

import cool.klass.model.meta.domain.api.property.AssociationEnd;
import org.eclipse.collections.api.list.ImmutableList;

public class AssociationEndErrorContext implements ErrorContext
{
    private final AssociationEnd        associationEnd;
    private final ImmutableList<Object> keys;

    public AssociationEndErrorContext(AssociationEnd associationEnd, ImmutableList<Object> keys)
    {
        this.associationEnd = Objects.requireNonNull(associationEnd);
        this.keys = Objects.requireNonNull(keys);
    }

    public AssociationEnd getAssociationEnd()
    {
        return this.associationEnd;
    }

    public ImmutableList<Object> getKeys()
    {
        return this.keys;
    }

    @Override
    public String toString()
    {
        return this.associationEnd.getName() + this.keys;
    }
}
