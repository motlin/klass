package cool.klass.model.meta.domain.api.order;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.IElement;

public interface IOrderByDirectionDeclaration extends IElement
{
    @Nonnull
    OrderByDirection getOrderByDirection();
}
