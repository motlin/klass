package cool.klass.generator.meta.json;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Path;
import java.time.Clock;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.node.ObjectNode;
import cool.klass.data.store.DataStore;
import cool.klass.model.meta.domain.api.DomainModel;
import klass.model.meta.domain.Association;
import klass.model.meta.domain.Enumeration;
import klass.model.meta.domain.Interface;
import klass.model.meta.domain.Klass;
import klass.model.meta.domain.NamedProjection;
import klass.model.meta.domain.ServiceGroup;
import klass.model.meta.domain.json.view.AssociationSummaryProjection_JsonView;
import klass.model.meta.domain.json.view.ClassSummaryProjection_JsonView;
import klass.model.meta.domain.json.view.EnumerationSummaryProjection_JsonView;
import klass.model.meta.domain.json.view.InterfaceSummaryProjection_JsonView;
import klass.model.meta.domain.json.view.NamedProjectionProjection_JsonView;
import klass.model.meta.domain.json.view.ServiceGroupSummaryProjection_JsonView;
import klass.model.meta.domain.service.resource.AssociationResource;
import klass.model.meta.domain.service.resource.EnumerationResource;
import klass.model.meta.domain.service.resource.InterfaceResource;
import klass.model.meta.domain.service.resource.KlassResource;
import klass.model.meta.domain.service.resource.NamedProjectionResource;
import klass.model.meta.domain.service.resource.ServiceGroupResource;

public class JsonMetaModelGenerator
{
    @Nonnull
    private final DomainModel  domainModel;
    @Nonnull
    private final DataStore    dataStore;
    @Nonnull
    private final ObjectMapper objectMapper;
    @Nonnull
    private final Clock        clock;
    @Nonnull
    private final String       applicationName;
    @Nonnull
    private final String       rootPackageName;
    @Nonnull
    private final Path         outputPath;

    public JsonMetaModelGenerator(
            @Nonnull DomainModel domainModel,
            @Nonnull DataStore dataStore,
            @Nonnull ObjectMapper objectMapper,
            @Nonnull Clock clock,
            @Nonnull String applicationName,
            @Nonnull String rootPackageName,
            @Nonnull Path outputPath)
    {
        this.domainModel     = Objects.requireNonNull(domainModel);
        this.dataStore       = Objects.requireNonNull(dataStore);
        this.objectMapper    = Objects.requireNonNull(objectMapper);
        this.clock           = Objects.requireNonNull(clock);
        this.applicationName = Objects.requireNonNull(applicationName);
        this.rootPackageName = Objects.requireNonNull(rootPackageName);
        this.outputPath      = Objects.requireNonNull(outputPath);
    }

    public void writeJsonMetaModelFiles()
    {
        ObjectNode resultNode = this.objectMapper.createObjectNode();

        resultNode.set("enumerations", this.getEnumerationNode());
        resultNode.set("interfaces",   this.getInterfaceNode());
        resultNode.set("classes",      this.getKlassNode());
        resultNode.set("associations", this.getAssociationNode());
        resultNode.set("projections",  this.getProjectionNode());
        resultNode.set("services",     this.getServiceGroupNode());

        try
        {
            String string     = this.objectMapper.writeValueAsString(resultNode);
            Path   outputPath = this.getOutputPath();
            this.printStringToFile(outputPath, string);
        }
        catch (JsonProcessingException e)
        {
            throw new RuntimeException(e);
        }
    }

    public JsonNode getEnumerationNode()
    {
        EnumerationResource enumerationResource = new EnumerationResource(this.domainModel, this.dataStore, this.clock);
        List<Enumeration>   enumerations        = enumerationResource.method2();
        ObjectMapper        objectMapper        = this.objectMapperWithView(EnumerationSummaryProjection_JsonView.class);
        return objectMapper.valueToTree(enumerations);
    }

    public JsonNode getInterfaceNode()
    {
        InterfaceResource interfaceResource = new InterfaceResource(this.domainModel, this.dataStore, this.clock);
        List<Interface>   interfaces        = interfaceResource.method2();
        ObjectMapper      objectMapper      = this.objectMapperWithView(InterfaceSummaryProjection_JsonView.class);
        return objectMapper.valueToTree(interfaces);
    }

    public JsonNode getKlassNode()
    {
        KlassResource klassResource = new KlassResource(this.domainModel, this.dataStore, this.clock);
        List<Klass>   klasses       = klassResource.method2();
        ObjectMapper  objectMapper  = this.objectMapperWithView(ClassSummaryProjection_JsonView.class);
        return objectMapper.valueToTree(klasses);
    }

    public JsonNode getAssociationNode()
    {
        AssociationResource associationResource = new AssociationResource(this.domainModel, this.dataStore, this.clock);
        List<Association>   associations        = associationResource.method2();
        ObjectMapper        objectMapper        = this.objectMapperWithView(AssociationSummaryProjection_JsonView.class);
        return objectMapper.valueToTree(associations);
    }

    public JsonNode getProjectionNode()
    {
        NamedProjectionResource projectionResource = new NamedProjectionResource(
                this.domainModel,
                this.dataStore,
                this.clock);
        List<NamedProjection> serviceProjections = projectionResource.method2();
        ObjectMapper objectMapper = this.objectMapperWithView(
                NamedProjectionProjection_JsonView.class);
        return objectMapper.valueToTree(serviceProjections);
    }

    public JsonNode getServiceGroupNode()
    {
        ServiceGroupResource serviceGroupResource = new ServiceGroupResource(
                this.domainModel,
                this.dataStore,
                this.clock);
        List<ServiceGroup> serviceGroups = serviceGroupResource.method2();
        ObjectMapper       objectMapper  = this.objectMapperWithView(ServiceGroupSummaryProjection_JsonView.class);
        return objectMapper.valueToTree(serviceGroups);
    }

    public ObjectMapper objectMapperWithView(Class<?> viewClass)
    {
        SerializationConfig serializationConfig = this.objectMapper
                .getSerializationConfig()
                .withView(viewClass);
        ObjectMapper objectMapperCopy = this.objectMapper.copy();
        return objectMapperCopy.setConfig(serializationConfig);
    }

    @Nonnull
    private Path getOutputPath()
    {
        String packageRelativePath = this.rootPackageName.replaceAll("\\.", "/");
        Path outputDirectory = this.outputPath
                .resolve(packageRelativePath);
        outputDirectory.toFile().mkdirs();
        String fileName = this.applicationName + ".json";
        return outputDirectory.resolve(fileName);
    }

    private void printStringToFile(@Nonnull Path path, String contents)
    {
        try (var printStream = new PrintStream(new FileOutputStream(path.toFile())))
        {
            printStream.print(contents);
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
    }
}
