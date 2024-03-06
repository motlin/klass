package cool.klass.model.reladomo.tree;

import cool.klass.model.meta.domain.api.Classifier;
import org.eclipse.collections.api.factory.Lists;
import org.eclipse.collections.api.factory.Stacks;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.api.stack.MutableStack;

public interface ReladomoTreeNode
{
    String getName();

    Classifier getOwningClassifier();

    Classifier getType();

    MutableMap<String, ReladomoTreeNode> getChildren();

    ReladomoTreeNode computeChild(String name, ReladomoTreeNode node);

    default String getShortString()
    {
        return '.' + this.getName() + "()";
    }

    default String getNodeString()
    {
        return this.getOwningClassifier().getName() + this.getShortString() + ": " + this.getType().getName();
    }

    default boolean isLeaf()
    {
        return this.getChildren().isEmpty();
    }

    default ImmutableList<String> getDeepFetchStrings()
    {
        if (this.isLeaf())
        {
            return Lists.immutable.empty();
        }
        MutableList<String> result = Lists.mutable.empty();
        MutableStack<String> stack = Stacks.mutable.empty();
        this.getDeepFetchStrings(result, stack);
        return result.toImmutable();
    }

    private void getDeepFetchStrings(
            MutableList<String> result,
            MutableStack<String> stack)
    {
        stack.push(this.getShortString());
        if (this.isLeaf())
        {
            String string = stack.toList().asReversed().makeString("");
            result.add(string);
        }
        else
        {
            for (ReladomoTreeNode child : this.getChildren())
            {
                child.getDeepFetchStrings(result, stack);
            }
        }
        stack.pop();
    }
}
