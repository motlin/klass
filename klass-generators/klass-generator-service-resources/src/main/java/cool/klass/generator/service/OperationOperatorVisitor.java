package cool.klass.generator.service;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.operator.EqualityOperator;
import cool.klass.model.meta.domain.operator.InOperator;
import cool.klass.model.meta.domain.operator.InequalityOperator;
import cool.klass.model.meta.domain.operator.OperatorVisitor;
import cool.klass.model.meta.domain.operator.StringOperator;

public class OperationOperatorVisitor implements OperatorVisitor
{
    private final StringBuilder stringBuilder;

    public OperationOperatorVisitor(StringBuilder stringBuilder)
    {
        this.stringBuilder = Objects.requireNonNull(stringBuilder);
    }

    @Override
    public void visitEquality(EqualityOperator equalityOperator)
    {
        this.stringBuilder.append(".eq(");
    }

    @Override
    public void visitInequality(@Nonnull InequalityOperator inequalityOperator)
    {
        switch (inequalityOperator.getOperatorText())
        {
            case "<":
                this.stringBuilder.append(".lessThan(");
                break;
            case ">":
                this.stringBuilder.append(".greaterThan(");
                break;
            case "<=":
                this.stringBuilder.append(".lessThanEquals(");
                break;
            case ">=":
                this.stringBuilder.append(".greaterThanEquals(");
                break;
            default:
                throw new AssertionError();
        }
    }

    @Override
    public void visitIn(InOperator inOperator)
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
