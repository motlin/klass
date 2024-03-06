package cool.klass.model.converter.compiler.state.operator;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.annotation.CompilerAnnotationHolder;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.meta.domain.operator.EqualityOperatorImpl.EqualityOperatorBuilder;
import cool.klass.model.meta.grammar.KlassParser.EqualityOperatorContext;
import org.eclipse.collections.api.list.ListIterable;

public class AntlrEqualityOperator
        extends AntlrOperator
{
    private EqualityOperatorBuilder elementBuilder;

    public AntlrEqualityOperator(
            @Nonnull EqualityOperatorContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull String operatorText)
    {
        super(elementContext, compilationUnit, operatorText);
    }

    @Nonnull
    @Override
    public EqualityOperatorBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new EqualityOperatorBuilder(
                (EqualityOperatorContext) this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.operatorText);
        return this.elementBuilder;
    }

    @Nonnull
    @Override
    public EqualityOperatorBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }

    @Override
    public void checkTypes(
            @Nonnull CompilerAnnotationHolder compilerAnnotationHolder,
            @Nonnull ListIterable<AntlrType> sourceTypes,
            @Nonnull ListIterable<AntlrType> targetTypes)
    {
        if (sourceTypes.isEmpty() || targetTypes.isEmpty())
        {
            return;
        }

        // TODO: If two string types have different maxLengths, warn or error

        if (sourceTypes.equals(targetTypes))
        {
            return;
        }

        if (sourceTypes.size() == 1 && targetTypes.contains(sourceTypes.getOnly()))
        {
            return;
        }

        String message = String.format(
                "Incompatible types: '%s' and '%s'.",
                sourceTypes.getFirst(),
                targetTypes.getFirst());
        compilerAnnotationHolder.add(
                "ERR_OPR_EQL",
                message,
                this,
                this.owningOperatorAntlrCriteria.getElementContext());
    }
}
