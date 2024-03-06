package cool.klass.generator.dropwizard;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.DomainModel;
import cool.klass.model.meta.domain.service.ServiceGroup;

public class AbstractApplicationGenerator
{
    @Nonnull
    private final DomainModel domainModel;
    private final String      rootPackageName;
    private final String      applicationName;
    @Nonnull
    private final String      packageName;
    @Nonnull
    private final String      relativePath;

    public AbstractApplicationGenerator(
            @Nonnull DomainModel domainModel,
            String rootPackageName,
            String applicationName)
    {
        this.domainModel = Objects.requireNonNull(domainModel);
        this.rootPackageName = Objects.requireNonNull(rootPackageName);
        this.applicationName = Objects.requireNonNull(applicationName);

        this.packageName = rootPackageName + ".dropwizard.application";
        this.relativePath = this.packageName.replaceAll("\\.", "/");
    }

    public void writeAbstractApplicationFile(@Nonnull Path outputPath) throws IOException
    {
        Path path = outputPath.resolve(this.relativePath);
        path.toFile().mkdirs();
        Path   javaPath   = path.resolve("Abstract" + this.applicationName + "Application.java");
        //language=JAVA
        String sourceCode = ""
                + "package " + this.packageName + ";\n"
                + "\n"
                + "import javax.annotation.Nonnull;\n"
                + "\n"
                + "import cool.klass.dropwizard.bundle.httplogging.HttpLoggingBundle;\n"
                + "import cool.klass.dropwizard.bundle.objectmapper.ObjectMapperBundle;\n"
                + "import cool.klass.dropwizard.bundle.reladomo.ReladomoBundle;\n"
                + "import cool.klass.model.meta.domain.DomainModel;\n"
                + "import cool.klass.model.meta.loader.DomainModelLoader;\n"
                + "import " + this.packageName + "." + this.applicationName + "Configuration;\n"
                + "import " + this.rootPackageName + ".service.resource.QuestionResource;\n"
                + "import " + this.rootPackageName + ".dropwizard.command.*;\n"
                + "import " + this.rootPackageName + ".service.resource.*;\n"
                + "import com.typesafe.config.Config;\n"
                + "import com.typesafe.config.ConfigFactory;\n"
                + "import com.typesafe.config.ConfigRenderOptions;\n"
                + "import io.dropwizard.Application;\n"
                + "import io.dropwizard.setup.Bootstrap;\n"
                + "import io.dropwizard.setup.Environment;\n"
                + "import org.slf4j.Logger;\n"
                + "import org.slf4j.LoggerFactory;\n"
                + "\n"
                + "/**\n"
                + " * Auto-generated by {@link cool.klass.generator.dropwizard.AbstractApplicationGenerator}\n"
                + " */\n"
                + "public abstract class Abstract" + this.applicationName + "Application extends Application<" + this.applicationName + "Configuration>\n"
                + "{\n"
                + "    private static final Logger LOGGER = LoggerFactory.getLogger(Abstract" + this.applicationName + "Application.class);\n"
                + "    \n"
                + "    protected DomainModel domainModel;\n"
                + "\n"
                + "    @Nonnull\n"
                + "    @Override\n"
                + "    public String getName()\n"
                + "    {\n"
                + "        return \"" + this.applicationName + "\";\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public void initialize(Bootstrap<" + this.applicationName + "Configuration> bootstrap)\n"
                + "    {\n"
                + "        super.initialize(bootstrap);\n"
                + "\n"
                + "        bootstrap.addCommand(new " + this.applicationName + "TestDataGeneratorCommand(this));\n"
                + "\n"
                + "        bootstrap.addBundle(new HttpLoggingBundle());\n"
                + "        bootstrap.addBundle(new ObjectMapperBundle());\n"
                + "        bootstrap.addBundle(new ReladomoBundle());\n"
                + "    }\n"
                + "\n"
                + "    public void run(\n"
                + "            " + this.applicationName + "Configuration configuration,\n"
                + "            Environment environment)\n"
                + "    {\n"
                + "        Config config      = ConfigFactory.load();\n"
                + "        Config klassConfig = config.getConfig(\"klass\");\n"
                + "\n"
                + "        if (LOGGER.isInfoEnabled())\n"
                + "        {\n"
                + "            ConfigRenderOptions configRenderOptions = ConfigRenderOptions.defaults()\n"
                + "                    .setJson(false)\n"
                + "                    .setOriginComments(false);\n"
                + "            String render = klassConfig.root().render(configRenderOptions);\n"
                + "            LOGGER.info(\"Klass configuration:\\n{}\", render);\n"
                + "        }\n"
                + "\n"
                + "        this.domainModel = getDomainModel(klassConfig);\n"
                + "\n"
                + this.getRegisterResourcesSourceCode()
                + "    }\n"
                + "\n"
                + "    public DomainModel getDomainModel(Config klassConfig)\n"
                + "    {\n"
                + "        String rootPackage = klassConfig.getString(\"rootPackage\");\n"
                + "\n"
                + "        DomainModelLoader domainModelLoader = new DomainModelLoader(rootPackage);\n"
                + "        return domainModelLoader.load();\n"
                + "    }\n"
                + "}\n";
        this.printStringToFile(javaPath, sourceCode);
    }

    private String getRegisterResourcesSourceCode()
    {
        return this.domainModel
                .getServiceGroups()
                .collect(this::getRegisterResourceSourceCode)
                .makeString();
    }

    private String getRegisterResourceSourceCode(ServiceGroup serviceGroup)
    {
        return String.format(
                "        environment.jersey().register(new %sResource(this.domainModel));\n",
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
