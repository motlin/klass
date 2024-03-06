package cool.klass.generator.reladomo;

import java.io.IOException;
import java.nio.file.Path;

import javax.annotation.Nonnull;

import com.gs.fw.common.mithra.generator.metamodel.Mithra;
import com.gs.fw.common.mithra.generator.metamodel.MithraGeneratorMarshaller;
import com.gs.fw.common.mithra.generator.metamodel.MithraObjectResourceType;
import com.gs.fw.common.mithra.generator.metamodel.MithraPureObjectResourceType;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.NamedElement;
import org.eclipse.collections.api.list.ImmutableList;

public class ReladomoClassListGenerator extends AbstractReladomoGenerator
{
    public ReladomoClassListGenerator(DomainModel domainModel)
    {
        super(domainModel);
    }

    public void writeClassListFile(@Nonnull Path path) throws IOException
    {
        MithraGeneratorMarshaller mithraGeneratorMarshaller = new MithraGeneratorMarshaller();
        mithraGeneratorMarshaller.setIndent(true);

        Mithra mithra = this.generateObjectResources();

        StringBuilder stringBuilder = new StringBuilder();
        mithraGeneratorMarshaller.marshall(stringBuilder, mithra);
        String xmlString = this.sanitizeXmlString(stringBuilder);

        this.printStringToFile(path, xmlString);
    }

    @Nonnull
    private Mithra generateObjectResources()
    {
        ImmutableList<MithraObjectResourceType> objectResources = this.domainModel
                .getKlasses()
                .reject(Klass::isTransient)
                .collect(NamedElement::getName)
                .collect(this::getObjectResource);

        ImmutableList<MithraPureObjectResourceType> pureObjectResources = this.domainModel
                .getKlasses()
                .select(Klass::isTransient)
                .collect(NamedElement::getName)
                .collect(this::getPureObjectResource);

        Mithra mithra = new Mithra();
        mithra.setMithraObjectResources(objectResources.castToList());
        mithra.setMithraPureObjectResources(pureObjectResources.castToList());
        return mithra;
    }

    @Nonnull
    private MithraObjectResourceType getObjectResource(String className)
    {
        MithraObjectResourceType objectResource = new MithraObjectResourceType();
        objectResource.setName(className);
        return objectResource;
    }

    @Nonnull
    private MithraPureObjectResourceType getPureObjectResource(String className)
    {
        MithraPureObjectResourceType pureObjectResource = new MithraPureObjectResourceType();
        pureObjectResource.setName(className);
        return pureObjectResource;
    }
}
