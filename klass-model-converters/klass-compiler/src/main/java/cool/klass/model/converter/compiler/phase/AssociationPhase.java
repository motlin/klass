package cool.klass.model.converter.compiler.phase;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.AntlrAssociation;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrClassType;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEndModifier;
import cool.klass.model.converter.compiler.state.property.AntlrClassTypeOwner;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndModifierContext;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ClassTypeContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.MultiplicityContext;

public class AssociationPhase extends AbstractCompilerPhase
{
    @Nullable
    private AntlrAssociation    associationState;
    @Nullable
    private AntlrAssociationEnd associationEndState;
    @Nullable
    private AntlrClassType      classTypeState;

    @Nullable
    private AntlrClassTypeOwner classTypeOwnerState;

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
                identifier,
                identifier.getText(),
                this.compilerState.getOrdinal(ctx),
                this.compilerState.getCompilerWalkState().getPackageNameContext(),
                this.compilerState.getCompilerWalkState().getPackageName());
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
        if (this.classTypeOwnerState != null)
        {
            throw new IllegalStateException();
        }

        String associationEndName = ctx.identifier().getText();
        this.associationEndState = new AntlrAssociationEnd(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                ctx.identifier(),
                associationEndName,
                this.associationState.getNumAssociationEnds() + 1,
                this.associationState);
        this.classTypeOwnerState = this.associationEndState;

        this.associationState.enterAssociationEnd(this.associationEndState);
    }

    @Override
    public void exitAssociationEnd(@Nonnull AssociationEndContext ctx)
    {
        Objects.requireNonNull(this.associationEndState);
        this.associationEndState = null;
        this.classTypeOwnerState = null;

        super.exitAssociationEnd(ctx);
    }

    @Override
    public void enterAssociationEndModifier(@Nonnull AssociationEndModifierContext ctx)
    {
        super.enterAssociationEndModifier(ctx);

        Objects.requireNonNull(this.associationEndState);
        AntlrAssociationEndModifier antlrAssociationEndModifier = new AntlrAssociationEndModifier(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                ctx,
                ctx.getText(),
                this.associationEndState.getNumModifiers() + 1,
                this.associationEndState);
        this.associationEndState.enterModifier(antlrAssociationEndModifier);
    }

    @Override
    public void enterClassType(@Nonnull ClassTypeContext ctx)
    {
        super.enterClassType(ctx);

        if (this.classTypeState != null)
        {
            throw new AssertionError();
        }

        if (this.classTypeOwnerState == null)
        {
            return;
        }

        this.classTypeState = new AntlrClassType(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                this.classTypeOwnerState);
        this.classTypeOwnerState.enterClassType(this.classTypeState);
    }

    @Override
    public void exitClassType(@Nonnull ClassTypeContext ctx)
    {
        this.classTypeState = null;

        super.exitClassType(ctx);
    }

    @Override
    public void enterClassReference(@Nonnull ClassReferenceContext ctx)
    {
        super.enterClassReference(ctx);

        if (this.classTypeState == null)
        {
            return;
        }

        String           className        = ctx.identifier().getText();
        AntlrDomainModel domainModelState = this.compilerState.getDomainModelState();
        AntlrClass       classState       = domainModelState.getClassByName(className);

        this.classTypeState.enterClassReference(classState);
    }

    @Override
    public void enterMultiplicity(@Nonnull MultiplicityContext ctx)
    {
        super.enterMultiplicity(ctx);

        if (this.classTypeState == null)
        {
            return;
        }

        AntlrMultiplicity multiplicityState = new AntlrMultiplicity(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                this.classTypeState);

        this.classTypeState.enterMultiplicity(multiplicityState);
    }
}
