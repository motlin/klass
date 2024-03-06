package cool.klass.model.converter.compiler.state.order;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.value.AntlrThisMemberValue;
import org.antlr.v4.runtime.ParserRuleContext;

public class AntlrOrderByProperty extends AntlrElement
{
    @Nonnull
    private final AntlrOrderBy          orderByState;
    private final int                   ordinal;
    @Nonnull
    private final AntlrThisMemberValue  thisMemberValueState;
    @Nonnull
    private final AntlrOrderByDirection orderByDirectionState;

    public AntlrOrderByProperty(
            @Nonnull ParserRuleContext elementContext,
            @Nullable CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull AntlrOrderBy orderByState,
            int ordinal,
            AntlrThisMemberValue thisMemberValueState,
            AntlrOrderByDirection orderByDirectionState)
    {
        super(elementContext, compilationUnit, inferred);
        this.orderByState = Objects.requireNonNull(orderByState);
        this.ordinal = ordinal;
        this.thisMemberValueState = Objects.requireNonNull(thisMemberValueState);
        this.orderByDirectionState = Objects.requireNonNull(orderByDirectionState);
    }

    public int getOrdinal()
    {
        return this.ordinal;
    }
}
