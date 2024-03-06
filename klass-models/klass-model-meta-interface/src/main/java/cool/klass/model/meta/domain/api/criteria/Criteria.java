package cool.klass.model.meta.domain.api.criteria;

import cool.klass.model.meta.domain.api.Element;

public interface Criteria
        extends Element
{
    void visit(CriteriaVisitor visitor);
}
