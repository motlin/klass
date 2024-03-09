package cool.klass.model.converter.compiler.phase;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrCompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrPackage;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import cool.klass.model.meta.grammar.KlassParser.PackageDeclarationContext;
import org.antlr.v4.runtime.ParserRuleContext;

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

        CompilationUnit currentCompilationUnit = this.compilerState.getCompilerWalk().getCurrentCompilationUnit();
        ParserRuleContext parserContext        = currentCompilationUnit.getParserContext();
        if (ctx != parserContext)
        {
            throw new AssertionError();
        }

        this.compilationUnitState = new AntlrCompilationUnit(
                ctx,
                Optional.of(currentCompilationUnit));
    }

    @Override
    public void exitCompilationUnit(@Nonnull CompilationUnitContext ctx)
    {
        this.compilerState.getDomainModel().exitCompilationUnit(this.compilationUnitState);
        this.compilationUnitState = null;
        super.exitCompilationUnit(ctx);
    }

    @Override
    public void enterPackageDeclaration(@Nonnull PackageDeclarationContext ctx)
    {
        super.enterPackageDeclaration(ctx);

        AntlrPackage pkg = new AntlrPackage(
                ctx,
                Optional.of(this.compilerState.getCompilerWalk().getCurrentCompilationUnit()),
                -1,
                ctx.packageName(),
                this.compilationUnitState);

        this.compilationUnitState.enterPackageDeclaration(pkg);
    }
}
