package cool.klass.model.converter.compiler.state.criteria;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameter;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.map.OrderedMap;

public abstract class AntlrBinaryCriteria extends AntlrCriteria
{
    protected AntlrCriteria left;
    protected AntlrCriteria right;

    protected AntlrBinaryCriteria(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull IAntlrElement criteriaOwner)
    {
        super(elementContext, compilationUnit, inferred, criteriaOwner);
    }

    public void setLeft(AntlrCriteria left)
    {
        if (this.left != null)
        {
            throw new IllegalStateException();
        }
        this.left = Objects.requireNonNull(left);
    }

    public void setRight(AntlrCriteria right)
    {
        if (this.right != null)
        {
            throw new IllegalStateException();
        }
        this.right = Objects.requireNonNull(right);
    }

    @Override
    public void reportErrors(CompilerErrorHolder compilerErrorHolder)
    {
        // TODO: Error if both clauses are identical, or if any left true subclause is a subclause of the right
        // Java | Probable bugs | Constant conditions & exceptions

        this.left.reportErrors(compilerErrorHolder);
        this.right.reportErrors(compilerErrorHolder);
    }

    @Override
    public final void resolveServiceVariables(OrderedMap<String, AntlrParameter> formalParametersByName)
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
