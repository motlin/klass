package cool.klass.model.converter.compiler.state.operator;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.meta.domain.operator.InequalityOperatorImpl.InequalityOperatorBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ListIterable;

public class AntlrInequalityOperator extends AntlrOperator
{
    private InequalityOperatorBuilder elementBuilder;

    public AntlrInequalityOperator(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            String operatorText)
    {
        super(elementContext, compilationUnit, inferred, operatorText);
    }

    @Nonnull
    @Override
    public InequalityOperatorBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new InequalityOperatorBuilder(this.elementContext, this.inferred, this.operatorText);
        return this.elementBuilder;
    }

    @Nonnull
    @Override
    public InequalityOperatorBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }

    @Override
    public void checkTypes(
            CompilerErrorState compilerErrorHolder,
            ListIterable<AntlrType> sourceTypes,
            ListIterable<AntlrType> targetTypes)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".checkTypes() not implemented yet");
    }
}
