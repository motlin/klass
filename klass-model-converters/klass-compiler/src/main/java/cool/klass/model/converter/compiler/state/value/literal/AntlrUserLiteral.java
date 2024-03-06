package cool.klass.model.converter.compiler.state.value.literal;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveType;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.meta.domain.value.literal.UserLiteralImpl.UserLiteralBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrUserLiteral extends AbstractAntlrLiteralValue
{
    private UserLiteralBuilder elementBuilder;

    public AntlrUserLiteral(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            IAntlrElement expressionValueOwner)
    {
        super(elementContext, compilationUnit, inferred, expressionValueOwner);
    }

    @Override
    public void reportErrors(CompilerErrorState compilerErrorHolder)
    {
    }

    @Nonnull
    @Override
    public UserLiteralBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new UserLiteralBuilder(this.elementContext, this.inferred);
        return this.elementBuilder;
    }

    @Nonnull
    @Override
    public UserLiteralBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }

    @Nonnull
    @Override
    public ImmutableList<AntlrType> getPossibleTypes()
    {
        return Lists.immutable.with(AntlrPrimitiveType.STRING);
    }
}
