package cool.klass.model.converter.compiler.phase;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.phase.criteria.CriteriaVisitor;
import cool.klass.model.converter.compiler.state.AntlrAssociation;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.criteria.AntlrCriteria;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEndModifier;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndModifierContext;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ClassTypeContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.MultiplicityContext;
import cool.klass.model.meta.grammar.KlassParser.RelationshipContext;
import cool.klass.model.meta.grammar.KlassVisitor;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.MutableMap;
import org.eclipse.collections.impl.list.mutable.ListAdapter;

public class AssociationPhase extends AbstractCompilerPhase
{
    @Nonnull
    private final AntlrDomainModel domainModelState;

    @Nullable
    private AntlrAssociation associationState;

    public AssociationPhase(
            @Nonnull CompilerErrorHolder compilerErrorHolder,
            @Nonnull MutableMap<ParserRuleContext, CompilationUnit> compilationUnitsByContext,
            @Nonnull AntlrDomainModel domainModelState,
            boolean isInference)
    {
        super(compilerErrorHolder, compilationUnitsByContext, isInference);
        this.domainModelState = Objects.requireNonNull(domainModelState);
    }

    @Override
    public void enterAssociationDeclaration(@Nonnull AssociationDeclarationContext ctx)
    {
        IdentifierContext identifier = ctx.identifier();
        this.associationState = new AntlrAssociation(
                ctx,
                this.currentCompilationUnit,
                this.isInference,
                identifier,
                identifier.getText(),
                this.domainModelState.getNumTopLevelElements() + 1,
                this.packageName);
    }

    @Override
    public void exitAssociationDeclaration(AssociationDeclarationContext ctx)
    {
        this.associationState.exitAssociationDeclaration();
        this.domainModelState.exitAssociationDeclaration(this.associationState);
        this.associationState = null;
    }

    @Override
    public void enterAssociationEnd(@Nonnull AssociationEndContext ctx)
    {
        IdentifierContext     identifier            = ctx.identifier();
        ClassTypeContext      classTypeContext      = ctx.classType();
        ClassReferenceContext classReferenceContext = classTypeContext.classReference();
        MultiplicityContext   multiplicityContext   = classTypeContext.multiplicity();

        String     associationEndName = identifier.getText();
        AntlrClass antlrClass         = this.domainModelState.getClassByName(classReferenceContext.getText());
        AntlrMultiplicity multiplicityState = new AntlrMultiplicity(
                multiplicityContext,
                this.currentCompilationUnit,
                this.isInference);

        ImmutableList<AntlrAssociationEndModifier> associationEndModifiers = ListAdapter.adapt(ctx.associationEndModifier())
                .collectWithIndex(this::getAntlrAssociationEndModifier)
                .toImmutable();

        AntlrAssociationEnd antlrAssociationEnd = new AntlrAssociationEnd(
                ctx,
                this.currentCompilationUnit,
                this.isInference,
                ctx.identifier(),
                associationEndName,
                this.associationState.getNumAssociationEnds() + 1,
                this.associationState,
                antlrClass,
                multiplicityState,
                associationEndModifiers);

        this.associationState.enterAssociationEnd(antlrAssociationEnd);
    }

    // TODO: This is too early to resolve relationships. It won't be reliable until we're done inferring associations and we've also compiled parameterized properties.
    @Override
    public void enterRelationship(@Nonnull RelationshipContext ctx)
    {
        AntlrClass thisReference = this.associationState.getAssociationEndStates()
                .getFirstOptional()
                .map(AntlrAssociationEnd::getType)
                .orElse(AntlrClass.NOT_FOUND);

        KlassVisitor<AntlrCriteria> visitor = new CriteriaVisitor(
                this.currentCompilationUnit,
                this.domainModelState,
                this.associationState,
                thisReference);
        visitor.visit(ctx.criteriaExpression());
    }

    @Nonnull
    public AntlrAssociationEndModifier getAntlrAssociationEndModifier(
            @Nonnull AssociationEndModifierContext context,
            int ordinal)
    {
        return new AntlrAssociationEndModifier(
                context,
                this.currentCompilationUnit,
                this.isInference,
                context,
                context.getText(),
                ordinal + 1);
    }
}
