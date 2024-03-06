package cool.klass.model.meta.domain.api;

import cool.klass.model.meta.domain.api.criteria.ICriteria;
import cool.klass.model.meta.domain.api.property.IAssociationEnd;
import org.eclipse.collections.api.list.ImmutableList;

public interface IAssociation extends IPackageableElement
{
    ICriteria getCriteria();

    ImmutableList<IAssociationEnd> getAssociationEnds();

    IAssociationEnd getSourceAssociationEnd();

    IAssociationEnd getTargetAssociationEnd();
}
