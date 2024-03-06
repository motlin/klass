package cool.klass.model.meta.domain.api.value.literal;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.value.ExpressionValueVisitor;

public interface IStringLiteralValue extends ILiteralValue
{
    @Nonnull
    String getValue();

    @Override
    default void visit(@Nonnull ExpressionValueVisitor visitor)
    {
        visitor.visitStringLiteral(this);
    }
}
