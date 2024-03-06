package cool.klass.model.converter.compiler.state.operator;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.meta.domain.operator.InequalityOperatorImpl.InequalityOperatorBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.ListIterable;

public class AntlrInequalityOperator extends AntlrOperator
{
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
        return new InequalityOperatorBuilder(this.elementContext, this.inferred, this.operatorText);
    }

    @Override
    public void checkTypes(
            CompilerErrorHolder compilerErrorHolder,
            ImmutableList<ParserRuleContext> parserRuleContexts,
            ListIterable<AntlrType> sourceTypes,
            ListIterable<AntlrType> targetTypes)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".checkTypes() not implemented yet");
    }
}
