package cool.klass.model.reladomo.projection;

import java.util.Objects;

import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.projection.Projection;

public class RootReladomoNode
        extends AbstractProjectionElementReladomoNode
{
    private final Classifier classifier;
    private final Projection projection;

    public RootReladomoNode(String name, Classifier classifier, Projection projection)
    {
        super(name);
        this.classifier = Objects.requireNonNull(classifier);
        this.projection = Objects.requireNonNull(projection);
    }

    @Override
    public Classifier getOwningClassifier()
    {
        return this.classifier;
    }

    @Override
    public Classifier getType()
    {
        return this.classifier;
    }

    @Override
    public String getShortString()
    {
        return this.getType().getName() + "Finder";
    }

    @Override
    public String getNodeString()
    {
        return this.getShortString() + ": " + this.getType().getName();
    }

    public Projection getProjection()
    {
        return this.projection;
    }

    @Override
    public String toString()
    {
        return this.toString("");
    }
}
