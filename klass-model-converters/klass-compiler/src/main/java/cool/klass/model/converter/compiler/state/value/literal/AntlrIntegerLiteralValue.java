package cool.klass.model.converter.compiler.state.value.literal;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveType;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.meta.domain.value.literal.IntegerLiteralValueImpl.IntegerLiteralValueBuilder;
import cool.klass.model.meta.grammar.KlassParser.IntegerLiteralContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public final class AntlrIntegerLiteralValue extends AbstractAntlrLiteralValue
{
    private final long                       value;
    private       IntegerLiteralValueBuilder elementBuilder;

    public AntlrIntegerLiteralValue(
            @Nonnull IntegerLiteralContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            long value,
            @Nonnull IAntlrElement expressionValueOwner)
    {
        super(elementContext, compilationUnit, expressionValueOwner);
        this.value = value;
    }

    @Override
    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
    }

    @Nonnull
    @Override
    public IntegerLiteralValueBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new IntegerLiteralValueBuilder(
                (IntegerLiteralContext) this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.value);
        return this.elementBuilder;
    }

    @Nonnull
    @Override
    public IntegerLiteralValueBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }

    @Nonnull
    @Override
    public ImmutableList<AntlrType> getPossibleTypes()
    {
        return Lists.immutable.with(
                AntlrPrimitiveType.INTEGER,
                AntlrPrimitiveType.LONG,
                AntlrPrimitiveType.FLOAT,
                AntlrPrimitiveType.DOUBLE);
    }
}
