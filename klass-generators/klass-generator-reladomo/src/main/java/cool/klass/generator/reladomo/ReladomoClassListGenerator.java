package cool.klass.generator.reladomo;

import java.io.IOException;
import java.nio.file.Path;

import com.gs.fw.common.mithra.generator.metamodel.Mithra;
import com.gs.fw.common.mithra.generator.metamodel.MithraGeneratorMarshaller;
import com.gs.fw.common.mithra.generator.metamodel.MithraObjectResourceType;
import cool.klass.model.meta.domain.DomainModel;
import cool.klass.model.meta.domain.NamedElement;
import org.eclipse.collections.api.list.ImmutableList;

public class ReladomoClassListGenerator extends AbstractReladomoGenerator
{
    public ReladomoClassListGenerator(DomainModel domainModel)
    {
        super(domainModel);
    }

    public void writeClassListFile(Path path) throws IOException
    {
        MithraGeneratorMarshaller mithraGeneratorMarshaller = new MithraGeneratorMarshaller();
        mithraGeneratorMarshaller.setIndent(true);

        Mithra mithra = this.generateObjectResources();

        StringBuilder stringBuilder = new StringBuilder();
        mithraGeneratorMarshaller.marshall(stringBuilder, mithra);
        String xmlString = this.sanitizeXmlString(stringBuilder);

        this.printStringToFile(path, xmlString);
    }

    private Mithra generateObjectResources()
    {
        ImmutableList<MithraObjectResourceType> mithraObjectResources = this.domainModel
                .getKlasses()
                .collect(NamedElement::getName)
                .collect(this::getMithraObjectResource);

        Mithra mithra = new Mithra();
        mithra.setMithraObjectResources(mithraObjectResources.castToList());
        return mithra;
    }

    private MithraObjectResourceType getMithraObjectResource(String className)
    {
        MithraObjectResourceType mithraObjectResource = new MithraObjectResourceType();
        mithraObjectResource.setName(className);
        return mithraObjectResource;
    }
}
