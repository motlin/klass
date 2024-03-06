package cool.klass.model.converter.compiler.phase;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.phase.criteria.ExpressionValueVisitor;
import cool.klass.model.converter.compiler.state.order.AntlrOrderBy;
import cool.klass.model.converter.compiler.state.order.AntlrOrderByMemberReferencePath;
import cool.klass.model.converter.compiler.state.order.AntlrOrderByOwner;
import cool.klass.model.converter.compiler.state.value.AntlrThisMemberReferencePath;
import cool.klass.model.meta.grammar.KlassParser.OrderByDeclarationContext;
import cool.klass.model.meta.grammar.KlassParser.OrderByMemberReferencePathContext;
import cool.klass.model.meta.grammar.KlassParser.ThisMemberReferencePathContext;

public class OrderByPhase extends AbstractCompilerPhase
{
    @Nullable
    private AntlrOrderBy orderByState;

    public OrderByPhase(@Nonnull CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void enterOrderByDeclaration(@Nonnull OrderByDeclarationContext ctx)
    {
        super.enterOrderByDeclaration(ctx);

        if (this.compilerState.getCompilerWalkState().getOrderByOwnerState() == null)
        {
            return;
        }

        this.orderByState = new AntlrOrderBy(
                ctx,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                this.compilerState.getCompilerWalkState().getThisReference(),
                this.compilerState.getCompilerWalkState().getOrderByOwnerState());
        AntlrOrderByOwner orderByOwnerState = this.compilerState.getCompilerWalkState().getOrderByOwnerState();
        orderByOwnerState.enterOrderByDeclaration(this.orderByState);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void exitOrderByDeclaration(@Nonnull OrderByDeclarationContext ctx)
    {
        this.orderByState = null;
        super.exitOrderByDeclaration(ctx);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void enterOrderByMemberReferencePath(@Nonnull OrderByMemberReferencePathContext ctx)
    {
        super.enterOrderByMemberReferencePath(ctx);
        if (this.compilerState.getCompilerWalkState().getOrderByOwnerState() == null)
        {
            return;
        }

        AntlrOrderByMemberReferencePath orderByMemberReferencePathState = this.convertOrderByMemberReferencePath(ctx);
        this.orderByState.enterOrderByMemberReferencePath(orderByMemberReferencePathState);
    }

    @Nonnull
    private AntlrOrderByMemberReferencePath convertOrderByMemberReferencePath(@Nonnull OrderByMemberReferencePathContext orderByMemberReferencePathContext)
    {
        AntlrThisMemberReferencePath thisMemberReferencePath = this.getAntlrThisMemberReferencePath(
                orderByMemberReferencePathContext);

        return new AntlrOrderByMemberReferencePath(
                orderByMemberReferencePathContext,
                Optional.of(this.compilerState.getCompilerWalkState().getCurrentCompilationUnit()),
                this.orderByState,
                this.orderByState.getNumProperties(),
                thisMemberReferencePath);
    }

    @Nonnull
    private AntlrThisMemberReferencePath getAntlrThisMemberReferencePath(@Nonnull OrderByMemberReferencePathContext orderByMemberReferencePathContext)
    {
        ExpressionValueVisitor expressionValueVisitor = new ExpressionValueVisitor(
                this.compilerState,
                this.compilerState.getCompilerWalkState().getThisReference(),
                this.orderByState);

        ThisMemberReferencePathContext thisMemberReferencePathContext =
                orderByMemberReferencePathContext.thisMemberReferencePath();

        return expressionValueVisitor.visitThisMemberReferencePath(thisMemberReferencePathContext);
    }
}
