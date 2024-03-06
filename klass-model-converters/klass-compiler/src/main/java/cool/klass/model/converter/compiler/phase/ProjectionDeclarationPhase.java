package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.projection.AntlrProjection;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;

public class ProjectionDeclarationPhase extends AbstractCompilerPhase
{
    private AntlrProjection projectionState;

    public ProjectionDeclarationPhase(CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public void enterProjectionDeclaration(@Nonnull ProjectionDeclarationContext ctx)
    {
        super.enterProjectionDeclaration(ctx);

        String            className   = ctx.classReference().identifier().getText();
        AntlrClass        klass       = this.compilerState.getDomainModelState().getClassByName(className);
        IdentifierContext nameContext = ctx.identifier();
        this.projectionState = new AntlrProjection(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().getMacroElement(),
                nameContext,
                nameContext.getText(),
                this.compilerState.getDomainModelState().getNumTopLevelElements() + 1,
                klass,
                this.compilerState.getCompilerWalkState().getPackageName());
    }

    @Override
    public void exitProjectionDeclaration(ProjectionDeclarationContext ctx)
    {
        this.compilerState.getDomainModelState().exitProjectionDeclaration(this.projectionState);

        super.exitProjectionDeclaration(ctx);
    }
}
