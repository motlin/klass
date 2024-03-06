package cool.klass.model.converter.compiler.state.criteria;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.service.CriteriaOwner;
import cool.klass.model.meta.domain.criteria.Criteria.CriteriaBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

public abstract class AntlrCriteria extends AntlrElement
{
    @Nonnull
    private final CriteriaOwner criteriaOwner;

    protected AntlrCriteria(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull CriteriaOwner criteriaOwner)
    {
        super(elementContext, compilationUnit, inferred);
        this.criteriaOwner = Objects.requireNonNull(criteriaOwner);
    }

    @Nonnull
    public abstract CriteriaBuilder build();

    public abstract void reportErrors(
            CompilerErrorHolder compilerErrorHolder,
            ImmutableList<ParserRuleContext> parserRuleContexts);

    public ImmutableList<ParserRuleContext> getParserRuleContexts()
    {
        MutableList<ParserRuleContext> parserRuleContexts = Lists.mutable.empty();
        this.criteriaOwner.getParserRuleContexts(parserRuleContexts);
        return parserRuleContexts.toImmutable();
    }
}
