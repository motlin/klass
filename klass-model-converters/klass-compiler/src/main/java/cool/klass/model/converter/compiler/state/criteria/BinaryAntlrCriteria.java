package cool.klass.model.converter.compiler.state.criteria;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.service.CriteriaOwner;
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
            @Nonnull CriteriaOwner criteriaOwner,
            @Nonnull AntlrCriteria left,
            @Nonnull AntlrCriteria right)
    {
        super(elementContext, compilationUnit, inferred, criteriaOwner);
        this.left = Objects.requireNonNull(left);
        this.right = Objects.requireNonNull(right);
    }
}
