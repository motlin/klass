package cool.klass.model.converter.compiler.state.value;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameter;
import cool.klass.model.meta.domain.value.AbstractExpressionValue.AbstractExpressionValueBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.OrderedMap;

public abstract class AntlrExpressionValue extends AntlrElement
{
    private final IAntlrElement expressionValueOwner;

    protected AntlrExpressionValue(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull IAntlrElement expressionValueOwner)
    {
        super(elementContext, compilationUnit);
        this.expressionValueOwner = Objects.requireNonNull(expressionValueOwner);
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.expressionValueOwner);
    }

    @Nonnull
    public abstract AbstractExpressionValueBuilder<?> build();

    @Override
    @Nonnull
    public abstract AbstractExpressionValueBuilder<?> getElementBuilder();

    public abstract void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder);

    @Nonnull
    public abstract ImmutableList<AntlrType> getPossibleTypes();

    public void resolveServiceVariables(@Nonnull OrderedMap<String, AntlrParameter> formalParametersByName)
    {
        // Intentionally blank
    }
}
