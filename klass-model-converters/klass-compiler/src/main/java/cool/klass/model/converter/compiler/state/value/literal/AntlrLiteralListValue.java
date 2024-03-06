package cool.klass.model.converter.compiler.state.value.literal;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.annotation.CompilerAnnotationState;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.value.AntlrExpressionValue;
import cool.klass.model.converter.compiler.state.value.AntlrExpressionValueVisitor;
import cool.klass.model.meta.domain.value.literal.AbstractLiteralValue.AbstractLiteralValueBuilder;
import cool.klass.model.meta.domain.value.literal.LiteralListValueImpl.LiteralListValueBuilder;
import cool.klass.model.meta.grammar.KlassParser.LiteralListContext;
import org.eclipse.collections.api.list.ImmutableList;

public class AntlrLiteralListValue
        extends AbstractAntlrLiteralValue
{
    private ImmutableList<AbstractAntlrLiteralValue> literalStates;
    private LiteralListValueBuilder                  elementBuilder;

    public AntlrLiteralListValue(
            @Nonnull LiteralListContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull IAntlrElement expressionValueOwner)
    {
        super(elementContext, compilationUnit, expressionValueOwner);
    }

    public void setLiteralStates(ImmutableList<AbstractAntlrLiteralValue> literalStates)
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
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new LiteralListValueBuilder(
                (LiteralListContext) this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.getInferredType().getTypeGetter());

        ImmutableList<AbstractLiteralValueBuilder<?>> literalValueBuilders = this.literalStates
                .<AbstractLiteralValueBuilder<?>>collect(AbstractAntlrLiteralValue::build)
                .toImmutable();
        this.elementBuilder.setLiteralValueBuilders(literalValueBuilders);

        return this.elementBuilder;
    }

    @Nonnull
    @Override
    public LiteralListValueBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }

    @Override
    public void reportErrors(@Nonnull CompilerAnnotationState compilerAnnotationHolder)
    {
        if (this.getPossibleTypes().isEmpty())
        {
            // TODO: Cover this with a test

            compilerAnnotationHolder.add("ERR_LIT_LST", "Literal list with heterogeneous values.", this);
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

    @Override
    public void visit(AntlrExpressionValueVisitor visitor)
    {
        visitor.visitLiteralList(this);
    }
}
