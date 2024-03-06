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
            @Nonnull AntlrDomainModel domainModelState)
    {
        super(compilerErrorHolder, compilationUnitsByContext);
        this.domainModelState = Objects.requireNonNull(domainModelState);
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
                ctx.identifier(),
                associationEndName,
                this.associationState,
                antlrClass,
                antlrMultiplicity,
                associationEndModifiers);

        this.associationState.enterAssociationEnd(antlrAssociationEnd);
    }

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
}
