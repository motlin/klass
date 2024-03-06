package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.phase.criteria.CriteriaVisitor;
import cool.klass.model.converter.compiler.state.AntlrAssociation;
import cool.klass.model.converter.compiler.state.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.AntlrAssociationEndModifier;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.criteria.AntlrCriteria;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ClassTypeContext;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.MultiplicityContext;
import cool.klass.model.meta.grammar.KlassParser.RelationshipContext;
import cool.klass.model.meta.grammar.KlassVisitor;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.MapIterable;
import org.eclipse.collections.impl.list.mutable.ListAdapter;

public class AssociationPhase extends AbstractCompilerPhase
{
    private final AntlrDomainModel domainModelState;

    @Nullable
    private AntlrAssociation associationState;

    public AssociationPhase(
            @Nonnull CompilerErrorHolder compilerErrorHolder,
            @Nonnull MapIterable<CompilationUnitContext, CompilationUnit> compilationUnitsByContext,
            AntlrDomainModel domainModelState)
    {
        super(compilerErrorHolder, compilationUnitsByContext);
        this.domainModelState = domainModelState;
    }

    @Override
    public void enterAssociationDeclaration(@Nonnull AssociationDeclarationContext ctx)
    {
        IdentifierContext identifier = ctx.identifier();
        this.associationState = new AntlrAssociation(
                ctx,
                this.currentCompilationUnit,
                false,
                identifier,
                identifier.getText(),
                this.packageName);
        this.domainModelState.enterAssociationDeclaration(this.associationState);
    }

    @Override
    public void exitAssociationDeclaration(AssociationDeclarationContext ctx)
    {
        this.associationState = null;
    }

    @Override
    public void enterAssociationEnd(@Nonnull AssociationEndContext ctx)
    {
        IdentifierContext     identifier            = ctx.identifier();
        ClassTypeContext      classTypeContext      = ctx.classType();
        ClassReferenceContext classReferenceContext = classTypeContext.classReference();
        MultiplicityContext   multiplicityContext   = classTypeContext.multiplicity();

        String            associationEndName = identifier.getText();
        AntlrClass        antlrClass         = this.domainModelState.getClassByName(classReferenceContext.getText());
        AntlrMultiplicity antlrMultiplicity = new AntlrMultiplicity(
                multiplicityContext,
                this.currentCompilationUnit,
                false);

        ImmutableList<AntlrAssociationEndModifier> associationEndModifiers = ListAdapter.adapt(ctx.associationEndModifier())
                .collect(AntlrAssociationEndModifier::new)
                .toImmutable();

        AntlrAssociationEnd antlrAssociationEnd = new AntlrAssociationEnd(
                ctx,
                this.currentCompilationUnit,
                false,
                associationEndName,
                ctx.identifier(),
                antlrClass,
                antlrMultiplicity,
                associationEndModifiers);

        this.associationState.enterAssociationEnd(antlrAssociationEnd);
    }

    @Override
    public void enterRelationship(@Nonnull RelationshipContext ctx)
    {
        KlassVisitor<AntlrCriteria> visitor = new CriteriaVisitor(
                this.currentCompilationUnit,
                this.associationState,
                this.domainModelState);
        AntlrCriteria antlrCriteria = visitor.visit(ctx.criteriaExpression());
        this.associationState.enterRelationship(antlrCriteria);
    }
}
