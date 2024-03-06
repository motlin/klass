package cool.klass.generator.service;

import cool.klass.model.meta.domain.projection.BaseProjectionListener;
import cool.klass.model.meta.domain.projection.ProjectionAssociationEnd;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.stack.MutableStack;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Stacks;

public final class DeepFetchProjectionListener extends BaseProjectionListener
{
    private final MutableStack<String> stack  = Stacks.mutable.empty();
    private final MutableList<String>  result = Lists.mutable.empty();

    @Override
    public void enterProjectionAssociationEnd(ProjectionAssociationEnd projectionAssociationEnd)
    {
        this.stack.push(projectionAssociationEnd.getAssociationEnd().getName());
    }

    @Override
    public void exitProjectionAssociationEnd(ProjectionAssociationEnd projectionAssociationEnd)
    {
        if (this.isLeaf(projectionAssociationEnd))
        {
            String string = this.stack
                    .toList()
                    .asReversed()
                    .collect(each -> each + "()")
                    .makeString(".");
            this.result.add(string);
        }
        this.stack.pop();
    }

    public boolean isLeaf(ProjectionAssociationEnd projectionAssociationEnd)
    {
        return projectionAssociationEnd
                .getChildren()
                .asLazy()
                .selectInstancesOf(ProjectionAssociationEnd.class)
                .isEmpty();
    }

    public ImmutableList<String> getResult()
    {
        return this.result.toImmutable();
    }
}
