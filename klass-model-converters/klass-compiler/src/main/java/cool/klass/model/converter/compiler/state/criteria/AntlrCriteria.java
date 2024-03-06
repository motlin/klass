package cool.klass.model.converter.compiler.state.criteria;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrElement;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameter;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.service.AntlrServiceCriteria;
import cool.klass.model.meta.domain.criteria.AbstractCriteria.AbstractCriteriaBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.map.OrderedMap;

public abstract class AntlrCriteria extends AntlrElement
{
    @Nonnull
    private final IAntlrElement criteriaOwner;

    protected AntlrCriteria(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull IAntlrElement criteriaOwner)
    {
        super(elementContext, compilationUnit);
        this.criteriaOwner = Objects.requireNonNull(criteriaOwner);
    }

    @Nonnull
    @Override
    public Optional<IAntlrElement> getSurroundingElement()
    {
        return Optional.of(this.criteriaOwner);
    }

    @Override
    public boolean omitParentFromSurroundingElements()
    {
        return this.criteriaOwner instanceof AntlrServiceCriteria
                || this.criteriaOwner instanceof AntlrBinaryCriteria;
    }

    @Nonnull
    public abstract AbstractCriteriaBuilder<?> build();

    @Override
    public abstract AbstractCriteriaBuilder<?> getElementBuilder();

    public abstract void reportErrors(CompilerErrorState compilerErrorHolder);

    public abstract void resolveServiceVariables(OrderedMap<String, AntlrParameter> formalParametersByName);

    public abstract void resolveTypes();

    public void addForeignKeys(
            boolean foreignKeysOnThis,
            AntlrAssociationEnd endWithForeignKeys)
    {
        throw new UnsupportedOperationException(this.getClass().getSimpleName()
                + ".addForeignKeys() not implemented yet");
    }
}
