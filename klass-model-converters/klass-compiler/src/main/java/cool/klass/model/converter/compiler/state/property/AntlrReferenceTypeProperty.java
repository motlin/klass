package cool.klass.model.converter.compiler.state.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.order.AntlrOrderBy;
import cool.klass.model.converter.compiler.state.order.AntlrOrderByOwner;
import cool.klass.model.meta.domain.KlassImpl;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AntlrReferenceTypeProperty extends AntlrProperty<KlassImpl> implements AntlrOrderByOwner
{
    @Nonnull
    protected final AntlrClass             type;
    protected final AntlrMultiplicity      multiplicityState;
    @Nonnull
    protected       Optional<AntlrOrderBy> orderByState = Optional.empty();
    protected       AntlrClass             owningClassState;

    public AntlrReferenceTypeProperty(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AntlrClass type,
            AntlrMultiplicity multiplicityState)
    {
        super(elementContext, compilationUnit, inferred, nameContext, name, ordinal);
        this.type = Objects.requireNonNull(type);
        this.multiplicityState = multiplicityState;
    }

    @Override
    @Nonnull
    public AntlrClass getType()
    {
        return this.type;
    }

    @Nonnull
    @Override
    public AntlrClass getOwningClassState()
    {
        return this.owningClassState;
    }

    public void setOwningClassState(@Nonnull AntlrClass owningClassState)
    {
        this.owningClassState = Objects.requireNonNull(owningClassState);
    }

    @Override
    public void setOrderByState(@Nonnull Optional<AntlrOrderBy> orderByState)
    {
        this.orderByState = Objects.requireNonNull(orderByState);
    }
}
