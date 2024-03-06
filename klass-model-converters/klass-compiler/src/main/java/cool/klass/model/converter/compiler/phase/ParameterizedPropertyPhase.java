package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.phase.criteria.CriteriaVisitor;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
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
import org.eclipse.collections.api.map.MutableMap;

public class ParameterizedPropertyPhase extends AbstractDomainModelCompilerPhase
{
    @Nullable
    private       AntlrParameterizedProperty parameterizedPropertyState;
    // TODO: Make better use of these Owner interfaces in shared compiler phases
    @Nullable
    private       IAntlrElement              criteriaOwnerState;
    @Nullable
    private       AntlrParameterOwner        parameterOwnerState;

    @Nullable
    private AntlrParameter parameterState;

    public ParameterizedPropertyPhase(
            @Nonnull CompilerErrorHolder compilerErrorHolder,
            @Nonnull MutableMap<ParserRuleContext, CompilationUnit> compilationUnitsByContext,
            boolean isInference,
            AntlrDomainModel domainModelState)
    {
        super(compilerErrorHolder, compilationUnitsByContext, isInference, domainModelState);
    }

    @Override
    public void enterParameterizedProperty(@Nonnull ParameterizedPropertyContext ctx)
    {
        super.enterParameterizedProperty(ctx);

        ClassTypeContext classTypeContext          = ctx.classType();
        String           parameterizedPropertyName = ctx.identifier().getText();
        String           className                 = classTypeContext.classReference().getText();
        AntlrClass       antlrClass                = this.domainModelState.getClassByName(className);

        AntlrMultiplicity multiplicityState = new AntlrMultiplicity(
                classTypeContext.multiplicity(),
                this.currentCompilationUnit,
                this.isInference);

        // TODO: Parameterized Property modifiers
        /*
        ImmutableList<AntlrParameterizedPropertyModifier> parameterizedPropertyModifiers = ListAdapter.adapt(ctx.parameterizedPropertyModifier())
                .collectWithIndex(this::getAntlrParameterizedPropertyModifier)
                .toImmutable();
        */

        this.parameterizedPropertyState = new AntlrParameterizedProperty(
                ctx,
                this.currentCompilationUnit,
                this.isInference,
                ctx.identifier(),
                parameterizedPropertyName,
                this.thisReference.getNumMembers() + 1,
                this.thisReference,
                antlrClass,
                multiplicityState);

        this.thisReference.enterParameterizedProperty(this.parameterizedPropertyState);

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
        if (this.parameterizedPropertyState == null)
        {
            return;
        }

        KlassVisitor<AntlrCriteria> visitor = new CriteriaVisitor(
                this.currentCompilationUnit,
                this.domainModelState,
                this.criteriaOwnerState,
                this.thisReference);
        AntlrCriteria criteriaState = visitor.visit(ctx.criteriaExpression());
        this.parameterizedPropertyState.setCriteria(criteriaState);
    }

    @Override
    public void enterPrimitiveParameterDeclaration(@Nonnull PrimitiveParameterDeclarationContext ctx)
    {
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
    }

    @Override
    public void enterEnumerationParameterDeclaration(@Nonnull EnumerationParameterDeclarationContext ctx)
    {
        if (this.parameterizedPropertyState == null)
        {
            return;
        }

        EnumerationReferenceContext enumerationReferenceContext = ctx.enumerationReference();
        AntlrType enumerationState = this.domainModelState.getEnumerationByName(
                enumerationReferenceContext.getText());

        this.enterParameterDeclaration(ctx, enumerationState, ctx.identifier(), ctx.multiplicity());
    }

    @Override
    public void exitEnumerationParameterDeclaration(EnumerationParameterDeclarationContext ctx)
    {
        this.parameterState = null;
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
        int ordinal = this.parameterState.getNumModifiers();
        AntlrParameterModifier parameterModifierState = new AntlrParameterModifier(
                ctx,
                this.currentCompilationUnit,
                false,
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
                this.currentCompilationUnit,
                false);

        AntlrParameter parameterState = new AntlrParameter(
                ctx,
                this.currentCompilationUnit,
                false,
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
