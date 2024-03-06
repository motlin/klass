package cool.klass.generator.dropwizard;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.service.ServiceGroup;

public class AbstractApplicationGenerator
{
    @Nonnull
    private final DomainModel domainModel;
    @Nonnull
    private final String      rootPackageName;
    @Nonnull
    private final String      applicationName;
    @Nonnull
    private final String      packageName;
    @Nonnull
    private final String      relativePath;

    public AbstractApplicationGenerator(
            @Nonnull DomainModel domainModel,
            @Nonnull String rootPackageName,
            @Nonnull String applicationName)
    {
        this.domainModel     = Objects.requireNonNull(domainModel);
        this.rootPackageName = Objects.requireNonNull(rootPackageName);
        this.applicationName = Objects.requireNonNull(applicationName);
        this.packageName     = rootPackageName + ".dropwizard.application";
        this.relativePath    = this.packageName.replaceAll("\\.", "/");
    }

    public void writeAbstractApplicationFile(@Nonnull Path outputPath) throws IOException
    {
        Path path = outputPath.resolve(this.relativePath);
        path.toFile().mkdirs();
        Path javaPath = path.resolve("Abstract" + this.applicationName + "Application.java");

        // @formatter:off
        //language=JAVA
        String sourceCode = ""
                + "package " + this.packageName + ";\n"
                + "\n"
                + "import java.time.Clock;\n"
                + "\n"
                + "import javax.annotation.Nonnull;\n"
                + "\n"
                + "import cool.klass.data.store.DataStore;\n"
                + "import io.liftwizard.dropwizard.application.AbstractLiftwizardApplication;\n"
                + "import io.dropwizard.setup.Environment;\n"
                + this.getResourceImports()
                + "import org.slf4j.Logger;\n"
                + "import org.slf4j.LoggerFactory;\n"
                + "\n"
                + "/**\n"
                + " * Auto-generated by {@link cool.klass.generator.dropwizard.AbstractApplicationGenerator}\n"
                + " */\n"
                + "public abstract class Abstract" + this.applicationName + "Application\n"
                + "        extends AbstractLiftwizardApplication<" + this.applicationName + "Configuration>\n"
                + "{\n"
                + "    private static final Logger LOGGER = LoggerFactory.getLogger(Abstract" + this.applicationName + "Application.class);\n"
                + "\n"
                + "    public Abstract" + this.applicationName + "Application()\n"
                + "    {\n"
                + "        super(\"" + this.applicationName + "\");\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public void run(\n"
                + "            @Nonnull " + this.applicationName + "Configuration configuration,\n"
                + "            @Nonnull Environment environment) throws Exception\n"
                + "    {\n"
                + "        super.run(configuration, environment);\n"
                + "\n"
                + "        DataStore dataStore = configuration.getKlassFactory().getDataStoreFactory().create"
                + "DataStore();\n"
                + "        Clock     clock     = configuration.getKlassFactory().getClockFactory().createClock();\n"
                + "\n"
                + this.getRegisterResourcesSourceCode()
                + "    }\n"
                + "}\n";
        // @formatter:on

        this.printStringToFile(javaPath, sourceCode);
    }

    private String getResourceImports()
    {
        return this.domainModel
                .getServiceGroups()
                .collect(this::getResourceImport)
                .makeString("");
    }

    private String getRegisterResourcesSourceCode()
    {
        return this.domainModel
                .getServiceGroups()
                .collect(this::getRegisterResourceSourceCode)
                .makeString("");
    }

    private String getResourceImport(ServiceGroup serviceGroup)
    {
        return String.format(
                "import %s.service.resource.%sResource;;\n",
                serviceGroup.getPackageName(),
                serviceGroup.getName());
    }

    private String getRegisterResourceSourceCode(@Nonnull ServiceGroup serviceGroup)
    {
        return String.format(
                "        environment.jersey().register(new %sResource(dataStore, clock));\n",
                serviceGroup.getKlass().getName());
    }

    private void printStringToFile(@Nonnull Path path, String contents) throws FileNotFoundException
    {
        try (PrintStream printStream = new PrintStream(new FileOutputStream(path.toFile())))
        {
            printStream.print(contents);
        }
    }
}
