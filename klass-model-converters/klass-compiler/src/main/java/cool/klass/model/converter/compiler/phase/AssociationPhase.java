package cool.klass.model.converter.compiler.phase;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

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
    @Nullable
    private AntlrAssociation    associationState;
    @Nullable
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
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
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
        AntlrClass       classState       = this.compilerState.getDomainModelState().getClassByName(className);
        AntlrMultiplicity multiplicityState = new AntlrMultiplicity(
                classTypeContext.multiplicity(),
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()));

        String associationEndName = ctx.identifier().getText();
        this.associationEndState = new AntlrAssociationEnd(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                ctx.identifier(),
                associationEndName,
                this.associationState.getNumAssociationEnds() + 1,
                this.associationState,
                classState,
                multiplicityState);

        this.associationState.enterAssociationEnd(this.associationEndState);
    }

    @Override
    public void enterAssociationEndModifier(@Nonnull AssociationEndModifierContext ctx)
    {
        super.enterAssociationEndModifier(ctx);
        AntlrAssociationEndModifier antlrAssociationEndModifier = new AntlrAssociationEndModifier(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                ctx,
                ctx.getText(),
                this.associationEndState.getNumModifiers() + 1,
                this.associationEndState);
        this.associationEndState.enterAssociationEndModifier(antlrAssociationEndModifier);
    }
}
