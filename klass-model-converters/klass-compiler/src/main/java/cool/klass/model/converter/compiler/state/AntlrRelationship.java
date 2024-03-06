
package cool.klass.model.converter.compiler.state;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.criteria.AntlrCriteria;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrRelationship
        extends AntlrElement
{
    private final AntlrAssociation association;

    private AntlrCriteria criteria;

    public AntlrRelationship(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            AntlrAssociation association)
    {
        super(elementContext, compilationUnit);
        this.association = Objects.requireNonNull(association);
    }

    public AntlrAssociation getAssociation()
    {
        return this.association;
    }

    public void setCriteria(AntlrCriteria criteria)
    {
        if (this.criteria != null)
        {
            throw new IllegalStateException();
        }
        this.criteria = Objects.requireNonNull(criteria);
    }

    public AntlrCriteria getCriteria()
    {
        return this.criteria;
    }

    @Override
    public boolean omitParentFromSurroundingElements()
    {
        return false;
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.ofNullable(this.association);
    }

    public void reportErrors(CompilerErrorState compilerErrorHolder)
    {
        this.criteria.reportErrors(compilerErrorHolder);
    }
}
