package cool.klass.model.converter.compiler.phase;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.phase.criteria.CriteriaVisitor;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveType;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.criteria.AntlrCriteria;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameter;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameterOwner;
import cool.klass.model.converter.compiler.state.property.AntlrModifier;
import cool.klass.model.converter.compiler.state.property.AntlrParameterizedProperty;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.grammar.KlassParser.EnumerationParameterDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterDeclarationListContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterModifierContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterizedPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitiveParameterDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.RelationshipContext;
import cool.klass.model.meta.grammar.KlassVisitor;
import org.antlr.v4.runtime.ParserRuleContext;

public class ParameterizedPropertyPhase
        extends ReferencePropertyPhase
{
    @Nullable
    private AntlrParameterizedProperty parameterizedPropertyState;
    @Nullable
    private AntlrParameter             parameterState;

    // TODO: Make better use of these Owner interfaces in shared compiler phases
    @Nullable
    private IAntlrElement       criteriaOwnerState;
    @Nullable
    private AntlrParameterOwner parameterOwnerState;

    public ParameterizedPropertyPhase(@Nonnull CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public void enterParameterizedProperty(@Nonnull ParameterizedPropertyContext ctx)
    {
        super.enterParameterizedProperty(ctx);

        if (this.parameterizedPropertyState != null)
        {
            throw new IllegalStateException();
        }
        if (this.parameterOwnerState != null)
        {
            throw new IllegalStateException();
        }
        if (this.criteriaOwnerState != null)
        {
            throw new IllegalStateException();
        }
        if (this.classReferenceOwnerState != null)
        {
            throw new IllegalStateException();
        }
        if (this.multiplicityOwnerState != null)
        {
            throw new IllegalStateException();
        }

        // TODO: Parameterized Property modifiers

        AntlrClass thisReference = (AntlrClass) this.compilerState.getCompilerWalkState().getThisReference();

        this.parameterizedPropertyState = new AntlrParameterizedProperty(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                ctx.identifier(),
                ctx.identifier().getText(),
                thisReference.getNumMembers() + 1,
                thisReference);

        thisReference.enterParameterizedProperty(this.parameterizedPropertyState);

        this.parameterOwnerState      = this.parameterizedPropertyState;
        this.criteriaOwnerState       = this.parameterizedPropertyState;
        this.classReferenceOwnerState = this.parameterizedPropertyState;
        this.multiplicityOwnerState   = this.parameterizedPropertyState;

        this.handleClassReference(ctx.classReference());
        this.handleMultiplicity(ctx.multiplicity());
    }

    @Override
    public void exitParameterizedProperty(@Nonnull ParameterizedPropertyContext ctx)
    {
        this.parameterizedPropertyState = null;
        this.parameterOwnerState        = null;
        this.criteriaOwnerState         = null;
        this.classReferenceOwnerState   = null;
        this.multiplicityOwnerState     = null;

        super.exitParameterizedProperty(ctx);
    }

    // TODO: Should probably be moved to its own phase for consistency
    @Override
    public void enterRelationship(@Nonnull RelationshipContext ctx)
    {
        super.enterRelationship(ctx);

        if (this.parameterizedPropertyState == null)
        {
            return;
        }

        KlassVisitor<AntlrCriteria> visitor = new CriteriaVisitor(
                this.compilerState,
                this.criteriaOwnerState);
        AntlrCriteria criteriaState = visitor.visit(ctx.criteriaExpression());
        this.parameterizedPropertyState.setCriteria(criteriaState);
    }

    @Override
    public void enterPrimitiveParameterDeclaration(@Nonnull PrimitiveParameterDeclarationContext ctx)
    {
        super.enterPrimitiveParameterDeclaration(ctx);

        if (this.parameterizedPropertyState == null)
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

        if (this.parameterizedPropertyState == null)
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
    public void enterParameterDeclarationList(@Nonnull ParameterDeclarationListContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterParameterDeclarationList() not implemented yet");
    }

    @Override
    public void exitParameterDeclarationList(@Nonnull ParameterDeclarationListContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitParameterDeclarationList() not implemented yet");
    }

    @Override
    public void enterParameterModifier(@Nonnull ParameterModifierContext ctx)
    {
        super.enterParameterModifier(ctx);
        if (this.parameterizedPropertyState == null && this.parameterState == null)
        {
            return;
        }

        int ordinal = this.parameterState.getNumModifiers();
        AntlrModifier modifierState = new AntlrModifier(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                ctx,
                ctx.getText(),
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
            throw new IllegalStateException();
        }

        this.parameterState         = new AntlrParameter(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                identifierContext,
                identifierContext.getText(),
                this.parameterOwnerState.getNumParameters() + 1,
                typeState,
                (IAntlrElement) this.parameterOwnerState);
        this.multiplicityOwnerState = this.parameterState;
        this.parameterOwnerState.enterParameterDeclaration(this.parameterState);
    }
}
