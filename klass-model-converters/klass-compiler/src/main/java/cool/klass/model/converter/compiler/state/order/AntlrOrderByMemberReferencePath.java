package cool.klass.model.converter.compiler.state.order;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.value.AntlrThisMemberReferencePath;
import cool.klass.model.meta.domain.order.OrderByDirectionDeclarationImpl.OrderByDirectionDeclarationBuilder;
import cool.klass.model.meta.domain.order.OrderByMemberReferencePathImpl.OrderByMemberReferencePathBuilder;
import cool.klass.model.meta.domain.value.ThisMemberReferencePathImpl.ThisMemberReferencePathBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrOrderByMemberReferencePath extends AntlrElement
{
    @Nonnull
    private final AntlrOrderBy                 orderByState;
    private final int                          ordinal;
    @Nonnull
    private final AntlrThisMemberReferencePath thisMemberReferencePathState;
    @Nonnull
    private final AntlrOrderByDirection        orderByDirectionState;
    private OrderByMemberReferencePathBuilder elementBuilder;

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

    @Override
    public boolean omitParentFromSurroundingElements()
    {
        return true;
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getSurroundingContext() not implemented yet");
    }

    public int getOrdinal()
    {
        return this.ordinal;
    }

    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        // TODO: ❗️ Redo context stack for error reporting
        this.thisMemberReferencePathState.reportErrors(compilerErrorHolder);
    }

    @Nonnull
    public OrderByMemberReferencePathBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        ThisMemberReferencePathBuilder     thisMemberReferencePathBuilder = this.thisMemberReferencePathState.build();
        OrderByDirectionDeclarationBuilder orderByDirectionBuilder        = this.orderByDirectionState.build();

        this.elementBuilder = new OrderByMemberReferencePathBuilder(
                this.elementContext,
                this.inferred,
                this.orderByState.getElementBuilder(),
                this.ordinal,
                thisMemberReferencePathBuilder,
                orderByDirectionBuilder);
        return this.elementBuilder;
    }

    @Nonnull
    public OrderByMemberReferencePathBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }
}
