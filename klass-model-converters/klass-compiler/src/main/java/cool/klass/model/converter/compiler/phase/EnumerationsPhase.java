package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.AntlrEnumerationLiteral;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPrettyNameContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;

public class EnumerationsPhase extends AbstractCompilerPhase
{
    @Nullable
    private       AntlrEnumeration enumerationState;

    public EnumerationsPhase(CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public void enterEnumerationDeclaration(@Nonnull EnumerationDeclarationContext ctx)
    {
        super.enterEnumerationDeclaration(ctx);

        IdentifierContext identifier = ctx.identifier();
        this.enumerationState = new AntlrEnumeration(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference(),
                identifier,
                identifier.getText(),
                this.compilerState.getDomainModelState().getNumTopLevelElements() + 1,
                this.compilerState.getAntlrWalkState().getPackageContext(),
                this.compilerState.getCompilerWalkState().getPackageName());
    }

    @Override
    public void exitEnumerationDeclaration(EnumerationDeclarationContext ctx)
    {
        this.compilerState.getDomainModelState().exitEnumerationDeclaration(this.enumerationState);
        this.enumerationState = null;
        super.exitEnumerationDeclaration(ctx);
    }

    @Override
    public void enterEnumerationLiteral(@Nonnull EnumerationLiteralContext ctx)
    {
        super.enterEnumerationLiteral(ctx);
        String literalName = ctx.identifier().getText();

        EnumerationPrettyNameContext prettyNameContext = ctx.enumerationPrettyName();

        String prettyName = prettyNameContext == null
                ? null
                : prettyNameContext.getText().substring(1, prettyNameContext.getText().length() - 1);

        AntlrEnumerationLiteral enumerationLiteralState = new AntlrEnumerationLiteral(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference(),
                ctx.identifier(),
                literalName,
                this.enumerationState.getNumLiterals() + 1,
                prettyName,
                this.enumerationState);
        this.enumerationState.enterEnumerationLiteral(enumerationLiteralState);
    }
}
