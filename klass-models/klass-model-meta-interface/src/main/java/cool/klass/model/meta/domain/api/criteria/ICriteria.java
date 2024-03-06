package cool.klass.model.meta.domain.api.criteria;

import cool.klass.model.meta.domain.api.IElement;

public interface ICriteria extends IElement
{
    void visit(CriteriaVisitor visitor);
}
