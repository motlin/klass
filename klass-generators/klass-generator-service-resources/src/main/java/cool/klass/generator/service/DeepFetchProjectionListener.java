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
    private final MutableStack<ProjectionAssociationEnd> associationEndStack = Stacks.mutable.empty();

    private final MutableStack<String> stringStack = Stacks.mutable.empty();
    private final MutableList<String>  result      = Lists.mutable.empty();
    private       Klass                owningClassifier;

    @Override
    public void enterProjectionAssociationEnd(ProjectionAssociationEnd projectionAssociationEnd)
    {
        if (this.stringStack.isEmpty())
        {
            if (this.owningClassifier != null)
            {
                throw new AssertionError();
            }
            this.owningClassifier = projectionAssociationEnd.getAssociationEnd().getOwningClassifier();
        }
        this.associationEndStack.push(projectionAssociationEnd);
        this.stringStack.push(projectionAssociationEnd.getAssociationEnd().getName());
    }

    @Override
    public void exitProjectionAssociationEnd(@Nonnull ProjectionAssociationEnd projectionAssociationEnd)
    {
        // TODO: Figure out how to deep fetch polymorphic projection properties
        if (this.isLeaf(projectionAssociationEnd) && this.associationEndStack.noneSatisfy(ProjectionAssociationEnd::isPolymorphic))
        {
            String string = this.stringStack
                    .toList()
                    .asReversed()
                    .collect(each -> each + "()")
                    .makeString(".");
            String navigation = String.format("%sFinder.%s", this.owningClassifier.getName(), string);
            this.result.add(navigation);
        }
        this.associationEndStack.pop();
        this.stringStack.pop();
        if (this.stringStack.isEmpty())
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
