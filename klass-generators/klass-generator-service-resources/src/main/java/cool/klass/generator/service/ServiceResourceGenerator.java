package cool.klass.generator.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.DataType;
import cool.klass.model.meta.domain.DomainModel;
import cool.klass.model.meta.domain.Klass;
import cool.klass.model.meta.domain.criteria.AllCriteria;
import cool.klass.model.meta.domain.criteria.Criteria;
import cool.klass.model.meta.domain.projection.Projection;
import cool.klass.model.meta.domain.service.Service;
import cool.klass.model.meta.domain.service.ServiceGroup;
import cool.klass.model.meta.domain.service.ServiceMultiplicity;
import cool.klass.model.meta.domain.service.ServiceProjectionDispatch;
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

            String packageRelativePath = serviceGroup
                    .getKlass()
                    .getPackageName()
                    .replaceAll("\\.", "/");
            Path serviceGroupDirectory = outputPath
                    .resolve(packageRelativePath)
                    .resolve("service")
                    .resolve("resource");
            serviceGroupDirectory.toFile().mkdirs();
            String fileName               = serviceGroup.getKlass().getName() + "Resource.java";
            Path   serviceGroupOutputPath = serviceGroupDirectory.resolve(fileName);

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
    public String getServiceGroupSourceCode(ServiceGroup serviceGroup)
    {
        Klass  klass               = serviceGroup.getKlass();
        String packageName         = klass.getPackageName() + ".service.resource";
        String serviceResourceName = klass.getName() + "Resource";

        String serviceMethodsSourceCode = serviceGroup
                .getUrls()
                .flatCollect(Url::getServices)
                .collectWithIndex(this::getServiceSourceCode)
                .makeString("\n");

        //language=JAVA
        String sourceCode = "package " + packageName + ";\n"
                + "\n"
                + "import java.util.List;\n"
                + "import java.util.Set;\n"
                + "\n"
                + "import javax.ws.rs.*;\n"
                + "import javax.ws.rs.core.*;\n"
                + "\n"
                + "import " + klass.getPackageName() + ".*;\n"
                + "import com.gs.fw.common.mithra.finder.Operation;\n"
                + "import org.eclipse.collections.api.set.primitive.*;\n"
                + "import org.eclipse.collections.impl.factory.primitive.*;\n"
                + "import org.eclipse.collections.impl.set.mutable.SetAdapter;\n"
                + "import org.eclipse.collections.impl.utility.Iterate;\n"
                + "\n"
                + "@Path(\"/\")\n"
                + "public class " + serviceResourceName + "\n"
                + "{\n"
                + ""
                + serviceMethodsSourceCode
                + "}\n";
        return sourceCode;
    }

    @Nonnull
    private String getServiceSourceCode(Service service, int index)
    {
        Url                 url                 = service.getUrl();
        Verb                verb                = service.getVerb();
        ServiceMultiplicity serviceMultiplicity = service.getServiceMultiplicity();

        ServiceGroup                     serviceGroup    = url.getServiceGroup();
        ImmutableList<UrlPathSegment>    urlPathSegments = url.getUrlPathSegments();
        ImmutableList<UrlQueryParameter> queryParameters = url.getQueryParameters();
        ImmutableList<UrlParameter>      urlParameters   = url.getUrlParameters();

        Klass  klass           = serviceGroup.getKlass();
        String klassName       = klass.getName();
        String returnType      = this.getReturnType(serviceMultiplicity, klassName);
        String returnStatement = this.getReturnStatement(serviceMultiplicity, service);

        String executeOperationSourceCode = MessageFormat.format(
                "        {0}List result = {0}Finder.findMany(queryOperation);\n",
                klassName);

        String urlPathString = urlPathSegments.makeString("/", "/", "");
        String queryParametersString = queryParameters.isEmpty()
                ? ""
                : queryParameters.makeString("?", "&", "");

        boolean hasAuthorizeCriteria = service.getAuthorizeCriteria() != AllCriteria.INSTANCE;

        int     numUrlParameters       = urlParameters.size();
        int     numAuthorizeParameters = hasAuthorizeCriteria ? 1 : 0;
        int     numParameters          = numUrlParameters + numAuthorizeParameters;
        boolean lineWrapParameters     = numParameters > 1;

        String parameterPrefix = lineWrapParameters ? "\n" : "";
        String parameterIndent = lineWrapParameters ? "            " : "";

        ImmutableList<String> basicParameterStrings = urlParameters
                .collectWith(this::getParameterSourceCode, parameterIndent);
        ImmutableList<String> parameterStrings = hasAuthorizeCriteria
                ? basicParameterStrings.newWith(parameterIndent + "@Context SecurityContext securityContext")
                : basicParameterStrings;

        String userPrincipalNameLocalVariable = hasAuthorizeCriteria
                ? "        String    userPrincipalName  = securityContext.getUserPrincipal().getName();\n"
                : "";

        String parametersSourceCode = parameterStrings.makeString(",\n");

        String finderName                   = klassName + "Finder";
        String queryOperationSourceCode     = this.getOperation(service.getQueryCriteria(), finderName);
        String authorizeOperationSourceCode = this.getOperation(service.getAuthorizeCriteria(), finderName);
        String validateOperationSourceCode  = this.getOperation(service.getValidateCriteria(), finderName);
        String conflictOperationSourceCode  = this.getOperation(service.getConflictCriteria(), finderName);

        //language=JAVA
        String sourceCode = "    @" + verb.name() + "\n"
                + "    @Path(\"" + urlPathString + queryParametersString + "\")\n"
                + "    @Produces(MediaType.APPLICATION_JSON)\n"
                + "    public " + returnType + " method" + index + "(" + parameterPrefix + parametersSourceCode + ")\n"
                + "    {\n"
                + userPrincipalNameLocalVariable
                + "        Operation queryOperation     = " + queryOperationSourceCode + ";\n"
                + "        Operation authorizeOperation = " + authorizeOperationSourceCode + ";\n"
                + "        Operation validateOperation  = " + validateOperationSourceCode + ";\n"
                + "        Operation conflictOperation  = " + conflictOperationSourceCode + ";\n"
                + "\n"
                + executeOperationSourceCode
                + "        // TODO: Deep fetch\n"
                + "\n"
                + "        boolean isAuthorized = !result.asEcList().allSatisfy(authorizeOperation::matches);\n"
                + "        boolean isValidated  = !result.asEcList().allSatisfy(validateOperation::matches);\n"
                + "        boolean hasConflict  = !result.asEcList().allSatisfy(conflictOperation::matches);\n"
                + returnStatement
                + "    }\n";

        return sourceCode;
    }

    @Nonnull
    private String getReturnType(ServiceMultiplicity serviceMultiplicity, String klassName)
    {
        boolean uniqueResult = serviceMultiplicity == ServiceMultiplicity.ONE;

        return uniqueResult
                ? klassName
                : "List<" + klassName + ">";
    }

    @Nonnull
    private String getReturnStatement(ServiceMultiplicity serviceMultiplicity, Service service)
    {
        ServiceProjectionDispatch projectionDispatch = service.getProjectionDispatch();
        Projection                projection         = projectionDispatch.getProjection();
        String                    projectionName     = projection.getName();

        boolean uniqueResult = serviceMultiplicity == ServiceMultiplicity.ONE;

        if (uniqueResult)
        {
            return ""
                    + "        // TODO: Use Projection " + projectionName + "\n"
                    + "        if (result.isEmpty())\n"
                    + "        {\n"
                    + "            throw new NotFoundException();\n"
                    + "        }\n"
                    + "        return Iterate.getOnly(result);\n";
        }

        return "        // TODO: Use Projection " + projectionName + "\n"
                + "        return result;\n";
    }

    @Nonnull
    private String getOperation(Criteria criteria, String finderName)
    {
        StringBuilder stringBuilder = new StringBuilder();
        criteria.visit(new OperationCriteriaVisitor(finderName, stringBuilder));
        return stringBuilder.toString();
    }

    private String getParameterSourceCode(@Nonnull UrlParameter urlParameter, String indent)
    {
        // TODO: Hibernate validation annotations

        DataType parameterType = urlParameter.getType();
        String typeString = urlParameter.getMultiplicity().isToMany()
                ? "Set<" + parameterType.getName() + ">"
                : parameterType.getName();

        return String.format(
                "%s@%s(\"%s\") %s %s",
                indent,
                this.getAnnotation(urlParameter),
                urlParameter.getName(),
                typeString,
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
