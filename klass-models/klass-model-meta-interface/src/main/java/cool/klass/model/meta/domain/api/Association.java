package cool.klass.model.meta.domain.api;

import cool.klass.model.meta.domain.api.criteria.Criteria;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import org.eclipse.collections.api.list.ImmutableList;

public interface Association extends PackageableElement
{
    Criteria getCriteria();

    ImmutableList<AssociationEnd> getAssociationEnds();

    AssociationEnd getSourceAssociationEnd();

    AssociationEnd getTargetAssociationEnd();
}
