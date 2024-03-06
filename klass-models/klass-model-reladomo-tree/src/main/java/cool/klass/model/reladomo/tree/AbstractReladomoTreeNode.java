package cool.klass.model.reladomo.tree;

import java.util.LinkedHashMap;
import java.util.Objects;

import cool.klass.model.meta.domain.api.Klass;
import org.eclipse.collections.api.map.MapIterable;
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
    public MapIterable<String, ReladomoTreeNode> getChildren()
    {
        return this.children.asUnmodifiable();
    }

    @Override
    public ReladomoTreeNode computeChild(String childName, ReladomoTreeNode childNode)
    {
        if (this.getType() != childNode.getOwningClassifier() && childNode.getOwningClassifier() instanceof Klass)
        {
            String detailMessage = "Type mismatch: %s != %s for %s".formatted(
                    this.getType(),
                    childNode.getOwningClassifier(),
                    childName);
            throw new AssertionError(detailMessage);
        }
        ReladomoTreeNode result = this.children.getIfAbsentPut(childName, childNode);
        if (!Objects.equals(result, childNode))
        {
            String detailMessage = "Expected %s but got %s for %s".formatted(result, childNode, childName);
            throw new AssertionError(detailMessage);
        }
        return result;
    }

    @Override
    public String toString()
    {
        return this.getNodeString();
    }
}
