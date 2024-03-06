package cool.klass.model.converter.compiler.state.value.literal;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveType;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.meta.domain.value.literal.BooleanLiteralValueImpl.BooleanLiteralValueBuilder;
import cool.klass.model.meta.grammar.KlassParser.BooleanLiteralContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public final class AntlrBooleanLiteralValue
        extends AbstractAntlrLiteralValue
{
    private final boolean                    value;
    private       BooleanLiteralValueBuilder elementBuilder;

    public AntlrBooleanLiteralValue(
            @Nonnull BooleanLiteralContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            boolean value,
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
    public BooleanLiteralValueBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new BooleanLiteralValueBuilder(
                (BooleanLiteralContext) this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.value);
        return this.elementBuilder;
    }

    @Nonnull
    @Override
    public BooleanLiteralValueBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }

    @Nonnull
    @Override
    public ImmutableList<AntlrType> getPossibleTypes()
    {
        return Lists.immutable.with(
                AntlrPrimitiveType.BOOLEAN,
                AntlrPrimitiveType.LONG,
                AntlrPrimitiveType.FLOAT,
                AntlrPrimitiveType.DOUBLE);
    }
}
