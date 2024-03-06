package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.criteria.AntlrCriteria;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameter;
import cool.klass.model.converter.compiler.state.service.AntlrService;
import cool.klass.model.converter.compiler.state.service.AntlrServiceCriteria;
import cool.klass.model.converter.compiler.state.service.url.AntlrUrl;
import cool.klass.model.meta.grammar.KlassParser.UrlDeclarationContext;
import org.eclipse.collections.api.map.OrderedMap;

// Phase must run after inferring additional parameters like version, in ServicePhase
public class VariableResolutionPhase extends AbstractCompilerPhase
{
    public VariableResolutionPhase(@Nonnull CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public void enterUrlDeclaration(@Nonnull UrlDeclarationContext ctx)
    {
        super.enterUrlDeclaration(ctx);

        AntlrUrl urlState = this.compilerState.getCompilerWalkState().getUrlState();

        OrderedMap<String, AntlrParameter> formalParametersByName = urlState.getFormalParametersByName();
        for (AntlrService serviceState : urlState.getServiceStates())
        {
            for (AntlrServiceCriteria serviceCriteriaState : serviceState.getServiceCriteriaStates())
            {
                AntlrCriteria criteria = serviceCriteriaState.getCriteria();
                criteria.resolveServiceVariables(formalParametersByName);
                // TODO: ❓ Type inference here?
                criteria.resolveTypes();
            }
        }
    }
}
