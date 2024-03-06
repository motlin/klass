package cool.klass.model.meta.domain.api.value.literal;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.value.ExpressionValueVisitor;

public interface UserLiteral
        extends LiteralValue
{
    Klass getUserClass();

    @Override
    default void visit(@Nonnull ExpressionValueVisitor visitor)
    {
        visitor.visitUserLiteral(this);
    }
}
