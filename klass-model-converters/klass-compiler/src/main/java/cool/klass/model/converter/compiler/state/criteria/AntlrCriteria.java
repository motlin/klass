package cool.klass.model.converter.compiler.state.criteria;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.meta.domain.criteria.Criteria.CriteriaBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AntlrCriteria extends AntlrElement
{
    protected AntlrCriteria(
            ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred)
    {
        super(elementContext, compilationUnit, inferred);
    }

    public abstract CriteriaBuilder build();
}
