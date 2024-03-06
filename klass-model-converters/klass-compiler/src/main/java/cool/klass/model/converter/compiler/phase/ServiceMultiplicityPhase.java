package cool.klass.model.converter.compiler.phase;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.service.AntlrService;
import cool.klass.model.converter.compiler.state.service.AntlrServiceMultiplicity;
import cool.klass.model.meta.domain.api.service.ServiceMultiplicity;
import cool.klass.model.meta.grammar.KlassParser.ServiceMultiplicityContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceMultiplicityDeclarationContext;

public class ServiceMultiplicityPhase extends AbstractCompilerPhase
{
    public ServiceMultiplicityPhase(@Nonnull CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public void enterServiceMultiplicityDeclaration(ServiceMultiplicityDeclarationContext ctx)
    {
        ServiceMultiplicityContext multiplicityContext = ctx.serviceMultiplicity();

        AntlrServiceMultiplicity serviceMultiplicityState = new AntlrServiceMultiplicity(
                multiplicityContext,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                this.getServiceMultiplicity(multiplicityContext));

        AntlrService serviceState = this.compilerState.getCompilerWalkState().getServiceState();
        serviceState.enterServiceMultiplicityDeclaration(serviceMultiplicityState);
    }

    @Nonnull
    private ServiceMultiplicity getServiceMultiplicity(@Nonnull ServiceMultiplicityContext serviceMultiplicityContext)
    {
        if (serviceMultiplicityContext.one != null)
        {
            return ServiceMultiplicity.ONE;
        }
        if (serviceMultiplicityContext.many != null)
        {
            return ServiceMultiplicity.MANY;
        }
        throw new AssertionError();
    }
}
