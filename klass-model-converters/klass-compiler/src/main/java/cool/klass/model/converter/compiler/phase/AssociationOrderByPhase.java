package cool.klass.model.converter.compiler.phase;

import java.util.Objects;

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
import cool.klass.model.converter.compiler.state.order.AntlrOrderByProperty;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.value.AntlrThisMemberValue;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.OrderByDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.OrderByPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.ThisMemberReferenceContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.map.MutableMap;

public class AssociationOrderByPhase extends AbstractCompilerPhase
{
    @Nonnull
    private final AntlrDomainModel domainModelState;

    @Nullable
    private AntlrAssociation    associationState;
    private AntlrAssociationEnd associationEndState;
    private AntlrClass          thisContext;
    private AntlrOrderBy        orderByState;

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

        this.orderByState = new AntlrOrderBy(
                ctx,
                this.currentCompilationUnit,
                false,
                this.thisContext);
    }

    @Override
    public void exitOrderByDeclaration(OrderByDeclarationContext ctx)
    {
        this.orderByState = null;
    }

    @Override
    public void enterOrderByProperty(OrderByPropertyContext ctx)
    {
        if (this.associationEndState == null)
        {
            return;
        }

        AntlrOrderByProperty orderByPropertyState = this.convertOrderByProperty(ctx);
        this.orderByState.enterOrderByProperty(orderByPropertyState);
    }

    private AntlrOrderByProperty convertOrderByProperty(OrderByPropertyContext orderByPropertyContext)
    {
        AntlrThisMemberValue  thisMemberValue  = this.getAntlrThisMemberValue(orderByPropertyContext);
        AntlrOrderByDirection orderByDirection = this.getAntlrOrderByDirection(orderByPropertyContext);

        return new AntlrOrderByProperty(
                orderByPropertyContext,
                this.currentCompilationUnit,
                false,
                this.orderByState,
                this.orderByState.getNumProperties(),
                thisMemberValue,
                orderByDirection);
    }

    @Nonnull
    private AntlrThisMemberValue getAntlrThisMemberValue(OrderByPropertyContext orderByPropertyContext)
    {
        ExpressionValueVisitor expressionValueVisitor = new ExpressionValueVisitor(
                this.currentCompilationUnit,
                this.thisContext,
                this.domainModelState);

        ThisMemberReferenceContext thisMemberReferenceContext = orderByPropertyContext.thisMemberReference();

        return expressionValueVisitor.visitThisMemberReference(thisMemberReferenceContext);
    }

    @Nonnull
    private AntlrOrderByDirection getAntlrOrderByDirection(OrderByPropertyContext orderByPropertyContext)
    {
        return new AntlrOrderByDirection(
                orderByPropertyContext.orderByDirection(),
                this.currentCompilationUnit,
                false);
    }
}
