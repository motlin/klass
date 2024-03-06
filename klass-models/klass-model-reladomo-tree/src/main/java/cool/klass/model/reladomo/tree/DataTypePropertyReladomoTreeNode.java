package cool.klass.model.reladomo.tree;

import java.util.Objects;

import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.DataType;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;

public class DataTypePropertyReladomoTreeNode
        extends AbstractReladomoTreeNode
{
    private final DataTypeProperty dataTypeProperty;

    public DataTypePropertyReladomoTreeNode(String name, DataTypeProperty dataTypeProperty)
    {
        super(name);
        this.dataTypeProperty = Objects.requireNonNull(dataTypeProperty);
    }

    @Override
    public void visit(ReladomoTreeNodeVisitor visitor)
    {
        visitor.visitDataTypeProperty(this);
    }

    public DataTypeProperty getDataTypeProperty()
    {
        return this.dataTypeProperty;
    }

    @Override
    public Classifier getOwningClassifier()
    {
        return this.dataTypeProperty.getOwningClassifier();
    }

    @Override
    public DataType getType()
    {
        return this.dataTypeProperty.getType();
    }
}
