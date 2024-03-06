package cool.klass.model.converter.compiler.phase;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.phase.criteria.CriteriaVisitor;
import cool.klass.model.converter.compiler.state.criteria.AntlrCriteria;
import cool.klass.model.converter.compiler.state.service.AntlrServiceCriteria;
import cool.klass.model.meta.grammar.KlassParser.CriteriaExpressionContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceCriteriaDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceCriteriaKeywordContext;

public class ServiceCriteriaPhase extends AbstractCompilerPhase
{
    public ServiceCriteriaPhase(CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public void enterServiceCriteriaDeclaration(@Nonnull ServiceCriteriaDeclarationContext ctx)
    {
        super.enterServiceCriteriaDeclaration(ctx);

        ServiceCriteriaKeywordContext serviceCriteriaKeywordContext = ctx.serviceCriteriaKeyword();

        String serviceCriteriaKeyword = serviceCriteriaKeywordContext.getText();

        AntlrServiceCriteria serviceCriteriaState = new AntlrServiceCriteria(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                serviceCriteriaKeyword,
                this.compilerState.getCompilerWalkState().getServiceState());

        CriteriaExpressionContext criteriaExpressionContext = ctx.criteriaExpression();

        CriteriaVisitor criteriaVisitor = new CriteriaVisitor(
                this.compilerState,
                serviceCriteriaState);

        AntlrCriteria antlrCriteria = criteriaVisitor.visit(criteriaExpressionContext);
        serviceCriteriaState.setCriteria(antlrCriteria);

        this.compilerState.getCompilerWalkState().getServiceState().enterServiceCriteriaDeclaration(serviceCriteriaState);
    }
}
