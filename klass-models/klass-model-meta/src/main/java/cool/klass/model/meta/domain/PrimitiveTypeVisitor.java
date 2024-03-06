package cool.klass.model.meta.domain;

public interface PrimitiveTypeVisitor
{
    void visitID();

    void visitString();

    void visitInteger();

    void visitLong();

    void visitDouble();

    void visitFloat();

    void visitBoolean();

    void visitInstant();

    void visitLocalDate();
}
