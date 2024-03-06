package cool.klass.model.meta.domain.criteria;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.value.MemberReferencePath;
import cool.klass.model.meta.domain.value.MemberReferencePath.MemberReferencePathBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class EdgePointCriteria extends Criteria
{
    @Nonnull
    private final MemberReferencePath memberExpressionValue;

    private EdgePointCriteria(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull MemberReferencePath memberExpressionValue)
    {
        super(elementContext, inferred);
        this.memberExpressionValue = Objects.requireNonNull(memberExpressionValue);
    }

    @Nonnull
    public MemberReferencePath getMemberExpressionValue()
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
        private final MemberReferencePathBuilder memberExpressionValue;

        public EdgePointCriteriaBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull MemberReferencePathBuilder memberExpressionValue)
        {
            super(elementContext, inferred);
            this.memberExpressionValue = Objects.requireNonNull(memberExpressionValue);
        }

        @Nonnull
        @Override
        public EdgePointCriteria build()
        {
            return new EdgePointCriteria(
                    this.elementContext,
                    this.inferred,
                    this.memberExpressionValue.build());
        }
    }
}
