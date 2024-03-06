package cool.klass.model.converter.compiler.phase;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.phase.criteria.ExpressionValueVisitor;
import cool.klass.model.converter.compiler.state.AntlrAssociation;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrDomainModel;
import cool.klass.model.converter.compiler.state.order.AntlrOrderBy;
import cool.klass.model.converter.compiler.state.order.AntlrOrderByDirection;
import cool.klass.model.converter.compiler.state.order.AntlrOrderByMemberReferencePath;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.value.AntlrThisMemberReferencePath;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.OrderByDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.OrderByMemberReferencePathContext;
import cool.klass.model.meta.grammar.KlassParser.ThisMemberReferencePathContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.map.MutableMap;

public class AssociationOrderByPhase extends AbstractCompilerPhase
{
    @Nonnull
    private final AntlrDomainModel domainModelState;

    @Nullable
    private AntlrAssociation       associationState;
    private AntlrAssociationEnd    associationEndState;
    private AntlrClass             thisContext;
    private Optional<AntlrOrderBy> orderByState = Optional.empty();

    public AssociationOrderByPhase(
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
        this.associationState = this.domainModelState.getAssociationByContext(ctx);
    }

    @Override
    public void exitAssociationDeclaration(AssociationDeclarationContext ctx)
    {
        this.associationState = null;
    }

    @Override
    public void enterAssociationEnd(@Nonnull AssociationEndContext ctx)
    {
        this.associationEndState = this.associationState.getAssociationEndByContext(ctx);
        this.thisContext = this.associationEndState.getType();
    }

    @Override
    public void exitAssociationEnd(AssociationEndContext ctx)
    {
        this.associationEndState = null;
        this.thisContext = null;
    }

    @Override
    public void enterOrderByDeclaration(OrderByDeclarationContext ctx)
    {
        if (this.associationEndState == null)
        {
            return;
        }

        this.orderByState = Optional.of(new AntlrOrderBy(
                ctx,
                this.currentCompilationUnit,
                false,
                this.thisContext,
                this.associationEndState));
        this.associationEndState.setOrderByState(this.orderByState);
    }

    @Override
    public void exitOrderByDeclaration(OrderByDeclarationContext ctx)
    {
        this.orderByState = null;
    }

    @Override
    public void enterOrderByMemberReferencePath(OrderByMemberReferencePathContext ctx)
    {
        if (this.associationEndState == null)
        {
            return;
        }

        AntlrOrderByMemberReferencePath orderByMemberReferencePathState = this.convertOrderByMemberReferencePath(ctx);
        this.orderByState.get().enterOrderByMemberReferencePath(orderByMemberReferencePathState);
    }

    private AntlrOrderByMemberReferencePath convertOrderByMemberReferencePath(OrderByMemberReferencePathContext orderByMemberReferencePathContext)
    {
        AntlrThisMemberReferencePath thisMemberReferencePath = this.getAntlrThisMemberReferencePath(
                orderByMemberReferencePathContext);
        AntlrOrderByDirection        orderByDirection        = this.getAntlrOrderByDirection(
                orderByMemberReferencePathContext);

        return new AntlrOrderByMemberReferencePath(
                orderByMemberReferencePathContext,
                this.currentCompilationUnit,
                false,
                this.orderByState.get(),
                this.orderByState.get().getNumProperties(),
                thisMemberReferencePath,
                orderByDirection);
    }

    @Nonnull
    private AntlrThisMemberReferencePath getAntlrThisMemberReferencePath(OrderByMemberReferencePathContext orderByMemberReferencePathContext)
    {
        ExpressionValueVisitor expressionValueVisitor = new ExpressionValueVisitor(
                this.currentCompilationUnit,
                this.thisContext,
                this.domainModelState);

        ThisMemberReferencePathContext thisMemberReferencePathContext = orderByMemberReferencePathContext.thisMemberReferencePath();

        return expressionValueVisitor.visitThisMemberReferencePath(thisMemberReferencePathContext);
    }

    @Nonnull
    private AntlrOrderByDirection getAntlrOrderByDirection(OrderByMemberReferencePathContext orderByMemberReferencePathContext)
    {
        return new AntlrOrderByDirection(
                orderByMemberReferencePathContext.orderByDirection(),
                this.currentCompilationUnit,
                false);
    }
}
