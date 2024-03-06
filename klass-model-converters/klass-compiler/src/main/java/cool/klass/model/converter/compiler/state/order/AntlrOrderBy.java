package cool.klass.model.converter.compiler.state.order;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClassifier;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.service.AntlrService;
import cool.klass.model.meta.domain.order.OrderByImpl.OrderByBuilder;
import cool.klass.model.meta.domain.order.OrderByMemberReferencePathImpl.OrderByMemberReferencePathBuilder;
import cool.klass.model.meta.grammar.KlassParser.OrderByMemberReferencePathContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.Token;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.map.MutableOrderedMap;
import org.eclipse.collections.api.tuple.Pair;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.map.ordered.mutable.OrderedMapAdapter;

// TODO: Specific subclasses for the specific antlr context types
public class AntlrOrderBy extends AntlrElement
{
    @Nonnull
    private final AntlrClassifier   thisContext;
    @Nonnull
    private final AntlrOrderByOwner orderByOwnerState;

    @Nonnull
    private final MutableList<AntlrOrderByMemberReferencePath> orderByMemberReferencePathStates = Lists.mutable.empty();

    @Nonnull
    private final MutableOrderedMap<ParserRuleContext, AntlrOrderByMemberReferencePath> orderByMemberReferencePathsByContext = OrderedMapAdapter.adapt(new LinkedHashMap<>());

    private OrderByBuilder elementBuilder;

    public AntlrOrderBy(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull AntlrClassifier thisContext,
            @Nonnull AntlrOrderByOwner orderByOwnerState)
    {
        super(elementContext, compilationUnit);
        this.thisContext       = Objects.requireNonNull(thisContext);
        this.orderByOwnerState = Objects.requireNonNull(orderByOwnerState);
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.orderByOwnerState);
    }

    @Override
    public boolean isContext()
    {
        return this.orderByOwnerState instanceof AntlrService;
    }

    @Override
    public Pair<Token, Token> getContextBefore()
    {
        return this.getEntireContext();
    }

    public int getNumProperties()
    {
        return this.orderByMemberReferencePathStates.size();
    }

    public void enterOrderByMemberReferencePath(AntlrOrderByMemberReferencePath orderByMemberReferencePathState)
    {
        this.orderByMemberReferencePathStates.add(orderByMemberReferencePathState);

        AntlrOrderByMemberReferencePath duplicate = this.orderByMemberReferencePathsByContext.put(
                orderByMemberReferencePathState.getElementContext(),
                orderByMemberReferencePathState);
        if (duplicate != null)
        {
            throw new AssertionError();
        }
    }

    public AntlrOrderByMemberReferencePath getOrderByMemberReferencePath(OrderByMemberReferencePathContext ctx)
    {
        return this.orderByMemberReferencePathsByContext.get(ctx);
    }

    public void reportErrors(CompilerErrorState compilerErrorHolder)
    {
        this.orderByMemberReferencePathStates.forEachWith(
                AntlrOrderByMemberReferencePath::reportErrors,
                compilerErrorHolder);
    }

    public OrderByBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new OrderByBuilder(
                this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.thisContext.getElementBuilder());

        ImmutableList<OrderByMemberReferencePathBuilder> orderByMemberReferencePathBuilders =
                this.orderByMemberReferencePathStates
                        .collect(AntlrOrderByMemberReferencePath::build)
                        .toImmutable();
        this.elementBuilder.setOrderByMemberReferencePathBuilders(orderByMemberReferencePathBuilders);
        return this.elementBuilder;
    }

    @Override
    @Nonnull
    public OrderByBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }
}
