package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.phase.criteria.CriteriaVisitor;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveType;
import cool.klass.model.converter.compiler.state.criteria.AntlrCriteria;
import cool.klass.model.converter.compiler.state.parameter.AntlrEnumerationParameter;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameterModifier;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameterOwner;
import cool.klass.model.converter.compiler.state.parameter.AntlrPrimitiveParameter;
import cool.klass.model.converter.compiler.state.property.AntlrParameterizedProperty;
import cool.klass.model.converter.compiler.state.service.AntlrCriteriaOwner;
import cool.klass.model.meta.domain.property.PrimitiveType;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
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
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.list.mutable.ListAdapter;

public class ParameterizedPropertyPhase extends AbstractCompilerPhase
{
    private final AntlrDomainModel           domainModelState;
    private       AntlrParameterizedProperty parameterizedPropertyState;
    // TODO: Make better use of these Owner interfaces in shared compiler phases
    private       AntlrCriteriaOwner         criteriaOwnerState;
    private       AntlrParameterOwner        parameterOwnerState;

    private AntlrClass thisReference;

    public ParameterizedPropertyPhase(
            @Nonnull CompilerErrorHolder compilerErrorHolder,
            @Nonnull MutableMap<ParserRuleContext, CompilationUnit> compilationUnitsByContext,
            boolean isInference,
            AntlrDomainModel domainModelState)
    {
        super(compilerErrorHolder, compilationUnitsByContext, isInference);
        this.domainModelState = domainModelState;
    }

    @Override
    public void enterClassDeclaration(ClassDeclarationContext ctx)
    {
        super.enterClassDeclaration(ctx);
        this.thisReference = this.domainModelState.getClassByContext(this.classDeclarationContext);
    }

    @Override
    public void exitClassDeclaration(ClassDeclarationContext ctx)
    {
        super.exitClassDeclaration(ctx);
        this.thisReference = null;
    }

    @Override
    public void enterParameterizedProperty(ParameterizedPropertyContext ctx)
    {
        IdentifierContext     identifier            = ctx.identifier();
        ClassTypeContext      classTypeContext      = ctx.classType();
        ClassReferenceContext classReferenceContext = classTypeContext.classReference();
        MultiplicityContext   multiplicityContext   = classTypeContext.multiplicity();

        String     parameterizedPropertyName = identifier.getText();
        AntlrClass antlrClass                = this.domainModelState.getClassByName(classReferenceContext.getText());
        AntlrMultiplicity multiplicityState = new AntlrMultiplicity(
                multiplicityContext,
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
    }

    // TODO: Should probably be moved to its own phase for consistency
    @Override
    public void enterRelationship(RelationshipContext ctx)
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

        visitor.visit(ctx.criteriaExpression());
    }

    @Override
    public void enterPrimitiveParameterDeclaration(PrimitiveParameterDeclarationContext ctx)
    {
        if (this.parameterizedPropertyState == null)
        {
            return;
        }

        IdentifierContext    identifier           = ctx.identifier();
        PrimitiveTypeContext primitiveTypeContext = ctx.primitiveType();
        MultiplicityContext  multiplicityContext  = ctx.multiplicity();

        PrimitiveType      primitiveType      = PrimitiveType.valueOf(primitiveTypeContext.getText());
        AntlrPrimitiveType primitiveTypeState = AntlrPrimitiveType.valueOf(primitiveType);

        AntlrMultiplicity multiplicityState = new AntlrMultiplicity(
                multiplicityContext,
                this.currentCompilationUnit,
                false);

        ImmutableList<AntlrParameterModifier> parameterModifiers = ListAdapter.adapt(ctx.parameterModifier())
                .collect(this::getAntlrParameterModifier)
                .toImmutable();

        AntlrPrimitiveParameter primitiveParameterState = new AntlrPrimitiveParameter(
                ctx,
                this.currentCompilationUnit,
                false,
                identifier,
                identifier.getText(),
                this.parameterOwnerState.getNumParameters() + 1,
                primitiveTypeState,
                multiplicityState,
                this.parameterOwnerState,
                parameterModifiers);

        this.parameterOwnerState.enterPrimitiveParameterDeclaration(primitiveParameterState);
    }

    @Override
    public void enterEnumerationParameterDeclaration(EnumerationParameterDeclarationContext ctx)
    {
        if (this.parameterizedPropertyState == null)
        {
            return;
        }

        IdentifierContext           identifier                  = ctx.identifier();
        EnumerationReferenceContext enumerationReferenceContext = ctx.enumerationReference();
        MultiplicityContext         multiplicityContext         = ctx.multiplicity();

        AntlrEnumeration enumerationState = this.domainModelState.getEnumerationByName(enumerationReferenceContext.getText());

        AntlrMultiplicity multiplicityState = new AntlrMultiplicity(
                multiplicityContext,
                this.currentCompilationUnit,
                false);

        ImmutableList<AntlrParameterModifier> parameterModifiers = ListAdapter.adapt(ctx.parameterModifier())
                .collect(this::getAntlrParameterModifier)
                .toImmutable();

        AntlrEnumerationParameter enumerationParameterState = new AntlrEnumerationParameter(
                ctx,
                this.currentCompilationUnit,
                false,
                identifier,
                identifier.getText(),
                this.parameterOwnerState.getNumParameters() + 1,
                enumerationState,
                multiplicityState,
                this.parameterOwnerState,
                parameterModifiers);

        this.parameterOwnerState.enterEnumerationParameterDeclaration(enumerationParameterState);
    }

    @Override
    public void exitEnumerationParameterDeclaration(EnumerationParameterDeclarationContext ctx)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".exitEnumerationParameterDeclaration() not implemented yet");
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

    @Nonnull
    public AntlrParameterModifier getAntlrParameterModifier(@Nonnull ParameterModifierContext context)
    {
        return new AntlrParameterModifier(context, this.currentCompilationUnit, false, context.getText());
    }
}
