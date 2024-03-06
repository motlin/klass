package cool.klass.model.meta.domain.api.order;

import cool.klass.model.meta.domain.api.IElement;
import org.eclipse.collections.api.list.ImmutableList;

public interface IOrderBy extends IElement
{
    ImmutableList<IOrderByMemberReferencePath> getOrderByMemberReferencePaths();
}
