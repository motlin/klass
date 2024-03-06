package cool.klass.generator.reladomo;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.criteria.AllCriteria;
import cool.klass.model.meta.domain.api.criteria.AndCriteria;
import cool.klass.model.meta.domain.api.criteria.CriteriaVisitor;
import cool.klass.model.meta.domain.api.criteria.EdgePointCriteria;
import cool.klass.model.meta.domain.api.criteria.OperatorCriteria;
import cool.klass.model.meta.domain.api.criteria.OrCriteria;
import cool.klass.model.meta.domain.api.operator.Operator;
import cool.klass.model.meta.domain.api.value.ExpressionValue;
import cool.klass.model.meta.domain.api.value.literal.NullLiteral;

public class CriteriaToRelationshipVisitor implements CriteriaVisitor
{
    @Nonnull
    private final StringBuilder stringBuilder;

    public CriteriaToRelationshipVisitor(@Nonnull StringBuilder stringBuilder)
    {
        this.stringBuilder = Objects.requireNonNull(stringBuilder);
    }

    @Override
    public void visitAll(@Nonnull AllCriteria allCriteria)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".visitAll() not implemented yet");
    }

    @Override
    public void visitAnd(@Nonnull AndCriteria andCriteria)
    {
        andCriteria.getLeft().visit(this);
        this.stringBuilder.append(" and ");
        andCriteria.getRight().visit(this);
    }

    @Override
    public void visitOr(@Nonnull OrCriteria orCriteria)
    {
        orCriteria.getLeft().visit(this);
        this.stringBuilder.append(" or ");
        orCriteria.getRight().visit(this);
    }

    @Override
    public void visitOperator(@Nonnull OperatorCriteria operatorCriteria)
    {
        Operator        operator    = operatorCriteria.getOperator();
        ExpressionValue sourceValue = operatorCriteria.getSourceValue();
        ExpressionValue targetValue = operatorCriteria.getTargetValue();

        if (targetValue instanceof NullLiteral)
        {
            sourceValue.visit(new ExpressionValueToRelationshipVisitor(this.stringBuilder));
            String reladomoNullOperator = this.getReladomoNullOperator(operator);
            this.stringBuilder.append(" ");
            this.stringBuilder.append(reladomoNullOperator);
            return;
        }

        sourceValue.visit(new ExpressionValueToRelationshipVisitor(this.stringBuilder));
        operator.visit(new OperatorToRelationshipVisitor(this.stringBuilder));
        targetValue.visit(new ExpressionValueToRelationshipVisitor(this.stringBuilder));
    }

    @Nonnull
    private String getReladomoNullOperator(@Nonnull Operator operator)
    {
        String operatorText = operator.getOperatorText();
        switch (operatorText)
        {
            case "==":
            {
                return "is null";
            }
            case "!=":
            {
                return "is not null";
            }
            default:
            {
                throw new AssertionError();
            }
        }
    }

    @Override
    public void visitEdgePoint(@Nonnull EdgePointCriteria edgePointCriteria)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".visitEdgePoint() not implemented yet");
    }
}
