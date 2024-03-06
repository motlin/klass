package cool.klass.model.converter.compiler.state.value;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.service.url.AntlrUrlParameter;
import cool.klass.model.meta.domain.value.AbstractExpressionValue.ExpressionValueBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.OrderedMap;

public abstract class AntlrExpressionValue extends AntlrElement
{
    protected AntlrExpressionValue(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred)
    {
        super(elementContext, compilationUnit, inferred);
    }

    @Nonnull
    public abstract ExpressionValueBuilder build();

    public abstract void reportErrors(
            CompilerErrorHolder compilerErrorHolder,
            ImmutableList<ParserRuleContext> parserRuleContexts);

    public abstract ImmutableList<AntlrType> getPossibleTypes();

    public void resolveServiceVariables(OrderedMap<String, AntlrUrlParameter> formalParametersByName)
    {
        // Intentionally blank
    }
}
