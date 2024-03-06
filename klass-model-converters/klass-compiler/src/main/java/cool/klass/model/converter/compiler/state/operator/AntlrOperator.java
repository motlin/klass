package cool.klass.model.converter.compiler.state.operator;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.criteria.OperatorAntlrCriteria;
import cool.klass.model.meta.domain.operator.AbstractOperator.AbstractOperatorBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ListIterable;

public abstract class AntlrOperator extends AntlrElement
{
    protected final String                operatorText;
    private         OperatorAntlrCriteria owningOperatorAntlrCriteria;

    protected AntlrOperator(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull String operatorText)
    {
        super(elementContext, compilationUnit);
        this.operatorText = Objects.requireNonNull(operatorText);
    }

    @Override
    public boolean omitParentFromSurroundingElements()
    {
        return true;
    }

    public void setOwningOperatorAntlrCriteria(OperatorAntlrCriteria operatorAntlrCriteria)
    {
        this.owningOperatorAntlrCriteria = Objects.requireNonNull(operatorAntlrCriteria);
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.owningOperatorAntlrCriteria);
    }

    @Nonnull
    public abstract AbstractOperatorBuilder<?> build();

    @Override
    @Nonnull
    public abstract AbstractOperatorBuilder<?> getElementBuilder();

    public abstract void checkTypes(
            CompilerErrorState compilerErrorHolder,
            ListIterable<AntlrType> sourceTypes,
            ListIterable<AntlrType> targetTypes);
}
