package cool.klass.model.converter.compiler.phase;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrAssociation;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.property.AntlrModifier;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndModifierContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;

public class AssociationPhase
        extends ReferencePropertyPhase
{
    @Nullable
    private AntlrAssociation    associationState;
    @Nullable
    private AntlrAssociationEnd associationEndState;

    public AssociationPhase(@Nonnull CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public void enterAssociationDeclaration(@Nonnull AssociationDeclarationContext ctx)
    {
        super.enterAssociationDeclaration(ctx);

        if (this.associationState != null)
        {
            throw new IllegalStateException();
        }

        IdentifierContext identifier = ctx.identifier();
        this.associationState = new AntlrAssociation(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                this.compilerState.getOrdinal(ctx),
                identifier,
                this.compilerState.getCompilerWalkState().getCompilationUnitState());
    }

    @Override
    public void exitAssociationDeclaration(@Nonnull AssociationDeclarationContext ctx)
    {
        this.associationState.exitAssociationDeclaration();
        this.compilerState.getDomainModelState().exitAssociationDeclaration(this.associationState);
        this.associationState = null;

        super.exitAssociationDeclaration(ctx);
    }

    @Override
    public void enterAssociationEnd(@Nonnull AssociationEndContext ctx)
    {
        super.enterAssociationEnd(ctx);

        if (this.associationEndState != null)
        {
            throw new IllegalStateException();
        }
        if (this.classReferenceOwnerState != null)
        {
            throw new IllegalStateException();
        }
        if (this.multiplicityOwnerState != null)
        {
            throw new IllegalStateException();
        }

        this.associationEndState      = new AntlrAssociationEnd(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                this.associationState.getNumAssociationEnds() + 1,
                ctx.identifier(),
                this.associationState);

        this.classReferenceOwnerState = this.associationEndState;
        this.multiplicityOwnerState   = this.associationEndState;
        this.associationState.enterAssociationEnd(this.associationEndState);

        this.handleClassReference(ctx.classReference());
        this.handleMultiplicity(ctx.multiplicity());
    }

    @Override
    public void exitAssociationEnd(@Nonnull AssociationEndContext ctx)
    {
        Objects.requireNonNull(this.associationEndState);
        this.associationEndState      = null;
        this.classReferenceOwnerState = null;
        this.multiplicityOwnerState   = null;
        super.exitAssociationEnd(ctx);
    }

    @Override
    public void enterAssociationEndModifier(@Nonnull AssociationEndModifierContext ctx)
    {
        super.enterAssociationEndModifier(ctx);

        Objects.requireNonNull(this.associationEndState);
        AntlrModifier antlrAssociationEndModifier = new AntlrModifier(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                this.associationEndState.getNumModifiers() + 1,
                this.associationEndState);
        this.associationEndState.enterModifier(antlrAssociationEndModifier);
    }
}
