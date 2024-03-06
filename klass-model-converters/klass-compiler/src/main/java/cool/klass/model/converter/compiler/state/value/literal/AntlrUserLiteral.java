package cool.klass.model.converter.compiler.state.value.literal;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveType;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.meta.domain.value.literal.UserLiteralImpl.UserLiteralBuilder;
import cool.klass.model.meta.grammar.KlassParser.NativeLiteralContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrUserLiteral extends AbstractAntlrLiteralValue
{
    private UserLiteralBuilder elementBuilder;

    public AntlrUserLiteral(
            @Nonnull NativeLiteralContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull IAntlrElement expressionValueOwner)
    {
        super(elementContext, compilationUnit, expressionValueOwner);
    }

    @Override
    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
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
        this.elementBuilder = new UserLiteralBuilder(
                (NativeLiteralContext) this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder());
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
