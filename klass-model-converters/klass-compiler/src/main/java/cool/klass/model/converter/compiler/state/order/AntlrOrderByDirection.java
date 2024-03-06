package cool.klass.model.converter.compiler.state.order;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.meta.domain.api.order.OrderByDirection;
import cool.klass.model.meta.domain.order.OrderByDirectionDeclarationImpl.OrderByDirectionDeclarationBuilder;
import cool.klass.model.meta.grammar.KlassParser.OrderByDirectionContext;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrOrderByDirection extends AntlrElement
{
    @Nonnull
    private final OrderByDirection orderByDirection;

    public AntlrOrderByDirection(
            @Nullable OrderByDirectionContext orderByDirectionContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred)
    {
        super(
                orderByDirectionContext == null ? new ParserRuleContext() : orderByDirectionContext,
                compilationUnit,
                inferred || orderByDirectionContext == null);
        this.orderByDirection = this.getOrderByDirection(orderByDirectionContext);
    }

    @Nonnull
    private OrderByDirection getOrderByDirection(@Nullable OrderByDirectionContext orderByDirectionContext)
    {
        if (orderByDirectionContext == null)
        {
            return OrderByDirection.ASCENDING;
        }

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

    @Nonnull
    public OrderByDirection getOrderByDirection()
    {
        return this.orderByDirection;
    }

    @Nonnull
    public OrderByDirectionDeclarationBuilder build()
    {
        return new OrderByDirectionDeclarationBuilder(this.elementContext, this.inferred, this.orderByDirection);
    }
}
