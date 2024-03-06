package cool.klass.model.converter.compiler.phase;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrAssociation;
import cool.klass.model.converter.compiler.state.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.AntlrAssociationEndModifier;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ClassTypeContext;
import cool.klass.model.meta.grammar.KlassParser.CompilationUnitContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.MultiplicityContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.MapIterable;
import org.eclipse.collections.impl.list.mutable.ListAdapter;

public class AssociationPhase extends AbstractCompilerPhase
{
    private final AntlrDomainModel domainModelState;

    private AntlrAssociation associationState;

    public AssociationPhase(
            CompilerErrorHolder compilerErrorHolder,
            MapIterable<CompilationUnitContext, CompilationUnit> compilationUnitsByContext,
            AntlrDomainModel domainModelState)
    {
        super(compilerErrorHolder, compilationUnitsByContext);
        this.domainModelState = domainModelState;
    }

    @Override
    public void enterAssociationDeclaration(AssociationDeclarationContext ctx)
    {
        IdentifierContext identifier = ctx.identifier();
        this.associationState = new AntlrAssociation(
                ctx,
                this.currentCompilationUnit,
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
    public void enterAssociationEnd(AssociationEndContext ctx)
    {
        IdentifierContext     identifier            = ctx.identifier();
        ClassTypeContext      classTypeContext      = ctx.classType();
        ClassReferenceContext classReferenceContext = classTypeContext.classReference();
        MultiplicityContext   multiplicityContext   = classTypeContext.multiplicity();

        String            associationEndName = identifier.getText();
        AntlrClass        antlrClass         = this.domainModelState.getClassByName(classReferenceContext.getText());
        AntlrMultiplicity antlrMultiplicity  = new AntlrMultiplicity(multiplicityContext);

        ImmutableList<AntlrAssociationEndModifier> associationEndModifiers = ListAdapter.adapt(ctx.associationEndModifier())
                .collect(AntlrAssociationEndModifier::new)
                .toImmutable();

        AntlrAssociationEnd antlrAssociationEnd = new AntlrAssociationEnd(
                ctx,
                this.currentCompilationUnit,
                associationEndName,
                ctx.identifier(),
                antlrClass,
                antlrMultiplicity,
                associationEndModifiers);

        this.associationState.enterAssociationEnd(antlrAssociationEnd);
    }
}
