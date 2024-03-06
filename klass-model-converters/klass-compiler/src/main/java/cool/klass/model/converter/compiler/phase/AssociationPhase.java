package cool.klass.model.converter.compiler.phase;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrAssociation;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEndModifier;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndModifierContext;
import cool.klass.model.meta.grammar.KlassParser.ClassTypeContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;

public class AssociationPhase extends AbstractCompilerPhase
{
    private AntlrAssociation    associationState;
    private AntlrAssociationEnd associationEndState;

    public AssociationPhase(CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public void enterAssociationDeclaration(@Nonnull AssociationDeclarationContext ctx)
    {
        super.enterAssociationDeclaration(ctx);

        IdentifierContext identifier = ctx.identifier();
        this.associationState = new AntlrAssociation(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference(),
                identifier,
                identifier.getText(),
                this.compilerState.getDomainModelState().getNumTopLevelElements() + 1,
                this.compilerState.getAntlrWalkState().getPackageContext(),
                this.compilerState.getCompilerWalkState().getPackageName());
    }

    @Override
    public void exitAssociationDeclaration(AssociationDeclarationContext ctx)
    {
        this.associationState.exitAssociationDeclaration();
        this.compilerState.getDomainModelState().exitAssociationDeclaration(this.associationState);

        super.exitAssociationDeclaration(ctx);
    }

    @Override
    public void enterAssociationEnd(@Nonnull AssociationEndContext ctx)
    {
        super.enterAssociationEnd(ctx);

        ClassTypeContext classTypeContext = ctx.classType();
        String           className        = classTypeContext.classReference().getText();
        AntlrClass       antlrClass       = this.compilerState.getDomainModelState().getClassByName(className);
        AntlrMultiplicity multiplicityState = new AntlrMultiplicity(
                classTypeContext.multiplicity(),
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference());

        String associationEndName = ctx.identifier().getText();
        this.associationEndState = new AntlrAssociationEnd(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference(),
                ctx.identifier(),
                associationEndName,
                this.associationState.getNumAssociationEnds() + 1,
                this.associationState,
                antlrClass,
                multiplicityState);

        this.associationState.enterAssociationEnd(this.associationEndState);
    }

    @Override
    public void enterAssociationEndModifier(@Nonnull AssociationEndModifierContext ctx)
    {
        super.enterAssociationEndModifier(ctx);
        AntlrAssociationEndModifier antlrAssociationEndModifier = new AntlrAssociationEndModifier(
                ctx,
                this.compilerState.getCompilerWalkState().getCurrentCompilationUnit(),
                this.compilerState.getCompilerInputState().isInference(),
                ctx,
                ctx.getText(),
                this.associationEndState.getNumModifiers() + 1,
                this.associationEndState);
        this.associationEndState.enterAssociationEndModifier(antlrAssociationEndModifier);
    }
}
