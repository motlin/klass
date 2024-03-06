package cool.klass.model.converter.compiler.phase;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.AntlrMultiplicityOwner;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveType;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameter;
import cool.klass.model.converter.compiler.state.property.AntlrModifier;
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

    @Nullable
    private AntlrMultiplicityOwner multiplicityOwnerState;

    public UrlParameterPhase(@Nonnull CompilerState compilerState)
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

        AntlrUrl urlState = this.compilerState.getCompilerWalkState().getUrlState();
        AntlrUrlConstant antlrUrlConstant = new AntlrUrlConstant(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                ctx.identifier(),
                urlState.getNumPathSegments() + 1);
        urlState.enterUrlConstant(antlrUrlConstant);
    }

    @Override
    public void enterQueryParameterList(@Nonnull QueryParameterListContext ctx)
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

        String             primitiveTypeName  = ctx.primitiveType().getText();
        PrimitiveType      primitiveType      = PrimitiveType.byPrettyName(primitiveTypeName);
        AntlrPrimitiveType primitiveTypeState = AntlrPrimitiveType.valueOf(primitiveType);

        this.enterParameterDeclaration(ctx, primitiveTypeState, ctx.identifier());
    }

    @Override
    public void exitPrimitiveParameterDeclaration(@Nonnull PrimitiveParameterDeclarationContext ctx)
    {
        this.parameterState         = null;
        this.multiplicityOwnerState = null;

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

        String enumerationName = ctx.enumerationReference().getText();
        AntlrEnumeration enumerationState =
                this.compilerState.getDomainModelState().getEnumerationByName(enumerationName);

        this.enterParameterDeclaration(ctx, enumerationState, ctx.identifier());
    }

    @Override
    public void exitEnumerationParameterDeclaration(@Nonnull EnumerationParameterDeclarationContext ctx)
    {
        this.parameterState         = null;
        this.multiplicityOwnerState = null;

        super.exitEnumerationParameterDeclaration(ctx);
    }

    @Override
    public void enterParameterModifier(@Nonnull ParameterModifierContext ctx)
    {
        super.enterParameterModifier(ctx);

        // TODO: Check if parameterState is non null?
        int ordinal = this.parameterState.getNumModifiers();
        AntlrModifier modifierState = new AntlrModifier(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                ctx,
                ordinal,
                this.parameterState);
        this.parameterState.enterModifier(modifierState);
    }

    private void enterParameterDeclaration(
            @Nonnull ParserRuleContext ctx,
            @Nonnull AntlrType typeState,
            @Nonnull IdentifierContext identifierContext)
    {
        if (this.parameterState != null)
        {
            throw new AssertionError();
        }

        AntlrUrl urlState = this.compilerState.getCompilerWalkState().getUrlState();
        int ordinal = this.inQueryParameterList
                ? urlState.getNumQueryParameters() + 1
                : urlState.getNumPathSegments() + 1;

        this.parameterState         = new AntlrParameter(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                identifierContext,
                ordinal,
                typeState,
                urlState);
        this.multiplicityOwnerState = this.parameterState;

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
    public void enterMultiplicity(@Nonnull MultiplicityContext ctx)
    {
        super.enterMultiplicity(ctx);

        if (this.multiplicityOwnerState != null)
        {
            AntlrMultiplicity multiplicityState = new AntlrMultiplicity(
                    ctx,
                    Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                    this.multiplicityOwnerState);

            this.multiplicityOwnerState.enterMultiplicity(multiplicityState);
        }
    }
}
