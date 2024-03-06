package cool.klass.model.meta.domain.api.order;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.IElement;
import cool.klass.model.meta.domain.api.value.IThisMemberReferencePath;

public interface IOrderByMemberReferencePath extends IElement
{
    @Nonnull
    IThisMemberReferencePath getThisMemberReferencePath();

    @Nonnull
    IOrderByDirectionDeclaration getOrderByDirectionDeclaration();
}
