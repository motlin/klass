package cool.klass.model.converter.compiler.state.criteria;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.meta.domain.criteria.AndCriteriaImpl.AndCriteriaBuilder;
import cool.klass.model.meta.grammar.KlassParser.CriteriaExpressionAndContext;

public class AntlrAndCriteria extends AntlrBinaryCriteria
{
    private AndCriteriaBuilder elementBuilder;

    public AntlrAndCriteria(
            @Nonnull CriteriaExpressionAndContext elementContext,
            @Nonnull CompilationUnit compilationUnit,
            Optional<AntlrElement> macroElement,
            @Nonnull IAntlrElement criteriaOwner)
    {
        super(elementContext, compilationUnit, macroElement, criteriaOwner);
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
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new AndCriteriaBuilder(
                this.elementContext,
                this.macroElement.map(AntlrElement::getElementBuilder),
                this.left.build(),
                this.right.build());
        return this.elementBuilder;
    }

    @Override
    public AndCriteriaBuilder getElementBuilder()
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

    @Override
    public void addForeignKeys(
            boolean foreignKeysOnThis,
            AntlrAssociationEnd endWithForeignKeys)
    {
        this.left.addForeignKeys(foreignKeysOnThis, endWithForeignKeys);
        this.right.addForeignKeys(foreignKeysOnThis, endWithForeignKeys);
    }
}
