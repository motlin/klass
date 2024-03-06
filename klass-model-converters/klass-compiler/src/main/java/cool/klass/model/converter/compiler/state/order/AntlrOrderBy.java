package cool.klass.model.converter.compiler.state.order;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.meta.domain.order.OrderBy.OrderByBuilder;
import cool.klass.model.meta.domain.order.OrderByMemberReferencePath.OrderByMemberReferencePathBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrOrderBy extends AntlrElement
{
    @Nonnull
    private final AntlrClass          thisContext;
    // TODO: OrderByOwner
    @Nonnull
    private final AntlrAssociationEnd associationEndState;

    @Nonnull
    private final MutableList<AntlrOrderByMemberReferencePath> orderByMemberReferencePathStates = Lists.mutable.empty();
    private       OrderByBuilder                               orderByBuilder;

    public AntlrOrderBy(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull AntlrClass thisContext,
            // TODO: OrderByOwner
            @Nonnull AntlrAssociationEnd associationEndState)
    {
        super(elementContext, compilationUnit, inferred);
        this.thisContext = Objects.requireNonNull(thisContext);
        this.associationEndState = Objects.requireNonNull(associationEndState);
    }

    public int getNumProperties()
    {
        return this.orderByMemberReferencePathStates.size();
    }

    public void enterOrderByMemberReferencePath(AntlrOrderByMemberReferencePath orderByMemberReferencePathState)
    {
        this.orderByMemberReferencePathStates.add(orderByMemberReferencePathState);
    }

    public void reportErrors(CompilerErrorHolder compilerErrorHolder)
    {
        this.orderByMemberReferencePathStates.forEachWith(
                AntlrOrderByMemberReferencePath::reportErrors,
                compilerErrorHolder);
    }

    public OrderByBuilder getOrderByBuilder()
    {
        return Objects.requireNonNull(this.orderByBuilder);
    }

    public OrderByBuilder build()
    {
        if (this.orderByBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.orderByBuilder = new OrderByBuilder(
                this.elementContext,
                this.inferred,
                this.thisContext.getKlassBuilder(),
                this.associationEndState.getAssociationEndBuilder());

        ImmutableList<OrderByMemberReferencePathBuilder> orderByMemberReferencePathBuilders =
                this.orderByMemberReferencePathStates
                        .collect(AntlrOrderByMemberReferencePath::build)
                        .toImmutable();
        this.orderByBuilder.setOrderByMemberReferencePathBuilders(orderByMemberReferencePathBuilders);
        return this.orderByBuilder;
    }
}
