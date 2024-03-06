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
import cool.klass.model.meta.domain.value.literal.NullLiteralImpl.NullLiteralBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrNullLiteral extends AbstractAntlrLiteralValue
{
    private NullLiteralBuilder elementBuilder;

    public AntlrNullLiteral(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            Optional<AntlrElement> macroElement,
            IAntlrElement expressionValueOwner)
    {
        super(elementContext, compilationUnit, macroElement, expressionValueOwner);
    }

    @Override
    public void reportErrors(CompilerErrorState compilerErrorHolder)
    {
    }

    @Nonnull
    @Override
    public NullLiteralBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new NullLiteralBuilder(
                this.elementContext,
                this.macroElement.map(AntlrElement::getElementBuilder));
        return this.elementBuilder;
    }

    @Nonnull
    @Override
    public NullLiteralBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }

    @Nonnull
    @Override
    public ImmutableList<AntlrType> getPossibleTypes()
    {
        return Lists.immutable.with(
                AntlrPrimitiveType.STRING,
                AntlrPrimitiveType.INTEGER,
                AntlrPrimitiveType.LONG,
                AntlrPrimitiveType.DOUBLE,
                AntlrPrimitiveType.FLOAT,
                AntlrPrimitiveType.BOOLEAN,
                AntlrPrimitiveType.INSTANT,
                AntlrPrimitiveType.LOCAL_DATE);
    }
}
