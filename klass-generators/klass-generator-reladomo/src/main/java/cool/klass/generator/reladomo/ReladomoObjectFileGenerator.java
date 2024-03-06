package cool.klass.generator.reladomo;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import javax.annotation.Nonnull;

import com.google.common.base.CaseFormat;
import com.gs.fw.common.mithra.generator.metamodel.AsOfAttributePureType;
import com.gs.fw.common.mithra.generator.metamodel.AsOfAttributeType;
import com.gs.fw.common.mithra.generator.metamodel.AttributePureType;
import com.gs.fw.common.mithra.generator.metamodel.AttributeType;
import com.gs.fw.common.mithra.generator.metamodel.CardinalityType;
import com.gs.fw.common.mithra.generator.metamodel.MithraCommonObjectType;
import com.gs.fw.common.mithra.generator.metamodel.MithraGeneratorMarshaller;
import com.gs.fw.common.mithra.generator.metamodel.MithraObject;
import com.gs.fw.common.mithra.generator.metamodel.MithraPureObject;
import com.gs.fw.common.mithra.generator.metamodel.ObjectType;
import com.gs.fw.common.mithra.generator.metamodel.RelationshipType;
import com.gs.fw.common.mithra.generator.metamodel.TimezoneConversionType;
import cool.klass.model.meta.domain.DomainModel;
import cool.klass.model.meta.domain.Klass;
import cool.klass.model.meta.domain.Multiplicity;
import cool.klass.model.meta.domain.criteria.Criteria;
import cool.klass.model.meta.domain.criteria.CriteriaVisitor;
import cool.klass.model.meta.domain.order.OrderBy;
import cool.klass.model.meta.domain.order.OrderByDirection;
import cool.klass.model.meta.domain.order.OrderByMemberReferencePath;
import cool.klass.model.meta.domain.property.AssociationEnd;
import cool.klass.model.meta.domain.property.DataTypeProperty;
import cool.klass.model.meta.domain.property.EnumerationProperty;
import cool.klass.model.meta.domain.property.PrimitiveProperty;
import cool.klass.model.meta.domain.property.PrimitiveType;
import org.eclipse.collections.api.list.ImmutableList;

// TODO: ⬆ Generate default order-bys (or infer default order-bys) and gererate order-bys on association ends.
public class ReladomoObjectFileGenerator extends AbstractReladomoGenerator
{
    public ReladomoObjectFileGenerator(DomainModel domainModel)
    {
        super(domainModel);
    }

    public void writeObjectFiles(@Nonnull Path outputPath) throws IOException
    {
        for (Klass klass : this.domainModel.getKlasses())
        {
            this.writeObjectFile(outputPath, klass);
        }
    }

    private void writeObjectFile(Path outputPath, @Nonnull Klass klass) throws IOException
    {
        MithraGeneratorMarshaller mithraGeneratorMarshaller = new MithraGeneratorMarshaller();
        mithraGeneratorMarshaller.setIndent(true);
        StringBuilder stringBuilder = new StringBuilder();

        this.convertAndMarshall(klass, mithraGeneratorMarshaller, stringBuilder);

        String xmlString = this.sanitizeXmlString(stringBuilder);

        // TODO: Arrange Reladomo xmls in relative paths
        Path fullPath = outputPath.resolve(klass.getName() + ".xml");
        this.printStringToFile(fullPath, xmlString);
    }

    private void convertAndMarshall(
            @Nonnull Klass klass,
            MithraGeneratorMarshaller mithraGeneratorMarshaller,
            StringBuilder stringBuilder) throws IOException
    {
        if (klass.isTransient())
        {
            MithraPureObject mithraPureObject = this.convertToMithraPureObject(klass);
            mithraGeneratorMarshaller.marshall(stringBuilder, mithraPureObject);
        }
        else
        {
            MithraObject mithraObject = this.convertToMithraObject(klass);
            mithraGeneratorMarshaller.marshall(stringBuilder, mithraObject);
        }
    }

    @Nonnull
    private MithraPureObject convertToMithraPureObject(Klass klass)
    {
        MithraPureObject mithraPureObject = new MithraPureObject();

        this.convertCommonObject(klass, mithraPureObject);

        ImmutableList<AsOfAttributePureType> asOfAttributeTypes = klass.getDataTypeProperties()
                .select(DataTypeProperty::isTemporalRange)
                .collect(this::convertToAsOfAttributePureType);

        ImmutableList<AttributePureType> attributeTypes = klass.getDataTypeProperties()
                .reject(DataTypeProperty::isTemporal)
                .collect(this::convertToAttributePureType);

        // TODO: Test that private properties are not included in Projections
        // TODO: Add foreign keys

        mithraPureObject.setAsOfAttributes(asOfAttributeTypes.castToList());
        mithraPureObject.setAttributes(attributeTypes.castToList());

        mithraPureObject.setRelationships(this.convertRelationships(klass.getAssociationEnds()));

        return mithraPureObject;
    }

    @Nonnull
    private MithraObject convertToMithraObject(Klass klass)
    {
        MithraObject mithraObject = new MithraObject();
        this.convertCommonObject(klass, mithraObject);
        mithraObject.setDefaultTable(CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, klass.getName()));

        ImmutableList<AsOfAttributeType> asOfAttributeTypes = klass.getDataTypeProperties()
                .select(DataTypeProperty::isTemporalRange)
                .collect(this::convertToAsOfAttributeType);

        ImmutableList<AttributeType> attributeTypes = klass.getDataTypeProperties()
                .reject(DataTypeProperty::isTemporal)
                .collect(this::convertToAttributeType);

        // TODO: Test that private properties are not included in Projections
        // TODO: Add foreign keys

        mithraObject.setAsOfAttributes(asOfAttributeTypes.castToList());
        mithraObject.setAttributes(attributeTypes.castToList());

        mithraObject.setRelationships(this.convertRelationships(klass.getAssociationEnds()));

        return mithraObject;
    }

    private void convertCommonObject(Klass klass, MithraCommonObjectType mithraCommonObject)
    {
        ObjectType objectType = new ObjectType();
        objectType.with("transactional", mithraCommonObject);
        mithraCommonObject.setObjectType(objectType);

        mithraCommonObject.setPackageName(klass.getPackageName());
        mithraCommonObject.setClassName(klass.getName());
        mithraCommonObject.setInitializePrimitivesToNull(true);
    }

    private List<RelationshipType> convertRelationships(ImmutableList<AssociationEnd> associationEnds)
    {
        return associationEnds
                .select(associationEnd ->
                        associationEnd == associationEnd.getOwningAssociation().getTargetAssociationEnd())
                .collect(this::convertRelationship)
                .castToList();
    }

    @Nonnull
    private RelationshipType convertRelationship(AssociationEnd associationEnd)
    {
        AssociationEnd   opposite         = associationEnd.getOpposite();
        RelationshipType relationshipType = new RelationshipType();
        relationshipType.setName(associationEnd.getName());
        relationshipType.setReverseRelationshipName(opposite.getName());
        relationshipType.setCardinality(this.getCardinality(associationEnd, opposite));
        relationshipType.setRelatedIsDependent(associationEnd.isOwned());
        relationshipType.setRelatedObject(associationEnd.getType().getName());
        // TODO: Reladomo Order-By generation
        relationshipType.setOrderBy(associationEnd.getOrderBy().map(this::convertOrderBy).orElse(null));
        relationshipType._setValue(this.getRelationshipString(associationEnd.getOwningAssociation().getCriteria()));
        return relationshipType;
    }

    private CardinalityType getCardinality(
            AssociationEnd associationEnd,
            AssociationEnd opposite)
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

    private String getRelationshipString(Criteria criteria)
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

    private String convertOrderBy(OrderBy orderBy)
    {
        return orderBy.getOrderByMemberReferencePaths()
                .select(this::isConvertibleToOrderBy)
                .collect(this::convertOrderByMemberReferencePath)
                .makeString();
    }

    private boolean isConvertibleToOrderBy(OrderByMemberReferencePath each)
    {
        // Reladomo only supports simple property orderBys, like (title asc), not paths like (question.title asc)
        return each.getThisMemberReferencePath().getAssociationEnds().isEmpty();
    }

    private String convertOrderByMemberReferencePath(OrderByMemberReferencePath orderByMemberReferencePath)
    {
        String propertyName           = orderByMemberReferencePath.getThisMemberReferencePath().getProperty().getName();
        String orderByDirectionString = this.getOrderByDirectionString(orderByMemberReferencePath);
        return String.format("%s %s", propertyName, orderByDirectionString);
    }

    private String getOrderByDirectionString(OrderByMemberReferencePath orderByMemberReferencePath)
    {
        OrderByDirection orderByDirection = orderByMemberReferencePath.getOrderByDirectionDeclaration().getOrderByDirection();
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
    private AsOfAttributeType convertToAsOfAttributeType(DataTypeProperty<?> dataTypeProperty)
    {
        AsOfAttributeType asOfAttributeType = new AsOfAttributeType();
        this.convertToAsOfAttributeType(dataTypeProperty, asOfAttributeType);
        return asOfAttributeType;
    }

    private void convertToAsOfAttributeType(
            DataTypeProperty<?> dataTypeProperty,
            AsOfAttributePureType asOfAttributeType)
    {
        String propertyName = dataTypeProperty.getName();
        // TODO: Use actual temporal properties
        String fromName       = propertyName + "From";
        String toName         = propertyName + "To";
        String fromColumnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fromName);
        String toColumnName   = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, toName);

        asOfAttributeType.setName(propertyName);
        asOfAttributeType.setFromColumnName(fromColumnName);
        asOfAttributeType.setToColumnName(toColumnName);
        asOfAttributeType.setToIsInclusive(false);
        asOfAttributeType.setInfinityDate("[com.gs.fw.common.mithra.util.DefaultInfinityTimestamp.getDefaultInfinity()]");
        asOfAttributeType.setInfinityIsNull(false);
        // TODO: futureExpiringRowsExist is a de-optimization that allows for future times, and also makes the end dates more understandable. Add a better explanation and allow this to be customizable.
        asOfAttributeType.setFutureExpiringRowsExist(true);
        asOfAttributeType.setFinalGetter(true);

        TimezoneConversionType timezoneConversion = new TimezoneConversionType();
        timezoneConversion.with("convert-to-utc", asOfAttributeType);
        asOfAttributeType.setTimezoneConversion(timezoneConversion);

        if (propertyName.equals("valid"))
        {
            asOfAttributeType.setIsProcessingDate(false);
        }
        else if (propertyName.equals("system"))
        {
            asOfAttributeType.setDefaultIfNotSpecified(
                    "[com.gs.fw.common.mithra.util.DefaultInfinityTimestamp.getDefaultInfinity()]");
            asOfAttributeType.setIsProcessingDate(true);
        }
        else
        {
            throw new AssertionError(propertyName);
        }
    }

    @Nonnull
    private AsOfAttributePureType convertToAsOfAttributePureType(DataTypeProperty<?> dataTypeProperty)
    {
        AsOfAttributePureType asOfAttributeType = new AsOfAttributePureType();
        this.convertToAsOfAttributeType(dataTypeProperty, asOfAttributeType);
        return asOfAttributeType;
    }

    @Nonnull
    private AttributeType convertToAttributeType(DataTypeProperty<?> dataTypeProperty)
    {
        AttributeType attributeType = new AttributeType();
        this.convertToAttributeType(dataTypeProperty, attributeType);
        return attributeType;
    }

    private void convertToAttributeType(DataTypeProperty<?> dataTypeProperty, AttributePureType attributeType)
    {
        String propertyName = dataTypeProperty.getName();
        attributeType.setName(propertyName);
        attributeType.setColumnName(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, propertyName));
        attributeType.setPrimaryKey(dataTypeProperty.isKey());
        attributeType.setNullable(dataTypeProperty.isOptional());

        this.handleType(attributeType, dataTypeProperty);
    }

    private void handleType(@Nonnull AttributePureType attributeType, DataTypeProperty<?> dataTypeProperty)
    {
        if (dataTypeProperty instanceof EnumerationProperty)
        {
            attributeType.setJavaType("String");
            attributeType.setTrim(false);
        }

        if (dataTypeProperty instanceof PrimitiveProperty)
        {
            PrimitiveProperty primitiveProperty = (PrimitiveProperty) dataTypeProperty;
            PrimitiveType     primitiveType     = primitiveProperty.getType();
            primitiveType.visit(new AttributeTypeVisitor(attributeType, primitiveProperty));
        }
    }

    @Nonnull
    private AttributePureType convertToAttributePureType(DataTypeProperty<?> dataTypeProperty)
    {
        AttributePureType attributeType = new AttributePureType();
        this.convertToAttributeType(dataTypeProperty, attributeType);
        return attributeType;
    }
}
