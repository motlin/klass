package cool.klass.model.converter.compiler.state.operator;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.meta.domain.operator.StringOperator.StringOperatorBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrStringOperator extends AntlrOperator
{
    public AntlrStringOperator(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            String operatorText)
    {
        super(elementContext, compilationUnit, inferred, operatorText);
    }

    @Nonnull
    @Override
    public StringOperatorBuilder build()
    {
        return new StringOperatorBuilder(this.elementContext, this.operatorText);
    }
}
