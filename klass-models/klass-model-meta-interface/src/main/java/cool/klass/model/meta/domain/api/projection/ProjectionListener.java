package cool.klass.model.meta.domain.api.projection;

public interface ProjectionListener
{
    void enterProjection(Projection projection);

    void exitProjection(Projection projection);

    void enterProjectionAssociationEnd(ProjectionAssociationEnd projectionAssociationEnd);

    void exitProjectionAssociationEnd(ProjectionAssociationEnd projectionAssociationEnd);

    void enterProjectionProjectionReference(ProjectionProjectionReference projectionProjectionReference);

    void exitProjectionProjectionReference(ProjectionProjectionReference projectionProjectionReference);

    void enterProjectionDataTypeProperty(ProjectionDataTypeProperty projectionDataTypeProperty);

    void exitProjectionDataTypeProperty(ProjectionDataTypeProperty projectionDataTypeProperty);
}
