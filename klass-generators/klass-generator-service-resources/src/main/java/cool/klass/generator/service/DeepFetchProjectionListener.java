package cool.klass.generator.service;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.projection.BaseProjectionListener;
import cool.klass.model.meta.domain.api.projection.ProjectionAssociationEnd;
import cool.klass.model.meta.domain.api.projection.ProjectionProjectionReference;
import cool.klass.model.meta.domain.api.projection.ProjectionWithAssociationEnd;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.stack.MutableStack;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.Stacks;

public final class DeepFetchProjectionListener extends BaseProjectionListener
{
    private final MutableStack<ProjectionWithAssociationEnd> associationEndStack = Stacks.mutable.empty();

    private final MutableStack<String> stringStack = Stacks.mutable.empty();
    private final MutableList<String>  result      = Lists.mutable.empty();
    private       Klass                owningClassifier;

    // TODO: Figure out how to deep fetch polymorphic projection properties

    @Override
    public void enterProjectionAssociationEnd(@Nonnull ProjectionAssociationEnd projectionAssociationEnd)
    {
        this.enterProjectionWithAssociationEnd(projectionAssociationEnd);
    }

    @Override
    public void exitProjectionAssociationEnd(@Nonnull ProjectionAssociationEnd projectionAssociationEnd)
    {
        this.exitProjectionWithAssociationEnd(projectionAssociationEnd);
    }

    @Override
    public void enterProjectionProjectionReference(@Nonnull ProjectionProjectionReference projectionProjectionReference)
    {
        this.enterProjectionWithAssociationEnd(projectionProjectionReference);
    }

    @Override
    public void exitProjectionProjectionReference(@Nonnull ProjectionProjectionReference projectionProjectionReference)
    {
        this.exitProjectionWithAssociationEnd(projectionProjectionReference);
    }

    public ImmutableList<String> getResult()
    {
        return this.result.toImmutable();
    }

    private void enterProjectionWithAssociationEnd(@Nonnull ProjectionWithAssociationEnd projectionWithAssociationEnd)
    {
        if (this.stringStack.isEmpty())
        {
            if (this.owningClassifier != null)
            {
                throw new AssertionError();
            }
            this.owningClassifier = projectionWithAssociationEnd.getProperty().getOwningClassifier();
        }
        this.associationEndStack.push(projectionWithAssociationEnd);
        this.stringStack.push(projectionWithAssociationEnd.getProperty().getName());
    }

    private void exitProjectionWithAssociationEnd(@Nonnull ProjectionWithAssociationEnd projectionWithAssociationEnd)
    {
        // TODO: Figure out how to deep fetch polymorphic projection properties
        if (projectionWithAssociationEnd.isLeaf()
                && this.associationEndStack.noneSatisfy(ProjectionWithAssociationEnd::isPolymorphic))
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
}
