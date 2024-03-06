package cool.klass.model.converter.compiler.state.value.literal;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.annotation.CompilerAnnotationState;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveType;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.meta.domain.value.literal.StringLiteralValueImpl.StringLiteralValueBuilder;
import cool.klass.model.meta.grammar.KlassParser.StringLiteralContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public final class AntlrStringLiteralValue extends AbstractAntlrLiteralValue
{
    private final String                    value;
    private       StringLiteralValueBuilder elementBuilder;

    public AntlrStringLiteralValue(
            @Nonnull StringLiteralContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull String value,
            @Nonnull IAntlrElement expressionValueOwner)
    {
        super(elementContext, compilationUnit, expressionValueOwner);
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public void reportErrors(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
    }

    @Nonnull
    @Override
    public StringLiteralValueBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new StringLiteralValueBuilder(
                (StringLiteralContext) this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.value);
        return this.elementBuilder;
    }

    @Nonnull
    @Override
    public StringLiteralValueBuilder getElementBuilder()
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
