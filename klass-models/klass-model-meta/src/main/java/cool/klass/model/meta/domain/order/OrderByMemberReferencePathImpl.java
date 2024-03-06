package cool.klass.model.meta.domain.order;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.order.OrderByMemberReferencePath;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.order.OrderByDirectionDeclarationImpl.OrderByDirectionDeclarationBuilder;
import cool.klass.model.meta.domain.order.OrderByImpl.OrderByBuilder;
import cool.klass.model.meta.domain.value.ThisMemberReferencePathImpl;
import cool.klass.model.meta.domain.value.ThisMemberReferencePathImpl.ThisMemberReferencePathBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class OrderByMemberReferencePathImpl
        extends AbstractElement
        implements OrderByMemberReferencePath
{
    @Nonnull
    private final OrderByImpl                 orderBy;
    private final int                         ordinal;
    @Nonnull
    private final ThisMemberReferencePathImpl thisMemberReferencePath;

    @Nullable
    private OrderByDirectionDeclarationImpl orderByDirectionDeclaration;

    private OrderByMemberReferencePathImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            @Nonnull OrderByImpl orderBy,
            int ordinal,
            @Nonnull ThisMemberReferencePathImpl thisMemberReferencePath)
    {
        super(elementContext, macroElement, sourceCode);
        this.orderBy                 = Objects.requireNonNull(orderBy);
        this.ordinal                 = ordinal;
        this.thisMemberReferencePath = Objects.requireNonNull(thisMemberReferencePath);
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

    public void setOrderByDirectionDeclaration(@Nonnull OrderByDirectionDeclarationImpl orderByDirectionDeclaration)
    {
        this.orderByDirectionDeclaration = Objects.requireNonNull(orderByDirectionDeclaration);
    }

    public static final class OrderByMemberReferencePathBuilder
            extends ElementBuilder<OrderByMemberReferencePathImpl>
    {
        @Nonnull
        private final OrderByBuilder                 orderByBuilder;
        private final int                            ordinal;
        @Nonnull
        private final ThisMemberReferencePathBuilder thisMemberReferencePathBuilder;

        @Nullable
        private OrderByDirectionDeclarationBuilder orderByDirectionBuilder;

        public OrderByMemberReferencePathBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                @Nonnull OrderByBuilder orderByBuilder,
                int ordinal,
                @Nonnull ThisMemberReferencePathBuilder thisMemberReferencePathBuilder)
        {
            super(elementContext, macroElement, sourceCode);
            this.orderByBuilder                 = Objects.requireNonNull(orderByBuilder);
            this.ordinal                        = ordinal;
            this.thisMemberReferencePathBuilder = Objects.requireNonNull(thisMemberReferencePathBuilder);
        }

        public void setOrderByDirectionBuilder(@Nonnull OrderByDirectionDeclarationBuilder orderByDirectionBuilder)
        {
            this.orderByDirectionBuilder = Objects.requireNonNull(orderByDirectionBuilder);
        }

        @Override
        @Nonnull
        protected OrderByMemberReferencePathImpl buildUnsafe()
        {
            return new OrderByMemberReferencePathImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.orderByBuilder.getElement(),
                    this.ordinal,
                    this.thisMemberReferencePathBuilder.build());
        }

        @Override
        protected void buildChildren()
        {
            this.element.setOrderByDirectionDeclaration(this.orderByDirectionBuilder.build());
        }
    }
}
