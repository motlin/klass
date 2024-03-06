package cool.klass.model.converter.compiler.state.operator;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.meta.domain.operator.EqualityOperator.EqualityOperatorBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrEqualityOperator extends AntlrOperator
{
    public AntlrEqualityOperator(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            String operatorText)
    {
        super(elementContext, compilationUnit, inferred, operatorText);
    }

    @Nonnull
    @Override
    public EqualityOperatorBuilder build()
    {
        return new EqualityOperatorBuilder(this.elementContext, this.operatorText);
    }
}
