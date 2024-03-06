package cool.klass.model.converter.compiler.state.order;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.annotation.CompilerAnnotationHolder;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.value.AntlrThisMemberReferencePath;
import cool.klass.model.meta.domain.order.OrderByMemberReferencePathImpl.OrderByMemberReferencePathBuilder;
import cool.klass.model.meta.domain.value.ThisMemberReferencePathImpl.ThisMemberReferencePathBuilder;
import cool.klass.model.meta.grammar.KlassParser.OrderByMemberReferencePathContext;

public class AntlrOrderByMemberReferencePath
        extends AntlrElement
{
    @Nonnull
    private final AntlrOrderBy                 orderBy;
    private final int                          ordinal;
    @Nonnull
    private final AntlrThisMemberReferencePath thisMemberReferencePath;

    @Nullable
    private AntlrOrderByDirection             orderByDirection;
    private OrderByMemberReferencePathBuilder elementBuilder;

    public AntlrOrderByMemberReferencePath(
            @Nonnull OrderByMemberReferencePathContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull AntlrOrderBy orderBy,
            int ordinal,
            @Nonnull AntlrThisMemberReferencePath thisMemberReferencePath)
    {
        super(elementContext, compilationUnit);
        this.orderBy                 = Objects.requireNonNull(orderBy);
        this.ordinal                 = ordinal;
        this.thisMemberReferencePath = Objects.requireNonNull(thisMemberReferencePath);
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

    public void enterOrderByDirection(@Nonnull AntlrOrderByDirection orderByDirection)
    {
        this.orderByDirection = Objects.requireNonNull(orderByDirection);
    }

    @Nullable
    public AntlrOrderByDirection getOrderByDirection()
    {
        return this.orderByDirection;
    }

    public void reportErrors(@Nonnull CompilerAnnotationHolder compilerAnnotationHolder)
    {
        // TODO: ❗️ Redo context stack for error reporting
        this.thisMemberReferencePath.reportErrors(compilerAnnotationHolder);
    }

    @Nonnull
    public OrderByMemberReferencePathBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        ThisMemberReferencePathBuilder thisMemberReferencePathBuilder = this.thisMemberReferencePath.build();

        this.elementBuilder = new OrderByMemberReferencePathBuilder(
                (OrderByMemberReferencePathContext) this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.orderBy.getElementBuilder(),
                this.ordinal,
                thisMemberReferencePathBuilder);

        this.elementBuilder.setOrderByDirectionBuilder(this.orderByDirection.build());
        return this.elementBuilder;
    }

    @Override
    @Nonnull
    public OrderByMemberReferencePathBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }
}
