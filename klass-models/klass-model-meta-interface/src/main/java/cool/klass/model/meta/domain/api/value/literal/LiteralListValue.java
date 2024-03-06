package cool.klass.model.meta.domain.api.value.literal;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Type;
import cool.klass.model.meta.domain.api.value.ExpressionValue;
import cool.klass.model.meta.domain.api.value.ExpressionValueVisitor;
import org.eclipse.collections.api.list.ImmutableList;

public interface LiteralListValue extends ExpressionValue
{
    @Override
    default void visit(@Nonnull ExpressionValueVisitor visitor)
    {
        visitor.visitLiteralList(this);
    }

    @Nonnull
    ImmutableList<LiteralValue> getLiteralValues();

    Type getType();
}
