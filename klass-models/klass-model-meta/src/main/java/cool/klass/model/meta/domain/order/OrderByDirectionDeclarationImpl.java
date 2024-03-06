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
import org.antlr.v4.runtime.ParserRuleContext;

public final class OrderByDirectionDeclarationImpl
        extends AbstractElement
        implements OrderByDirectionDeclaration
{
    @Nonnull
    private final OrderByDirection orderByDirection;

    private OrderByDirectionDeclarationImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            @Nonnull OrderByDirection orderByDirection)
    {
        super(elementContext, macroElement, sourceCode);
        this.orderByDirection = Objects.requireNonNull(orderByDirection);
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
                @Nonnull ParserRuleContext elementContext,
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
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.orderByDirection);
        }
    }
}
