package cool.klass.generator.react.prop.type;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.projection.Projection;
import cool.klass.model.meta.domain.api.projection.ProjectionDataTypeProperty;
import cool.klass.model.meta.domain.api.projection.ProjectionElement;
import cool.klass.model.meta.domain.api.projection.ProjectionProjectionReference;
import cool.klass.model.meta.domain.api.projection.ProjectionReferenceProperty;
import cool.klass.model.meta.domain.api.projection.ProjectionVisitor;
import org.eclipse.collections.api.set.ImmutableSet;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Sets;

public class GatherProjectionReferencesVisitor
        implements ProjectionVisitor
{
    private final Projection             originalProjection;
    private final MutableSet<Projection> referencedProjections = Sets.mutable.empty();

    public GatherProjectionReferencesVisitor(Projection projection)
    {
        this.originalProjection = projection;
    }

    public ImmutableSet<Projection> getReferencedProjections()
    {
        return this.referencedProjections.toImmutable();
    }

    @Override
    public void visitProjection(@Nonnull Projection projection)
    {
        if (projection == this.originalProjection)
        {
            return;
        }

        boolean added = this.referencedProjections.add(projection);
        if (added)
        {
            projection.getChildren().forEachWith(ProjectionElement::visit, this);
        }
    }

    @Override
    public void visitProjectionReferenceProperty(@Nonnull ProjectionReferenceProperty projectionReferenceProperty)
    {
        projectionReferenceProperty.getChildren().forEachWith(ProjectionElement::visit, this);
    }

    @Override
    public void visitProjectionProjectionReference(@Nonnull ProjectionProjectionReference projectionProjectionReference)
    {
        projectionProjectionReference.getProjection().visit(this);
    }

    @Override
    public void visitProjectionDataTypeProperty(ProjectionDataTypeProperty projectionDataTypeProperty)
    {
        // Deliberately empty
    }
}
