package cool.klass.model.converter.compiler.state.criteria;

import java.util.Objects;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameter;
import cool.klass.model.converter.compiler.state.value.AntlrMemberReferencePath;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.domain.criteria.EdgePointCriteriaImpl.EdgePointCriteriaBuilder;
import cool.klass.model.meta.grammar.KlassParser.CriteriaEdgePointContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.ListIterable;
import org.eclipse.collections.api.map.OrderedMap;

public class EdgePointAntlrCriteria extends AntlrCriteria
{
    @Nullable
    private AntlrMemberReferencePath memberExpressionValue;

    public EdgePointAntlrCriteria(
            @Nonnull CriteriaEdgePointContext elementContext,
            @Nonnull CompilationUnit compilationUnit,
            boolean inferred,
            @Nonnull IAntlrElement criteriaOwner)
    {
        super(elementContext, compilationUnit, inferred, criteriaOwner);
    }

    @Nonnull
    @Override
    public CriteriaEdgePointContext getElementContext()
    {
        return (CriteriaEdgePointContext) super.getElementContext();
    }

    @Nonnull
    public AntlrMemberReferencePath getMemberExpressionValue()
    {
        return Objects.requireNonNull(this.memberExpressionValue);
    }

    public void setMemberExpressionValue(@Nullable AntlrMemberReferencePath memberExpressionValue)
    {
        if (this.memberExpressionValue != null)
        {
            throw new IllegalStateException();
        }
        this.memberExpressionValue = Objects.requireNonNull(memberExpressionValue);
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
        this.memberExpressionValue.reportErrors(compilerErrorHolder);
        ListIterable<AntlrType> possibleTypes = this.memberExpressionValue.getPossibleTypes();
        if (possibleTypes.anySatisfy(each -> each.getTypeGetter() == PrimitiveType.TEMPORAL_RANGE))
        {
            return;
        }

        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".reportErrors() not implemented yet");
    }

    @Override
    public void resolveServiceVariables(OrderedMap<String, AntlrParameter> formalParametersByName)
    {
        // Intentionally blank
    }

    @Override
    public void resolveTypes()
    {
        // Intentionally blank
    }
}
