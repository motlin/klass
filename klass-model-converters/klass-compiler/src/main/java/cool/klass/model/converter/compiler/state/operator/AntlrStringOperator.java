package cool.klass.model.converter.compiler.state.operator;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.meta.domain.operator.StringOperator.StringOperatorBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrStringOperator extends AntlrOperator
{
    public AntlrStringOperator(
            ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            String operatorText)
    {
        super(elementContext, compilationUnit, inferred, operatorText);
    }

    @Override
    public StringOperatorBuilder build()
    {
        return new StringOperatorBuilder(this.elementContext, this.operatorText);
    }
}
