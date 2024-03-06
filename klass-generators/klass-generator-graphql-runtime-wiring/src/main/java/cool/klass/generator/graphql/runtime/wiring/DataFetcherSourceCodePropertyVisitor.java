package cool.klass.generator.graphql.runtime.wiring;

import java.util.Objects;

import javax.annotation.Nonnull;

import com.google.common.base.CaseFormat;
import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.EnumerationProperty;
import cool.klass.model.meta.domain.api.property.ParameterizedProperty;
import cool.klass.model.meta.domain.api.property.PrimitiveProperty;
import cool.klass.model.meta.domain.api.property.Property;
import cool.klass.model.meta.domain.api.property.PropertyVisitor;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public final class DataFetcherSourceCodePropertyVisitor implements PropertyVisitor
{
    public static final ImmutableList<PrimitiveType> SPECIAL_PRIMITIVE_TYPES = Lists.immutable.with(
            PrimitiveType.LOCAL_DATE,
            PrimitiveType.INSTANT,
            PrimitiveType.TEMPORAL_INSTANT,
            PrimitiveType.TEMPORAL_RANGE);
    @Nonnull
    private final       Classifier                   owningClassifier;
    @Nonnull
    private final       Property                     property;

    private String dataFetcherSourceCode;

    public DataFetcherSourceCodePropertyVisitor(
            Classifier owningClassifier,
            Property property)
    {
        this.owningClassifier = Objects.requireNonNull(owningClassifier);
        this.property = Objects.requireNonNull(property);
    }

    @Nonnull
    private String getOwningClassifierName()
    {
        return this.owningClassifier.getName();
    }

    @Nonnull
    private String getMethodName()
    {
        return this.getMethodPrefix() + this.getMethodSuffix();
    }

    @Nonnull
    private String getMethodPrefix()
    {
        return this.property.getType() == PrimitiveType.BOOLEAN
                ? "is"
                : "get";
    }

    private String getMethodSuffix()
    {
        return CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, this.property.getName());
    }

    @Override
    public void visitPrimitiveProperty(@Nonnull PrimitiveProperty primitiveProperty)
    {
        PrimitiveType primitiveType = primitiveProperty.getType();
        this.dataFetcherSourceCode = this.getPrimitiveDataFetcherSourceCode(primitiveType);
    }

    @Nonnull
    private String getPrimitiveDataFetcherSourceCode(@Nonnull PrimitiveType primitiveType)
    {
        if (SPECIAL_PRIMITIVE_TYPES.contains(primitiveType))
        {
            return this.getCustomDataFetcherSourceCode(primitiveType.getPrettyName());
        }

        return this.getPropertyDataFetcherSourceCode();
    }

    @Nonnull
    private String getCustomDataFetcherSourceCode(String type)
    {
        return String.format(
                "new Reladomo%sDataFetcher(%sFinder.%s())",
                type,
                this.getOwningClassifierName(),
                this.property.getName());
    }

    @Override
    public void visitEnumerationProperty(EnumerationProperty enumerationProperty)
    {
        this.dataFetcherSourceCode = this.getPropertyDataFetcherSourceCode();
    }

    @Override
    public void visitAssociationEnd(AssociationEnd associationEnd)
    {
        this.dataFetcherSourceCode = this.getPropertyDataFetcherSourceCode();
    }

    @Override
    public void visitParameterizedProperty(ParameterizedProperty parameterizedProperty)
    {
        this.dataFetcherSourceCode = this.getPropertyDataFetcherSourceCode();
    }

    @Nonnull
    private String getPropertyDataFetcherSourceCode()
    {
        return String.format(
                "PropertyDataFetcher.fetching(%s::%s)",
                this.getOwningClassifierName(),
                this.getMethodName());
    }

    public String getSourceCode()
    {
        return this.dataFetcherSourceCode;
    }
}
