package cool.klass.model.converter.compiler.state.order;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.annotation.CompilerAnnotationState;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.value.AntlrThisMemberReferencePath;
import cool.klass.model.meta.domain.order.OrderByMemberReferencePathImpl.OrderByMemberReferencePathBuilder;
import cool.klass.model.meta.domain.value.ThisMemberReferencePathImpl.ThisMemberReferencePathBuilder;
import cool.klass.model.meta.grammar.KlassParser.OrderByMemberReferencePathContext;

public class AntlrOrderByMemberReferencePath extends AntlrElement
{
    @Nonnull
    private final AntlrOrderBy                 orderByState;
    private final int                          ordinal;
    @Nonnull
    private final AntlrThisMemberReferencePath thisMemberReferencePathState;

    @Nullable
    private AntlrOrderByDirection orderByDirectionState;

    private OrderByMemberReferencePathBuilder elementBuilder;

    public AntlrOrderByMemberReferencePath(
            @Nonnull OrderByMemberReferencePathContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull AntlrOrderBy orderByState,
            int ordinal,
            @Nonnull AntlrThisMemberReferencePath thisMemberReferencePathState)
    {
        super(elementContext, compilationUnit);
        this.orderByState                 = Objects.requireNonNull(orderByState);
        this.ordinal                      = ordinal;
        this.thisMemberReferencePathState = Objects.requireNonNull(thisMemberReferencePathState);
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

    public void enterOrderByDirection(@Nonnull AntlrOrderByDirection orderByDirectionState)
    {
        this.orderByDirectionState = Objects.requireNonNull(orderByDirectionState);
    }

    @Nullable
    public AntlrOrderByDirection getOrderByDirectionState()
    {
        return this.orderByDirectionState;
    }

    public void reportErrors(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        // TODO: ❗️ Redo context stack for error reporting
        this.thisMemberReferencePathState.reportErrors(compilerAnnotationHolder);
    }

    @Nonnull
    public OrderByMemberReferencePathBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        ThisMemberReferencePathBuilder thisMemberReferencePathBuilder = this.thisMemberReferencePathState.build();

        this.elementBuilder = new OrderByMemberReferencePathBuilder(
                (OrderByMemberReferencePathContext) this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.orderByState.getElementBuilder(),
                this.ordinal,
                thisMemberReferencePathBuilder);

        this.elementBuilder.setOrderByDirectionBuilder(this.orderByDirectionState.build());
        return this.elementBuilder;
    }

    @Override
    @Nonnull
    public OrderByMemberReferencePathBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }
}
