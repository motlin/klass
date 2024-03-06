package cool.klass.model.converter.compiler.state.criteria;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.meta.domain.criteria.OrCriteria.OrCriteriaBuilder;
import cool.klass.model.meta.grammar.KlassParser.CriteriaExpressionOrContext;

public class OrAntlrCriteria extends BinaryAntlrCriteria
{
    public OrAntlrCriteria(
            CriteriaExpressionOrContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            AntlrCriteria left,
            AntlrCriteria right)
    {
        super(elementContext, compilationUnit, inferred, left, right);
    }

    @Override
    public CriteriaExpressionOrContext getElementContext()
    {
        return (CriteriaExpressionOrContext) super.getElementContext();
    }

    @Override
    public OrCriteriaBuilder build()
    {
        return new OrCriteriaBuilder(this.elementContext, this.left.build(), this.right.build());
    }
}
