package cool.klass.generator.graphql.runtime.wiring;

import java.util.Objects;

import javax.annotation.Nonnull;

import com.google.common.base.CaseFormat;
import com.google.common.base.Converter;
import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.NamedElement;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.AssociationEndSignature;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.property.EnumerationProperty;
import cool.klass.model.meta.domain.api.property.ParameterizedProperty;
import cool.klass.model.meta.domain.api.property.PrimitiveProperty;
import cool.klass.model.meta.domain.api.property.Property;
import cool.klass.model.meta.domain.api.property.PropertyVisitor;
import cool.klass.model.meta.domain.api.property.ReferenceProperty;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

public final class DataFetcherSourceCodePropertyVisitor
        implements PropertyVisitor
{
    private static final Converter<String, String> UPPER_TO_LOWER_CAMEL =
            CaseFormat.UPPER_CAMEL.converterTo(CaseFormat.LOWER_CAMEL);

    private static final ImmutableList<PrimitiveType> SPECIAL_PRIMITIVE_TYPES = Lists.immutable.with(
            PrimitiveType.LOCAL_DATE,
            PrimitiveType.INSTANT,
            PrimitiveType.TEMPORAL_INSTANT,
            PrimitiveType.TEMPORAL_RANGE);

    @Nonnull
    private final Klass    owningClass;
    @Nonnull
    private final Property property;

    private String dataFetcherSourceCode;

    public DataFetcherSourceCodePropertyVisitor(
            Klass owningClass,
            Property property)
    {
        this.owningClass = Objects.requireNonNull(owningClass);
        this.property    = Objects.requireNonNull(property);
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
        this.dataFetcherSourceCode = this.getAttributeDataFetcherSourceCode(primitiveProperty, primitiveType);
    }

    @Nonnull
    private String getAttributeDataFetcherSourceCode(
            @Nonnull DataTypeProperty property,
            @Nonnull PrimitiveType primitiveType)
    {
        if (property.isDerived())
        {
            return this.getDerivedPropertyDataFetcherSourceCode();
        }

        String primitiveName = SPECIAL_PRIMITIVE_TYPES.contains(primitiveType)
                ? primitiveType.getPrettyName()
                : "Attribute";

        return this.getAttributeDataFetcherSourceCode(primitiveName);
    }

    @Nonnull
    private String getAttributeDataFetcherSourceCode(String type)
    {
        ImmutableList<Klass> superClasses = this.getSuperClassChain();

        String superClassesString = superClasses
                .collect(NamedElement::getName)
                .collect(UPPER_TO_LOWER_CAMEL::convert)
                .collect(each -> "." + each + "SuperClass()")
                .makeString("");

        String result = String.format(
                "new Reladomo%sDataFetcher(%sFinder%s.%s())",
                type,
                this.owningClass.getName(),
                superClassesString,
                this.property.getName());
        return result;
    }

    @Nonnull
    private String getDerivedPropertyDataFetcherSourceCode()
    {
        return String.format(
                "PropertyDataFetcher.fetching(%s::%s)",
                this.owningClass.getName(),
                this.getMethodName());
    }

    @Override
    public void visitEnumerationProperty(EnumerationProperty enumerationProperty)
    {
        this.dataFetcherSourceCode = this.getAttributeDataFetcherSourceCode(enumerationProperty, PrimitiveType.STRING);
    }

    @Override
    public void visitAssociationEndSignature(AssociationEndSignature associationEndSignature)
    {
        this.dataFetcherSourceCode = this.getReferencePropertyDataFetcherSourceCode(associationEndSignature);
    }

    @Override
    public void visitAssociationEnd(AssociationEnd associationEnd)
    {
        this.dataFetcherSourceCode = this.getReferencePropertyDataFetcherSourceCode(associationEnd);
    }

    @Override
    public void visitParameterizedProperty(ParameterizedProperty parameterizedProperty)
    {
        this.dataFetcherSourceCode = this.getReferencePropertyDataFetcherSourceCode(parameterizedProperty);
    }

    private String getReferencePropertyDataFetcherSourceCode(ReferenceProperty referenceProperty)
    {
        return referenceProperty.getMultiplicity().isToMany()
                ? this.getDeepFetchPropertyDataFetcherSourceCode(referenceProperty)
                : this.getAttributeDataFetcherSourceCode("Relationship");
    }

    @Nonnull
    private String getDeepFetchPropertyDataFetcherSourceCode(ReferenceProperty referenceProperty)
    {
        ImmutableList<Klass> superClasses = this.getSuperClassChain();

        String superClassesString = superClasses
                .collect(NamedElement::getName)
                .collect(UPPER_TO_LOWER_CAMEL::convert)
                .collect(each -> "." + each + "SuperClass()")
                .makeString("");

        String result = String.format(
                "new ReladomoRelationshipDataFetcher<>(%sFinder%s.%s())",
                this.owningClass.getName(),
                superClassesString,
                referenceProperty.getName());
        return result;
    }

    public String getSourceCode()
    {
        return this.dataFetcherSourceCode;
    }

    private ImmutableList<Klass> getSuperClassChain()
    {
        if (this.property instanceof DataTypeProperty dataTypeProperty && dataTypeProperty.isKey())
        {
            return Lists.immutable.empty();
        }

        MutableList<Klass> result = Lists.mutable.empty();

        Classifier propertyClassifier = this.property.getOwningClassifier();
        Klass      eachClass     = this.owningClass;
        while (eachClass.getSuperClass().isPresent()
            && eachClass.getSuperClass().get().isSubTypeOf(propertyClassifier))
        {
            Klass superClass = eachClass.getSuperClass().get();
            result.add(superClass);
            eachClass = superClass;
        }
        return result.toImmutable();
    }
}
