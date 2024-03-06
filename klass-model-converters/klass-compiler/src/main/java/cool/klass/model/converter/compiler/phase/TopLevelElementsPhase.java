package cool.klass.model.converter.compiler.phase;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.meta.grammar.KlassParser.TopLevelDeclarationContext;

public class TopLevelElementsPhase extends AbstractCompilerPhase
{
    public TopLevelElementsPhase(CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public void enterTopLevelDeclaration(TopLevelDeclarationContext ctx)
    {
        super.enterTopLevelDeclaration(ctx);
        this.compilerState.getDomainModelState().enterTopLevelDeclaration(ctx);
    }
}
