package cool.klass.model.converter.compiler.phase;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrAssociation;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.order.AntlrOrderByOwner;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.RelationshipContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.map.MutableMap;

public abstract class AbstractDomainModelCompilerPhase extends AbstractCompilerPhase
{
    protected final AntlrDomainModel domainModelState;

    @Nullable
    protected AntlrClass          classState;
    @Nullable
    protected AntlrAssociation    associationState;
    @Nullable
    protected AntlrAssociationEnd associationEndState;

    @Nullable
    protected AntlrClass        thisReference;
    @Nullable
    protected AntlrOrderByOwner orderByOwnerState;

    // TODO: Consider abstracting these four pieces of information into a type called CompilationState
    protected AbstractDomainModelCompilerPhase(
            @Nonnull CompilerErrorHolder compilerErrorHolder,
            @Nonnull MutableMap<ParserRuleContext, CompilationUnit> compilationUnitsByContext,
            boolean isInference,
            AntlrDomainModel domainModelState)
    {
        super(compilerErrorHolder, compilationUnitsByContext, isInference);
        this.domainModelState = Objects.requireNonNull(domainModelState);
    }

    @Override
    public void enterClassDeclaration(ClassDeclarationContext ctx)
    {
        AbstractDomainModelCompilerPhase.assertNull(this.classDeclarationContext);
        AbstractDomainModelCompilerPhase.assertNull(this.classState);
        AbstractDomainModelCompilerPhase.assertNull(this.thisReference);

        AntlrClass classByContext = this.domainModelState.getClassByContext(ctx);

        this.classDeclarationContext = ctx;
        this.classState = classByContext;
        this.thisReference = classByContext;
    }

    @Override
    public void exitClassDeclaration(ClassDeclarationContext ctx)
    {
        this.classDeclarationContext = null;
        this.classState = null;
        this.thisReference = null;
    }

    @Override
    public void enterAssociationDeclaration(@Nonnull AssociationDeclarationContext ctx)
    {
        AbstractDomainModelCompilerPhase.assertNull(this.associationDeclarationContext);
        AbstractDomainModelCompilerPhase.assertNull(this.associationState);

        this.associationDeclarationContext = ctx;
        this.associationState = this.domainModelState.getAssociationByContext(ctx);
    }

    @Override
    public void exitAssociationDeclaration(AssociationDeclarationContext ctx)
    {
        this.associationDeclarationContext = null;
        this.associationState = null;
    }

    @Override
    public void enterAssociationEnd(@Nonnull AssociationEndContext ctx)
    {
        if (this.associationState == null)
        {
            return;
        }

        AbstractDomainModelCompilerPhase.assertNull(this.associationEndState);
        AbstractDomainModelCompilerPhase.assertNull(this.thisReference);
        AbstractDomainModelCompilerPhase.assertNull(this.orderByOwnerState);

        super.enterAssociationEnd(ctx);
        this.associationEndState = this.associationState.getAssociationEndByContext(ctx);
        if (this.associationEndState == null)
        {
            return;
        }
        this.orderByOwnerState = this.associationEndState;
        this.thisReference = this.associationEndState.getType();
    }

    @Override
    public void exitAssociationEnd(AssociationEndContext ctx)
    {
        super.exitAssociationEnd(ctx);
        this.associationEndState = null;
        this.thisReference = null;
        this.orderByOwnerState = null;
    }

    @Override
    public void exitRelationship(RelationshipContext ctx)
    {
        this.thisReference = null;
    }

    private static void assertNull(Object object)
    {
        if (object != null)
        {
            throw new IllegalStateException();
        }
    }
}
