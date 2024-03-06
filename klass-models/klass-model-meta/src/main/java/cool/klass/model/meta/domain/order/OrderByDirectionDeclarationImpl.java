package cool.klass.model.meta.domain.order;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.api.order.OrderByDirection;
import cool.klass.model.meta.domain.api.order.OrderByDirectionDeclaration;
import org.antlr.v4.runtime.ParserRuleContext;

public final class OrderByDirectionDeclarationImpl extends AbstractElement implements OrderByDirectionDeclaration
{
    @Nonnull
    private final OrderByDirection orderByDirection;

    private OrderByDirectionDeclarationImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull OrderByDirection orderByDirection)
    {
        super(elementContext, inferred);
        this.orderByDirection = Objects.requireNonNull(orderByDirection);
    }

    @Override
    @Nonnull
    public OrderByDirection getOrderByDirection()
    {
        return this.orderByDirection;
    }

    public static final class OrderByDirectionDeclarationBuilder extends ElementBuilder
    {
        @Nonnull
        private final OrderByDirection orderByDirection;

        public OrderByDirectionDeclarationBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull OrderByDirection orderByDirection)
        {
            super(elementContext, inferred);
            this.orderByDirection = Objects.requireNonNull(orderByDirection);
        }

        public OrderByDirectionDeclarationImpl build()
        {
            return new OrderByDirectionDeclarationImpl(this.elementContext, this.inferred, this.orderByDirection);
        }
    }
}
