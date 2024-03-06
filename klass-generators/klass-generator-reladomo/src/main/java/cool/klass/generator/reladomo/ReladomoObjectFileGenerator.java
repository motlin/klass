package cool.klass.generator.reladomo;

import java.io.IOException;
import java.nio.file.Path;

import com.google.common.base.CaseFormat;
import com.gs.fw.common.mithra.generator.metamodel.AttributeType;
import com.gs.fw.common.mithra.generator.metamodel.MithraGeneratorMarshaller;
import com.gs.fw.common.mithra.generator.metamodel.MithraObject;
import com.gs.fw.common.mithra.generator.metamodel.ObjectType;
import cool.klass.model.meta.domain.DataTypeProperty;
import cool.klass.model.meta.domain.DomainModel;
import cool.klass.model.meta.domain.Klass;
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

        ImmutableList<AttributeType> attributeTypes = klass.getDataTypeProperties()
                .collect(this::convertToAttributeType);

        // TODO: Add foreign keys
        mithraObject.setAttributes(attributeTypes.castToList());

        return mithraObject;
    }

    private <V> AttributeType convertToAttributeType(DataTypeProperty<?> dataTypeProperty)
    {
        AttributeType attributeType = new AttributeType();
        String        propertyName  = dataTypeProperty.getName();
        attributeType.setName(propertyName);
        attributeType.setColumnName(CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, propertyName));
        attributeType.setNullable(dataTypeProperty.isOptional());
        attributeType.setPrimaryKey(dataTypeProperty.isKey());

        // TODO: Type
        // TODO: SimulatedSequence

        return attributeType;
    }
}
