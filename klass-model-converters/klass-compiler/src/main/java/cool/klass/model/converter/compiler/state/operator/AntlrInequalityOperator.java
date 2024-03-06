package cool.klass.model.converter.compiler.state.operator;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.meta.domain.operator.InequalityOperator.InequalityOperatorBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

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
        return new InequalityOperatorBuilder(this.elementContext, this.operatorText);
    }
}
