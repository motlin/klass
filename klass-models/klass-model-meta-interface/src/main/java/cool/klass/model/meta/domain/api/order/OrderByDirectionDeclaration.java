package cool.klass.model.meta.domain.api.order;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;

public interface OrderByDirectionDeclaration
        extends Element
{
    @Nonnull
    OrderByDirection getOrderByDirection();
}
