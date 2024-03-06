package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.service.AntlrService;
import cool.klass.model.meta.grammar.KlassParser;
import cool.klass.model.meta.grammar.KlassParser.UrlDeclarationContext;

public class ServiceVersionInferencePhase extends AbstractCompilerPhase
{
    public ServiceVersionInferencePhase(CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public void exitUrlDeclaration(@Nonnull UrlDeclarationContext ctx)
    {
        // TODO: ♻️ It no longer makes sense to share one url for a bunch of services, because of inference like this
        if (this.compilerState.getCompilerWalkState()
                .getUrlState()
                .getServiceStates()
                .anySatisfy(AntlrService::needsVersionCriteria))
        {
            this.compilerState.runNonRootCompilerMacro(
                    ctx,
                    ServiceVersionInferencePhase.class,
                    "?{version: Integer[0..1] version}",
                    KlassParser::queryParameterList,
                    new UrlParameterPhase(this.compilerState));
        }

        super.exitUrlDeclaration(ctx);
    }
}
