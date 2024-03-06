package cool.klass.model.converter.compiler.state.operator;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.meta.domain.operator.EqualityOperatorImpl.EqualityOperatorBuilder;
import cool.klass.model.meta.grammar.KlassParser.CriteriaOperatorContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ListIterable;

public class AntlrEqualityOperator extends AntlrOperator
{
    private EqualityOperatorBuilder elementBuilder;

    public AntlrEqualityOperator(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            Optional<AntlrElement> macroElement,
            String operatorText)
    {
        super(elementContext, compilationUnit, macroElement, operatorText);
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
                this.elementContext,
                this.macroElement.map(AntlrElement::getElementBuilder),
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
            @Nonnull CompilerErrorState compilerErrorHolder,
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
        // Cast is a deliberate assertion
        CriteriaOperatorContext criteriaOperatorContext = (CriteriaOperatorContext) this.elementContext.getParent().getParent();
        compilerErrorHolder.add("ERR_OPR_EQL", message, this, criteriaOperatorContext);
    }
}
