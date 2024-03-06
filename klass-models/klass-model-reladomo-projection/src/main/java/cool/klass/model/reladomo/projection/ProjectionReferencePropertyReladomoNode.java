package cool.klass.model.reladomo.projection;

import java.util.Objects;

import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.projection.ProjectionReferenceProperty;

public class ProjectionReferencePropertyReladomoNode
        extends AbstractProjectionElementReladomoNode
{
    private final ProjectionReferenceProperty projectionReferenceProperty;

    public ProjectionReferencePropertyReladomoNode(String name, ProjectionReferenceProperty projectionReferenceProperty)
    {
        super(name);
        this.projectionReferenceProperty = Objects.requireNonNull(projectionReferenceProperty);
    }

    @Override
    public Classifier getOwningClassifier()
    {
        return this.projectionReferenceProperty.getProperty().getOwningClassifier();
    }

    @Override
    public Classifier getType()
    {
        return this.projectionReferenceProperty.getProperty().getType();
    }
}
