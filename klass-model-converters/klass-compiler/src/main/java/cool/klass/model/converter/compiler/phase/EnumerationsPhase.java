package cool.klass.model.converter.compiler.phase;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.AntlrEnumerationLiteral;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPrettyNameContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.RuleContext;

public class EnumerationsPhase extends AbstractCompilerPhase
{
    @Nullable
    private AntlrEnumeration enumerationState;

    public EnumerationsPhase(@Nonnull CompilerState compilerState)
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
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                this.compilerState.getOrdinal(ctx),
                identifier,
                this.compilerState.getCompilerWalkState().getCompilationUnitState());
    }

    @Override
    public void exitEnumerationDeclaration(@Nonnull EnumerationDeclarationContext ctx)
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

        Optional<EnumerationPrettyNameContext> prettyNameContext = Optional.ofNullable(ctx.enumerationPrettyName());

        Optional<String> prettyName = prettyNameContext
                .map(RuleContext::getText)
                .map(this::trimQuotes);

        AntlrEnumerationLiteral enumerationLiteralState = new AntlrEnumerationLiteral(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                this.enumerationState.getNumLiterals() + 1,
                ctx.identifier(),
                prettyName,
                this.enumerationState);
        this.enumerationState.enterEnumerationLiteral(enumerationLiteralState);
    }

    @Nonnull
    private String trimQuotes(@Nonnull String text)
    {
        return text.substring(1, text.length() - 1);
    }
}
