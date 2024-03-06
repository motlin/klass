package cool.klass.model.meta.domain.order;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.order.OrderByMemberReferencePath;
import cool.klass.model.meta.domain.order.OrderByDirectionDeclarationImpl.OrderByDirectionDeclarationBuilder;
import cool.klass.model.meta.domain.order.OrderByImpl.OrderByBuilder;
import cool.klass.model.meta.domain.value.ThisMemberReferencePathImpl;
import cool.klass.model.meta.domain.value.ThisMemberReferencePathImpl.ThisMemberReferencePathBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class OrderByMemberReferencePathImpl extends AbstractElement implements OrderByMemberReferencePath
{
    @Nonnull
    private final OrderByImpl                     orderBy;
    private final int                             ordinal;
    @Nonnull
    private final ThisMemberReferencePathImpl     thisMemberReferencePath;
    @Nonnull
    private final OrderByDirectionDeclarationImpl orderByDirectionDeclaration;

    private OrderByMemberReferencePathImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nonnull OrderByImpl orderBy,
            int ordinal,
            @Nonnull ThisMemberReferencePathImpl thisMemberReferencePath,
            @Nonnull OrderByDirectionDeclarationImpl orderByDirectionDeclaration)
    {
        super(elementContext, macroElement);
        this.orderBy                     = Objects.requireNonNull(orderBy);
        this.ordinal                     = ordinal;
        this.thisMemberReferencePath     = Objects.requireNonNull(thisMemberReferencePath);
        this.orderByDirectionDeclaration = Objects.requireNonNull(orderByDirectionDeclaration);
    }

    @Override
    @Nonnull
    public ThisMemberReferencePathImpl getThisMemberReferencePath()
    {
        return this.thisMemberReferencePath;
    }

    @Override
    @Nonnull
    public OrderByDirectionDeclarationImpl getOrderByDirectionDeclaration()
    {
        return this.orderByDirectionDeclaration;
    }

    public static final class OrderByMemberReferencePathBuilder extends ElementBuilder<OrderByMemberReferencePathImpl>
    {
        @Nonnull
        private final OrderByBuilder                     orderByBuilder;
        private final int                                ordinal;
        @Nonnull
        private final ThisMemberReferencePathBuilder     thisMemberReferencePathBuilder;
        @Nonnull
        private final OrderByDirectionDeclarationBuilder orderByDirectionBuilder;

        public OrderByMemberReferencePathBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull OrderByBuilder orderByBuilder,
                int ordinal,
                @Nonnull ThisMemberReferencePathBuilder thisMemberReferencePathBuilder,
                @Nonnull OrderByDirectionDeclarationBuilder orderByDirectionBuilder)
        {
            super(elementContext, macroElement);
            this.orderByBuilder                 = Objects.requireNonNull(orderByBuilder);
            this.ordinal                        = ordinal;
            this.thisMemberReferencePathBuilder = Objects.requireNonNull(thisMemberReferencePathBuilder);
            this.orderByDirectionBuilder        = Objects.requireNonNull(orderByDirectionBuilder);
        }

        @Override
        @Nonnull
        protected OrderByMemberReferencePathImpl buildUnsafe()
        {
            return new OrderByMemberReferencePathImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.orderByBuilder.getElement(),
                    this.ordinal,
                    this.thisMemberReferencePathBuilder.build(),
                    this.orderByDirectionBuilder.build());
        }
    }
}
