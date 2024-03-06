package cool.klass.model.converter.compiler.state.value;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameter;
import cool.klass.model.meta.domain.value.VariableReferenceImpl.VariableReferenceBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.map.OrderedMap;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrVariableReference extends AntlrExpressionValue
{
    @Nonnull
    private final String variableName;

    @Nullable
    private AntlrParameter antlrParameter;

    public AntlrVariableReference(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull String variableName,
            IAntlrElement expressionValueOwner)
    {
        super(elementContext, compilationUnit, inferred, expressionValueOwner);
        this.variableName = Objects.requireNonNull(variableName);
    }

    @Nonnull
    @Override
    public VariableReferenceBuilder build()
    {
        return new VariableReferenceBuilder(
                this.elementContext,
                this.inferred,
                this.antlrParameter.getElementBuilder());
    }

    @Override
    public void reportErrors(
            @Nonnull CompilerErrorHolder compilerErrorHolder)
    {
        if (this.antlrParameter == AntlrParameter.NOT_FOUND)
        {
            String message = String.format("ERR_VAR_REF: Cannot find parameter '%s'.", this.elementContext.getText());
            compilerErrorHolder.add(message, this);
            return;
        }
        if (this.antlrParameter == AntlrParameter.AMBIGUOUS)
        {
            throw new AssertionError();
        }
    }

    @Override
    public ImmutableList<AntlrType> getPossibleTypes()
    {
        Objects.requireNonNull(this.antlrParameter);
        AntlrType type = this.antlrParameter.getType();
        if (type == AntlrEnumeration.NOT_FOUND)
        {
            return Lists.immutable.empty();
        }
        return Lists.immutable.with(type);
    }

    @Override
    public void resolveServiceVariables(@Nonnull OrderedMap<String, AntlrParameter> formalParametersByName)
    {
        this.antlrParameter = formalParametersByName.getIfAbsentValue(
                this.variableName,
                AntlrParameter.NOT_FOUND);
    }
}
