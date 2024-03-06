package cool.klass.model.converter.compiler.state.criteria;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.meta.domain.criteria.OrCriteriaImpl.OrCriteriaBuilder;
import cool.klass.model.meta.grammar.KlassParser.CriteriaExpressionOrContext;

public class AntlrOrCriteria extends AntlrBinaryCriteria
{
    private OrCriteriaBuilder elementBuilder;

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
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new OrCriteriaBuilder(
                this.elementContext,
                this.inferred,
                this.left.build(),
                this.right.build());
        return this.elementBuilder;
    }

    @Nonnull
    @Override
    public OrCriteriaBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }

    @Override
    public void reportErrors(CompilerErrorState compilerErrorHolder)
    {
        // TODO: Error if both clauses are identical, or if any left true subclause is a subclause of the right
        // Java | Probable bugs | Constant conditions & exceptions

        super.reportErrors(compilerErrorHolder);
    }
}
