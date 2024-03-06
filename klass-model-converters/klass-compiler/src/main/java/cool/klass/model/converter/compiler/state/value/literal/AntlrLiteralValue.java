package cool.klass.model.converter.compiler.state.value.literal;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.meta.domain.value.literal.AbstractLiteralValue.AbstractLiteralValueBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public abstract class AntlrLiteralValue extends AbstractAntlrLiteralValue
{
    protected AntlrLiteralValue(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            IAntlrElement expressionValueOwner)
    {
        super(elementContext, compilationUnit, inferred, expressionValueOwner);
    }

    @Override
    @Nonnull
    public abstract AbstractLiteralValueBuilder<?> build();

    @Override
    public void reportErrors(
            CompilerErrorHolder compilerErrorHolder,
            ImmutableList<ParserRuleContext> parserRuleContexts)
    {
        // Deliberately empty
    }
}
