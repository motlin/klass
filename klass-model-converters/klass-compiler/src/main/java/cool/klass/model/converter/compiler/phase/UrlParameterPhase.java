package cool.klass.model.converter.compiler.phase;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveType;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameter;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameterModifier;
import cool.klass.model.converter.compiler.state.service.url.AntlrUrl;
import cool.klass.model.converter.compiler.state.service.url.AntlrUrlConstant;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.grammar.KlassParser.EnumerationParameterDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.MultiplicityContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterModifierContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitiveParameterDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.QueryParameterListContext;
import cool.klass.model.meta.grammar.KlassParser.UrlConstantContext;
import cool.klass.model.meta.grammar.KlassParser.UrlDeclarationContext;
import org.antlr.v4.runtime.ParserRuleContext;

public class UrlParameterPhase extends AbstractCompilerPhase
{
    @Nullable
    private Boolean        inQueryParameterList;
    @Nullable
    private AntlrParameter parameterState;

    public UrlParameterPhase(CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public void enterUrlDeclaration(@Nonnull UrlDeclarationContext ctx)
    {
        super.enterUrlDeclaration(ctx);
        this.inQueryParameterList = false;
    }

    @Override
    public void exitUrlDeclaration(@Nonnull UrlDeclarationContext ctx)
    {
        this.inQueryParameterList = null;
        super.exitUrlDeclaration(ctx);
    }

    @Override
    public void enterUrlConstant(@Nonnull UrlConstantContext ctx)
    {
        super.enterUrlConstant(ctx);
        AntlrUrl        urlState               = this.compilerState.getCompilerWalkState().getUrlState();
        CompilationUnit currentCompilationUnit = this.compilerState.getCompilerWalkState().getCurrentCompilationUnit();
        AntlrUrlConstant antlrUrlConstant = new AntlrUrlConstant(
                ctx,
                Optional.of(currentCompilationUnit),
                ctx.identifier(),
                ctx.identifier().getText(),
                urlState.getNumPathSegments() + 1);
        urlState.enterUrlConstant(antlrUrlConstant);
    }

    @Override
    public void enterQueryParameterList(QueryParameterListContext ctx)
    {
        super.enterQueryParameterList(ctx);
        this.inQueryParameterList = true;
    }

    @Override
    public void enterPrimitiveParameterDeclaration(@Nonnull PrimitiveParameterDeclarationContext ctx)
    {
        super.enterPrimitiveParameterDeclaration(ctx);
        if (this.compilerState.getCompilerWalkState().getUrlState() == null)
        {
            return;
        }

        String        primitiveTypeName = ctx.primitiveType().getText();
        PrimitiveType primitiveType     = PrimitiveType.byPrettyName(primitiveTypeName);
        AntlrType     antlrType         = AntlrPrimitiveType.valueOf(primitiveType);

        this.enterParameterDeclaration(ctx, antlrType, ctx.identifier(), ctx.multiplicity());
    }

    @Override
    public void exitPrimitiveParameterDeclaration(PrimitiveParameterDeclarationContext ctx)
    {
        this.parameterState = null;
        super.exitPrimitiveParameterDeclaration(ctx);
    }

    @Override
    public void enterEnumerationParameterDeclaration(@Nonnull EnumerationParameterDeclarationContext ctx)
    {
        super.enterEnumerationParameterDeclaration(ctx);
        if (this.compilerState.getCompilerWalkState().getUrlState() == null)
        {
            return;
        }

        String    enumerationName = ctx.enumerationReference().getText();
        AntlrType antlrType       = this.compilerState.getDomainModelState().getEnumerationByName(enumerationName);

        this.enterParameterDeclaration(ctx, antlrType, ctx.identifier(), ctx.multiplicity());
    }

    @Override
    public void exitEnumerationParameterDeclaration(EnumerationParameterDeclarationContext ctx)
    {
        this.parameterState = null;
        super.exitEnumerationParameterDeclaration(ctx);
    }

    private void enterParameterDeclaration(
            @Nonnull ParserRuleContext ctx,
            @Nonnull AntlrType antlrType,
            @Nonnull IdentifierContext identifier, @Nonnull MultiplicityContext multiplicityContext)
    {
        CompilationUnit currentCompilationUnit = this.compilerState.getCompilerWalkState().getCurrentCompilationUnit();
        AntlrMultiplicity multiplicityState = new AntlrMultiplicity(
                multiplicityContext,
                Optional.of(currentCompilationUnit)
        );

        AntlrUrl urlState = this.compilerState.getCompilerWalkState().getUrlState();
        int ordinal = this.inQueryParameterList
                ? urlState.getNumQueryParameters() + 1
                : urlState.getNumPathSegments() + 1;

        this.parameterState = new AntlrParameter(
                ctx,
                Optional.of(currentCompilationUnit),
                identifier,
                identifier.getText(),
                ordinal,
                antlrType,
                multiplicityState,
                urlState);

        if (this.inQueryParameterList)
        {
            urlState.enterQueryParameterDeclaration(this.parameterState);
        }
        else
        {
            urlState.enterPathParameterDeclaration(this.parameterState);
        }
    }

    @Override
    public void enterParameterModifier(@Nonnull ParameterModifierContext ctx)
    {
        super.enterParameterModifier(ctx);
        int ordinal = this.parameterState.getNumModifiers();
        CompilationUnit currentCompilationUnit = this.compilerState.getCompilerWalkState().getCurrentCompilationUnit();
        AntlrParameterModifier parameterModifierState = new AntlrParameterModifier(
                ctx,
                Optional.of(currentCompilationUnit),
                ctx,
                ctx.getText(),
                ordinal);
        this.parameterState.enterParameterModifier(parameterModifierState);
    }
}
