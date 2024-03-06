package cool.klass.model.meta.domain.order;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.KlassImpl;
import cool.klass.model.meta.domain.KlassImpl.KlassBuilder;
import cool.klass.model.meta.domain.api.order.OrderBy;
import cool.klass.model.meta.domain.api.order.OrderByMemberReferencePath;
import cool.klass.model.meta.domain.order.OrderByMemberReferencePathImpl.OrderByMemberReferencePathBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class OrderByImpl extends AbstractElement implements OrderBy
{
    @Nonnull
    private final KlassImpl thisContext;

    private ImmutableList<OrderByMemberReferencePath> orderByMemberReferencePaths;

    private OrderByImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull KlassImpl thisContext)
    {
        super(elementContext, inferred);
        this.thisContext = Objects.requireNonNull(thisContext);
    }

    @Override
    public ImmutableList<OrderByMemberReferencePath> getOrderByMemberReferencePaths()
    {
        return this.orderByMemberReferencePaths;
    }

    private void setOrderByMemberReferencePaths(ImmutableList<OrderByMemberReferencePath> orderByMemberReferencePaths)
    {
        this.orderByMemberReferencePaths = Objects.requireNonNull(orderByMemberReferencePaths);
    }

    public static final class OrderByBuilder extends ElementBuilder
    {
        @Nonnull
        private final KlassBuilder          thisContextBuilder;

        private ImmutableList<OrderByMemberReferencePathBuilder> orderByMemberReferencePathBuilders;
        private OrderByImpl                                      orderBy;

        public OrderByBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull KlassBuilder thisContextBuilder)
        {
            super(elementContext, inferred);
            this.thisContextBuilder = Objects.requireNonNull(thisContextBuilder);
        }

        public void setOrderByMemberReferencePathBuilders(ImmutableList<OrderByMemberReferencePathBuilder> orderByMemberReferencePathBuilders)
        {
            this.orderByMemberReferencePathBuilders = Objects.requireNonNull(orderByMemberReferencePathBuilders);
        }

        public OrderByImpl getOrderBy()
        {
            return Objects.requireNonNull(this.orderBy);
        }

        public OrderByImpl build()
        {
            if (this.orderBy != null)
            {
                throw new IllegalStateException();
            }

            this.orderBy = new OrderByImpl(
                    this.elementContext,
                    this.inferred,
                    this.thisContextBuilder.getElement()
            );

            ImmutableList<OrderByMemberReferencePath> orderByMemberReferencePaths =
                    this.orderByMemberReferencePathBuilders.collect(OrderByMemberReferencePathBuilder::build);
            this.orderBy.setOrderByMemberReferencePaths(orderByMemberReferencePaths);

            return this.orderBy;
        }
    }
}
