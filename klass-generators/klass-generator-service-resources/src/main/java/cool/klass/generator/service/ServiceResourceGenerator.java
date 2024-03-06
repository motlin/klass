package cool.klass.generator.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.DomainModel;
import cool.klass.model.meta.domain.Klass;
import cool.klass.model.meta.domain.service.Service;
import cool.klass.model.meta.domain.service.ServiceGroup;
import cool.klass.model.meta.domain.service.ServiceMultiplicity;
import cool.klass.model.meta.domain.service.Verb;
import cool.klass.model.meta.domain.service.url.Url;
import cool.klass.model.meta.domain.service.url.UrlParameter;
import cool.klass.model.meta.domain.service.url.UrlPathParameter;
import cool.klass.model.meta.domain.service.url.UrlPathSegment;
import cool.klass.model.meta.domain.service.url.UrlQueryParameter;
import org.eclipse.collections.api.list.ImmutableList;

public class ServiceResourceGenerator
{
    @Nonnull
    private final DomainModel domainModel;

    public ServiceResourceGenerator(@Nonnull DomainModel domainModel)
    {
        this.domainModel = Objects.requireNonNull(domainModel);
    }

    public void writeServiceResourceFiles(@Nonnull Path outputPath) throws IOException
    {
        for (ServiceGroup serviceGroup : this.domainModel.getServiceGroups())
        {
            // TODO: Instead of inferring a resource name, change the DSL to include it in the declaration, like this:
            // service QuestionResource on Question

            Klass  klass                   = serviceGroup.getKlass();
            String className               = klass.getName();
            String serviceResourceFileName = className + "Resource.java";

            String packageRelativePath = serviceGroup
                    .getKlass()
                    .getPackageName()
                    .replaceAll("\\.", "/");
            Path serviceGroupDirectory = outputPath.resolve(packageRelativePath);
            serviceGroupDirectory.toFile().mkdirs();
            Path serviceGroupOutputPath = serviceGroupDirectory.resolve(serviceResourceFileName);

            this.printStringToFile(serviceGroupOutputPath, this.getServiceGroupSourceCode(serviceGroup));
        }
    }

    private void printStringToFile(@Nonnull Path path, String contents) throws FileNotFoundException
    {
        try (PrintStream printStream = new PrintStream(new FileOutputStream(path.toFile())))
        {
            printStream.print(contents);
        }
    }

    @Nonnull
    private String getServiceGroupSourceCode(ServiceGroup serviceGroup)
    {
        Klass  klass               = serviceGroup.getKlass();
        String className           = klass.getName();
        String packageName         = klass.getPackageName();
        String serviceResourceName = className + "Resource";

        String serviceMethodsSourceCode = serviceGroup
                .getUrls()
                .flatCollect(Url::getServices)
                .collectWithIndex(this::getServiceSourceCode)
                .makeString("\n");

        //language=JAVA
        String sourceCode = "package " + packageName + ";\n"
                + "\n"
                + "import java.util.List;\n"
                + "\n"
                + "import javax.ws.rs.*;\n"
                + "import javax.ws.rs.core.MediaType;\n"
                + "\n"
                + "public class " + serviceResourceName + "\n"
                + "{\n"
                + serviceMethodsSourceCode
                + "}\n";
        return sourceCode;
    }

    @Nonnull
    private String getServiceSourceCode(Service service, int index)
    {
        Url          url          = service.getUrl();
        ServiceGroup serviceGroup = url.getServiceGroup();
        Klass        klass        = serviceGroup.getKlass();

        ImmutableList<UrlPathSegment>    urlPathSegments = url.getUrlPathSegments();
        ImmutableList<UrlQueryParameter> queryParameters = url.getQueryParameters();
        ImmutableList<UrlParameter>      urlParameters   = url.getUrlParameters();

        Verb                verb                = service.getVerb();
        ServiceMultiplicity serviceMultiplicity = service.getServiceMultiplicity();

        String returnType = serviceMultiplicity == ServiceMultiplicity.ONE
                ? klass.getName()
                : "List<" + klass.getName() + ">";

        String urlPathString = urlPathSegments.makeString("/", "/", "");
        String queryParametersString = queryParameters.isEmpty()
                ? ""
                : queryParameters.makeString("?", "&", "");

        String prefix = urlParameters.size() > 1 ? "\n" : "";
        String indent = urlParameters.size() > 1 ? "            " : "";
        String parametersSourceCode = urlParameters
                .collectWith(this::getParameterSourceCode, indent)
                .makeString(",\n");

        String sourceCode = ""
                + "    @" + verb.name() + "\n"
                + "    @Path(\"" + urlPathString + queryParametersString + "\")\n"
                + "    @Produces(MediaType.APPLICATION_JSON)\n"
                + "    public " + returnType + " method" + index + "(" + prefix
                + parametersSourceCode + ")\n"
                + "    {\n"
                + "        throw new AssertionError();\n"
                + "    }\n";

        return sourceCode;
    }

    private String getParameterSourceCode(@Nonnull UrlParameter urlParameter, String indent)
    {
        return String.format(
                "%s@%s(\"%s\") %s %s",
                indent,
                this.getAnnotation(urlParameter),
                urlParameter.getName(),
                urlParameter.getType(),
                urlParameter.getName());
    }

    @Nonnull
    private String getAnnotation(UrlParameter urlParameter)
    {
        if (urlParameter instanceof UrlPathParameter)
        {
            return "PathParam";
        }
        if (urlParameter instanceof UrlQueryParameter)
        {
            return "QueryParam";
        }
        throw new AssertionError();
    }
}
