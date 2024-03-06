package cool.klass.model.reladomo.tree;

import java.util.LinkedHashMap;
import java.util.Objects;

import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.map.mutable.MapAdapter;

public abstract class AbstractReladomoTreeNode
        implements ReladomoTreeNode
{
    protected final MutableMap<String, ReladomoTreeNode> children = MapAdapter.adapt(new LinkedHashMap<>());

    private final String name;

    protected AbstractReladomoTreeNode(String name)
    {
        this.name = Objects.requireNonNull(name);
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public MutableMap<String, ReladomoTreeNode> getChildren()
    {
        return this.children;
    }

    @Override
    public ReladomoTreeNode computeChild(String name, ReladomoTreeNode child)
    {
        if (this.getType() != child.getOwningClassifier())
        {
            throw new AssertionError(this.getType() + " != " + child.getOwningClassifier());
        }
        ReladomoTreeNode result = this.children.getIfAbsentPut(name, child);
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
