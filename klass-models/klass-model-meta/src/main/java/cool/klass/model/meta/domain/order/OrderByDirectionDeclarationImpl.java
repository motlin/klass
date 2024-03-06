package cool.klass.model.meta.domain.order;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.order.OrderByDirection;
import cool.klass.model.meta.domain.api.order.OrderByDirectionDeclaration;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.grammar.KlassParser.OrderByDirectionContext;

public final class OrderByDirectionDeclarationImpl
        extends AbstractElement
        implements OrderByDirectionDeclaration
{
    @Nonnull
    private final OrderByDirection orderByDirection;

    private OrderByDirectionDeclarationImpl(
            @Nonnull OrderByDirectionContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            @Nonnull OrderByDirection orderByDirection)
    {
        super(elementContext, macroElement, sourceCode);
        this.orderByDirection = Objects.requireNonNull(orderByDirection);
    }

    @Nonnull
    @Override
    public OrderByDirectionContext getElementContext()
    {
        return (OrderByDirectionContext) super.getElementContext();
    }

    @Override
    @Nonnull
    public OrderByDirection getOrderByDirection()
    {
        return this.orderByDirection;
    }

    public static final class OrderByDirectionDeclarationBuilder
            extends ElementBuilder<OrderByDirectionDeclarationImpl>
    {
        @Nonnull
        private final OrderByDirection orderByDirection;

        public OrderByDirectionDeclarationBuilder(
                @Nonnull OrderByDirectionContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                @Nonnull OrderByDirection orderByDirection)
        {
            super(elementContext, macroElement, sourceCode);
            this.orderByDirection = Objects.requireNonNull(orderByDirection);
        }

        @Override
        @Nonnull
        protected OrderByDirectionDeclarationImpl buildUnsafe()
        {
            return new OrderByDirectionDeclarationImpl(
                    (OrderByDirectionContext) this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.orderByDirection);
        }
    }
}
