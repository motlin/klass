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
    private AntlrAssociation    association;
    @Nullable
    private AntlrAssociationEnd associationEnd;

    public AssociationPhase(@Nonnull CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public void enterAssociationDeclaration(@Nonnull AssociationDeclarationContext ctx)
    {
        super.enterAssociationDeclaration(ctx);

        if (this.association != null)
        {
            throw new IllegalStateException();
        }

        IdentifierContext identifier = ctx.identifier();
        this.association = new AntlrAssociation(
                ctx,
                Optional.of(this.compilerState.getCompilerWalk().getCurrentCompilationUnit()),
                this.compilerState.getOrdinal(ctx),
                identifier,
                this.compilerState.getCompilerWalk().getCompilationUnit());
    }

    @Override
    public void exitAssociationDeclaration(@Nonnull AssociationDeclarationContext ctx)
    {
        this.association.exitAssociationDeclaration();
        this.compilerState.getDomainModel().exitAssociationDeclaration(this.association);
        this.association = null;

        super.exitAssociationDeclaration(ctx);
    }

    @Override
    public void enterAssociationEnd(@Nonnull AssociationEndContext ctx)
    {
        super.enterAssociationEnd(ctx);

        if (this.associationEnd != null)
        {
            throw new IllegalStateException();
        }
        if (this.classReferenceOwner != null)
        {
            throw new IllegalStateException();
        }
        if (this.multiplicityOwner != null)
        {
            throw new IllegalStateException();
        }

        this.associationEnd = new AntlrAssociationEnd(
                ctx,
                Optional.of(this.compilerState.getCompilerWalk().getCurrentCompilationUnit()),
                this.association.getNumAssociationEnds() + 1,
                ctx.identifier(),
                this.association);

        this.classReferenceOwner = this.associationEnd;
        this.multiplicityOwner   = this.associationEnd;
        this.association.enterAssociationEnd(this.associationEnd);

        this.handleClassReference(ctx.classReference());
        this.handleMultiplicity(ctx.multiplicity());
    }

    @Override
    public void exitAssociationEnd(@Nonnull AssociationEndContext ctx)
    {
        Objects.requireNonNull(this.associationEnd);
        this.associationEnd      = null;
        this.classReferenceOwner = null;
        this.multiplicityOwner   = null;
        super.exitAssociationEnd(ctx);
    }

    @Override
    public void enterAssociationEndModifier(@Nonnull AssociationEndModifierContext ctx)
    {
        super.enterAssociationEndModifier(ctx);

        Objects.requireNonNull(this.associationEnd);
        AntlrModifier antlrAssociationEndModifier = new AntlrModifier(
                ctx,
                Optional.of(this.compilerState.getCompilerWalk().getCurrentCompilationUnit()),
                this.associationEnd.getNumModifiers() + 1,
                this.associationEnd);
        this.associationEnd.enterModifier(antlrAssociationEndModifier);
    }
}
