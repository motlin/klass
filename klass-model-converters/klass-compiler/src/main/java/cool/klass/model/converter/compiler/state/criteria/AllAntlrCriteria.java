package cool.klass.model.converter.compiler.state.criteria;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameter;
import cool.klass.model.meta.domain.criteria.AllCriteriaImpl.AllCriteriaBuilder;
import cool.klass.model.meta.grammar.KlassParser.CriteriaAllContext;
import org.eclipse.collections.api.map.OrderedMap;

public class AllAntlrCriteria extends AntlrCriteria
{
    private AllCriteriaBuilder elementBuilder;

    public AllAntlrCriteria(
            @Nonnull CriteriaAllContext elementContext,
            @Nonnull CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull IAntlrElement criteriaOwner)
    {
        super(elementContext, compilationUnit, inferred, criteriaOwner);
    }

    @Nonnull
    @Override
    public CriteriaAllContext getElementContext()
    {
        return (CriteriaAllContext) super.getElementContext();
    }

    @Nonnull
    @Override
    public AllCriteriaBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new AllCriteriaBuilder(this.elementContext, this.inferred);
        return this.elementBuilder;
    }

    @Nonnull
    @Override
    public AllCriteriaBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }

    @Override
    public void reportErrors(CompilerErrorState compilerErrorHolder)
    {
        // Intentionally blank
    }

    @Override
    public void resolveServiceVariables(OrderedMap<String, AntlrParameter> formalParametersByName)
    {
        // Intentionally blank
    }

    @Override
    public void resolveTypes()
    {
        // Intentionally blank
    }
}
