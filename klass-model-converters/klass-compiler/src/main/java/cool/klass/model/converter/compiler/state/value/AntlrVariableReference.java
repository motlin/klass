package cool.klass.model.converter.compiler.state.value;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrEnumeration;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.service.url.AntlrEnumerationUrlPathParameter;
import cool.klass.model.converter.compiler.state.service.url.AntlrPrimitiveUrlPathParameter;
import cool.klass.model.converter.compiler.state.service.url.AntlrUrlParameter;
import cool.klass.model.meta.domain.value.VariableReference.VariableReferenceBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public class AntlrVariableReference extends AntlrExpressionValue
{
    @Nonnull
    private final AntlrUrlParameter antlrUrlParameter;

    public AntlrVariableReference(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull AntlrUrlParameter antlrUrlParameter)
    {
        super(elementContext, compilationUnit, inferred);
        this.antlrUrlParameter = Objects.requireNonNull(antlrUrlParameter);
    }

    @Nonnull
    @Override
    public VariableReferenceBuilder build()
    {
        return new VariableReferenceBuilder(this.elementContext, this.antlrUrlParameter.getUrlParameterBuilder());
    }

    @Override
    public void reportErrors(
            @Nonnull CompilerErrorHolder compilerErrorHolder,
            @Nonnull ImmutableList<ParserRuleContext> parserRuleContexts)
    {
        if (this.antlrUrlParameter == AntlrEnumerationUrlPathParameter.NOT_FOUND)
        {
            String message = String.format("ERR_VAR_REF: Cannot find parameter '%s'.", this.elementContext.getText());
            compilerErrorHolder.add(
                    this.compilationUnit,
                    message,
                    this.elementContext,
                    parserRuleContexts.toArray(new ParserRuleContext[]{}));
            return;
        }
        if (this.antlrUrlParameter == AntlrPrimitiveUrlPathParameter.AMBIGUOUS
                || this.antlrUrlParameter == AntlrEnumerationUrlPathParameter.AMBIGUOUS)
        {
            throw new AssertionError();
        }
    }

    @Override
    public ImmutableList<AntlrType> getPossibleTypes()
    {
        AntlrType type = this.antlrUrlParameter.getType();
        if (type == AntlrEnumeration.NOT_FOUND)
        {
            return Lists.immutable.empty();
        }
        return Lists.immutable.with(type);
    }
}
