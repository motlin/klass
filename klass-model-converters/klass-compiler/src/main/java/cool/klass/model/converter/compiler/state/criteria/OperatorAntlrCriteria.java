package cool.klass.model.converter.compiler.state.criteria;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.operator.AntlrOperator;
import cool.klass.model.converter.compiler.state.service.CriteriaOwner;
import cool.klass.model.converter.compiler.state.service.url.AntlrUrlParameter;
import cool.klass.model.converter.compiler.state.value.AntlrExpressionValue;
import cool.klass.model.converter.compiler.state.value.literal.AbstractAntlrLiteralValue;
import cool.klass.model.meta.domain.criteria.OperatorCriteria.OperatorCriteriaBuilder;
import cool.klass.model.meta.grammar.KlassParser.CriteriaOperatorContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.ListIterable;
import org.eclipse.collections.api.map.OrderedMap;

public class OperatorAntlrCriteria extends AntlrCriteria
{
    @Nonnull
    private final AntlrOperator        operator;
    @Nonnull
    private final AntlrExpressionValue sourceValue;
    @Nonnull
    private final AntlrExpressionValue targetValue;

    public OperatorAntlrCriteria(
            @Nonnull CriteriaOperatorContext elementContext,
            @Nonnull CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull CriteriaOwner criteriaOwner,
            @Nonnull AntlrOperator operator,
            @Nonnull AntlrExpressionValue sourceValue,
            @Nonnull AntlrExpressionValue targetValue)
    {
        super(elementContext, compilationUnit, inferred, criteriaOwner);
        this.operator = Objects.requireNonNull(operator);
        this.sourceValue = Objects.requireNonNull(sourceValue);
        this.targetValue = Objects.requireNonNull(targetValue);
    }

    @Nonnull
    @Override
    public CriteriaOperatorContext getElementContext()
    {
        return (CriteriaOperatorContext) super.getElementContext();
    }

    @Nonnull
    @Override
    public OperatorCriteriaBuilder build()
    {
        return new OperatorCriteriaBuilder(
                this.elementContext,
                this.operator.build(),
                this.sourceValue.build(),
                this.targetValue.build());
    }

    @Override
    public void reportErrors(
            CompilerErrorHolder compilerErrorHolder,
            ImmutableList<ParserRuleContext> parserRuleContexts)
    {
        this.sourceValue.reportErrors(compilerErrorHolder, parserRuleContexts);
        this.targetValue.reportErrors(compilerErrorHolder, parserRuleContexts);
        ListIterable<AntlrType> sourceTypes = this.sourceValue.getPossibleTypes();
        ListIterable<AntlrType> targetTypes = this.targetValue.getPossibleTypes();
        this.operator.checkTypes(compilerErrorHolder, parserRuleContexts, sourceTypes, targetTypes);
    }

    @Override
    public void resolveServiceVariables(OrderedMap<String, AntlrUrlParameter> formalParametersByName)
    {
        this.sourceValue.resolveServiceVariables(formalParametersByName);
        this.targetValue.resolveServiceVariables(formalParametersByName);
    }

    @Override
    public void resolveTypes()
    {
        ImmutableList<AntlrType> sourcePossibleTypes = this.sourceValue.getPossibleTypes();
        ImmutableList<AntlrType> targetPossibleTypes = this.targetValue.getPossibleTypes();

        if (this.sourceValue instanceof AbstractAntlrLiteralValue)
        {
            if (targetPossibleTypes.size() != 1)
            {
                throw new AssertionError();
            }
            ((AbstractAntlrLiteralValue) this.sourceValue).setInferredType(targetPossibleTypes.getOnly());
        }
        if (this.targetValue instanceof AbstractAntlrLiteralValue)
        {
            if (sourcePossibleTypes.size() != 1)
            {
                throw new AssertionError();
            }
            ((AbstractAntlrLiteralValue) this.targetValue).setInferredType(sourcePossibleTypes.getOnly());
        }
    }
}
