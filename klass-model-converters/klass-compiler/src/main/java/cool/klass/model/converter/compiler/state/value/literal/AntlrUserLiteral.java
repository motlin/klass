package cool.klass.model.converter.compiler.state.value.literal;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.annotation.CompilerAnnotationState;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveType;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.meta.domain.KlassImpl.KlassBuilder;
import cool.klass.model.meta.domain.value.literal.UserLiteralImpl.UserLiteralBuilder;
import cool.klass.model.meta.grammar.KlassParser.NativeLiteralContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrUserLiteral
        extends AbstractAntlrLiteralValue
{
    @Nonnull
    private final Optional<AntlrClass> userClassState;

    private UserLiteralBuilder elementBuilder;

    public AntlrUserLiteral(
            @Nonnull NativeLiteralContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull IAntlrElement expressionValueOwner,
            @Nonnull Optional<AntlrClass> userClassState)
    {
        super(elementContext, compilationUnit, expressionValueOwner);
        this.userClassState = Objects.requireNonNull(userClassState);
    }

    @Override
    public void reportErrors(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        if (this.userClassState.isPresent())
        {
            return;
        }

        String message = "'user' literal requires one 'user' class in the domain model.";
        compilerAnnotationHolder.add("ERR_USR_LIT", message, this);
    }

    @Nonnull
    @Override
    public UserLiteralBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }

        Optional<KlassBuilder> userElementBuilder = this.userClassState.map(AntlrClass::getElementBuilder);
        if (userElementBuilder.isEmpty())
        {
            throw new IllegalStateException();
        }

        this.elementBuilder = new UserLiteralBuilder(
                (NativeLiteralContext) this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                userElementBuilder.get());
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
