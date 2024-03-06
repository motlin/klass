package cool.klass.model.converter.compiler.state.operator;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.annotation.CompilerAnnotationState;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.meta.domain.operator.StringOperatorImpl.StringOperatorBuilder;
import cool.klass.model.meta.grammar.KlassParser.StringOperatorContext;
import org.eclipse.collections.api.list.ListIterable;

public class AntlrStringOperator extends AntlrOperator
{
    private StringOperatorBuilder elementBuilder;

    public AntlrStringOperator(
            @Nonnull StringOperatorContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull String operatorText)
    {
        super(elementContext, compilationUnit, operatorText);
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
                (StringOperatorContext) this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
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
            CompilerAnnotationState compilerAnnotationHolder,
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
