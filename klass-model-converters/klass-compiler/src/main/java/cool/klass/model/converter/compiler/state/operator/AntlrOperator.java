package cool.klass.model.converter.compiler.state.operator;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.meta.domain.operator.AbstractOperator.AbstractOperatorBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ListIterable;

public abstract class AntlrOperator extends AntlrElement
{
    protected final String operatorText;

    protected AntlrOperator(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            String operatorText)
    {
        super(elementContext, compilationUnit, inferred);
        this.operatorText = operatorText;
    }

    @Override
    public boolean omitParentFromSurroundingElements()
    {
        return true;
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getSurroundingContext() not implemented yet");
    }

    @Nonnull
    public abstract AbstractOperatorBuilder<?> build();

    @Nonnull
    public abstract AbstractOperatorBuilder<?> getElementBuilder();

    public abstract void checkTypes(
            CompilerErrorState compilerErrorHolder,
            ListIterable<AntlrType> sourceTypes,
            ListIterable<AntlrType> targetTypes);
}
