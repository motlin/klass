package cool.klass.generator.reladomo;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.operator.EqualityOperator;
import cool.klass.model.meta.domain.api.operator.InOperator;
import cool.klass.model.meta.domain.api.operator.InequalityOperator;
import cool.klass.model.meta.domain.api.operator.Operator;
import cool.klass.model.meta.domain.api.operator.OperatorVisitor;
import cool.klass.model.meta.domain.api.operator.StringOperator;

public class OperatorToRelationshipVisitor implements OperatorVisitor
{
    private final StringBuilder stringBuilder;

    public OperatorToRelationshipVisitor(StringBuilder stringBuilder)
    {
        this.stringBuilder = stringBuilder;
    }

    @Override
    public void visitEquality(@Nonnull EqualityOperator equalityOperator)
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

    private void appendOperatorText(@Nonnull Operator operator)
    {
        this.stringBuilder.append(' ');
        this.stringBuilder.append(operator.getOperatorText());
        this.stringBuilder.append(' ');
    }
}
