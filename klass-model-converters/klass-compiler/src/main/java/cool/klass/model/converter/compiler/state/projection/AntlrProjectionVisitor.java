package cool.klass.model.converter.compiler.state.projection;

public interface AntlrProjectionVisitor
{
    void visitProjectionReference(AntlrProjectionProjectionReference projectionReference);

    void visitReferenceProperty(AntlrProjectionReferenceProperty referenceProperty);

    void visitDataTypeProperty(AntlrProjectionDataTypeProperty dataTypeProperty);
}
