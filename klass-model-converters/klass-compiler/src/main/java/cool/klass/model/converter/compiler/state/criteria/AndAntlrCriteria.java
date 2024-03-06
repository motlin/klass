package cool.klass.model.converter.compiler.state.criteria;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.meta.domain.criteria.AndCriteria.AndCriteriaBuilder;
import cool.klass.model.meta.grammar.KlassParser.CriteriaExpressionAndContext;

public class AndAntlrCriteria extends BinaryAntlrCriteria
{
    public AndAntlrCriteria(
            CriteriaExpressionAndContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            AntlrCriteria left,
            AntlrCriteria right)
    {
        super(elementContext, compilationUnit, inferred, left, right);
    }

    @Override
    public CriteriaExpressionAndContext getElementContext()
    {
        return (CriteriaExpressionAndContext) super.getElementContext();
    }

    @Override
    public AndCriteriaBuilder build()
    {
        return new AndCriteriaBuilder(this.elementContext, this.left.build(), this.right.build());
    }
}
