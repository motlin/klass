package cool.klass.model.converter.compiler.phase;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.phase.criteria.CriteriaVisitor;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrClassType;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.AntlrMultiplicityOwner;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveType;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.criteria.AntlrCriteria;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameter;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameterModifier;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameterOwner;
import cool.klass.model.converter.compiler.state.property.AntlrClassTypeOwner;
import cool.klass.model.converter.compiler.state.property.AntlrParameterizedProperty;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ClassTypeContext;
import cool.klass.model.meta.grammar.KlassParser.EnumerationParameterDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.MultiplicityContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterDeclarationListContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterModifierContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterizedPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.PrimitiveParameterDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.RelationshipContext;
import cool.klass.model.meta.grammar.KlassVisitor;
import org.antlr.v4.runtime.ParserRuleContext;

public class ParameterizedPropertyPhase extends AbstractCompilerPhase
{
    @Nullable
    private AntlrParameterizedProperty parameterizedPropertyState;
    @Nullable
    private AntlrParameter             parameterState;
    @Nullable
    private AntlrClassType             classTypeState;

    // TODO: Make better use of these Owner interfaces in shared compiler phases
    @Nullable
    private IAntlrElement          criteriaOwnerState;
    @Nullable
    private AntlrParameterOwner    parameterOwnerState;
    @Nullable
    private AntlrClassTypeOwner    classTypeOwnerState;
    @Nullable
    private AntlrMultiplicityOwner multiplicityOwnerState;

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
        if (this.classTypeOwnerState != null)
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

        this.parameterOwnerState = this.parameterizedPropertyState;
        this.criteriaOwnerState  = this.parameterizedPropertyState;
        this.classTypeOwnerState = this.parameterizedPropertyState;
    }

    @Override
    public void exitParameterizedProperty(@Nonnull ParameterizedPropertyContext ctx)
    {
        this.parameterizedPropertyState = null;
        this.parameterOwnerState        = null;
        this.criteriaOwnerState         = null;
        this.classTypeOwnerState        = null;

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
        AntlrParameterModifier parameterModifierState = new AntlrParameterModifier(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                ctx,
                ctx.getText(),
                ordinal);
        this.parameterState.enterParameterModifier(parameterModifierState);
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

    @Nullable
    protected AntlrClassTypeOwner getAntlrClassTypeOwner()
    {
        return this.parameterizedPropertyState;
    }

    @Override
    public void enterClassType(@Nonnull ClassTypeContext ctx)
    {
        super.enterClassType(ctx);

        if (this.classTypeState != null)
        {
            throw new IllegalStateException();
        }
        if (this.multiplicityOwnerState != null)
        {
            throw new IllegalStateException();
        }

        if (this.getAntlrClassTypeOwner() == null)
        {
            return;
        }

        this.classTypeState         = new AntlrClassType(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                this.getAntlrClassTypeOwner());
        this.multiplicityOwnerState = this.classTypeState;
        this.getAntlrClassTypeOwner().enterClassType(this.classTypeState);
    }

    @Override
    public void exitClassType(@Nonnull ClassTypeContext ctx)
    {
        this.classTypeState         = null;
        this.multiplicityOwnerState = null;

        super.exitClassType(ctx);
    }

    @Override
    public void enterClassReference(@Nonnull ClassReferenceContext ctx)
    {
        super.enterClassReference(ctx);

        if (this.classTypeState == null)
        {
            return;
        }

        String           className        = ctx.identifier().getText();
        AntlrDomainModel domainModelState = this.compilerState.getDomainModelState();
        AntlrClass       classState       = domainModelState.getClassByName(className);

        this.classTypeState.enterClassReference(classState);
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
