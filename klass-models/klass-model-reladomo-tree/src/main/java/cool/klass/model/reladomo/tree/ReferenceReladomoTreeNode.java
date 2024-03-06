package cool.klass.model.reladomo.tree;

import java.util.Objects;

import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.property.ReferenceProperty;

public class ReferenceReladomoTreeNode
        extends AbstractReladomoTreeNode
{
    private final ReladomoTreeNode  reladomoTreeNode;
    private final ReferenceProperty referenceProperty;

    public ReferenceReladomoTreeNode(
            String name,
            ReladomoTreeNode reladomoTreeNode,
            ReferenceProperty referenceProperty)
    {
        super(name);
        this.reladomoTreeNode  = Objects.requireNonNull(reladomoTreeNode);
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
        return this.reladomoTreeNode.getOwningClassifier();
    }
}
