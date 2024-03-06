package cool.klass.model.meta.domain.api.order;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.value.ThisMemberReferencePath;

public interface OrderByMemberReferencePath
        extends Element
{
    @Nonnull
    ThisMemberReferencePath getThisMemberReferencePath();

    @Nonnull
    OrderByDirectionDeclaration getOrderByDirectionDeclaration();
}
