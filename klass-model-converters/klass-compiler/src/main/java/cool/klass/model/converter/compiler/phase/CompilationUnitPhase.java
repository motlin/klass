package cool.klass.model.converter.compiler.phase;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrCompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrPackage;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import cool.klass.model.meta.grammar.KlassParser.PackageDeclarationContext;

public class CompilationUnitPhase
        extends AbstractCompilerPhase
{
    private AntlrCompilationUnit compilationUnitState;

    public CompilationUnitPhase(@Nonnull CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public void enterCompilationUnit(@Nonnull CompilationUnitContext ctx)
    {
        super.enterCompilationUnit(ctx);

        this.compilationUnitState = new AntlrCompilationUnit(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()));
    }

    @Override
    public void exitCompilationUnit(@Nonnull CompilationUnitContext ctx)
    {
        this.compilerState.getDomainModelState().exitCompilationUnit(compilationUnitState);
        this.compilationUnitState = null;
        super.exitCompilationUnit(ctx);
    }

    @Override
    public void enterPackageDeclaration(@Nonnull PackageDeclarationContext ctx)
    {
        super.enterPackageDeclaration(ctx);

        String packageName = ctx.packageName().getText();
        AntlrPackage packageState = new AntlrPackage(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                ctx.packageName(),
                -1,
                this.compilationUnitState);

        this.compilationUnitState.enterPackageDeclaration(packageState);
    }
}
