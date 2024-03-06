package cool.klass.model.converter.compiler.state.criteria;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.service.AntlrCriteriaOwner;
import cool.klass.model.converter.compiler.state.service.url.AntlrUrlParameter;
import cool.klass.model.converter.compiler.state.value.AntlrMemberExpressionValue;
import cool.klass.model.meta.domain.criteria.EdgePointCriteria.EdgePointCriteriaBuilder;
import cool.klass.model.meta.domain.property.PrimitiveType;
import cool.klass.model.meta.grammar.KlassParser.CriteriaEdgePointContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.ListIterable;
import org.eclipse.collections.api.map.OrderedMap;

public class EdgePointAntlrCriteria extends AntlrCriteria
{
    @Nonnull
    private final AntlrMemberExpressionValue memberExpressionValue;

    public EdgePointAntlrCriteria(
            @Nonnull CriteriaEdgePointContext elementContext,
            @Nonnull CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull AntlrCriteriaOwner criteriaOwner,
            @Nonnull AntlrMemberExpressionValue memberExpressionValue)
    {
        super(elementContext, compilationUnit, inferred, criteriaOwner);
        this.memberExpressionValue = memberExpressionValue;
    }

    @Nonnull
    @Override
    public CriteriaEdgePointContext getElementContext()
    {
        return (CriteriaEdgePointContext) super.getElementContext();
    }

    @Nonnull
    public AntlrMemberExpressionValue getMemberExpressionValue()
    {
        return this.memberExpressionValue;
    }

    @Nonnull
    @Override
    public EdgePointCriteriaBuilder build()
    {
        return new EdgePointCriteriaBuilder(
                this.elementContext,
                this.inferred,
                this.memberExpressionValue.build());
    }

    @Override
    public void reportErrors(
            CompilerErrorHolder compilerErrorHolder,
            ImmutableList<ParserRuleContext> parserRuleContexts)
    {
        this.memberExpressionValue.reportErrors(compilerErrorHolder, parserRuleContexts);
        ListIterable<AntlrType> possibleTypes = this.memberExpressionValue.getPossibleTypes();
        if (possibleTypes.anySatisfy(each -> each.getTypeGetter() == PrimitiveType.TEMPORAL_RANGE))
        {
            return;
        }

        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".reportErrors() not implemented yet");
    }

    @Override
    public void resolveServiceVariables(OrderedMap<String, AntlrUrlParameter> formalParametersByName)
    {
        // Intentionally blank
    }

    @Override
    public void resolveTypes()
    {
        // Intentionally blank
    }
}
