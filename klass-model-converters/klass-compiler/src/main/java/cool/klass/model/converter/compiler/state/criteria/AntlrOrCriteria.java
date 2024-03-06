package cool.klass.model.converter.compiler.state.criteria;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.meta.domain.criteria.OrCriteriaImpl.OrCriteriaBuilder;
import cool.klass.model.meta.grammar.KlassParser.CriteriaExpressionOrContext;

public class AntlrOrCriteria extends AntlrBinaryCriteria
{
    public AntlrOrCriteria(
            @Nonnull CriteriaExpressionOrContext elementContext,
            @Nonnull CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull IAntlrElement criteriaOwner)
    {
        super(elementContext, compilationUnit, inferred, criteriaOwner);
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
        return new OrCriteriaBuilder(this.elementContext, this.inferred, this.left.build(), this.right.build());
    }

    @Override
    public void reportErrors(CompilerErrorHolder compilerErrorHolder)
    {
        // TODO: Error if both clauses are identical, or if any left true subclause is a subclause of the right
        // Java | Probable bugs | Constant conditions & exceptions

        super.reportErrors(compilerErrorHolder);
    }
}
