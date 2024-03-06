package cool.klass.model.converter.compiler.phase;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.AntlrEnumerationLiteral;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPrettyNameContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.eclipse.collections.api.map.MapIterable;

public class EnumerationsPhase extends AbstractCompilerPhase
{
    private final AntlrDomainModel domainModelState;
    private       AntlrEnumeration enumerationState;

    public EnumerationsPhase(
            CompilerErrorHolder compilerErrorHolder,
            MapIterable<CompilationUnitContext, CompilationUnit> compilationUnitsByContext,
            AntlrDomainModel domainModelState)
    {
        super(compilerErrorHolder, compilationUnitsByContext);
        this.domainModelState = domainModelState;
    }

    @Override
    public void enterEnumerationDeclaration(EnumerationDeclarationContext ctx)
    {
        IdentifierContext identifier = ctx.identifier();
        this.enumerationState = new AntlrEnumeration(
                ctx,
                this.currentCompilationUnit,
                false,
                identifier,
                identifier.getText(),
                this.packageName);
        this.domainModelState.enterEnumerationDeclaration(this.enumerationState);
    }

    @Override
    public void exitEnumerationDeclaration(EnumerationDeclarationContext ctx)
    {
        this.enumerationState = null;
    }

    @Override
    public void enterEnumerationLiteral(EnumerationLiteralContext ctx)
    {
        String literalName = ctx.identifier().getText();

        EnumerationPrettyNameContext prettyNameContext = ctx.enumerationPrettyName();

        String prettyName = prettyNameContext == null
                ? null
                : prettyNameContext.getText().substring(1, prettyNameContext.getText().length() - 1);

        AntlrEnumerationLiteral antlrEnumerationLiteral = new AntlrEnumerationLiteral(
                ctx,
                this.currentCompilationUnit,
                false,
                literalName,
                ctx.identifier(),
                prettyName,
                this.enumerationState);
        this.enumerationState.enterEnumerationLiteral(antlrEnumerationLiteral);
    }
}
