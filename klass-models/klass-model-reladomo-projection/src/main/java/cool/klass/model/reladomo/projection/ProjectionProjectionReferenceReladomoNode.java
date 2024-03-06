package cool.klass.model.reladomo.projection;

import java.util.Objects;

import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.projection.ProjectionProjectionReference;
import cool.klass.model.meta.domain.api.property.ReferenceProperty;

public class ProjectionProjectionReferenceReladomoNode
        extends ProjectionWithReferencePropertyReladomoNode
{
    private final ProjectionProjectionReference projectionProjectionReference;

    public ProjectionProjectionReferenceReladomoNode(
            String name,
            ProjectionProjectionReference projectionProjectionReference)
    {
        super(name);
        this.projectionProjectionReference = Objects.requireNonNull(projectionProjectionReference);
    }

    @Override
    public Classifier getOwningClassifier()
    {
        return this.projectionProjectionReference.getProperty().getOwningClassifier();
    }

    @Override
    public Classifier getType()
    {
        return this.projectionProjectionReference.getProperty().getType();
    }

    @Override
    public ReferenceProperty getReferenceProperty()
    {
        return this.projectionProjectionReference.getProperty();
    }

    @Override
    public String getNodeString()
    {
        return super.getNodeString() + " -> " + this.projectionProjectionReference.getProjection().getName();
    }
}
