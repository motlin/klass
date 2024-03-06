package cool.klass.model.meta.domain.api.order;

import cool.klass.model.meta.domain.api.Element;
import org.eclipse.collections.api.list.ImmutableList;

public interface OrderBy extends Element
{
    ImmutableList<OrderByMemberReferencePath> getOrderByMemberReferencePaths();
}
