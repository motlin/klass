package cool.klass.model.converter.compiler.state.criteria;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.error.CompilerErrorState;
import cool.klass.model.converter.compiler.state.AntlrAssociation;
import cool.klass.model.converter.compiler.state.AntlrClass;
import cool.klass.model.converter.compiler.state.AntlrType;
import cool.klass.model.converter.compiler.state.IAntlrElement;
import cool.klass.model.converter.compiler.state.operator.AntlrOperator;
import cool.klass.model.converter.compiler.state.parameter.AntlrParameter;
import cool.klass.model.converter.compiler.state.property.AntlrAssociationEnd;
import cool.klass.model.converter.compiler.state.property.AntlrDataTypeProperty;
import cool.klass.model.converter.compiler.state.value.AntlrExpressionValue;
import cool.klass.model.converter.compiler.state.value.AntlrThisMemberReferencePath;
import cool.klass.model.converter.compiler.state.value.AntlrTypeMemberReferencePath;
import cool.klass.model.converter.compiler.state.value.literal.AbstractAntlrLiteralValue;
import cool.klass.model.meta.domain.criteria.OperatorCriteriaImpl.OperatorCriteriaBuilder;
import cool.klass.model.meta.grammar.KlassParser.CriteriaOperatorContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.ListIterable;
import org.eclipse.collections.api.map.OrderedMap;

public class OperatorAntlrCriteria extends AntlrCriteria
{
    @Nonnull
    private final AntlrOperator operator;

    private AntlrExpressionValue    sourceValue;
    private AntlrExpressionValue    targetValue;
    private OperatorCriteriaBuilder elementBuilder;

    public OperatorAntlrCriteria(
            @Nonnull CriteriaOperatorContext elementContext,
            @Nonnull Optional<CompilationUnit> compilationUnit,
            @Nonnull IAntlrElement criteriaOwner,
            @Nonnull AntlrOperator operator)
    {
        super(elementContext, compilationUnit, criteriaOwner);
        this.operator = Objects.requireNonNull(operator);
    }

    @Nonnull
    @Override
    public CriteriaOperatorContext getElementContext()
    {
        return (CriteriaOperatorContext) super.getElementContext();
    }

    @Nullable
    public AntlrExpressionValue getSourceValue()
    {
        return Objects.requireNonNull(this.sourceValue);
    }

    public void setSourceValue(@Nonnull AntlrExpressionValue sourceValue)
    {
        if (this.sourceValue != null)
        {
            throw new IllegalArgumentException(this.sourceValue.toString());
        }
        this.sourceValue = Objects.requireNonNull(sourceValue);
    }

    @Nullable
    public AntlrExpressionValue getTargetValue()
    {
        return Objects.requireNonNull(this.targetValue);
    }

    public void setTargetValue(@Nonnull AntlrExpressionValue targetValue)
    {
        if (this.targetValue != null)
        {
            throw new IllegalArgumentException(this.targetValue.toString());
        }
        this.targetValue = Objects.requireNonNull(targetValue);
    }

    @Nonnull
    @Override
    public OperatorCriteriaBuilder build()
    {
        if (this.elementBuilder != null)
        {
            throw new IllegalStateException(this.elementBuilder.toString());
        }
        // TODO: Refactor to build the parent before the children
        this.elementBuilder = new OperatorCriteriaBuilder(
                (CriteriaOperatorContext) this.elementContext,
                this.getMacroElementBuilder(),
                this.getSourceCodeBuilder(),
                this.operator.build(),
                this.sourceValue.build(),
                this.targetValue.build());
        return this.elementBuilder;
    }

    @Nonnull
    @Override
    public OperatorCriteriaBuilder getElementBuilder()
    {
        return Objects.requireNonNull(this.elementBuilder);
    }

    @Override
    public void reportErrors(@Nonnull CompilerErrorState compilerErrorHolder)
    {
        this.sourceValue.reportErrors(compilerErrorHolder);
        this.targetValue.reportErrors(compilerErrorHolder);
        ListIterable<AntlrType> sourceTypes = this.sourceValue.getPossibleTypes();
        ListIterable<AntlrType> targetTypes = this.targetValue.getPossibleTypes();
        this.operator.checkTypes(compilerErrorHolder, sourceTypes, targetTypes);
    }

    @Override
    public void resolveServiceVariables(@Nonnull OrderedMap<String, AntlrParameter> formalParametersByName)
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
                return;
            }
            ((AbstractAntlrLiteralValue) this.sourceValue).setInferredType(targetPossibleTypes.getOnly());
        }
        if (this.targetValue instanceof AbstractAntlrLiteralValue)
        {
            if (sourcePossibleTypes.size() != 1)
            {
                return;
            }
            ((AbstractAntlrLiteralValue) this.targetValue).setInferredType(sourcePossibleTypes.getOnly());
        }
    }

    @Override
    public void addForeignKeys()
    {
        AntlrAssociation association = this.getSurroundingElement(AntlrAssociation.class).get();

        AntlrAssociationEnd sourceEnd = association.getSourceEnd();
        AntlrAssociationEnd targetEnd = association.getTargetEnd();

        AntlrThisMemberReferencePath thisMemberReferencePathState = this.getThisMemberRefrencePathState();
        AntlrTypeMemberReferencePath typeMemberReferencePathState = this.getTypeMemberReferencePathState();

        AntlrDataTypeProperty<?> thisDataTypePropertyState = thisMemberReferencePathState.getDataTypePropertyState();
        AntlrDataTypeProperty<?> typeDataTypePropertyState = typeMemberReferencePathState.getDataTypePropertyState();

        if (sourceEnd.isToMany() && targetEnd.isToMany())
        {
            throw new AssertionError("TODO: Support many-to-many associations");
        }

        AntlrAssociationEnd endWithForeignKeys = this.getEndWithForeignKeys(
                association,
                thisDataTypePropertyState,
                typeDataTypePropertyState);

        if (endWithForeignKeys == null)
        {
            return;
        }

        AntlrClass typeWithForeignKeys = endWithForeignKeys.getOwningClassifierState();
        if (typeWithForeignKeys == AntlrClass.NOT_FOUND || typeWithForeignKeys == AntlrClass.AMBIGUOUS)
        {
            return;
        }

        if (!endWithForeignKeys.isToOne())
        {
            throw new AssertionError();
        }

        boolean isTargetEnd = endWithForeignKeys.isTargetEnd();

        AntlrDataTypeProperty<?> foreignKeyProperty = isTargetEnd
                ? thisDataTypePropertyState
                : typeDataTypePropertyState;
        AntlrDataTypeProperty<?> keyProperty        = isTargetEnd
                ? typeDataTypePropertyState
                : thisDataTypePropertyState;
        if (foreignKeyProperty.isKey() && !keyProperty.isKey())
        {
            // This can happen for non-key but unique properties
            // TODO: Implement unique properties, and assert that the property is EITHER key or unique
            // throw new AssertionError(foreignKeyProperty);
        }

        endWithForeignKeys.addForeignKeyPropertyMatchingProperty(
                foreignKeyProperty,
                keyProperty);
    }

    @Nullable
    private AntlrAssociationEnd getEndWithForeignKeys(
            @Nonnull AntlrAssociation association,
            @Nonnull AntlrDataTypeProperty<?> thisDataTypePropertyState,
            @Nonnull AntlrDataTypeProperty<?> typeDataTypePropertyState)
    {
        AntlrAssociationEnd sourceEnd = association.getSourceEnd();
        AntlrAssociationEnd targetEnd = association.getTargetEnd();

        if (targetEnd.isToMany())
        {
            return sourceEnd;
        }

        if (sourceEnd.isToMany())
        {
            return targetEnd;
        }

        if (sourceEnd.isToOneRequired() && targetEnd.isToOneOptional())
        {
            return sourceEnd;
        }

        if (targetEnd.isToOneRequired() && sourceEnd.isToOneOptional())
        {
            return targetEnd;
        }

        // TODO: These two conditions don't make sense here, because we're only considering one pair of joined properties among several &&-clauses
        if (thisDataTypePropertyState.isKey() && !typeDataTypePropertyState.isKey())
        {
            return sourceEnd;
        }

        // TODO: These two conditions don't make sense here, because we're only considering one pair of joined properties among several &&-clauses
        if (typeDataTypePropertyState.isKey() && !thisDataTypePropertyState.isKey())
        {
            return targetEnd;
        }

        // TODO: Delete these two special cases
        if (targetEnd.isOwned() && targetEnd.isVersion())
        {
            return sourceEnd;
        }

        // TODO: Delete these two special cases
        if (sourceEnd.isOwned() && sourceEnd.isVersion())
        {
            return targetEnd;
        }

        if (sourceEnd.isToOne() && targetEnd.isToOne())
        {
            if (sourceEnd.isOwned() && !targetEnd.isOwned())
            {
                return targetEnd;
            }
            else if (!sourceEnd.isOwned() && targetEnd.isOwned())
            {
                return sourceEnd;
            }
        }

        return null;
    }

    @Nonnull
    private AntlrThisMemberReferencePath getThisMemberRefrencePathState()
    {
        if (this.sourceValue instanceof AntlrThisMemberReferencePath)
        {
            return (AntlrThisMemberReferencePath) this.sourceValue;
        }

        if (this.targetValue instanceof AntlrThisMemberReferencePath)
        {
            return (AntlrThisMemberReferencePath) this.targetValue;
        }

        throw new AssertionError();
    }

    @Nonnull
    private AntlrTypeMemberReferencePath getTypeMemberReferencePathState()
    {
        if (this.sourceValue instanceof AntlrTypeMemberReferencePath)
        {
            return (AntlrTypeMemberReferencePath) this.sourceValue;
        }

        if (this.targetValue instanceof AntlrTypeMemberReferencePath)
        {
            return (AntlrTypeMemberReferencePath) this.targetValue;
        }

        throw new AssertionError();
    }
}
