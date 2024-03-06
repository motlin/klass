package cool.klass.model.converter.compiler.state.value.literal;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.value.AntlrExpressionValue;
import cool.klass.model.meta.domain.value.literal.LiteralListValue.LiteralListValueBuilder;
import cool.klass.model.meta.domain.value.literal.LiteralValue.LiteralValueBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public class AntlrLiteralListValue extends AbstractAntlrLiteralValue
{
    @Nonnull
    private final ImmutableList<AntlrLiteralValue> literalStates;
    private final ImmutableList<AntlrType>         possibleTypes;

    public AntlrLiteralListValue(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull ImmutableList<AntlrLiteralValue> literalStates)
    {
        super(elementContext, compilationUnit, inferred);
        this.literalStates = Objects.requireNonNull(literalStates);

        this.possibleTypes = this.literalStates
                .flatCollect(AntlrExpressionValue::getPossibleTypes)
                .toBag()
                .selectByOccurrences(occurrences -> occurrences == literalStates.size())
                .toList()
                .distinct()
                .toImmutable();
    }

    @Nonnull
    @Override
    public LiteralListValueBuilder build()
    {
        ImmutableList<LiteralValueBuilder> literalValueBuilders = this.literalStates.collect(AntlrLiteralValue::build);
        return new LiteralListValueBuilder(
                this.elementContext,
                literalValueBuilders,
                this.getInferredType().getTypeBuilder());
    }

    @Override
    public void reportErrors(
            @Nonnull CompilerErrorHolder compilerErrorHolder,
            @Nonnull ImmutableList<ParserRuleContext> parserRuleContexts)
    {
        if (this.possibleTypes.isEmpty())
        {
            // TODO: Cover this with a test

            compilerErrorHolder.add(
                    "Literal list with heterogeneous values.",
                    this.elementContext,
                    parserRuleContexts.toArray(new ParserRuleContext[]{}));
        }
    }

    @Nonnull
    @Override
    public ImmutableList<AntlrType> getPossibleTypes()
    {
        return this.possibleTypes;
    }
}
