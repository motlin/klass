package cool.klass.generator.reladomo;

import java.io.IOException;
import java.nio.file.Path;

import com.google.common.base.CaseFormat;
import com.gs.fw.common.mithra.generator.metamodel.MithraGeneratorMarshaller;
import com.gs.fw.common.mithra.generator.metamodel.MithraObject;
import com.gs.fw.common.mithra.generator.metamodel.ObjectType;
import cool.klass.model.meta.domain.DomainModel;
import cool.klass.model.meta.domain.Klass;

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

        MithraObject mithraObject = this.convertToMithraObject(klass);
        StringBuilder stringBuilder = new StringBuilder();
        mithraGeneratorMarshaller.marshall(stringBuilder, mithraObject);
        String xmlString = this.sanitizeXmlString(stringBuilder);

        // TODO: Use package in relative path
        Path fullPath = outputPath.resolve(klass.getName() + ".xml");
        this.printStringToFile(fullPath, xmlString);
    }

    private MithraObject convertToMithraObject(Klass klass)
    {
        MithraObject mithraObject = new MithraObject();
        ObjectType objectType = new ObjectType();
        objectType.with("transactional", mithraObject);
        mithraObject.setObjectType(objectType);
        mithraObject.setPackageName(klass.getPackageName());
        mithraObject.setClassName(klass.getName());
        mithraObject.setDefaultTable(CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, klass.getName()));
        mithraObject.setInitializePrimitivesToNull(true);

        // TODO

        return mithraObject;
    }
}
