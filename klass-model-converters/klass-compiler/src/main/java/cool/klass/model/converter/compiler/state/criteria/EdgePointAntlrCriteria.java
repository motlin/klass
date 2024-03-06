package cool.klass.model.converter.compiler.state.criteria;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameter;
import cool.klass.model.converter.compiler.state.value.AntlrMemberReferencePath;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.domain.criteria.EdgePointCriteriaImpl.EdgePointCriteriaBuilder;
import cool.klass.model.meta.grammar.KlassParser.CriteriaEdgePointContext;
import org.eclipse.collections.api.list.ListIterable;
import org.eclipse.collections.api.map.OrderedMap;

public class EdgePointAntlrCriteria extends AntlrCriteria
{
    @Nullable
    private AntlrMemberReferencePath memberExpressionValue;
    private EdgePointCriteriaBuilder elementBuilder;

    public EdgePointAntlrCriteria(
            @Nonnull CriteriaEdgePointContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull IAntlrElement criteriaOwner)
    {
        super(elementContext, compilationUnit, criteriaOwner);
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
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException();
        }
        this.elementBuilder = new EdgePointCriteriaBuilder(
                this.elementContext,
                this.getMacroElementBuilder(),
                this.memberExpressionValue.build());
        return this.elementBuilder;
    }

    @Nonnull
    @Override
    public EdgePointCriteriaBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }

    @Override
    public void reportErrors(CompilerErrorState compilerErrorHolder)
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
