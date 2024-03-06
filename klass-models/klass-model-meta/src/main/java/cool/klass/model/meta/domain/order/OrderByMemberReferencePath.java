package cool.klass.model.meta.domain.order;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Element;
import cool.klass.model.meta.domain.order.OrderBy.OrderByBuilder;
import cool.klass.model.meta.domain.order.OrderByDirectionDeclaration.OrderByDirectionDeclarationBuilder;
import cool.klass.model.meta.domain.value.ThisMemberReferencePath;
import cool.klass.model.meta.domain.value.ThisMemberReferencePath.ThisMemberReferencePathBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class OrderByMemberReferencePath extends Element
{
    @Nonnull
    private final OrderBy                     orderBy;
    private final int                         ordinal;
    @Nonnull
    private final ThisMemberReferencePath     thisMemberReferencePath;
    @Nonnull
    private final OrderByDirectionDeclaration orderByDirectionDeclaration;

    private OrderByMemberReferencePath(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull OrderBy orderBy,
            int ordinal,
            @Nonnull ThisMemberReferencePath thisMemberReferencePath,
            @Nonnull OrderByDirectionDeclaration orderByDirectionDeclaration)
    {
        super(elementContext, inferred);
        this.orderBy = Objects.requireNonNull(orderBy);
        this.ordinal = ordinal;
        this.thisMemberReferencePath = Objects.requireNonNull(thisMemberReferencePath);
        this.orderByDirectionDeclaration = Objects.requireNonNull(orderByDirectionDeclaration);
    }

    @Nonnull
    public ThisMemberReferencePath getThisMemberReferencePath()
    {
        return this.thisMemberReferencePath;
    }

    @Nonnull
    public OrderByDirectionDeclaration getOrderByDirectionDeclaration()
    {
        return this.orderByDirectionDeclaration;
    }

    public static final class OrderByMemberReferencePathBuilder extends ElementBuilder
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
                boolean inferred,
                @Nonnull OrderByBuilder orderByBuilder,
                int ordinal,
                @Nonnull ThisMemberReferencePathBuilder thisMemberReferencePathBuilder,
                @Nonnull OrderByDirectionDeclarationBuilder orderByDirectionBuilder)
        {
            super(elementContext, inferred);
            this.orderByBuilder = Objects.requireNonNull(orderByBuilder);
            this.ordinal = ordinal;
            this.thisMemberReferencePathBuilder = Objects.requireNonNull(thisMemberReferencePathBuilder);
            this.orderByDirectionBuilder = Objects.requireNonNull(orderByDirectionBuilder);
        }

        public OrderByMemberReferencePath build()
        {
            return new OrderByMemberReferencePath(
                    this.elementContext,
                    this.inferred,
                    this.orderByBuilder.getOrderBy(),
                    this.ordinal,
                    this.thisMemberReferencePathBuilder.build(),
                    this.orderByDirectionBuilder.build());
        }
    }
}
