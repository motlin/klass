package cool.klass.model.converter.compiler.state.criteria;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.service.CriteriaOwner;
import cool.klass.model.converter.compiler.state.service.url.AntlrUrlParameter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.OrderedMap;

public abstract class BinaryAntlrCriteria extends AntlrCriteria
{
    @Nonnull
    protected final AntlrCriteria left;
    @Nonnull
    protected final AntlrCriteria right;

    protected BinaryAntlrCriteria(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull CriteriaOwner criteriaOwner,
            @Nonnull AntlrCriteria left,
            @Nonnull AntlrCriteria right)
    {
        super(elementContext, compilationUnit, inferred, criteriaOwner);
        this.left = Objects.requireNonNull(left);
        this.right = Objects.requireNonNull(right);
    }

    @Override
    public void reportErrors(
            CompilerErrorHolder compilerErrorHolder,
            ImmutableList<ParserRuleContext> parserRuleContexts)
    {
        // TODO: Error if both clauses are identical, or if any left true subclause is a subclause of the right
        // Java | Probable bugs | Constant conditions & exceptions

        this.left.reportErrors(compilerErrorHolder, parserRuleContexts);
        this.right.reportErrors(compilerErrorHolder, parserRuleContexts);
    }

    @Override
    public final void resolveServiceVariables(OrderedMap<String, AntlrUrlParameter> formalParametersByName)
    {
        this.left.resolveServiceVariables(formalParametersByName);
        this.right.resolveServiceVariables(formalParametersByName);
    }

    @Override
    public final void resolveTypes()
    {
        this.left.resolveTypes();
        this.right.resolveTypes();
    }
}
