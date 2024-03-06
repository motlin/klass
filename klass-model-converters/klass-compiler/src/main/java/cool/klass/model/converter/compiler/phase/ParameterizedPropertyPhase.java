package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.phase.criteria.CriteriaVisitor;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveType;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.criteria.AntlrCriteria;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameter;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameterModifier;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameterOwner;
import cool.klass.model.converter.compiler.state.property.AntlrParameterizedProperty;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.grammar.KlassParser.ClassTypeContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationParameterDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.MultiplicityContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterDeclarationListContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterModifierContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterizedPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitiveParameterDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitiveTypeContext;
import cool.klass.model.meta.grammar.KlassParser.RelationshipContext;
import cool.klass.model.meta.grammar.KlassVisitor;
import org.antlr.v4.runtime.ParserRuleContext;

public class ParameterizedPropertyPhase extends AbstractCompilerPhase
{
    @Nullable
    private AntlrParameterizedProperty parameterizedPropertyState;
    // TODO: Make better use of these Owner interfaces in shared compiler phases
    @Nullable
    private IAntlrElement              criteriaOwnerState;
    @Nullable
    private AntlrParameterOwner        parameterOwnerState;

    @Nullable
    private AntlrParameter parameterState;

    public ParameterizedPropertyPhase(CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public void enterParameterizedProperty(@Nonnull ParameterizedPropertyContext ctx)
    {
        super.enterParameterizedProperty(ctx);

        ClassTypeContext classTypeContext          = ctx.classType();
        String           parameterizedPropertyName = ctx.identifier().getText();
        String           className                 = classTypeContext.classReference().getText();
        AntlrClass       classState                = this.compilerState.getDomainModelState().getClassByName(className);

        AntlrMultiplicity multiplicityState = new AntlrMultiplicity(
                classTypeContext.multiplicity(),
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference());

        // TODO: Parameterized Property modifiers
        /*
        ImmutableList<AntlrParameterizedPropertyModifier> parameterizedPropertyModifiers = ListAdapter.adapt(ctx.parameterizedPropertyModifier())
                .collectWithIndex(this::getAntlrParameterizedPropertyModifier)
                .toImmutable();
        */

        AntlrClass thisReference = (AntlrClass) this.compilerState.getCompilerWalkState().getThisReference();
        this.parameterizedPropertyState = new AntlrParameterizedProperty(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference(),
                ctx.identifier(),
                parameterizedPropertyName,
                thisReference.getNumMembers() + 1,
                thisReference,
                classState,
                multiplicityState);

        thisReference.enterParameterizedProperty(this.parameterizedPropertyState);

        this.parameterOwnerState = this.parameterizedPropertyState;
        this.criteriaOwnerState = this.parameterizedPropertyState;
    }

    @Override
    public void exitParameterizedProperty(ParameterizedPropertyContext ctx)
    {
        this.parameterizedPropertyState = null;
        this.parameterOwnerState = null;
        this.criteriaOwnerState = null;

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

        PrimitiveTypeContext primitiveTypeContext = ctx.primitiveType();
        PrimitiveType        primitiveType        = PrimitiveType.byPrettyName(primitiveTypeContext.getText());
        AntlrType            primitiveTypeState   = AntlrPrimitiveType.valueOf(primitiveType);

        this.enterParameterDeclaration(ctx, primitiveTypeState, ctx.identifier(), ctx.multiplicity());
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
        if (this.parameterizedPropertyState == null)
        {
            return;
        }

        EnumerationReferenceContext enumerationReferenceContext = ctx.enumerationReference();
        AntlrType enumerationState = this.compilerState.getDomainModelState().getEnumerationByName(
                enumerationReferenceContext.getText());

        this.enterParameterDeclaration(ctx, enumerationState, ctx.identifier(), ctx.multiplicity());
    }

    @Override
    public void exitEnumerationParameterDeclaration(EnumerationParameterDeclarationContext ctx)
    {
        this.parameterState = null;
        super.exitEnumerationParameterDeclaration(ctx);
    }

    @Override
    public void enterParameterDeclarationList(ParameterDeclarationListContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".enterParameterDeclarationList() not implemented yet");
    }

    @Override
    public void exitParameterDeclarationList(ParameterDeclarationListContext ctx)
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
        AntlrParameterModifier parameterModifierState = new AntlrParameterModifier(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference(),
                ctx,
                ctx.getText(),
                ordinal);
        this.parameterState.enterParameterModifier(parameterModifierState);
    }

    private void enterParameterDeclaration(
            @Nonnull ParserRuleContext ctx,
            @Nonnull AntlrType typeState,
            @Nonnull IdentifierContext identifierContext, @Nonnull MultiplicityContext multiplicityContext)
    {
        AntlrMultiplicity multiplicityState = new AntlrMultiplicity(
                multiplicityContext,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference());

        AntlrParameter parameterState = new AntlrParameter(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference(),
                identifierContext,
                identifierContext.getText(),
                this.parameterOwnerState.getNumParameters() + 1,
                typeState,
                multiplicityState,
                (IAntlrElement) this.parameterOwnerState);
        this.parameterState = parameterState;
        this.parameterOwnerState.enterParameterDeclaration(parameterState);
    }
}
