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
    private final String      packageName;
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
                + "import " + this.rootPackageName + ".service.resource.*;\n"
                + "import io.dropwizard.Application;\n"
                + "import io.dropwizard.setup.Bootstrap;\n"
                + "import io.dropwizard.setup.Environment;\n"
                + "\n"
                + "public abstract class Abstract" + (this.applicationName + "Application")
                + " extends Application<" + this.applicationName + "Configuration>\n"
                + "{\n"
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
                + "        // TODO: application initialization\n"
                + "    }\n"
                + "\n"
                + "    @Override\n"
                + "    public void run(\n"
                + "            " + this.applicationName + "Configuration configuration,\n"
                + "            Environment environment)\n"
                + "    {\n"
                + this.getRegisterResourcesSourceCode()
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
                "        environment.jersey().register(new %sResource());\n",
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
