package cool.klass.model.meta.domain.api.projection;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public interface ProjectionParent extends ProjectionElement
{
    @Nonnull
    Klass getKlass();

    default void visitChildren(ProjectionListener projectionListener)
    {
        for (ProjectionElement projectionElement : this.getChildren())
        {
            projectionElement.visit(projectionListener);
        }
    }

    default ImmutableList<ProjectionAssociationEnd> getAssociationEndChildren()
    {
        return this.getChildren().selectInstancesOf(ProjectionAssociationEnd.class);
    }

    default ImmutableList<AssociationEnd> getAssociationEnds()
    {
        return this.getAssociationEndChildren().collect(ProjectionAssociationEnd::getAssociationEnd);
    }

    default ImmutableList<AssociationEnd> getAssociationEndsOutsideProjection()
    {
        ImmutableList<AssociationEnd> associationEndsInProjection = this.getAssociationEnds();

        ImmutableList<AssociationEnd> optionalReturnPath = Lists.immutable.with(this)
                .selectInstancesOf(ProjectionAssociationEnd.class)
                .collect(ProjectionAssociationEnd::getAssociationEnd)
                .collect(AssociationEnd::getOpposite);

        return this.getKlass()
                .getAssociationEnds()
                .reject(associationEndsInProjection::contains)
                .reject(optionalReturnPath::contains);
    }
}
