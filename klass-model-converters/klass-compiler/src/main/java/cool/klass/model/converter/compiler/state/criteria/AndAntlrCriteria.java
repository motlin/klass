package cool.klass.model.converter.compiler.state.criteria;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.meta.domain.criteria.AndCriteria.AndCriteriaBuilder;
import cool.klass.model.meta.grammar.KlassParser.CriteriaExpressionAndContext;

public class AndAntlrCriteria extends BinaryAntlrCriteria
{
    public AndAntlrCriteria(
            @Nonnull CriteriaExpressionAndContext elementContext,
            @Nonnull CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull AntlrCriteria left,
            @Nonnull AntlrCriteria right)
    {
        super(elementContext, compilationUnit, inferred, left, right);
    }

    @Nonnull
    @Override
    public CriteriaExpressionAndContext getElementContext()
    {
        return (CriteriaExpressionAndContext) super.getElementContext();
    }

    @Nonnull
    @Override
    public AndCriteriaBuilder build()
    {
        return new AndCriteriaBuilder(this.elementContext, this.left.build(), this.right.build());
    }
}
