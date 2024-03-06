package cool.klass.model.converter.compiler.state.value.literal;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.value.AntlrExpressionValue;
import cool.klass.model.meta.domain.value.literal.AbstractLiteralValue.AbstractLiteralValueBuilder;
import cool.klass.model.meta.domain.value.literal.LiteralListValueImpl.LiteralListValueBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public class AntlrLiteralListValue extends AbstractAntlrLiteralValue
{
    private ImmutableList<AntlrLiteralValue> literalStates;

    public AntlrLiteralListValue(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            IAntlrElement expressionValueOwner)
    {
        super(elementContext, compilationUnit, inferred, expressionValueOwner);
    }

    public void setLiteralStates(ImmutableList<AntlrLiteralValue> literalStates)
    {
        if (this.literalStates != null)
        {
            throw new IllegalStateException();
        }
        this.literalStates = Objects.requireNonNull(literalStates);
    }

    @Nonnull
    @Override
    public LiteralListValueBuilder build()
    {
        LiteralListValueBuilder literalListValueBuilder = new LiteralListValueBuilder(
                this.elementContext,
                this.inferred,
                this.getInferredType().getTypeGetter());

        ImmutableList<AbstractLiteralValueBuilder<?>> literalValueBuilders = this.literalStates
                .<AbstractLiteralValueBuilder<?>>collect(AntlrLiteralValue::build)
                .toImmutable();
        literalListValueBuilder.setLiteralValueBuilders(literalValueBuilders);

        return literalListValueBuilder;
    }

    @Override
    public void reportErrors(
            @Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        if (this.getPossibleTypes().isEmpty())
        {
            // TODO: Cover this with a test

            compilerErrorHolder.add("Literal list with heterogeneous values.", this.elementContext, this);
        }
    }

    @Nonnull
    @Override
    public ImmutableList<AntlrType> getPossibleTypes()
    {
        return this.literalStates
                .flatCollect(AntlrExpressionValue::getPossibleTypes)
                .toBag()
                .selectByOccurrences(occurrences -> occurrences == this.literalStates.size())
                .toList()
                .distinct()
                .toImmutable();
    }
}
