package cool.klass.generator.service;

import java.util.Objects;

import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.stack.MutableStack;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Stacks;

public class DeepFetchWalker
{
    private final MutableStack<AssociationEnd> associationEndStack = Stacks.mutable.empty();

    private final MutableStack<String> stringStack = Stacks.mutable.empty();
    private final MutableList<String>  result      = Lists.mutable.empty();

    private final Klass klass;

    public DeepFetchWalker(Klass klass)
    {
        this.klass = Objects.requireNonNull(klass);
    }

    public ImmutableList<String> getResult()
    {
        return this.result.toImmutable();
    }

    // TODO: Figure out how to deep fetch polymorphic projection properties

    public void walk()
    {
        this.klass.getAssociationEnds()
                .select(AssociationEnd::isOwned)
                .each(this::handleAssociationEnd);
    }

    public static ImmutableList<String> walk(Klass klass)
    {
        DeepFetchWalker deepFetchWalker = new DeepFetchWalker(klass);
        deepFetchWalker.walk();
        return deepFetchWalker.getResult();
    }

    private void handleAssociationEnd(AssociationEnd associationEnd)
    {
        this.associationEndStack.push(associationEnd);
        this.stringStack.push(associationEnd.getName());

        // TODO: Figure out how to deep fetch polymorphic projection properties
        if (this.isLeaf(associationEnd))
        {
            String string = this.stringStack
                    .toList()
                    .asReversed()
                    .collect(each -> each + "()")
                    .makeString(".");
            String navigation = String.format("%sFinder.%s", this.klass.getName(), string);
            this.result.add(navigation);
        }
        this.associationEndStack.pop();
        this.stringStack.pop();
    }

    private boolean isLeaf(AssociationEnd associationEnd)
    {
        return associationEnd.getType()
                .getAssociationEnds()
                .noneSatisfy(AssociationEnd::isOwned);
    }
}
