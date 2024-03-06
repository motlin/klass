package cool.klass.model.meta.domain.api.value;

import cool.klass.model.meta.domain.api.IElement;

public interface IExpressionValue extends IElement
{
    void visit(ExpressionValueVisitor visitor);
}
