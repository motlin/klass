package cool.klass.model.converter.compiler.phase;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.phase.criteria.ExpressionValueVisitor;
import cool.klass.model.converter.compiler.state.order.AntlrOrderBy;
import cool.klass.model.converter.compiler.state.order.AntlrOrderByDirection;
import cool.klass.model.converter.compiler.state.order.AntlrOrderByMemberReferencePath;
import cool.klass.model.converter.compiler.state.order.AntlrOrderByOwner;
import cool.klass.model.converter.compiler.state.value.AntlrThisMemberReferencePath;
import cool.klass.model.meta.grammar.KlassParser.OrderByDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.OrderByMemberReferencePathContext;
import cool.klass.model.meta.grammar.KlassParser.ThisMemberReferencePathContext;

public class OrderByPhase extends AbstractCompilerPhase
{
    @Nonnull
    private Optional<AntlrOrderBy> orderByState = Optional.empty();

    public OrderByPhase(CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    public void enterOrderByDeclaration(@Nonnull OrderByDeclarationContext ctx)
    {
        super.enterOrderByDeclaration(ctx);

        if (this.compilerState.getCompilerWalkState().getOrderByOwnerState() == null)
        {
            return;
        }

        this.orderByState = Optional.of(new AntlrOrderBy(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                this.compilerState.getCompilerWalkState().getThisReference(),
                this.compilerState.getCompilerWalkState().getOrderByOwnerState()));
        AntlrOrderByOwner orderByOwnerState = this.compilerState.getCompilerWalkState().getOrderByOwnerState();
        orderByOwnerState.enterOrderByDeclaration(this.orderByState);
    }

    @Override
    public void exitOrderByDeclaration(OrderByDeclarationContext ctx)
    {
        this.orderByState = null;
        super.exitOrderByDeclaration(ctx);
    }

    @Override
    public void enterOrderByMemberReferencePath(@Nonnull OrderByMemberReferencePathContext ctx)
    {
        super.enterOrderByMemberReferencePath(ctx);
        if (this.compilerState.getCompilerWalkState().getOrderByOwnerState() == null)
        {
            return;
        }

        AntlrOrderByMemberReferencePath orderByMemberReferencePathState = this.convertOrderByMemberReferencePath(ctx);
        this.orderByState.get().enterOrderByMemberReferencePath(orderByMemberReferencePathState);
    }

    @Nonnull
    private AntlrOrderByMemberReferencePath convertOrderByMemberReferencePath(@Nonnull OrderByMemberReferencePathContext orderByMemberReferencePathContext)
    {
        AntlrThisMemberReferencePath thisMemberReferencePath = this.getAntlrThisMemberReferencePath(
                orderByMemberReferencePathContext);
        AntlrOrderByDirection orderByDirection = this.getAntlrOrderByDirection(
                orderByMemberReferencePathContext);

        return new AntlrOrderByMemberReferencePath(
                orderByMemberReferencePathContext,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                this.orderByState.get(),
                this.orderByState.get().getNumProperties(),
                thisMemberReferencePath,
                orderByDirection);
    }

    @Nonnull
    private AntlrThisMemberReferencePath getAntlrThisMemberReferencePath(@Nonnull OrderByMemberReferencePathContext orderByMemberReferencePathContext)
    {
        ExpressionValueVisitor expressionValueVisitor = new ExpressionValueVisitor(
                this.compilerState,
                this.compilerState.getCompilerWalkState().getThisReference(),
                this.orderByState.get());

        ThisMemberReferencePathContext thisMemberReferencePathContext = orderByMemberReferencePathContext.thisMemberReferencePath();

        return expressionValueVisitor.visitThisMemberReferencePath(thisMemberReferencePathContext);
    }

    @Nonnull
    private AntlrOrderByDirection getAntlrOrderByDirection(@Nonnull OrderByMemberReferencePathContext orderByMemberReferencePathContext)
    {
        return new AntlrOrderByDirection(
                orderByMemberReferencePathContext.orderByDirection(),
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()));
    }
}
