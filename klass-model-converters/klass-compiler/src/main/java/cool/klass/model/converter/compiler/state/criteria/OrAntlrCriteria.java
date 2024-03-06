package cool.klass.model.converter.compiler.state.criteria;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.meta.domain.criteria.OrCriteria.OrCriteriaBuilder;
import cool.klass.model.meta.grammar.KlassParser.CriteriaExpressionOrContext;

public class OrAntlrCriteria extends BinaryAntlrCriteria
{
    public OrAntlrCriteria(
            @Nonnull CriteriaExpressionOrContext elementContext,
            @Nonnull CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull AntlrCriteria left,
            @Nonnull AntlrCriteria right)
    {
        super(elementContext, compilationUnit, inferred, left, right);
    }

    @Nonnull
    @Override
    public CriteriaExpressionOrContext getElementContext()
    {
        return (CriteriaExpressionOrContext) super.getElementContext();
    }

    @Nonnull
    @Override
    public OrCriteriaBuilder build()
    {
        return new OrCriteriaBuilder(this.elementContext, this.left.build(), this.right.build());
    }
}
