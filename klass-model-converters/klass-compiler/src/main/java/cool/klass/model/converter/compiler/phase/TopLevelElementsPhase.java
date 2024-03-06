package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.meta.grammar.KlassParser.TopLevelDeclarationContext;

public class TopLevelElementsPhase
        extends AbstractCompilerPhase
{
    public TopLevelElementsPhase(@Nonnull CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public void enterTopLevelDeclaration(@Nonnull TopLevelDeclarationContext ctx)
    {
        super.enterTopLevelDeclaration(ctx);
        this.compilerState.getDomainModelState().enterTopLevelDeclaration(ctx);
    }
}
