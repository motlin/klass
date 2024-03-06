package cool.klass.model.converter.compiler.phase;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.OverridingMethodsMustInvokeSuper;

import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.state.order.AntlrOrderByDirection;
import cool.klass.model.converter.compiler.state.order.AntlrOrderByMemberReferencePath;
import cool.klass.model.meta.domain.api.order.OrderByDirection;
import cool.klass.model.meta.grammar.KlassParser.OrderByDirectionContext;

public class OrderByDirectionPhase
        extends AbstractCompilerPhase
{
    public OrderByDirectionPhase(@Nonnull CompilerState compilerState)
    {
        super(compilerState);
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void enterOrderByDirection(@Nonnull OrderByDirectionContext ctx)
    {
        super.enterOrderByDirection(ctx);

        AntlrOrderByDirection orderByDirection = new AntlrOrderByDirection(
                ctx,
                Optional.of(this.compilerState.getCompilerWalk().getCurrentCompilationUnit()),
                OrderByDirectionPhase.getOrderByDirection(ctx));

        AntlrOrderByMemberReferencePath orderByMemberReferencePath =
                this.compilerState.getCompilerWalk().getOrderByMemberReferencePath();
        orderByMemberReferencePath.enterOrderByDirection(orderByDirection);
    }

    @Nonnull
    private static OrderByDirection getOrderByDirection(@Nonnull OrderByDirectionContext orderByDirectionContext)
    {
        String text = orderByDirectionContext.getText();

        if ("ascending".equals(text))
        {
            return OrderByDirection.ASCENDING;
        }

        if ("descending".equals(text))
        {
            return OrderByDirection.DESCENDING;
        }

        throw new AssertionError(text);
    }
}
