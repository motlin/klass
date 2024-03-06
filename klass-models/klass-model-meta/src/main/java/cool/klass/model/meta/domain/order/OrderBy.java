package cool.klass.model.meta.domain.order;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Element;
import cool.klass.model.meta.domain.Klass;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import cool.klass.model.meta.domain.order.OrderByMemberReferencePath.OrderByMemberReferencePathBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class OrderBy extends Element
{
    @Nonnull
    private final Klass          thisContext;

    private ImmutableList<OrderByMemberReferencePath> orderByMemberReferencePaths;

    private OrderBy(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull Klass thisContext)
    {
        super(elementContext, inferred);
        this.thisContext = Objects.requireNonNull(thisContext);
    }

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
        private OrderBy                                          orderBy;

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

        public OrderBy getOrderBy()
        {
            return Objects.requireNonNull(this.orderBy);
        }

        public OrderBy build()
        {
            if (this.orderBy != null)
            {
                throw new IllegalStateException();
            }

            this.orderBy = new OrderBy(
                    this.elementContext,
                    this.inferred,
                    this.thisContextBuilder.getKlass()
            );

            ImmutableList<OrderByMemberReferencePath> orderByMemberReferencePaths =
                    this.orderByMemberReferencePathBuilders.collect(OrderByMemberReferencePathBuilder::build);
            this.orderBy.setOrderByMemberReferencePaths(orderByMemberReferencePaths);

            return this.orderBy;
        }
    }
}
