package cool.klass.model.converter.compiler.state.operator;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrPrimitiveType;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.meta.domain.operator.EqualityOperator.EqualityOperatorBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.ListIterable;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrEqualityOperator extends AntlrOperator
{
    public AntlrEqualityOperator(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            String operatorText)
    {
        super(elementContext, compilationUnit, inferred, operatorText);
    }

    @Nonnull
    @Override
    public EqualityOperatorBuilder build()
    {
        return new EqualityOperatorBuilder(this.elementContext, this.operatorText);
    }

    @Override
    public void checkTypes(
            @Nonnull CompilerErrorHolder compilerErrorHolder,
            @Nonnull ImmutableList<ParserRuleContext> parserRuleContexts,
            @Nonnull ListIterable<AntlrType> sourceTypes,
            @Nonnull ListIterable<AntlrType> targetTypes)
    {
        if (sourceTypes.isEmpty() || targetTypes.isEmpty())
        {
            return;
        }

        if (sourceTypes.equals(targetTypes))
        {
            return;
        }

        if (sourceTypes.equals(Lists.immutable.with(AntlrPrimitiveType.ID))
                && targetTypes.contains(AntlrPrimitiveType.LONG))
        {
            return;
        }

        if (targetTypes.equals(Lists.immutable.with(AntlrPrimitiveType.ID))
                && sourceTypes.contains(AntlrPrimitiveType.LONG))
        {
            return;
        }

        String message = String.format(
                "Incompatible types: '%s' and '%s'.",
                sourceTypes.getFirst(),
                targetTypes.getFirst());
        compilerErrorHolder.add(
                this.compilationUnit,
                message,
                this.elementContext,
                parserRuleContexts.toArray(new ParserRuleContext[]{}));
    }
}
