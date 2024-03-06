package cool.klass.model.meta.domain.api.value;

import javax.annotation.Nonnull;

public interface TypeMemberReferencePath extends MemberReferencePath
{
    @Override
    default void visit(@Nonnull ExpressionValueVisitor visitor)
    {
        visitor.visitTypeMember(this);
    }
}
