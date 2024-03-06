package cool.klass.model.converter.compiler.state.order;

import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.annotation.CompilerAnnotationHolder;
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
public class AntlrOrderBy
        extends AntlrElement
{
    @Nonnull
    private final AntlrClassifier   thisContext;
    @Nonnull
    private final AntlrOrderByOwner orderByOwner;

    @Nonnull
    private final MutableList<AntlrOrderByMemberReferencePath> orderByMemberReferencePaths = Lists.mutable.empty();

    @Nonnull
    private final MutableOrderedMap<ParserRuleContext, AntlrOrderByMemberReferencePath> orderByMemberReferencePathsByContext = OrderedMapAdapter.adapt(new LinkedHashMap<>());

    private OrderByBuilder elementBuilder;

    public AntlrOrderBy(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull AntlrClassifier thisContext,
            @Nonnull AntlrOrderByOwner orderByOwner)
    {
        super(elementContext, compilationUnit);
        this.thisContext  = Objects.requireNonNull(thisContext);
        this.orderByOwner = Objects.requireNonNull(orderByOwner);
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.orderByOwner);
    }

    @Override
    public boolean isContext()
    {
        return this.orderByOwner instanceof AntlrService;
    }

    @Override
    public Pair<Token, Token> getContextBefore()
    {
        return this.getEntireContext();
    }

    public int getNumProperties()
    {
        return this.orderByMemberReferencePaths.size();
    }

    public void enterOrderByMemberReferencePath(AntlrOrderByMemberReferencePath orderByMemberReferencePath)
    {
        this.orderByMemberReferencePaths.add(orderByMemberReferencePath);

        AntlrOrderByMemberReferencePath duplicate = this.orderByMemberReferencePathsByContext.put(
                orderByMemberReferencePath.getElementContext(),
                orderByMemberReferencePath);
        if (duplicate != null)
        {
            throw new AssertionError();
        }
    }

    public AntlrOrderByMemberReferencePath getOrderByMemberReferencePath(OrderByMemberReferencePathContext ctx)
    {
        return this.orderByMemberReferencePathsByContext.get(ctx);
    }

    public void reportErrors(CompilerAnnotationHolder compilerAnnotationHolder)
    {
        this.orderByMemberReferencePaths.forEachWith(
                AntlrOrderByMemberReferencePath::reportErrors,
                compilerAnnotationHolder);
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
                this.orderByMemberReferencePaths
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
