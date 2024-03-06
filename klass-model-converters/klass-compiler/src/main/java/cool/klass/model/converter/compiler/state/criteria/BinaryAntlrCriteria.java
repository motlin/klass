package cool.klass.model.converter.compiler.state.criteria;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class BinaryAntlrCriteria extends AntlrCriteria
{
    @Nonnull
    protected final AntlrCriteria left;
    @Nonnull
    protected final AntlrCriteria right;

    protected BinaryAntlrCriteria(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull AntlrCriteria left,
            @Nonnull AntlrCriteria right)
    {
        super(elementContext, compilationUnit, inferred);
        this.left = Objects.requireNonNull(left);
        this.right = Objects.requireNonNull(right);
    }
}
