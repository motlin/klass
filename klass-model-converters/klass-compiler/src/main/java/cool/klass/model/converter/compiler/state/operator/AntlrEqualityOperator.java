package cool.klass.model.converter.compiler.state.operator;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.meta.domain.operator.EqualityOperator.EqualityOperatorBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrEqualityOperator extends AntlrOperator
{
    public AntlrEqualityOperator(
            ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            String operatorText)
    {
        super(elementContext, compilationUnit, inferred, operatorText);
    }

    @Override
    public EqualityOperatorBuilder build()
    {
        return new EqualityOperatorBuilder(this.elementContext, this.operatorText);
    }
}
