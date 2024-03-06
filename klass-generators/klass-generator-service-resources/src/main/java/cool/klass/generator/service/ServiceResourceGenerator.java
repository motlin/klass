package cool.klass.generator.service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.time.Instant;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.DataType;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.Enumeration;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.domain.api.criteria.Criteria;
import cool.klass.model.meta.domain.api.order.OrderBy;
import cool.klass.model.meta.domain.api.order.OrderByDirection;
import cool.klass.model.meta.domain.api.order.OrderByDirectionDeclaration;
import cool.klass.model.meta.domain.api.order.OrderByMemberReferencePath;
import cool.klass.model.meta.domain.api.parameter.Parameter;
import cool.klass.model.meta.domain.api.projection.Projection;
import cool.klass.model.meta.domain.api.projection.ProjectionWalker;
import cool.klass.model.meta.domain.api.service.Service;
import cool.klass.model.meta.domain.api.service.ServiceGroup;
import cool.klass.model.meta.domain.api.service.ServiceMultiplicity;
import cool.klass.model.meta.domain.api.service.ServiceProjectionDispatch;
import cool.klass.model.meta.domain.api.service.Verb;
import cool.klass.model.meta.domain.api.service.url.Url;
import cool.klass.model.meta.domain.api.value.ThisMemberReferencePath;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.api.tuple.primitive.ObjectBooleanPair;
import org.eclipse.collections.impl.tuple.primitive.PrimitiveTuples;

public class ServiceResourceGenerator
{
    @Nonnull
    private final DomainModel domainModel;
    @Nonnull
    private final String      applicationName;
    @Nonnull
    private final String      rootPackageName;
    @Nonnull
    private final Instant     now;

    public ServiceResourceGenerator(
            @Nonnull DomainModel domainModel,
            @Nonnull String applicationName,
            @Nonnull String rootPackageName,
            @Nonnull Instant now)
    {
        this.domainModel = Objects.requireNonNull(domainModel);
        this.applicationName = Objects.requireNonNull(applicationName);
        this.rootPackageName = Objects.requireNonNull(rootPackageName);
        this.now = Objects.requireNonNull(now);
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
    public String getServiceGroupSourceCode(@Nonnull ServiceGroup serviceGroup)
    {
        Klass  klass               = serviceGroup.getKlass();
        String packageName         = klass.getPackageName() + ".service.resource";
        String serviceResourceName = klass.getName() + "Resource";

        String serviceMethodsSourceCode = serviceGroup
                .getUrls()
                .flatCollect(Url::getServices)
                .collectWithIndex(this::getServiceSourceCode)
                .makeString("\n");

        String jsr310Import = serviceGroup.getUrls().anySatisfy(this::hasDropwizardParamWrapper)
                ? "import io.dropwizard.jersey.jsr310.*;\n"
                : "";

        boolean hasWriteServices = serviceGroup.getUrls()
                .asLazy()
                .flatCollect(Url::getServices)
                .collect(Service::getVerb)
                .contains(Verb.PUT);
        String writeImports = hasWriteServices
                ? ""
                + "import javax.validation.constraints.NotNull;\n"
                + "import com.fasterxml.jackson.databind.node.ObjectNode;\n"
                + "import cool.klass.deserializer.json.JsonTypeCheckingValidator;\n"
                + "import cool.klass.deserializer.json.OperationMode;\n"
                + "import cool.klass.deserializer.json.RequiredPropertiesValidator;\n"
                + "import cool.klass.reladomo.persistent.writer.IncomingUpdateDataModelValidator;\n"
                + "import cool.klass.reladomo.persistent.writer.PersistentReplacer;\n"
                : "";

        // @formatter:off
        //language=JAVA
        return ""
                + "package " + packageName + ";\n"
                + "\n"
                + "import java.sql.Timestamp;\n"
                + "import java.util.List;\n"
                + "import java.util.Set;\n"
                + "\n"
                + "import javax.annotation.*;\n"
                + "import javax.ws.rs.*;\n"
                + "import javax.ws.rs.core.*;\n"
                + "import javax.ws.rs.core.Response.Status;\n"
                + "\n"
                + "import " + klass.getPackageName() + ".*;\n"
                + "import " + this.rootPackageName + ".meta.constants." + this.applicationName + "DomainModel;\n"
                + "import " + this.rootPackageName + ".json.view.*;\n"
                + "import com.codahale.metrics.annotation.*;\n"
                + "import com.fasterxml.jackson.annotation.JsonView;\n"
                + "import com.gs.fw.common.mithra.finder.*;\n"
                + "import cool.klass.data.store.*;\n"
                + jsr310Import
                + writeImports
                + "\n"
                + "import org.eclipse.collections.api.list.MutableList;\n"
                + "import org.eclipse.collections.impl.factory.Lists;\n"
                + "import org.eclipse.collections.impl.factory.primitive.LongSets;\n"
                + "import org.eclipse.collections.impl.set.mutable.*;\n"
                + "import org.eclipse.collections.impl.utility.*;\n"
                + "\n"
                + "/**\n"
                + " * Auto-generated by {@link cool.klass.generator.service.ServiceResourceGenerator}\n"
                + " */\n"
                + "@Generated(\n"
                + "        value = \"cool.klass.generator.service.ServiceResourceGenerator\",\n"
                + "        date = \"" + this.now + "\")\n"
                + "@Path(\"/\")\n"
                + "public class " + serviceResourceName + "\n"
                + "{\n"
                + "    private final DataStore dataStore;\n"
                + "\n"
                + "    public " + serviceResourceName + "(DataStore dataStore)\n"
                + "    {\n"
                + "        this.dataStore = dataStore;\n"
                + "    }\n"
                + "\n"
                + ""
                + serviceMethodsSourceCode
                + "}\n";
        // @formatter:on
    }

    private boolean hasDropwizardParamWrapper(Url url)
    {
        return url.getParameters().anySatisfy(this::hasDropwizardParamWrapper);
    }

    private boolean hasDropwizardParamWrapper(Parameter parameter)
    {
        DataType dataType = parameter.getType();
        if (!(dataType instanceof PrimitiveType))
        {
            return false;
        }

        return ((PrimitiveType) dataType).isTemporal()
                || dataType == PrimitiveType.LOCAL_DATE
                || dataType == PrimitiveType.INSTANT;
    }

    @Nonnull
    private String getServiceSourceCode(Service service, int index)
    {
        if (service.getVerb() == Verb.GET)
        {
            return this.getGetSourceCode(service, index);
        }

        if (service.getVerb() == Verb.POST)
        {
            return this.getPostSourceCode();
        }

        if (service.getVerb() == Verb.PUT)
        {
            return this.getPutSourceCode(service, index);
        }

        if (service.getVerb() == Verb.DELETE)
        {
            return this.getDeleteSourceCode();
        }

        throw new AssertionError(service.getVerb().name());
    }

    @Nonnull
    private String getGetSourceCode(Service service, int index)
    {
        Url url = service.getUrl();

        ServiceGroup serviceGroup = url.getServiceGroup();

        ImmutableList<ObjectBooleanPair<Parameter>> pathParameters = url.getPathParameters()
                .collectWith(PrimitiveTuples::pair, true);
        ImmutableList<ObjectBooleanPair<Parameter>> queryParameters = url.getQueryParameters()
                .collectWith(PrimitiveTuples::pair, false);

        String klassName = serviceGroup.getKlass().getName();
        String returnType = service.getServiceMultiplicity() == ServiceMultiplicity.ONE
                ? klassName
                : "List<" + klassName + ">";
        String returnStatement = this.getReturnStatement(service.getServiceMultiplicity());

        String queryParametersString = queryParameters.isEmpty()
                ? ""
                : queryParameters.collect(ObjectBooleanPair::getOne).makeString(" // ?", "&", "");

        boolean hasAuthorizeCriteria = service.isAuthorizeClauseRequired();

        int     numParameters      = service.getNumParameters();
        boolean lineWrapParameters = numParameters > 1;

        String parameterPrefix = lineWrapParameters ? "\n" : "";
        String parameterIndent = lineWrapParameters ? "            " : "";

        ImmutableList<String> parameterStrings1 = pathParameters.newWithAll(queryParameters)
                .collectWith(this::getParameterSourceCode, parameterIndent);
        // TODO: This version query parameter should be inferred during the compilation of the service
        ImmutableList<String> parameterStrings = hasAuthorizeCriteria
                ? parameterStrings1.newWith(parameterIndent + "@Context SecurityContext securityContext")
                : parameterStrings1;

        String userPrincipalNameLocalVariable = hasAuthorizeCriteria
                ? "        String    userPrincipalName  = securityContext.getUserPrincipal().getName();\n"
                : "";

        String parametersSourceCode = parameterStrings.makeString(",\n");

        String finderName                   = klassName + "Finder";
        String queryOperationSourceCode     = this.getOperation(finderName, service.getQueryCriteria(), "query");
        String authorizeOperationSourceCode = this.getOperation(finderName, service.getAuthorizeCriteria(), "authorize");
        String validateOperationSourceCode  = this.getOperation(finderName, service.getValidateCriteria(), "validate");
        String conflictOperationSourceCode  = this.getOperation(finderName, service.getConflictCriteria(), "conflict");
        String versionOperationSourceCode   = this.getOptionalOperation(finderName, service.getVersionCriteria(), "version");

        String authorizePredicateSourceCode = this.checkPredicate(service.getAuthorizeCriteria(), "authorize", "isAuthorized", "ForbiddenException()");
        String validatePredicateSourceCode  = this.checkPredicate(service.getValidateCriteria(), "validate", "isValidated", "BadRequestException()");
        String conflictPredicateSourceCode  = this.checkPredicate(service.getConflictCriteria(), "conflict", "hasConflict", "ClientErrorException(Status.CONFLICT)");

        String executeOperationSourceCode = this.getExecuteOperationSourceCode(
                service.getQueryCriteria(),
                service.getVersionCriteria(),
                klassName);

        Optional<ServiceProjectionDispatch> projectionDispatch        = service.getProjectionDispatch();
        ServiceProjectionDispatch           serviceProjectionDispatch = projectionDispatch.get();
        Projection                          projection                = serviceProjectionDispatch.getProjection();

        DeepFetchProjectionListener deepFetchProjectionListener = new DeepFetchProjectionListener();
        ProjectionWalker.walk(projection, deepFetchProjectionListener);
        ImmutableList<String> deepFetchStrings = deepFetchProjectionListener.getResult();
        String deepFetchSourceCode = deepFetchStrings
                .collect(each -> "        result.deepFetch(" + each + ");\n")
                .makeString("");

        String orderBySourceCode = service.getOrderBy().map(this::getOrderBysSourceCode).orElse("");

        // @formatter:off
        //language=JAVA
        return ""
                + "    @Timed\n"
                + "    @ExceptionMetered\n"
                + "    @" + service.getVerb().name() + "\n"
                + "    @Path(\"" + url.getUrlString() + "\")" + queryParametersString + "\n"
                + "    @Produces(MediaType.APPLICATION_JSON)\n"
                + "    @JsonView(" + projection.getName() + "_JsonView.class)\n"
                + "    public " + returnType + " method" + index + "(" + parameterPrefix + parametersSourceCode + ")\n"
                + "    {\n"
                + "        // " + klassName + "\n"
                + "\n"
                + userPrincipalNameLocalVariable
                + queryOperationSourceCode
                + authorizeOperationSourceCode
                + validateOperationSourceCode
                + conflictOperationSourceCode
                + versionOperationSourceCode
                + "\n"
                + executeOperationSourceCode
                + "        // Deep fetch using projection " + projection.getName() + "\n"
                + deepFetchSourceCode
                + orderBySourceCode
                + "\n"
                + authorizePredicateSourceCode
                + validatePredicateSourceCode
                + conflictPredicateSourceCode
                + returnStatement
                + "    }\n";
        // @formatter:on
    }

    @Nonnull
    private String getPostSourceCode()
    {
        return "    // TODO: POST\n";
    }

    @Nonnull
    private String getDeleteSourceCode()
    {
        return "    // TODO: DELETE\n";
    }

    @Nonnull
    private String getPutSourceCode(Service service, int index)
    {
        Url url = service.getUrl();

        ServiceGroup serviceGroup = url.getServiceGroup();

        ImmutableList<ObjectBooleanPair<Parameter>> pathParameters = url.getPathParameters()
                .collectWith(PrimitiveTuples::pair, true);
        ImmutableList<ObjectBooleanPair<Parameter>> queryParameters = url.getQueryParameters()
                .collectWith(PrimitiveTuples::pair, false);

        String klassName = serviceGroup.getKlass().getName();

        String queryParametersString = queryParameters.isEmpty()
                ? ""
                : queryParameters.collect(ObjectBooleanPair::getOne).makeString(" // ?", "&", "");

        boolean hasAuthorizeCriteria = service.isAuthorizeClauseRequired();

        int     numParameters      = service.getNumParameters();
        boolean lineWrapParameters = numParameters > 1;

        String parameterPrefix = lineWrapParameters ? "\n" : "";
        String parameterIndent = lineWrapParameters ? "            " : "";

        ImmutableList<String> urlParameterStrings = pathParameters.newWithAll(queryParameters)
                .collectWith(this::getParameterSourceCode, parameterIndent);

        MutableList<String> parameterStrings = urlParameterStrings.toList();

        if (hasAuthorizeCriteria)
        {
            parameterStrings.add(parameterIndent + "@Context SecurityContext securityContext");
        }
        parameterStrings.add(parameterIndent + "@Nonnull @NotNull ObjectNode incomingInstance");

        String userPrincipalNameLocalVariable = hasAuthorizeCriteria
                ? "        String    userPrincipalName  = securityContext.getUserPrincipal().getName();\n"
                : "";

        String parametersSourceCode = parameterStrings.makeString(",\n");

        String finderName               = klassName + "Finder";
        String queryOperationSourceCode = this.getOperation(finderName, service.getQueryCriteria(), "query");
        String authorizeOperationSourceCode = this.getOperation(
                finderName,
                service.getAuthorizeCriteria(),
                "authorize");
        String validateOperationSourceCode = this.getOperation(
                finderName,
                service.getValidateCriteria(),
                "validate");
        String conflictOperationSourceCode = this.getOperation(
                finderName,
                service.getConflictCriteria(),
                "conflict");
        String versionOperationSourceCode = this.getOptionalOperation(
                finderName,
                service.getVersionCriteria(),
                "version");

        String authorizePredicateSourceCode = this.checkPredicate(
                service.getAuthorizeCriteria(),
                "authorize",
                "isAuthorized",
                "ForbiddenException()");
        String validatePredicateSourceCode = this.checkPredicate(
                service.getValidateCriteria(),
                "validate",
                "isValidated",
                "BadRequestException()");
        String conflictPredicateSourceCode = this.checkPredicate(
                service.getConflictCriteria(),
                "conflict",
                "hasConflict",
                "ClientErrorException(Status.CONFLICT)");

        String executeOperationSourceCode = this.getExecuteOperationSourceCode(
                service.getQueryCriteria(),
                service.getVersionCriteria(),
                klassName);

        ImmutableList<String> deepFetchStrings = DeepFetchWalker.walk(serviceGroup.getKlass());
        String deepFetchSourceCode = deepFetchStrings
                .collect(each -> "        result.deepFetch(" + each + ");\n")
                .makeString("");

        String orderBySourceCode = service.getOrderBy().map(this::getOrderBysSourceCode).orElse("");

        // @formatter:off
        //language=JAVA
        return ""
                + "    @Timed\n"
                + "    @ExceptionMetered\n"
                + "    @" + service.getVerb().name() + "\n"
                + "    @Path(\"" + url.getUrlString() + "\")" + queryParametersString + "\n"
                + "    @Produces(MediaType.APPLICATION_JSON)\n"
                + "    public void method" + index + "(" + parameterPrefix + parametersSourceCode + ")\n"
                + "    {\n"
                + "        // " + klassName + "\n"
                + "\n"
                + "        MutableList<String> errors = Lists.mutable.empty();\n"
                + "        JsonTypeCheckingValidator.validate(incomingInstance, " + this.applicationName + "DomainModel." + klassName + ", errors);\n"
                + "        RequiredPropertiesValidator.validate(\n"
                + "                " + this.applicationName + "DomainModel." + klassName + ",\n"
                + "                incomingInstance,\n"
                + "                OperationMode.REPLACE,\n"
                + "                errors);\n"
                + "\n"
                + "        if (errors.notEmpty())\n"
                + "        {\n"
                + "            Response response = Response\n"
                + "                    .status(Status.BAD_REQUEST)\n"
                + "                    .entity(errors)\n"
                + "                    .build();\n"
                + "            throw new BadRequestException(\"Incoming data failed validation.\", response);\n"
                + "        }\n"
                + "\n"
                + userPrincipalNameLocalVariable
                + queryOperationSourceCode
                + authorizeOperationSourceCode
                + validateOperationSourceCode
                + conflictOperationSourceCode
                + versionOperationSourceCode
                + "\n"
                + executeOperationSourceCode
                + deepFetchSourceCode
                + orderBySourceCode
                + "\n"
                + "        if (result.isEmpty())\n"
                + "        {\n"
                + "            throw new ClientErrorException(\"Url valid, data not found.\", Status.GONE);\n"
                + "        }\n"
                + "\n"
                + authorizePredicateSourceCode
                + validatePredicateSourceCode
                + conflictPredicateSourceCode
                + "\n"
                + "        if (result.size() > 1)\n"
                + "        {\n"
                + "            throw new InternalServerErrorException(\"TODO\");\n"
                + "        }\n"
                + "        Object persistentInstance = result.get(0);\n"
                + "\n"
                + "        IncomingUpdateDataModelValidator.validate(\n"
                + "                this.dataStore,\n"
                + "                " + this.applicationName + "DomainModel." + klassName + ",\n"
                + "                persistentInstance,\n"
                + "                incomingInstance,\n"
                + "                errors);\n"
                + "        if (errors.notEmpty())\n"
                + "        {\n"
                + "            Response response = Response\n"
                + "                    .status(Status.BAD_REQUEST)\n"
                + "                    .entity(errors)\n"
                + "                    .build();\n"
                + "            throw new BadRequestException(\"Incoming data failed validation.\", response);\n"
                + "        }\n"
                + "\n"
                + "        // TODO: Create a mutation context with now and the principal\n"
                + "        // TODO: Only increment version number if data actually changed\n"
                + "\n"
                + "        PersistentReplacer replacer = new PersistentReplacer(this.dataStore);\n"
                + "        replacer.synchronize(" + this.applicationName + "DomainModel." + klassName + ", persistentInstance, incomingInstance);\n"
                + "    }\n";
        // @formatter:on
    }

    @Nonnull
    private String getReturnStatement(ServiceMultiplicity serviceMultiplicity)
    {
        if (serviceMultiplicity == ServiceMultiplicity.MANY)
        {
            return "        return result;\n";
        }

        // TODO: throw a better error than 500 for getOnly

        // @formatter:off
        //language=JAVA
        return ""
                + "        if (result.isEmpty())\n"
                + "        {\n"
                + "            throw new ClientErrorException(\"Url valid, data not found.\", Status.GONE);\n"
                + "        }\n"
                + "        return Iterate.getOnly(result);\n";
        // @formatter:on
    }

    private String getParameterSourceCode(@Nonnull ObjectBooleanPair<Parameter> pair, String indent)
    {
        Parameter parameter       = pair.getOne();
        boolean   isPathParameter = pair.getTwo();

        // TODO: Hibernate validation annotations

        String nullableAnnotation = parameter.getMultiplicity() == Multiplicity.ZERO_TO_ONE
                ? "@Nullable "
                : "";

        DataType parameterType = parameter.getType();
        String typeString = parameter.getMultiplicity().isToMany()
                ? "Set<" + this.getParameterType(parameterType) + ">"
                : this.getParameterType(parameterType);

        return String.format(
                "%s%s@%s(\"%s\") %s %s",
                indent,
                nullableAnnotation,
                isPathParameter ? "PathParam" : "QueryParam",
                parameter.getName(),
                typeString,
                parameter.getName());
    }

    @Nonnull
    private String getOperation(
            String finderName,
            Optional<Criteria> optionalCriteria,
            String criteriaName)
    {
        return optionalCriteria
                .map(criteria -> this.getOperation(finderName, criteria, criteriaName))
                .orElse("");
    }

    @Nonnull
    private String getOptionalOperation(
            String finderName,
            Optional<Criteria> optionalCriteria,
            String criteriaName)
    {
        return optionalCriteria
                .map(criteria -> this.getOptionalOperation(finderName, criteria, criteriaName))
                .orElse("");
    }

    private String checkPredicate(
            Optional<Criteria> optionalCriteria,
            String criteriaName,
            String flagName,
            String exceptionName)
    {
        return optionalCriteria
                .map(criteria -> this.checkPredicate(criteriaName, flagName, exceptionName))
                .orElse("");
    }

    private String getExecuteOperationSourceCode(
            Optional<Criteria> queryCriteria,
            @Nonnull Optional<Criteria> versionCriteria,
            String klassName)
    {
        if (!queryCriteria.isPresent())
        {
            return "";
        }

        String versionClause = versionCriteria.isPresent() ? ".and(versionOperation)" : "";
        return MessageFormat.format(
                "        {0}List result = {0}Finder.findMany(queryOperation{1});\n",
                klassName,
                versionClause);
    }

    private String getOrderBysSourceCode(OrderBy orderBy)
    {
        ImmutableList<String> orderBySourceCodeClauses = orderBy.getOrderByMemberReferencePaths().collect(this::getOrderBySourceCode);

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < orderBySourceCodeClauses.size(); i++)
        {
            String orderBySourceCodeClause = orderBySourceCodeClauses.get(i);
            if (i == 0)
            {
                stringBuilder.append(orderBySourceCodeClause);
            }
            else
            {
                stringBuilder.append(".and(");
                stringBuilder.append(orderBySourceCodeClause);
                stringBuilder.append(")");
            }
        }

        return "\n        result.setOrderBy(" + stringBuilder + ");\n";
    }

    private String getOrderBySourceCode(OrderByMemberReferencePath orderByMemberReferencePath)
    {
        return this.getThisMemberReferencePathSourceCode(orderByMemberReferencePath.getThisMemberReferencePath())
                + this.getOrderByDirectionDeclarationSourceCode(orderByMemberReferencePath.getOrderByDirectionDeclaration());
    }

    private String getThisMemberReferencePathSourceCode(ThisMemberReferencePath thisMemberReferencePath)
    {
        if (thisMemberReferencePath.getAssociationEnds().notEmpty())
        {
            throw new AssertionError();
        }

        return String.format(
                "%sFinder.%s()",
                thisMemberReferencePath.getKlass().getName(),
                thisMemberReferencePath.getProperty().getName());
    }

    private String getOrderByDirectionDeclarationSourceCode(OrderByDirectionDeclaration orderByDirectionDeclaration)
    {
        OrderByDirection orderByDirection = orderByDirectionDeclaration.getOrderByDirection();
        switch (orderByDirection)
        {
            case ASCENDING:
                return ".ascendingOrderBy()";
            case DESCENDING:
                return ".descendingOrderBy()";
            default:
                throw new AssertionError();
        }
    }

    private String getParameterType(DataType dataType)
    {
        if (dataType instanceof Enumeration)
        {
            return ((Enumeration) dataType).getName();
        }
        if (dataType instanceof PrimitiveType)
        {
            return PrimitiveToJavaParameterTypeVisitor.getJavaType((PrimitiveType) dataType);
        }
        throw new AssertionError();
    }

    @Nonnull
    private String getOperation(String finderName, @Nonnull Criteria criteria, String criteriaName)
    {
        String operation           = this.getOperation(finderName, criteria);
        String comment             = this.getComment(criteria);
        String paddedOperationName = String.format("%-18s", criteriaName + "Operation");
        return comment
                + "        Operation " + paddedOperationName + " = " + operation + ";\n";
    }

    @Nonnull
    private String getOptionalOperation(String finderName, @Nonnull Criteria criteria, String criteriaName)
    {
        String operation           = this.getOperation(finderName, criteria);
        String comment             = this.getComment(criteria);
        String paddedOperationName = String.format("%-18s", criteriaName + "Operation");

        return comment
                + "        Operation " + paddedOperationName + " = " + criteriaName + " == null\n"
                + "                ? " + finderName + ".all()\n"
                + "                : " + operation + ";\n";
    }

    private String checkPredicate(String criteriaName, String flagName, String exceptionName)
    {
        // @formatter:off
        return ""
                + "        boolean " + flagName + " = !result.asEcList().allSatisfy(" + criteriaName + "Operation::matches);\n"
                + "        if (!" + flagName + ")\n"
                + "        {\n"
                + "            throw new " + exceptionName + ";\n"
                + "        }\n";

        // @formatter:on
    }

    @Nonnull
    private String getOperation(String finderName, Criteria criteria)
    {
        StringBuilder stringBuilder = new StringBuilder();
        criteria.visit(new OperationCriteriaVisitor(finderName, stringBuilder));
        return stringBuilder.toString();
    }

    @Nonnull
    private String getComment(@Nonnull Criteria criteria)
    {
        return "        // " + criteria.getSourceCode()
                .replaceAll("\\s+", " ")
                .replaceAll(" &&", "\n        //     &&")
                .replaceAll(" \\|\\|", "\n        //     ||")
                + "\n";
    }
}
