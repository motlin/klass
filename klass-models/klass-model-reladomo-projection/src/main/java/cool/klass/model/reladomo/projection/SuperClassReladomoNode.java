package cool.klass.model.reladomo.projection;

import java.util.Objects;

import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.Klass;

public class SuperClassReladomoNode
        extends AbstractProjectionElementReladomoNode
{
    private final Klass klass;
    private final Klass superClass;

    public SuperClassReladomoNode(String name, Klass klass, Klass superClass)
    {
        super(name);
        this.klass      = Objects.requireNonNull(klass);
        this.superClass = Objects.requireNonNull(superClass);
    }

    @Override
    public Classifier getOwningClassifier()
    {
        return this.klass;
    }

    @Override
    public Classifier getType()
    {
        return this.superClass;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || this.getClass() != o.getClass())
        {
            return false;
        }

        SuperClassReladomoNode that = (SuperClassReladomoNode) o;

        return this.klass.equals(that.klass) && this.superClass.equals(that.superClass);
    }

    @Override
    public int hashCode()
    {
        int result = this.klass.hashCode();
        result = 31 * result + this.superClass.hashCode();
        return result;
    }
}
