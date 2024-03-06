package cool.klass.model.meta.domain.api.value.literal;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Type;
import cool.klass.model.meta.domain.api.value.ExpressionValueVisitor;
import cool.klass.model.meta.domain.api.value.IExpressionValue;
import org.eclipse.collections.api.list.ImmutableList;

public interface ILiteralListValue extends IExpressionValue
{
    @Override
    default void visit(@Nonnull ExpressionValueVisitor visitor)
    {
        visitor.visitLiteralList(this);
    }

    @Nonnull
    ImmutableList<ILiteralValue> getLiteralValues();

    Type getType();
}
