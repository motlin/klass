package cool.klass.model.converter.compiler.state.order;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.value.AntlrThisMemberReferencePath;
import cool.klass.model.meta.domain.order.OrderByDirectionDeclarationImpl.OrderByDirectionDeclarationBuilder;
import cool.klass.model.meta.domain.order.OrderByMemberReferencePathImpl.OrderByMemberReferencePathBuilder;
import cool.klass.model.meta.domain.value.ThisMemberReferencePathImpl.ThisMemberReferencePathBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrOrderByMemberReferencePath extends AntlrElement
{
    @Nonnull
    private final AntlrOrderBy                 orderByState;
    private final int                          ordinal;
    @Nonnull
    private final AntlrThisMemberReferencePath thisMemberReferencePathState;
    @Nonnull
    private final AntlrOrderByDirection        orderByDirectionState;

    public AntlrOrderByMemberReferencePath(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull AntlrOrderBy orderByState,
            int ordinal,
            AntlrThisMemberReferencePath thisMemberReferencePathState,
            AntlrOrderByDirection orderByDirectionState)
    {
        super(elementContext, compilationUnit, inferred);
        this.orderByState = Objects.requireNonNull(orderByState);
        this.ordinal = ordinal;
        this.thisMemberReferencePathState = Objects.requireNonNull(thisMemberReferencePathState);
        this.orderByDirectionState = Objects.requireNonNull(orderByDirectionState);
    }

    public int getOrdinal()
    {
        return this.ordinal;
    }

    public void reportErrors(@Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        // TODO: ❗️ Redo context stack for error reporting
        this.thisMemberReferencePathState.reportErrors(compilerErrorHolder, Lists.immutable.empty());
    }

    @Nonnull
    public OrderByMemberReferencePathBuilder build()
    {
        ThisMemberReferencePathBuilder     thisMemberReferencePathBuilder = this.thisMemberReferencePathState.build();
        OrderByDirectionDeclarationBuilder orderByDirectionBuilder        = this.orderByDirectionState.build();

        return new OrderByMemberReferencePathBuilder(
                this.elementContext,
                this.inferred,
                this.orderByState.getOrderByBuilder(),
                this.ordinal,
                thisMemberReferencePathBuilder,
                orderByDirectionBuilder);
    }
}
