package cool.klass.model.converter.compiler.state.criteria;

import java.util.Objects;

import cool.klass.model.converter.compiler.CompilationUnit;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class BinaryAntlrCriteria extends AntlrCriteria
{
    protected final AntlrCriteria left;
    protected final AntlrCriteria right;

    protected BinaryAntlrCriteria(
            ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            AntlrCriteria left,
            AntlrCriteria right)
    {
        super(elementContext, compilationUnit, inferred);
        this.left = Objects.requireNonNull(left);
        this.right = Objects.requireNonNull(right);
    }
}
