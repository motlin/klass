package cool.klass.model.reladomo.projection;

import java.util.Objects;

import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.DataType;
import cool.klass.model.meta.domain.api.projection.ProjectionDataTypeProperty;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;

public class ProjectionDataTypePropertyReladomoNode
        extends AbstractProjectionElementReladomoNode
{
    private final ProjectionDataTypeProperty projectionDataTypeProperty;

    public ProjectionDataTypePropertyReladomoNode(String name, ProjectionDataTypeProperty projectionDataTypeProperty)
    {
        super(name);
        this.projectionDataTypeProperty = Objects.requireNonNull(projectionDataTypeProperty);
    }

    @Override
    public Classifier getOwningClassifier()
    {
        return this.projectionDataTypeProperty.getProperty().getOwningClassifier();
    }

    @Override
    public DataType getType()
    {
        return this.projectionDataTypeProperty.getProperty().getType();
    }

    public DataTypeProperty getProperty()
    {
        return this.projectionDataTypeProperty.getProperty();
    }
}
