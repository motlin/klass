package cool.klass.generator.reladomo.interfacefile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.base.CaseFormat;
import com.gs.fw.common.mithra.generator.metamodel.AsOfAttributeInterfaceType;
import com.gs.fw.common.mithra.generator.metamodel.AttributeInterfaceType;
import com.gs.fw.common.mithra.generator.metamodel.CardinalityType;
import com.gs.fw.common.mithra.generator.metamodel.MithraGeneratorMarshaller;
import com.gs.fw.common.mithra.generator.metamodel.MithraInterface;
import com.gs.fw.common.mithra.generator.metamodel.RelationshipInterfaceType;
import com.gs.fw.common.mithra.generator.metamodel.TimezoneConversionType;
import cool.klass.generator.reladomo.AbstractReladomoGenerator;
import cool.klass.generator.reladomo.CriteriaToRelationshipVisitor;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.Interface;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.NamedElement;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.domain.api.criteria.Criteria;
import cool.klass.model.meta.domain.api.criteria.CriteriaVisitor;
import cool.klass.model.meta.domain.api.order.OrderBy;
import cool.klass.model.meta.domain.api.order.OrderByDirection;
import cool.klass.model.meta.domain.api.order.OrderByMemberReferencePath;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.property.EnumerationProperty;
import cool.klass.model.meta.domain.api.property.PrimitiveProperty;
import org.eclipse.collections.api.list.ImmutableList;

public class ReladomoInterfaceFileGenerator extends AbstractReladomoGenerator
{
    public ReladomoInterfaceFileGenerator(@Nonnull DomainModel domainModel)
    {
        super(domainModel);
    }

    public void writeObjectFiles(@Nonnull Path outputPath) throws IOException
    {
        for (Interface anInterface : this.domainModel.getInterfaces())
        {
            this.writeObjectFile(outputPath, anInterface);
        }
    }

    private void writeObjectFile(@Nonnull Path outputPath, @Nonnull Interface anInterface) throws IOException
    {
        MithraGeneratorMarshaller mithraGeneratorMarshaller = new MithraGeneratorMarshaller();
        mithraGeneratorMarshaller.setIndent(true);
        StringBuilder stringBuilder = new StringBuilder();

        this.convertAndMarshall(anInterface, mithraGeneratorMarshaller, stringBuilder);

        String xmlString = this.sanitizeXmlString(stringBuilder);

        // TODO: Arrange Reladomo xmls in relative paths
        Path fullPath = outputPath.resolve(anInterface.getName() + ".xml");
        this.printStringToFile(fullPath, xmlString);
    }

    private void convertAndMarshall(
            @Nonnull Interface anInterface,
            @Nonnull MithraGeneratorMarshaller mithraGeneratorMarshaller,
            StringBuilder stringBuilder) throws IOException
    {
        MithraInterface mithraInterface = this.convertToMithraInterface(anInterface);
        mithraGeneratorMarshaller.marshall(stringBuilder, mithraInterface);
    }

    @Nonnull
    private MithraInterface convertToMithraInterface(@Nonnull Interface anInterface)
    {
        MithraInterface mithraInterface = new MithraInterface();
        mithraInterface.setPackageName(anInterface.getPackageName());
        mithraInterface.setClassName(anInterface.getName());

        ImmutableList<String> superInterfaceNames = anInterface.getInterfaces().collect(NamedElement::getName);
        mithraInterface.setSuperInterfaces(superInterfaceNames.castToList());

        ImmutableList<AsOfAttributeInterfaceType> asOfAttributeTypes = anInterface.getDataTypeProperties()
                .select(DataTypeProperty::isTemporalRange)
                .collect(this::convertToAsOfAttributeType);

        ImmutableList<AttributeInterfaceType> attributeTypes = anInterface.getDataTypeProperties()
                .reject(DataTypeProperty::isTemporal)
                .collect(this::convertToAttributeType);

        // TODO: Test that private properties are not included in Projections
        // TODO: Add foreign keys

        mithraInterface.setAsOfAttributes(asOfAttributeTypes.castToList());
        mithraInterface.setAttributes(attributeTypes.castToList());

        // TODO: Relationships
        // mithraInterface.setRelationships(this.convertRelationships(anInterface.getAssociationEnds()));

        return mithraInterface;
    }

    private List<RelationshipInterfaceType> convertRelationships(@Nonnull ImmutableList<AssociationEnd> associationEnds)
    {
        return associationEnds
                .select(associationEnd ->
                        associationEnd == associationEnd.getOwningAssociation().getTargetAssociationEnd())
                .collect(this::convertRelationship)
                .castToList();
    }

    @Nonnull
    private RelationshipInterfaceType convertRelationship(@Nonnull AssociationEnd associationEnd)
    {
        AssociationEnd            opposite         = associationEnd.getOpposite();
        RelationshipInterfaceType relationshipType = new RelationshipInterfaceType();
        relationshipType.setName(associationEnd.getName());
        relationshipType.setCardinality(this.getCardinality(associationEnd, opposite));
        relationshipType.setRelatedObject(associationEnd.getType().getName());
        relationshipType._setValue(this.getRelationshipString(associationEnd.getOwningAssociation().getCriteria()));
        return relationshipType;
    }

    private CardinalityType getCardinality(
            @Nonnull AssociationEnd associationEnd,
            @Nonnull AssociationEnd opposite)
    {
        Multiplicity multiplicity         = associationEnd.getMultiplicity();
        Multiplicity oppositeMultiplicity = opposite.getMultiplicity();

        boolean fromOne  = oppositeMultiplicity.isToOne();
        boolean toOne    = multiplicity.isToOne();
        boolean fromMany = oppositeMultiplicity.isToMany();
        boolean toMany   = multiplicity.isToMany();

        if (fromOne && toOne)
        {
            return this.getCardinalityType("one-to-one");
        }
        if (fromOne && toMany)
        {
            return this.getCardinalityType("one-to-many");
        }
        if (fromMany && toOne)
        {
            return this.getCardinalityType("many-to-one");
        }
        if (fromMany && toMany)
        {
            return this.getCardinalityType("many-to-many");
        }
        throw new AssertionError();
    }

    @Nonnull
    private String getRelationshipString(@Nonnull Criteria criteria)
    {
        StringBuilder   stringBuilder = new StringBuilder();
        CriteriaVisitor visitor       = new CriteriaToRelationshipVisitor(stringBuilder);
        criteria.visit(visitor);
        return stringBuilder.toString();
    }

    private CardinalityType getCardinalityType(String attributeValue)
    {
        CardinalityType cardinalityType = new CardinalityType();
        return cardinalityType.with(attributeValue, cardinalityType);
    }

    private String convertOrderBy(@Nonnull OrderBy orderBy)
    {
        return orderBy.getOrderByMemberReferencePaths()
                .select(this::isConvertibleToOrderBy)
                .collect(this::convertOrderByMemberReferencePath)
                .makeString();
    }

    private boolean isConvertibleToOrderBy(@Nonnull OrderByMemberReferencePath each)
    {
        // Reladomo only supports simple property orderBys, like (title asc), not paths like (question.title asc)
        return each.getThisMemberReferencePath().getAssociationEnds().isEmpty();
    }

    private String convertOrderByMemberReferencePath(@Nonnull OrderByMemberReferencePath orderByMemberReferencePath)
    {
        String propertyName           = orderByMemberReferencePath.getThisMemberReferencePath().getProperty().getName();
        String orderByDirectionString = this.getOrderByDirectionString(orderByMemberReferencePath);
        return String.format("%s %s", propertyName, orderByDirectionString);
    }

    @Nonnull
    private String getOrderByDirectionString(@Nonnull OrderByMemberReferencePath orderByMemberReferencePath)
    {
        OrderByDirection orderByDirection =
                orderByMemberReferencePath.getOrderByDirectionDeclaration().getOrderByDirection();
        switch (orderByDirection)
        {
            case ASCENDING:
                return "asc";
            case DESCENDING:
                return "desc";
            default:
                throw new AssertionError();
        }
    }

    @Nonnull
    private AsOfAttributeInterfaceType convertToAsOfAttributeType(@Nonnull DataTypeProperty dataTypeProperty)
    {
        AsOfAttributeInterfaceType asOfAttributeType = new AsOfAttributeInterfaceType();
        this.convertToAsOfAttributeType(dataTypeProperty, asOfAttributeType);
        return asOfAttributeType;
    }

    private void convertToAsOfAttributeType(
            @Nonnull DataTypeProperty dataTypeProperty,
            @Nonnull AsOfAttributeInterfaceType asOfAttributeType)
    {
        String propertyName = dataTypeProperty.getName();
        // TODO: Use actual temporal properties
        String fromName       = propertyName + "From";
        String toName         = propertyName + "To";
        String fromColumnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fromName);
        String toColumnName   = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, toName);

        asOfAttributeType.setName(propertyName);
        asOfAttributeType.setToIsInclusive(false);
        asOfAttributeType.setInfinityDate("[com.gs.fw.common.mithra.util.DefaultInfinityTimestamp.getDefaultInfinity()]");
        asOfAttributeType.setInfinityIsNull(false);
        // TODO: futureExpiringRowsExist is a de-optimization that allows for future times, and also makes the end dates more understandable. Add a better explanation and allow this to be customizable.

        TimezoneConversionType timezoneConversion = new TimezoneConversionType();
        timezoneConversion.with("convert-to-utc", asOfAttributeType);
        asOfAttributeType.setTimezoneConversion(timezoneConversion);

        if (propertyName.equals("valid"))
        {
            asOfAttributeType.setIsProcessingDate(false);
        }
        else if (propertyName.equals("system"))
        {
            asOfAttributeType.setIsProcessingDate(true);
        }
        else
        {
            throw new AssertionError(propertyName);
        }
    }

    @Nonnull
    private AttributeInterfaceType convertToAttributeType(@Nonnull DataTypeProperty dataTypeProperty)
    {
        AttributeInterfaceType attributeType = new AttributeInterfaceType();
        this.convertToAttributeType(dataTypeProperty, attributeType);
        return attributeType;
    }

    private void convertToAttributeType(
            @Nonnull DataTypeProperty dataTypeProperty,
            @Nonnull AttributeInterfaceType attributeType)
    {
        String propertyName = dataTypeProperty.getName();
        attributeType.setName(propertyName);
        this.handleType(attributeType, dataTypeProperty);
    }

    private void handleType(@Nonnull AttributeInterfaceType attributeType, DataTypeProperty dataTypeProperty)
    {
        if (dataTypeProperty instanceof EnumerationProperty)
        {
            attributeType.setJavaType("String");
        }

        if (dataTypeProperty instanceof PrimitiveProperty)
        {
            PrimitiveProperty primitiveProperty = (PrimitiveProperty) dataTypeProperty;
            PrimitiveType     primitiveType     = primitiveProperty.getType();
            primitiveType.visit(new AttributeInterfaceTypeVisitor(attributeType));
        }
    }
}
