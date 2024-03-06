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
import cool.klass.model.converter.compiler.state.order.AntlrOrderByOwner;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.property.AntlrParameterizedProperty;
import cool.klass.model.converter.compiler.state.service.AntlrService;
import cool.klass.model.converter.compiler.state.service.AntlrServiceGroup;
import cool.klass.model.converter.compiler.state.service.url.AntlrUrl;
import cool.klass.model.converter.compiler.state.value.AntlrThisMemberReferencePath;
import cool.klass.model.meta.grammar.KlassParser.AssociationDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.AssociationEndContext;
import cool.klass.model.meta.grammar.KlassParser.ClassDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.OrderByDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.OrderByMemberReferencePathContext;
import cool.klass.model.meta.grammar.KlassParser.ParameterizedPropertyContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ServiceGroupDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.ThisMemberReferencePathContext;
import cool.klass.model.meta.grammar.KlassParser.UrlDeclarationContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.map.MutableMap;

public class OrderByPhase extends AbstractCompilerPhase
{
    @Nonnull
    private final AntlrDomainModel domainModelState;

    @Nullable
    private AntlrAssociation           associationState;
    @Nullable
    private AntlrAssociationEnd        associationEndState;
    @Nullable
    private AntlrParameterizedProperty parameterizedPropertyState;
    @Nullable
    private AntlrServiceGroup          serviceGroupState;
    @Nullable
    private AntlrUrl                   urlState;

    @Nullable
    private AntlrClass classState;

    @Nullable
    private AntlrOrderByOwner orderByOwnerState;

    @Nullable
    private AntlrClass             thisContext;
    @Nonnull
    private Optional<AntlrOrderBy> orderByState = Optional.empty();
    @Nullable
    private AntlrService           serviceState;

    public OrderByPhase(
            @Nonnull CompilerErrorHolder compilerErrorHolder,
            @Nonnull MutableMap<ParserRuleContext, CompilationUnit> compilationUnitsByContext,
            @Nonnull AntlrDomainModel domainModelState,
            boolean isInference)
    {
        super(compilerErrorHolder, compilationUnitsByContext, isInference);
        this.domainModelState = Objects.requireNonNull(domainModelState);
    }

    @Override
    public void enterAssociationEnd(@Nonnull AssociationEndContext ctx)
    {
        this.associationEndState = this.associationState.getAssociationEndByContext(ctx);
        this.thisContext = this.associationEndState.getType();
        this.orderByOwnerState = this.associationEndState;
    }

    @Override
    public void exitAssociationEnd(AssociationEndContext ctx)
    {
        this.associationEndState = null;
        this.thisContext = null;
        this.orderByOwnerState = null;
    }

    @Override
    public void enterOrderByDeclaration(@Nonnull OrderByDeclarationContext ctx)
    {
        if (this.orderByOwnerState == null)
        {
            return;
        }

        this.orderByState = Optional.of(new AntlrOrderBy(
                ctx,
                this.currentCompilationUnit,
                false,
                this.thisContext,
                this.orderByOwnerState));
        this.orderByOwnerState.setOrderByState(this.orderByState);
    }

    @Override
    public void exitOrderByDeclaration(OrderByDeclarationContext ctx)
    {
        this.orderByState = null;
    }

    @Override
    public void enterOrderByMemberReferencePath(@Nonnull OrderByMemberReferencePathContext ctx)
    {
        if (this.orderByOwnerState == null)
        {
            return;
        }

        AntlrOrderByMemberReferencePath orderByMemberReferencePathState = this.convertOrderByMemberReferencePath(ctx);
        this.orderByState.get().enterOrderByMemberReferencePath(orderByMemberReferencePathState);
    }

    private AntlrOrderByMemberReferencePath convertOrderByMemberReferencePath(@Nonnull OrderByMemberReferencePathContext orderByMemberReferencePathContext)
    {
        AntlrThisMemberReferencePath thisMemberReferencePath = this.getAntlrThisMemberReferencePath(orderByMemberReferencePathContext);
        AntlrOrderByDirection orderByDirection = this.getAntlrOrderByDirection(orderByMemberReferencePathContext);

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
                this.domainModelState,
                this.orderByState.get());

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

    @Override
    public void enterClassDeclaration(ClassDeclarationContext ctx)
    {
        this.classState = this.domainModelState.getClassByContext(ctx);
    }

    @Override
    public void exitClassDeclaration(ClassDeclarationContext ctx)
    {
        this.classState = null;
    }

    @Override
    public void enterAssociationDeclaration(@Nonnull AssociationDeclarationContext ctx)
    {
        // TODO: ❗ Move this stuff up
        this.associationState = this.domainModelState.getAssociationByContext(ctx);
    }

    @Override
    public void exitAssociationDeclaration(AssociationDeclarationContext ctx)
    {
        this.associationState = null;
    }

    @Override
    public void enterServiceGroupDeclaration(ServiceGroupDeclarationContext ctx)
    {
        this.serviceGroupState = this.domainModelState.getServiceGroupByContext(ctx);
    }

    @Override
    public void exitServiceGroupDeclaration(ServiceGroupDeclarationContext ctx)
    {
        this.serviceGroupState = null;
    }

    @Override
    public void enterUrlDeclaration(UrlDeclarationContext ctx)
    {
        this.urlState = this.serviceGroupState.getUrlByContext(ctx);
    }

    @Override
    public void exitUrlDeclaration(UrlDeclarationContext ctx)
    {
        this.urlState = null;
    }

    @Override
    public void enterServiceDeclaration(ServiceDeclarationContext ctx)
    {
        this.serviceState = this.urlState.getServiceByContext(ctx);
    }

    @Override
    public void exitServiceDeclaration(ServiceDeclarationContext ctx)
    {
        this.serviceState = null;
    }

    @Override
    public void enterParameterizedProperty(ParameterizedPropertyContext ctx)
    {
        if (this.orderByOwnerState != null)
        {
            throw new IllegalStateException();
        }
        this.orderByOwnerState = this.parameterizedPropertyState;
        this.parameterizedPropertyState = this.classState.getParameterizedPropertyByContext(ctx);
    }

    @Override
    public void exitParameterizedProperty(ParameterizedPropertyContext ctx)
    {
        if (this.orderByOwnerState != null)
        {
            throw new IllegalStateException();
        }
        this.orderByOwnerState = null;
        this.parameterizedPropertyState = null;
    }
}
