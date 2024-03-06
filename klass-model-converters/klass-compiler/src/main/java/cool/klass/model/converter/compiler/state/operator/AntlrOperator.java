package cool.klass.model.converter.compiler.state.operator;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.meta.domain.operator.Operator.OperatorBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

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

    @Nonnull
    public abstract OperatorBuilder build();
}
