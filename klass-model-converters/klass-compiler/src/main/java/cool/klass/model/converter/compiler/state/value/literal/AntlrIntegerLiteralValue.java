package cool.klass.model.converter.compiler.state.value.literal;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveType;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.meta.domain.value.literal.IntegerLiteralValue.IntegerLiteralValueBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public final class AntlrIntegerLiteralValue extends AntlrLiteralValue
{
    private final int value;

    public AntlrIntegerLiteralValue(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            int value)
    {
        super(elementContext, compilationUnit, inferred);
        this.value = value;
    }

    @Nonnull
    @Override
    public IntegerLiteralValueBuilder build()
    {
        return new IntegerLiteralValueBuilder(this.elementContext, this.inferred, this.value);
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
