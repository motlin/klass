package cool.klass.generator.service;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.projection.BaseProjectionListener;
import cool.klass.model.meta.domain.api.projection.ProjectionAssociationEnd;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.stack.MutableStack;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Stacks;

public final class DeepFetchProjectionListener extends BaseProjectionListener
{
    private final MutableStack<String> stack  = Stacks.mutable.empty();
    private final MutableList<String>  result = Lists.mutable.empty();
    private       Klass                owningClassifier;

    @Override
    public void enterProjectionAssociationEnd(ProjectionAssociationEnd projectionAssociationEnd)
    {
        if (this.stack.isEmpty())
        {
            if (this.owningClassifier != null)
            {
                throw new AssertionError();
            }
            this.owningClassifier = projectionAssociationEnd.getAssociationEnd().getOwningClassifier();
        }
        this.stack.push(projectionAssociationEnd.getAssociationEnd().getName());
    }

    @Override
    public void exitProjectionAssociationEnd(@Nonnull ProjectionAssociationEnd projectionAssociationEnd)
    {
        if (this.isLeaf(projectionAssociationEnd))
        {
            String string = this.stack
                    .toList()
                    .asReversed()
                    .collect(each -> each + "()")
                    .makeString(".");
            String navigation = String.format("%sFinder.%s", this.owningClassifier.getName(), string);
            this.result.add(navigation);
        }
        this.stack.pop();
        if (this.stack.isEmpty())
        {
            Objects.requireNonNull(this.owningClassifier);
            this.owningClassifier = null;
        }
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
