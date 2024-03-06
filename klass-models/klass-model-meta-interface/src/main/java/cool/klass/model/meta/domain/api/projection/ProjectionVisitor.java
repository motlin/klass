package cool.klass.model.meta.domain.api.projection;

public interface ProjectionVisitor
{
    void visitProjection(Projection projection);

    void visitProjectionAssociationEnd(ProjectionAssociationEnd projectionAssociationEnd);

    void visitProjectionProjectionReference(ProjectionProjectionReference projectionProjectionReference);

    void visitProjectionDataTypeProperty(ProjectionDataTypeProperty projectionDataTypeProperty);
}
