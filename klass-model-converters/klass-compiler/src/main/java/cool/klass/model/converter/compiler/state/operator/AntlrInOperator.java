package cool.klass.model.converter.compiler.state.operator;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.meta.domain.operator.InOperatorImpl.InOperatorBuilder;
import cool.klass.model.meta.grammar.KlassParser.CriteriaOperatorContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ListIterable;

public class AntlrInOperator extends AntlrOperator
{
    public AntlrInOperator(
            @Nonnull ParserRuleContext elementContext,
            CompilationUnit compilationUnit,
            boolean inferred,
            String operatorText)
    {
        super(elementContext, compilationUnit, inferred, operatorText);
    }

    @Nonnull
    @Override
    public InOperatorBuilder build()
    {
        return new InOperatorBuilder(this.elementContext, this.inferred, this.operatorText);
    }

    @Override
    public void checkTypes(
            @Nonnull CompilerErrorHolder compilerErrorHolder,
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
        compilerErrorHolder.add(message, this, criteriaOperatorContext);
    }
}
