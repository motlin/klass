package cool.klass.model.meta.domain.api.value;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.service.url.IUrlParameter;

public interface IVariableReference extends IExpressionValue
{
    @Nonnull
    IUrlParameter getUrlParameter();

    @Override
    default void visit(@Nonnull ExpressionValueVisitor visitor)
    {
        visitor.visitVariableReference(this);
    }
}
