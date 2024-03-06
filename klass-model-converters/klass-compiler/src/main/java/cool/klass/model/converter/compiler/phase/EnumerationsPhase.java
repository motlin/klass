package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.AntlrEnumerationLiteral;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPrettyNameContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.map.MutableMap;

public class EnumerationsPhase extends AbstractDomainModelCompilerPhase
{
    @Nullable
    private       AntlrEnumeration enumerationState;

    public EnumerationsPhase(
            @Nonnull CompilerErrorHolder compilerErrorHolder,
            @Nonnull MutableMap<ParserRuleContext, CompilationUnit> compilationUnitsByContext,
            boolean isInference,
            AntlrDomainModel domainModelState)
    {
        super(compilerErrorHolder, compilationUnitsByContext, isInference, domainModelState);
    }

    @Override
    public void enterEnumerationDeclaration(@Nonnull EnumerationDeclarationContext ctx)
    {
        IdentifierContext identifier = ctx.identifier();
        this.enumerationState = new AntlrEnumeration(
                ctx,
                this.currentCompilationUnit,
                false,
                identifier,
                identifier.getText(),
                this.domainModelState.getNumTopLevelElements() + 1,
                this.packageName);
    }

    @Override
    public void exitEnumerationDeclaration(EnumerationDeclarationContext ctx)
    {
        this.domainModelState.exitEnumerationDeclaration(this.enumerationState);
        this.enumerationState = null;
    }

    @Override
    public void enterEnumerationLiteral(@Nonnull EnumerationLiteralContext ctx)
    {
        String literalName = ctx.identifier().getText();

        EnumerationPrettyNameContext prettyNameContext = ctx.enumerationPrettyName();

        String prettyName = prettyNameContext == null
                ? null
                : prettyNameContext.getText().substring(1, prettyNameContext.getText().length() - 1);

        AntlrEnumerationLiteral enumerationLiteralState = new AntlrEnumerationLiteral(
                ctx,
                this.currentCompilationUnit,
                false,
                ctx.identifier(), literalName,
                this.enumerationState.getNumLiterals() + 1,
                prettyName,
                this.enumerationState);
        this.enumerationState.enterEnumerationLiteral(enumerationLiteralState);
    }
}
