package cool.klass.model.converter.compiler.state.value.literal;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveType;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.meta.domain.value.literal.IntegerLiteralValueImpl.IntegerLiteralValueBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public final class AntlrIntegerLiteralValue extends AbstractAntlrLiteralValue
{
    private final int value;
    private       IntegerLiteralValueBuilder elementBuilder;

    public AntlrIntegerLiteralValue(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            Optional<AntlrElement> macroElement,
            int value,
            IAntlrElement expressionValueOwner)
    {
        super(elementContext, compilationUnit, macroElement, expressionValueOwner);
        this.value = value;
    }

    @Override
    public void reportErrors(CompilerErrorState compilerErrorHolder)
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
                this.elementContext,
                this.macroElement.map(AntlrElement::getElementBuilder),
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
