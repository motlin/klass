package cool.klass.model.converter.compiler.state.value.literal;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.value.AntlrExpressionValue;
import cool.klass.model.meta.domain.value.literal.AbstractLiteralValue.AbstractLiteralValueBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public abstract class AbstractAntlrLiteralValue extends AntlrExpressionValue
{
    private AntlrType inferredType;

    protected AbstractAntlrLiteralValue(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            Optional<AntlrElement> macroElement,
            IAntlrElement expressionValueOwner)
    {
        super(elementContext, compilationUnit, macroElement, expressionValueOwner);
    }

    @Override
    @Nonnull
    public abstract AbstractLiteralValueBuilder<?> build();

    @Override
    @Nonnull
    public abstract AbstractLiteralValueBuilder<?> getElementBuilder();

    @Nonnull
    @Override
    public abstract ImmutableList<AntlrType> getPossibleTypes();

    public AntlrType getInferredType()
    {
        return Objects.requireNonNull(this.inferredType);
    }

    public void setInferredType(AntlrType inferredType)
    {
        // TODO: set inferred type
        this.inferredType = Objects.requireNonNull(inferredType);
    }
}
