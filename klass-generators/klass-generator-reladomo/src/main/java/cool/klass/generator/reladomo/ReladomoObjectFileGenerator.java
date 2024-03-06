package cool.klass.generator.reladomo;

import java.io.IOException;
import java.nio.file.Path;

import com.google.common.base.CaseFormat;
import com.gs.fw.common.mithra.generator.metamodel.AsOfAttributeType;
import com.gs.fw.common.mithra.generator.metamodel.AttributeType;
import com.gs.fw.common.mithra.generator.metamodel.MithraGeneratorMarshaller;
import com.gs.fw.common.mithra.generator.metamodel.MithraObject;
import com.gs.fw.common.mithra.generator.metamodel.ObjectType;
import com.gs.fw.common.mithra.generator.metamodel.TimezoneConversionType;
import cool.klass.model.meta.domain.DataTypeProperty;
import cool.klass.model.meta.domain.DomainModel;
import cool.klass.model.meta.domain.EnumerationProperty;
import cool.klass.model.meta.domain.Klass;
import cool.klass.model.meta.domain.PrimitiveProperty;
import cool.klass.model.meta.domain.PrimitiveType;
import org.eclipse.collections.api.list.ImmutableList;

public class ReladomoObjectFileGenerator extends AbstractReladomoGenerator
{
    public ReladomoObjectFileGenerator(DomainModel domainModel)
    {
        super(domainModel);
    }

    public void writeObjectFiles(Path outputPath) throws IOException
    {
        for (Klass klass : this.domainModel.getKlasses())
        {
            this.writeObjectFile(outputPath, klass);
        }
    }

    private void writeObjectFile(Path outputPath, Klass klass) throws IOException
    {
        MithraGeneratorMarshaller mithraGeneratorMarshaller = new MithraGeneratorMarshaller();
        mithraGeneratorMarshaller.setIndent(true);

        MithraObject  mithraObject  = this.convertToMithraObject(klass);
        StringBuilder stringBuilder = new StringBuilder();
        mithraGeneratorMarshaller.marshall(stringBuilder, mithraObject);
        String xmlString = this.sanitizeXmlString(stringBuilder);

        // TODO: Arrange Reladomo xmls in relative paths
        Path fullPath = outputPath.resolve(klass.getName() + ".xml");
        this.printStringToFile(fullPath, xmlString);
    }

    private MithraObject convertToMithraObject(Klass klass)
    {
        MithraObject mithraObject = new MithraObject();
        ObjectType   objectType   = new ObjectType();
        objectType.with("transactional", mithraObject);
        mithraObject.setObjectType(objectType);
        mithraObject.setPackageName(klass.getPackageName());
        mithraObject.setClassName(klass.getName());
        mithraObject.setDefaultTable(CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, klass.getName()));
        mithraObject.setInitializePrimitivesToNull(true);

        // TODO: AsOfAttributeType

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

        return mithraObject;
    }

    private AsOfAttributeType convertToAsOfAttributeType(DataTypeProperty<?> dataTypeProperty)
    {
        String propertyName   = dataTypeProperty.getName();
        String fromName       = propertyName + "From";
        String toName         = propertyName + "To";
        String fromColumnName = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, fromName);
        String toColumnName   = CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, toName);

        AsOfAttributeType asOfAttributeType = new AsOfAttributeType();
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

        return asOfAttributeType;
    }

    private AttributeType convertToAttributeType(DataTypeProperty<?> dataTypeProperty)
    {
        AttributeType attributeType = new AttributeType();
        String        propertyName  = dataTypeProperty.getName();
        attributeType.setName(propertyName);
        attributeType.setColumnName(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, propertyName));
        attributeType.setPrimaryKey(dataTypeProperty.isKey());
        attributeType.setNullable(dataTypeProperty.isOptional());

        this.handleType(attributeType, dataTypeProperty);

        return attributeType;
    }

    private void handleType(AttributeType attributeType, DataTypeProperty<?> dataTypeProperty)
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
}
