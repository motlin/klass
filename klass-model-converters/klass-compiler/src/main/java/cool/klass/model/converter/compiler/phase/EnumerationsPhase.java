package cool.klass.model.converter.compiler.phase;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.meta.domain.EnumerationLiteral.EnumerationLiteralBuilder;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationPrettyNameContext;
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
        this.enumerationState = new AntlrEnumeration(this.packageName, ctx, ctx.identifier().getText());
        this.domainModelState.enterEnumerationDeclaration(this.enumerationState);
    }

    @Override
    public void exitEnumerationDeclaration(EnumerationDeclarationContext ctx)
    {
        this.enumerationState.exitEnumerationDeclaration(this);
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

        EnumerationLiteralBuilder enumerationLiteralBuilder = new EnumerationLiteralBuilder(
                ctx,
                ctx.identifier(),
                literalName,
                prettyName);
        this.enumerationState.enterEnumerationLiteral(enumerationLiteralBuilder);
    }
}
