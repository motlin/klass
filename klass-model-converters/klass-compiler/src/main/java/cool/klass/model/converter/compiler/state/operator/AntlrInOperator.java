package cool.klass.model.converter.compiler.state.operator;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.meta.domain.operator.InOperator.InOperatorBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrInOperator extends AntlrOperator
{
    public AntlrInOperator(
            ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            String operatorText)
    {
        super(elementContext, compilationUnit, inferred, operatorText);
    }

    @Override
    public InOperatorBuilder build()
    {
        return new InOperatorBuilder(this.elementContext, this.operatorText);
    }
}
