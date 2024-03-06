package cool.klass.model.converter.compiler.state.order;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.meta.domain.api.order.OrderByDirection;
import cool.klass.model.meta.domain.order.OrderByDirectionDeclarationImpl.OrderByDirectionDeclarationBuilder;
import cool.klass.model.meta.grammar.KlassParser.OrderByDirectionContext;

public class AntlrOrderByDirection extends AntlrElement
{
    @Nonnull
    private final OrderByDirection orderByDirection;

    private OrderByDirectionDeclarationBuilder elementBuilder;

    public AntlrOrderByDirection(
            @Nonnull OrderByDirectionContext orderByDirectionContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull OrderByDirection orderByDirection)
    {
        super(orderByDirectionContext, compilationUnit);
        this.orderByDirection = Objects.requireNonNull(orderByDirection);
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".getSurroundingContext() not implemented yet");
    }

    @Nonnull
    public OrderByDirectionDeclarationBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new OrderByDirectionDeclarationBuilder(
                (OrderByDirectionContext) this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.orderByDirection);
        return this.elementBuilder;
    }

    @Override
    @Nonnull
    public OrderByDirectionDeclarationBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }
}
