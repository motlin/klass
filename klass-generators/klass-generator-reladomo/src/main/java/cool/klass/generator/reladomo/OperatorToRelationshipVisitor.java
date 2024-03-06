package cool.klass.generator.reladomo;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.operator.EqualityOperator;
import cool.klass.model.meta.domain.operator.InOperator;
import cool.klass.model.meta.domain.operator.InequalityOperator;
import cool.klass.model.meta.domain.operator.Operator;
import cool.klass.model.meta.domain.operator.OperatorVisitor;
import cool.klass.model.meta.domain.operator.StringOperator;

public class OperatorToRelationshipVisitor implements OperatorVisitor
{
    private final StringBuilder stringBuilder;

    public OperatorToRelationshipVisitor(StringBuilder stringBuilder)
    {
        this.stringBuilder = stringBuilder;
    }

    @Override
    public void visitEquality(EqualityOperator equalityOperator)
    {
        this.stringBuilder.append(" = ");
    }

    @Override
    public void visitInequality(@Nonnull InequalityOperator inequalityOperator)
    {
        this.appendOperatorText(inequalityOperator);
    }

    @Override
    public void visitIn(@Nonnull InOperator inOperator)
    {
        this.appendOperatorText(inOperator);
    }

    @Override
    public void visitString(@Nonnull StringOperator stringOperator)
    {
        this.appendOperatorText(stringOperator);
    }

    private void appendOperatorText(Operator operator)
    {
        this.stringBuilder.append(' ');
        this.stringBuilder.append(operator.getOperatorText());
        this.stringBuilder.append(' ');
    }
}
