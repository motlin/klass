package cool.klass.model.reladomo.projection;

import java.util.Objects;

import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.projection.Projection;

public class RootReladomoNode
        extends AbstractProjectionElementReladomoNode
{
    private final Klass klass;
    private final Projection projection;

    public RootReladomoNode(String name, Klass klass, Projection projection)
    {
        super(name);
        this.klass = Objects.requireNonNull(klass);
        this.projection = Objects.requireNonNull(projection);
    }

    @Override
    public Classifier getOwningClassifier()
    {
        return this.klass;
    }

    @Override
    public Klass getType()
    {
        return this.klass;
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
}
