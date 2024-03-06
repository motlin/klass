package cool.klass.model.converter.compiler.state.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrMultiplicity;
import cool.klass.model.converter.compiler.state.order.AntlrOrderBy;
import cool.klass.model.converter.compiler.state.order.AntlrOrderByOwner;
import cool.klass.model.meta.domain.KlassImpl;
import cool.klass.model.meta.grammar.KlassParser.ClassReferenceContext;
import cool.klass.model.meta.grammar.KlassParser.ClassTypeContext;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AntlrReferenceTypeProperty extends AntlrProperty<KlassImpl> implements AntlrOrderByOwner
{
    @Nonnull
    protected final AntlrClass             type;
    protected final AntlrMultiplicity      multiplicityState;
    @Nonnull
    protected       Optional<AntlrOrderBy> orderByState = Optional.empty();

    protected AntlrReferenceTypeProperty(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AntlrClass type,
            @Nonnull AntlrMultiplicity multiplicityState)
    {
        super(elementContext, compilationUnit, nameContext, name, ordinal);
        this.type = Objects.requireNonNull(type);
        this.multiplicityState = Objects.requireNonNull(multiplicityState);
    }

    @Override
    @Nonnull
    public AntlrClass getType()
    {
        return Objects.requireNonNull(this.type);
    }

    public AntlrMultiplicity getMultiplicity()
    {
        return this.multiplicityState;
    }

    @Override
    public void setOrderByState(@Nonnull Optional<AntlrOrderBy> orderByState)
    {
        if (this.orderByState.isPresent())
        {
            throw new IllegalStateException();
        }
        this.orderByState = Objects.requireNonNull(orderByState);
    }

    public void reportTypeNotFound(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        if (this.type != AntlrClass.NOT_FOUND)
        {
            return;
        }

        ClassReferenceContext offendingToken = this.getClassType().classReference();
        String message = String.format(
                "Cannot find class '%s'.",
                offendingToken.getText());
        compilerErrorHolder.add("ERR_PRP_TYP", message, this, offendingToken);
    }

    protected abstract ClassTypeContext getClassType();
}
