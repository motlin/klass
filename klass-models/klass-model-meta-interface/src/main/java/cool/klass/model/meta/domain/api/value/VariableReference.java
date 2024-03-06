package cool.klass.model.meta.domain.api.value;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.parameter.Parameter;

public interface VariableReference extends ExpressionValue
{
    @Nonnull
    Parameter getParameter();

    @Override
    default void visit(@Nonnull ExpressionValueVisitor visitor)
    {
        visitor.visitVariableReference(this);
    }
}
