package cool.klass.model.meta.domain.api.projection;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.ReferenceProperty;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public interface ProjectionParent
        extends ProjectionElement
{
    @Nonnull
    Classifier getClassifier();

    default void visitChildren(ProjectionListener projectionListener)
    {
        for (ProjectionElement projectionElement : this.getChildren())
        {
            projectionElement.visit(projectionListener);
        }
    }

    default ImmutableList<ProjectionReferenceProperty> getReferencePropertyChildren()
    {
        return this.getChildren().selectInstancesOf(ProjectionReferenceProperty.class);
    }

    default ImmutableList<ReferenceProperty> getReferenceProperties()
    {
        return this.getReferencePropertyChildren().collect(ProjectionReferenceProperty::getProperty);
    }

    default ImmutableList<AssociationEnd> getAssociationEndsOutsideProjection()
    {
        ImmutableList<ReferenceProperty> referencePropertiesInProjection = this.getReferenceProperties();

        ImmutableList<AssociationEnd> optionalReturnPath = Lists.immutable.with(this)
                .selectInstancesOf(ProjectionReferenceProperty.class)
                .collect(ProjectionReferenceProperty::getProperty)
                .selectInstancesOf(AssociationEnd.class)
                .collect(AssociationEnd::getOpposite);

        if (this.getClassifier() instanceof Klass)
        {
            return ((Klass) this.getClassifier())
                    .getAssociationEnds()
                    .reject(referencePropertiesInProjection::contains)
                    .reject(optionalReturnPath::contains);
        }

        throw new AssertionError(this.getClassifier());
    }

    default boolean hasPolymorphicChildren()
    {
        return this.getChildren()
                .asLazy()
                .selectInstancesOf(ProjectionChild.class)
                .anySatisfy(ProjectionChild::isPolymorphic);
    }
}
