package cool.klass.model.meta.domain.order;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Element;
import org.antlr.v4.runtime.ParserRuleContext;

public final class OrderByDirectionDeclaration extends Element
{
    @Nonnull
    private final OrderByDirection orderByDirection;

    private OrderByDirectionDeclaration(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull OrderByDirection orderByDirection)
    {
        super(elementContext);
        this.orderByDirection = Objects.requireNonNull(orderByDirection);
    }

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
                @Nonnull OrderByDirection orderByDirection)
        {
            super(elementContext);
            this.orderByDirection = Objects.requireNonNull(orderByDirection);
        }

        public OrderByDirectionDeclaration build()
        {
            return new OrderByDirectionDeclaration(this.elementContext, this.orderByDirection);
        }
    }
}
