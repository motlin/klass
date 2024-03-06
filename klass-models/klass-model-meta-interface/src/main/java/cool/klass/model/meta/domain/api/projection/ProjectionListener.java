package cool.klass.model.meta.domain.api.projection;

public interface ProjectionListener
{
    void enterProjection(IProjection projection);

    void exitProjection(IProjection projection);

    void enterProjectionAssociationEnd(ProjectionAssociationEnd projectionAssociationEnd);

    void exitProjectionAssociationEnd(ProjectionAssociationEnd projectionAssociationEnd);

    void enterProjectionDataTypeProperty(IProjectionDataTypeProperty projectionDataTypeProperty);

    void exitProjectionDataTypeProperty(IProjectionDataTypeProperty projectionDataTypeProperty);
}
