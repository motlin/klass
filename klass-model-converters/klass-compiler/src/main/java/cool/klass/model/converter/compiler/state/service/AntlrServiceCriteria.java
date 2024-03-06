package cool.klass.model.converter.compiler.state.service;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.criteria.AntlrCriteria;
import cool.klass.model.meta.grammar.KlassParser.ServiceCriteriaDeclarationContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;

public class AntlrServiceCriteria extends AntlrElement implements CriteriaOwner
{
    @Nonnull
    private final String serviceCriteriaKeyword;

    @Nonnull
    private AntlrCriteria antlrCriteria;

    public AntlrServiceCriteria(
            @Nonnull ServiceCriteriaDeclarationContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull String serviceCriteriaKeyword)
    {
        super(elementContext, compilationUnit, inferred);
        this.serviceCriteriaKeyword = Objects.requireNonNull(serviceCriteriaKeyword);
    }

    @Override
    @Nonnull
    public AntlrCriteria getCriteria()
    {
        return Objects.requireNonNull(this.antlrCriteria);
    }

    @Override
    public void setCriteria(@Nonnull AntlrCriteria antlrCriteria)
    {
        this.antlrCriteria = Objects.requireNonNull(antlrCriteria);
    }

    @Override
    public void getParserRuleContexts(MutableList<ParserRuleContext> parserRuleContexts)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getParserRuleContexts() not implemented yet");
    }

    public void reportDuplicateKeyword(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        // TODO: Test coverage of duplicate service criteria
        String message = String.format("ERR_DUP_CRI: Duplicate service criteria: '%s'.", this.serviceCriteriaKeyword);

        ImmutableList<ParserRuleContext> parserRuleContexts = this.antlrCriteria.getParserRuleContexts();

        compilerErrorHolder.add(
                this.compilationUnit,
                message,
                this.getElementContext(),
                parserRuleContexts.toArray(new ParserRuleContext[]{}));
    }

    @Nonnull
    @Override
    public ServiceCriteriaDeclarationContext getElementContext()
    {
        return (ServiceCriteriaDeclarationContext) super.getElementContext();
    }

    @Nonnull
    public String getServiceCriteriaKeyword()
    {
        return this.serviceCriteriaKeyword;
    }
}
