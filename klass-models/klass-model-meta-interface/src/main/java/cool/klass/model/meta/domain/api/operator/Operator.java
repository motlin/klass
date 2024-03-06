package cool.klass.model.meta.domain.api.operator;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;

public interface Operator
        extends Element
{
    void visit(OperatorVisitor visitor);

    @Nonnull
    String getOperatorText();
}
