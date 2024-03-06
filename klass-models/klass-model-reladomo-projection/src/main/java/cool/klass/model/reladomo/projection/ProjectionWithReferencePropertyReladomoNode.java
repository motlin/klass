package cool.klass.model.reladomo.projection;

import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.property.ReferenceProperty;

public abstract class ProjectionWithReferencePropertyReladomoNode
        extends AbstractProjectionElementReladomoNode
{
    public ProjectionWithReferencePropertyReladomoNode(String name)
    {
        super(name);
    }

    @Override
    public abstract Classifier getOwningClassifier();

    @Override
    public abstract Classifier getType();

    public abstract ReferenceProperty getReferenceProperty();
}
