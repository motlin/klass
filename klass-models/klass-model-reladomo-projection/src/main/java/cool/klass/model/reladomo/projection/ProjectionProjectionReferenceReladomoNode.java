package cool.klass.model.reladomo.projection;

import java.util.Objects;

import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.projection.ProjectionProjectionReference;

public class ProjectionProjectionReferenceReladomoNode
        extends AbstractProjectionElementReladomoNode
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
        return this.projectionProjectionReference.getProjection().getClassifier();
    }
}
