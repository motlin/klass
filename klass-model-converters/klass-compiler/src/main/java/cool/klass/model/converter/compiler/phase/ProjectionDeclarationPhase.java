package cool.klass.model.converter.compiler.phase;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.projection.AntlrProjection;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;

public class ProjectionDeclarationPhase extends AbstractCompilerPhase
{
    @Nullable
    private AntlrProjection projectionState;

    public ProjectionDeclarationPhase(@Nonnull CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public void enterProjectionDeclaration(@Nonnull ProjectionDeclarationContext ctx)
    {
        super.enterProjectionDeclaration(ctx);

        String            className              = ctx.classReference().identifier().getText();
        AntlrClass        klass                  = this.compilerState.getDomainModelState().getClassByName(className);
        IdentifierContext nameContext            = ctx.identifier();
        CompilationUnit   currentCompilationUnit =
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit();
        this.projectionState = new AntlrProjection(
                ctx,
                Optional.of(currentCompilationUnit),
                nameContext,
                nameContext.getText(),
                this.compilerState.getOrdinal(ctx),
                klass,
                this.compilerState.getCompilerWalkState().getPackageName());
    }

    @Override
    public void exitProjectionDeclaration(@Nonnull ProjectionDeclarationContext ctx)
    {
        this.compilerState.getDomainModelState().exitProjectionDeclaration(this.projectionState);

        super.exitProjectionDeclaration(ctx);
    }
}
