package cool.klass.model.converter.compiler.state.value.literal;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.value.AntlrExpressionValue;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public abstract class AbstractAntlrLiteralValue extends AntlrExpressionValue
{
    private AntlrType inferredType;

    protected AbstractAntlrLiteralValue(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            IAntlrElement expressionValueOwner)
    {
        super(elementContext, compilationUnit, inferred, expressionValueOwner);
    }

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
