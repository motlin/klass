package cool.klass.model.reladomo.projection;

import java.util.LinkedHashMap;
import java.util.Objects;

import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.map.mutable.MapAdapter;

public abstract class AbstractProjectionElementReladomoNode
        implements ProjectionElementReladomoNode
{
    protected final MutableMap<String, ProjectionElementReladomoNode> children = MapAdapter.adapt(new LinkedHashMap<>());

    private final String name;

    protected AbstractProjectionElementReladomoNode(String name)
    {
        this.name = Objects.requireNonNull(name);
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public MutableMap<String, ProjectionElementReladomoNode> getChildren()
    {
        return this.children;
    }

    @Override
    public ProjectionElementReladomoNode computeChild(String name, ProjectionElementReladomoNode child)
    {
        if (this.getType() != child.getOwningClassifier())
        {
            throw new AssertionError(this.getType() + " != " + child.getOwningClassifier());
        }
        ProjectionElementReladomoNode result = this.children.getIfAbsentPut(name, child);
        if (result != child)
        {
            if (!result.equals(child))
            {
                throw new AssertionError();
            }
        }
        return result;
    }

    @Override
    public String toString()
    {
        return this.getNodeString();
    }
}
