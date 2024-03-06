package cool.klass.model.converter.compiler.state.criteria;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.meta.domain.criteria.Criteria.CriteriaBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AntlrCriteria extends AntlrElement
{
    protected AntlrCriteria(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred)
    {
        super(elementContext, compilationUnit, inferred);
    }

    @Nonnull
    public abstract CriteriaBuilder build();
}
