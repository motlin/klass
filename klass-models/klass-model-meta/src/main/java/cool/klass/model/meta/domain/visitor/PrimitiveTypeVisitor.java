package cool.klass.model.meta.domain.visitor;

public interface PrimitiveTypeVisitor
{
    void visitString() throws Exception;

    void visitInteger() throws Exception;

    void visitLong() throws Exception;

    void visitDouble() throws Exception;

    void visitFloat() throws Exception;

    void visitBoolean() throws Exception;

    void visitInstant() throws Exception;

    void visitLocalDate() throws Exception;

    void visitTemporalInstant() throws Exception;

    void visitTemporalRange() throws Exception;
}
