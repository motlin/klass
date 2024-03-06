package cool.klass.model.converter.compiler.state.value;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.meta.domain.value.ExpressionValue.ExpressionValueBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AntlrExpressionValue extends AntlrElement
{
    protected AntlrExpressionValue(
            ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred)
    {
        super(elementContext, compilationUnit, inferred);
    }

    public abstract ExpressionValueBuilder build();
}
