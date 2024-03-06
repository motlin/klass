package cool.klass.model.converter.compiler.state.operator;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.meta.domain.operator.StringOperatorImpl.StringOperatorBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ListIterable;

public class AntlrStringOperator extends AntlrOperator
{
    private StringOperatorBuilder elementBuilder;

    public AntlrStringOperator(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            Optional<AntlrElement> macroElement,
            String operatorText)
    {
        super(elementContext, compilationUnit, macroElement, operatorText);
    }

    @Nonnull
    @Override
    public StringOperatorBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new StringOperatorBuilder(
                this.elementContext,
                this.macroElement.map(AntlrElement::getElementBuilder),
                this.operatorText);
        return this.elementBuilder;
    }

    @Nonnull
    @Override
    public StringOperatorBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }

    @Override
    public void checkTypes(
            CompilerErrorState compilerErrorHolder,
            @Nonnull ListIterable<AntlrType> sourceTypes,
            ListIterable<AntlrType> targetTypes)
    {
        if (sourceTypes.equals(targetTypes))
        {
            return;
        }

        throw new UnsupportedOperationException(this.getClass().getSimpleName() + ".checkTypes() not implemented yet");
    }
}
