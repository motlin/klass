package cool.klass.model.converter.compiler.state.value.literal;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveType;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.meta.domain.value.literal.StringLiteralValueImpl.StringLiteralValueBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public final class AntlrStringLiteralValue extends AntlrLiteralValue
{
    private final String value;

    public AntlrStringLiteralValue(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            String value)
    {
        super(elementContext, compilationUnit, inferred);
        this.value = value;
    }

    @Nonnull
    @Override
    public StringLiteralValueBuilder build()
    {
        return new StringLiteralValueBuilder(this.elementContext, this.inferred, this.value);
    }

    @Nonnull
    @Override
    public ImmutableList<AntlrType> getPossibleTypes()
    {
        return Lists.immutable.with(AntlrPrimitiveType.STRING);
    }
}
