package cool.klass.model.meta.domain.api.value;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.service.url.UrlParameter;

public interface VariableReference extends ExpressionValue
{
    @Nonnull
    UrlParameter getUrlParameter();

    @Override
    default void visit(@Nonnull ExpressionValueVisitor visitor)
    {
        visitor.visitVariableReference(this);
    }
}
