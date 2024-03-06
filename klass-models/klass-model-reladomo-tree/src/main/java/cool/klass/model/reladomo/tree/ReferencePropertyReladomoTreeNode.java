package cool.klass.model.reladomo.tree;

import java.util.Objects;

import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.property.ReferenceProperty;

public class ReferencePropertyReladomoTreeNode
        extends AbstractReladomoTreeNode
{
    private final ReferenceProperty referenceProperty;

    public ReferencePropertyReladomoTreeNode(String name, ReferenceProperty referenceProperty)
    {
        super(name);
        this.referenceProperty = Objects.requireNonNull(referenceProperty);
    }

    @Override
    public Classifier getOwningClassifier()
    {
        return this.referenceProperty.getOwningClassifier();
    }

    @Override
    public Classifier getType()
    {
        return this.referenceProperty.getType();
    }
}
