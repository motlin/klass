package cool.klass.model.converter.compiler.phase;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.projection.AntlrProjection;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionDeclarationContext;

public class ProjectionDeclarationPhase
        extends AbstractCompilerPhase
{
    @Nullable
    private AntlrProjection projection;

    public ProjectionDeclarationPhase(@Nonnull CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public void enterProjectionDeclaration(@Nonnull ProjectionDeclarationContext ctx)
    {
        super.enterProjectionDeclaration(ctx);

        String            classifierName = ctx.classifierReference().identifier().getText();
        AntlrClassifier   classifier     = this.compilerState.getDomainModel().getClassifierByName(classifierName);
        IdentifierContext nameContext    = ctx.identifier();
        CompilationUnit currentCompilationUnit =
                this.compilerState.getCompilerWalk().getCurrentCompilationUnit();
        this.projection = new AntlrProjection(
                ctx,
                Optional.of(currentCompilationUnit),
                this.compilerState.getOrdinal(ctx),
                nameContext,
                this.compilerState.getCompilerWalk().getCompilationUnit(),
                classifier,
                this.compilerState.getCompilerWalk().getPackageName());
    }

    @Override
    public void exitProjectionDeclaration(@Nonnull ProjectionDeclarationContext ctx)
    {
        this.compilerState.getDomainModel().exitProjectionDeclaration(this.projection);

        super.exitProjectionDeclaration(ctx);
    }
}
