package cool.klass.generator.service;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.operator.EqualityOperator;
import cool.klass.model.meta.domain.api.operator.InOperator;
import cool.klass.model.meta.domain.api.operator.InequalityOperator;
import cool.klass.model.meta.domain.api.operator.OperatorVisitor;
import cool.klass.model.meta.domain.api.operator.StringOperator;

public class OperationOperatorVisitor implements OperatorVisitor
{
    private final StringBuilder stringBuilder;

    public OperationOperatorVisitor(StringBuilder stringBuilder)
    {
        this.stringBuilder = Objects.requireNonNull(stringBuilder);
    }

    @Override
    public void visitEquality(@Nonnull EqualityOperator equalityOperator)
    {
        this.stringBuilder.append(".eq(");
    }

    @Override
    public void visitInequality(@Nonnull InequalityOperator inequalityOperator)
    {
        switch (inequalityOperator.getOperatorText())
        {
            case "<":
            {
                this.stringBuilder.append(".lessThan(");
                break;
            }
            case ">":
            {
                this.stringBuilder.append(".greaterThan(");
                break;
            }
            case "<=":
            {
                this.stringBuilder.append(".lessThanEquals(");
                break;
            }
            case ">=":
            {
                this.stringBuilder.append(".greaterThanEquals(");
                break;
            }
            default:
            {
                throw new AssertionError();
            }
        }
    }

    @Override
    public void visitIn(@Nonnull InOperator inOperator)
    {
        this.stringBuilder.append(".in(");
    }

    @Override
    public void visitString(@Nonnull StringOperator stringOperator)
    {
        this.stringBuilder.append(".");
        this.stringBuilder.append(stringOperator.getOperatorText());
        this.stringBuilder.append("(");
    }
}
