package cool.klass.model.meta.domain.api.value;

import javax.annotation.Nonnull;

public interface ThisMemberReferencePath extends MemberReferencePath
{
    @Override
    default void visit(@Nonnull ExpressionValueVisitor visitor)
    {
        visitor.visitThisMember(this);
    }
}
