package cool.klass.model.meta.domain.criteria;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.value.MemberExpressionValue;
import cool.klass.model.meta.domain.value.MemberExpressionValue.MemberExpressionValueBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class EdgePointCriteria extends Criteria
{
    @Nonnull
    private final MemberExpressionValue memberExpressionValue;

    private EdgePointCriteria(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull MemberExpressionValue memberExpressionValue)
    {
        super(elementContext);
        this.memberExpressionValue = Objects.requireNonNull(memberExpressionValue);
    }

    @Nonnull
    public MemberExpressionValue getMemberExpressionValue()
    {
        return this.memberExpressionValue;
    }

    @Override
    public void visit(CriteriaVisitor visitor)
    {
        visitor.visitEdgePoint(this);
    }

    public static final class EdgePointCriteriaBuilder extends CriteriaBuilder
    {
        @Nonnull
        private final MemberExpressionValueBuilder memberExpressionValue;

        public EdgePointCriteriaBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull MemberExpressionValueBuilder memberExpressionValue)
        {
            super(elementContext);
            this.memberExpressionValue = Objects.requireNonNull(memberExpressionValue);
        }

        @Nonnull
        @Override
        public EdgePointCriteria build()
        {
            return new EdgePointCriteria(
                    this.elementContext,
                    this.memberExpressionValue.build());
        }
    }
}
