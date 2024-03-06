package cool.klass.model.meta.domain.api.projection;

public interface ProjectionVisitor
{
    void visitProjection(Projection projection);

    void visitProjectionReferenceProperty(ProjectionReferenceProperty projectionReferenceProperty);

    void visitProjectionProjectionReference(ProjectionProjectionReference projectionProjectionReference);

    void visitProjectionDataTypeProperty(ProjectionDataTypeProperty projectionDataTypeProperty);
}
