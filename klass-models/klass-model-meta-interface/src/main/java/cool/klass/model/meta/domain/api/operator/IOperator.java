package cool.klass.model.meta.domain.api.operator;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.IElement;

public interface IOperator extends IElement
{
    void visit(OperatorVisitor visitor);

    @Nonnull
    String getOperatorText();
}
