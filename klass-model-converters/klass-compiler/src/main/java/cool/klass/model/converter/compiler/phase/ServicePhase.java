package cool.klass.model.converter.compiler.phase;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.projection.AntlrProjection;
import cool.klass.model.converter.compiler.state.service.AntlrService;
import cool.klass.model.converter.compiler.state.service.AntlrServiceGroup;
import cool.klass.model.converter.compiler.state.service.AntlrServiceProjectionDispatch;
import cool.klass.model.converter.compiler.state.service.AntlrVerb;
import cool.klass.model.converter.compiler.state.service.url.AntlrUrl;
import cool.klass.model.meta.domain.api.service.Verb;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceGroupDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceProjectionDispatchContext;
import cool.klass.model.meta.grammar.KlassParser.UrlDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.VerbContext;

public class ServicePhase extends AbstractCompilerPhase
{
    @Nullable
    private AntlrServiceGroup serviceGroupState;
    @Nullable
    private AntlrUrl          urlState;
    @Nullable
    private AntlrService      serviceState;

    public ServicePhase(@Nonnull CompilerState compilerState)
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
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                classNameContext,
                this.compilerState.getOrdinal(ctx),
                this.compilerState.getCompilerWalkState().getPackageNameContext(),
                this.compilerState.getCompilerWalkState().getPackageName(),
                this.compilerState.getDomainModelState().getClassByName(className));
    }

    @Override
    public void exitServiceGroupDeclaration(@Nonnull ServiceGroupDeclarationContext ctx)
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
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
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
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                Verb.valueOf(verb.getText()));

        this.serviceState = new AntlrService(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                this.urlState,
                antlrVerb);
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
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                this.serviceState,
                projection);

        this.serviceState.enterServiceProjectionDispatch(projectionDispatch);
    }
}
