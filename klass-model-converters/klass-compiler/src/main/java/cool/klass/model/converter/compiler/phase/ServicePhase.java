package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.projection.AntlrProjection;
import cool.klass.model.converter.compiler.state.service.AntlrService;
import cool.klass.model.converter.compiler.state.service.AntlrServiceGroup;
import cool.klass.model.converter.compiler.state.service.AntlrServiceMultiplicity;
import cool.klass.model.converter.compiler.state.service.AntlrServiceProjectionDispatch;
import cool.klass.model.converter.compiler.state.service.AntlrVerb;
import cool.klass.model.converter.compiler.state.service.url.AntlrUrl;
import cool.klass.model.meta.domain.api.service.ServiceMultiplicity;
import cool.klass.model.meta.domain.api.service.Verb;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceGroupDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceMultiplicityContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceMultiplicityDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceProjectionDispatchContext;
import cool.klass.model.meta.grammar.KlassParser.UrlDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.VerbContext;
import org.antlr.v4.runtime.ParserRuleContext;

public class ServicePhase extends AbstractCompilerPhase
{
    @Nullable
    private AntlrServiceGroup serviceGroupState;
    @Nullable
    private AntlrUrl          urlState;
    @Nullable
    private AntlrService      serviceState;

    public ServicePhase(CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public void enterServiceGroupDeclaration(@Nonnull ServiceGroupDeclarationContext ctx)
    {
        super.enterServiceGroupDeclaration(ctx);

        IdentifierContext classNameContext = ctx.classReference().identifier();
        String            className        = classNameContext.getText();

        this.serviceGroupState = new AntlrServiceGroup(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference(),
                classNameContext,
                className,
                this.compilerState.getDomainModelState().getNumTopLevelElements() + 1,
                this.compilerState.getAntlrWalkState().getPackageContext(),
                this.compilerState.getCompilerWalkState().getPackageName(),
                this.compilerState.getDomainModelState().getClassByName(className));
    }

    @Override
    public void exitServiceGroupDeclaration(ServiceGroupDeclarationContext ctx)
    {
        this.compilerState.getDomainModelState().exitServiceGroupDeclaration(this.serviceGroupState);
        this.serviceGroupState = null;
        super.exitServiceGroupDeclaration(ctx);
    }

    @Override
    public void enterUrlDeclaration(@Nonnull UrlDeclarationContext ctx)
    {
        super.enterUrlDeclaration(ctx);
        this.urlState = new AntlrUrl(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference(),
                this.serviceGroupState);
        this.serviceGroupState.enterUrlDeclaration(this.urlState);
    }

    @Override
    public void exitUrlDeclaration(@Nonnull UrlDeclarationContext ctx)
    {
        this.urlState = null;
        super.exitUrlDeclaration(ctx);
    }

    @Override
    public void enterServiceDeclaration(@Nonnull ServiceDeclarationContext ctx)
    {
        super.enterServiceDeclaration(ctx);
        VerbContext verb = ctx.verb();
        AntlrVerb antlrVerb = new AntlrVerb(
                verb,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference(),
                Verb.valueOf(verb.getText()));
        AntlrServiceMultiplicity serviceMultiplicity =
                this.getServiceMultiplicity(ctx.serviceDeclarationBody().serviceMultiplicityDeclaration());

        this.serviceState = new AntlrService(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference(),
                this.urlState,
                antlrVerb,
                serviceMultiplicity);
    }

    private AntlrServiceMultiplicity getServiceMultiplicity(@Nullable ServiceMultiplicityDeclarationContext serviceMultiplicityDeclarationContext)
    {
        if (serviceMultiplicityDeclarationContext == null)
        {
            return new AntlrServiceMultiplicity(
                    new ParserRuleContext(),
                    this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                    true,
                    ServiceMultiplicity.ONE);
        }

        ServiceMultiplicityContext serviceMultiplicityContext = serviceMultiplicityDeclarationContext.serviceMultiplicity();

        return new AntlrServiceMultiplicity(
                serviceMultiplicityContext,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference(),
                this.getServiceMultiplicity(serviceMultiplicityContext));
    }

    @Nonnull
    private ServiceMultiplicity getServiceMultiplicity(ServiceMultiplicityContext serviceMultiplicityContext)
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

    @Override
    public void exitServiceDeclaration(@Nonnull ServiceDeclarationContext ctx)
    {
        this.urlState.exitServiceDeclaration(this.serviceState);
        this.serviceState = null;
        super.exitServiceDeclaration(ctx);
    }

    @Override
    public void enterServiceProjectionDispatch(@Nonnull ServiceProjectionDispatchContext ctx)
    {
        super.enterServiceProjectionDispatch(ctx);
        ProjectionReferenceContext projectionReferenceContext = ctx.projectionReference();

        String          projectionName = projectionReferenceContext.identifier().getText();
        AntlrProjection projection     = this.compilerState.getDomainModelState().getProjectionByName(projectionName);

        if (ctx.argumentList() != null && !ctx.argumentList().argument().isEmpty())
        {
            throw new AssertionError();
        }

        AntlrServiceProjectionDispatch projectionDispatch = new AntlrServiceProjectionDispatch(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference(),
                this.serviceState,
                projection);

        this.serviceState.enterServiceProjectionDispatch(projectionDispatch);
    }
}
