package cool.klass.model.meta.domain.api.value;

import cool.klass.model.meta.domain.api.Element;

public interface ExpressionValue
        extends Element
{
    void visit(ExpressionValueVisitor visitor);
}
