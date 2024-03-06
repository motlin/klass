package cool.klass.generator.service.test;

import java.util.Optional;

import cool.klass.generator.service.ServiceResourceGenerator;
import cool.klass.model.converter.compiler.CompilationResult;
import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.CompilerState;
import cool.klass.model.converter.compiler.DomainModelCompilationResult;
import cool.klass.model.converter.compiler.ErrorsCompilationResult;
import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.converter.compiler.error.RootCompilerError;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.service.ServiceGroup;
import cool.klass.test.constants.KlassTestConstants;
import org.eclipse.collections.api.list.ImmutableList;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

public class ServiceResourceGeneratorTest
{
    @Test
    public void stackOverflow()
    {
        CompilationUnit compilationUnit = CompilationUnit.createFromText(
                Optional.empty(),
                "example.klass",
                KlassTestConstants.STACK_OVERFLOW_SOURCE_CODE_TEXT);
        CompilerState     compilerState     = new CompilerState(compilationUnit);
        KlassCompiler     compiler          = new KlassCompiler(compilerState);
        CompilationResult compilationResult = compiler.compile();

        if (compilationResult instanceof ErrorsCompilationResult)
        {
            ErrorsCompilationResult          errorsCompilationResult = (ErrorsCompilationResult) compilationResult;
            ImmutableList<RootCompilerError> compilerErrors          = errorsCompilationResult.getCompilerErrors();
            String                           message                 = compilerErrors.makeString("\n");
            fail(message);
        }
        else if (compilationResult instanceof DomainModelCompilationResult)
        {
            DomainModelCompilationResult domainModelCompilationResult = (DomainModelCompilationResult) compilationResult;
            DomainModel                  domainModel                  = domainModelCompilationResult.getDomainModel();
            assertThat(domainModel, notNullValue());

            ServiceResourceGenerator serviceResourceGenerator = new ServiceResourceGenerator(
                    domainModel,
                    "StackOverflow",
                    "com.stackoverflow");

            ServiceGroup serviceGroup           = domainModel.getServiceGroups().getOnly();
            String       serviceGroupSourceCode = serviceResourceGenerator.getServiceGroupSourceCode(serviceGroup);

            //<editor-fold desc="expected java code">
            //language=JAVA
            String expectedSourceCode = ""
                    + "package com.stackoverflow.service.resource;\n"
                    + "\n"
                    + "import java.sql.Timestamp;\n"
                    + "import java.time.Clock;\n"
                    + "import java.time.Instant;\n"
                    + "import java.util.List;\n"
                    + "import java.util.Objects;\n"
                    + "import java.util.Optional;\n"
                    + "import java.util.Set;\n"
                    + "\n"
                    + "import javax.annotation.*;\n"
                    + "import javax.ws.rs.*;\n"
                    + "import javax.ws.rs.core.*;\n"
                    + "import javax.ws.rs.core.Response.Status;\n"
                    + "\n"
                    + "import com.stackoverflow.*;\n"
                    + "import com.stackoverflow.json.view.*;\n"
                    + "import com.codahale.metrics.annotation.*;\n"
                    + "import com.fasterxml.jackson.annotation.JsonView;\n"
                    + "import com.gs.fw.common.mithra.finder.*;\n"
                    + "import cool.klass.data.store.*;\n"
                    + "import cool.klass.model.meta.domain.api.DomainModel;\n"
                    + "import cool.klass.model.meta.domain.api.Klass;\n"
                    + "import javax.validation.constraints.NotNull;\n"
                    + "import com.fasterxml.jackson.databind.node.ObjectNode;\n"
                    + "import cool.klass.deserializer.json.*;\n"
                    + "import cool.klass.reladomo.persistent.writer.*;\n"
                    + "\n"
                    + "import org.eclipse.collections.api.factory.Maps;\n"
                    + "import org.eclipse.collections.api.list.MutableList;\n"
                    + "import org.eclipse.collections.impl.factory.Lists;\n"
                    + "import org.eclipse.collections.impl.factory.primitive.LongSets;\n"
                    + "import org.eclipse.collections.impl.set.mutable.*;\n"
                    + "import org.eclipse.collections.impl.utility.*;\n"
                    + "\n"
                    + "/**\n"
                    + " * Auto-generated by {@link cool.klass.generator.service.ServiceResourceGenerator}\n"
                    + " */\n"
                    + "@Path(\"/\")\n"
                    + "public class QuestionResource\n"
                    + "{\n"
                    + "    @Nonnull\n"
                    + "    private final DomainModel domainModel;\n"
                    + "    @Nonnull\n"
                    + "    private final DataStore   dataStore;\n"
                    + "    @Nonnull\n"
                    + "    private final Clock       clock;\n"
                    + "\n"
                    + "    public QuestionResource(\n"
                    + "            @Nonnull DomainModel domainModel,\n"
                    + "            @Nonnull DataStore dataStore,\n"
                    + "            @Nonnull Clock clock)\n"
                    + "    {\n"
                    + "        this.domainModel = Objects.requireNonNull(domainModel);\n"
                    + "        this.dataStore   = Objects.requireNonNull(dataStore);\n"
                    + "        this.clock       = Objects.requireNonNull(clock);\n"
                    + "    }\n"
                    + "\n"
                    + "    @Timed\n"
                    + "    @ExceptionMetered\n"
                    + "    @GET\n"
                    + "    @Path(\"/question/{id}\")\n"
                    + "    @Produces(MediaType.APPLICATION_JSON)\n"
                    + "    @JsonView(QuestionReadProjection_JsonView.class)\n"
                    + "    public Question method0(\n"
                    + "            @PathParam(\"id\") Long id)\n"
                    + "    {\n"
                    + "        // Question\n"
                    + "\n"
                    + "        // this.id == id\n"
                    + "        Operation queryOperation     = QuestionFinder.id().eq(id);\n"
                    + "\n"
                    + "        QuestionList result = QuestionFinder.findMany(queryOperation);\n"
                    + "        // Deep fetch using projection QuestionReadProjection\n"
                    + "        result.deepFetch(QuestionFinder.answers());\n"
                    + "        result.deepFetch(QuestionFinder.tags().tag());\n"
                    + "        result.deepFetch(QuestionFinder.version());\n"
                    + "\n"
                    + "        if (result.isEmpty())\n"
                    + "        {\n"
                    + "            throw new ClientErrorException(\"Url valid, data not found.\", Status.GONE);\n"
                    + "        }\n"
                    + "        return Iterate.getOnly(result);\n"
                    + "    }\n"
                    + "\n"
                    + "    @Timed\n"
                    + "    @ExceptionMetered\n"
                    + "    @PUT\n"
                    + "    @Path(\"/question/{id}\")\n"
                    + "    @Produces(MediaType.APPLICATION_JSON)\n"
                    + "    public void method1(\n"
                    + "            @PathParam(\"id\") Long id,\n"
                    + "            @Nonnull @NotNull ObjectNode incomingInstance)\n"
                    + "    {\n"
                    + "        Klass klass = this.domainModel.getClassByName(\"Question\");\n"
                    + "\n"
                    + "        MutableList<String> errors = Lists.mutable.empty();\n"
                    + "        MutableList<String> warnings = Lists.mutable.empty();\n"
                    + "        JsonTypeCheckingValidator.validate(incomingInstance, klass, errors);\n"
                    + "        RequiredPropertiesValidator.validate(\n"
                    + "                klass,\n"
                    + "                incomingInstance,\n"
                    + "                OperationMode.REPLACE,\n"
                    + "                errors,\n"
                    + "                warnings);\n"
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
                    + "        if (warnings.notEmpty())\n"
                    + "        {\n"
                    + "            Response response = Response\n"
                    + "                    .status(Status.BAD_REQUEST)\n"
                    + "                    .entity(warnings)\n"
                    + "                    .build();\n"
                    + "            throw new BadRequestException(\"Incoming data failed validation.\", response);\n"
                    + "        }\n"
                    + "\n"
                    + "        // this.id == id\n"
                    + "        Operation queryOperation     = QuestionFinder.id().eq(id);\n"
                    + "\n"
                    + "        QuestionList result = QuestionFinder.findMany(queryOperation);\n"
                    + "        result.deepFetch(QuestionFinder.tags());\n"
                    + "        result.deepFetch(QuestionFinder.version());\n"
                    + "\n"
                    + "        if (result.isEmpty())\n"
                    + "        {\n"
                    + "            throw new ClientErrorException(\"Url valid, data not found.\", Status.GONE);\n"
                    + "        }\n"
                    + "\n"
                    + "\n"
                    + "        if (result.size() > 1)\n"
                    + "        {\n"
                    + "            throw new InternalServerErrorException(\"TODO\");\n"
                    + "        }\n"
                    + "        Object persistentInstance = result.get(0);\n"
                    + "\n"
                    + "        IncomingUpdateDataModelValidator.validate(\n"
                    + "                this.dataStore,\n"
                    + "                klass,\n"
                    + "                persistentInstance,\n"
                    + "                incomingInstance,\n"
                    + "                errors,\n"
                    + "                warnings);\n"
                    + "        if (errors.notEmpty())\n"
                    + "        {\n"
                    + "            Response response = Response\n"
                    + "                    .status(Status.BAD_REQUEST)\n"
                    + "                    .entity(errors)\n"
                    + "                    .build();\n"
                    + "            throw new BadRequestException(\"Incoming data failed validation.\", response);\n"
                    + "        }\n"
                    + "        if (warnings.notEmpty())\n"
                    + "        {\n"
                    + "            Response response = Response\n"
                    + "                    .status(Status.BAD_REQUEST)\n"
                    + "                    .entity(warnings)\n"
                    + "                    .build();\n"
                    + "            throw new BadRequestException(\"Incoming data failed validation.\", response);\n"
                    + "        }\n"
                    + "\n"
                    + "        Instant            transactionInstant = Instant.now(this.clock);\n"
                    + "        MutationContext    mutationContext    = new MutationContext(Optional.empty(), transactionInstant);\n"
                    + "        PersistentReplacer replacer           = new PersistentReplacer(mutationContext, this.dataStore);\n"
                    + "        replacer.synchronize(klass, persistentInstance, incomingInstance);\n"
                    + "    }\n"
                    + "\n"
                    + "    @Timed\n"
                    + "    @ExceptionMetered\n"
                    + "    @DELETE\n"
                    + "    @Path(\"/question/{id}\")\n"
                    + "    @Produces(MediaType.APPLICATION_JSON)\n"
                    + "    public void method2(\n"
                    + "            @PathParam(\"id\") Long id,\n"
                    + "            @Context SecurityContext securityContext)\n"
                    + "    {\n"
                    + "        Klass klass = this.domainModel.getClassByName(\"Question\");\n"
                    + "        String    userPrincipalName  = securityContext.getUserPrincipal().getName();\n"
                    + "        // this.id == id\n"
                    + "        Operation queryOperation     = QuestionFinder.id().eq(id);\n"
                    + "        // this.createdById == user\n"
                    + "        Operation authorizeOperation = QuestionFinder.createdById().eq(userPrincipalName);\n"
                    + "\n"
                    + "        QuestionList result = QuestionFinder.findMany(queryOperation);\n"
                    + "        result.deepFetch(QuestionFinder.tags());\n"
                    + "        result.deepFetch(QuestionFinder.version());\n"
                    + "\n"
                    + "        if (result.isEmpty())\n"
                    + "        {\n"
                    + "            throw new ClientErrorException(\"Url valid, data not found.\", Status.GONE);\n"
                    + "        }\n"
                    + "\n"
                    + "        boolean isAuthorized = !result.asEcList().allSatisfy(authorizeOperation::matches);\n"
                    + "        if (!isAuthorized)\n"
                    + "        {\n"
                    + "            throw new ForbiddenException();\n"
                    + "        }\n"
                    + "\n"
                    + "        if (result.size() > 1)\n"
                    + "        {\n"
                    + "            throw new InternalServerErrorException(\"TODO\");\n"
                    + "        }\n"
                    + "\n"
                    + "        Object persistentInstance = result.get(0);\n"
                    + "\n"
                    + "        // TODO: Create a mutation context with now and the principal\n"
                    + "        Instant           transactionInstant = Instant.now(this.clock);\n"
                    + "        MutationContext   mutationContext    = new MutationContext(Optional.empty(), transactionInstant);\n"
                    + "        PersistentDeleter deleter            = new PersistentDeleter(mutationContext, this.dataStore);\n"
                    + "        deleter.deleteOrTerminate(klass, persistentInstance);\n"
                    + "    }\n"
                    + "\n"
                    + "    @Timed\n"
                    + "    @ExceptionMetered\n"
                    + "    @GET\n"
                    + "    @Path(\"/question/in\") // ?{ids}\n"
                    + "    @Produces(MediaType.APPLICATION_JSON)\n"
                    + "    @JsonView(QuestionReadProjection_JsonView.class)\n"
                    + "    public List<Question> method3(@QueryParam(\"ids\") Set<Long> ids)\n"
                    + "    {\n"
                    + "        // Question\n"
                    + "\n"
                    + "        // this.id in ids\n"
                    + "        Operation queryOperation     = QuestionFinder.id().in(SetAdapter.adapt(ids).collectLong(x -> x, LongSets.mutable.empty()));\n"
                    + "\n"
                    + "        QuestionList result = QuestionFinder.findMany(queryOperation);\n"
                    + "        // Deep fetch using projection QuestionReadProjection\n"
                    + "        result.deepFetch(QuestionFinder.answers());\n"
                    + "        result.deepFetch(QuestionFinder.tags().tag());\n"
                    + "        result.deepFetch(QuestionFinder.version());\n"
                    + "\n"
                    + "        return result;\n"
                    + "    }\n"
                    + "\n"
                    + "    // TODO: POST\n"
                    + "\n"
                    + "    @Timed\n"
                    + "    @ExceptionMetered\n"
                    + "    @GET\n"
                    + "    @Path(\"/user/{userId}/questions\")\n"
                    + "    @Produces(MediaType.APPLICATION_JSON)\n"
                    + "    @JsonView(QuestionReadProjection_JsonView.class)\n"
                    + "    public List<Question> method5(@PathParam(\"userId\") String userId)\n"
                    + "    {\n"
                    + "        // Question\n"
                    + "\n"
                    + "        // this.createdById == userId\n"
                    + "        Operation queryOperation     = QuestionFinder.createdById().eq(userId);\n"
                    + "\n"
                    + "        QuestionList result = QuestionFinder.findMany(queryOperation);\n"
                    + "        // Deep fetch using projection QuestionReadProjection\n"
                    + "        result.deepFetch(QuestionFinder.answers());\n"
                    + "        result.deepFetch(QuestionFinder.tags().tag());\n"
                    + "        result.deepFetch(QuestionFinder.version());\n"
                    + "\n"
                    + "        result.setOrderBy(QuestionFinder.createdOn().ascendingOrderBy());\n"
                    + "\n"
                    + "        return result;\n"
                    + "    }\n"
                    + "}\n";
            //</editor-fold>

            assertThat(serviceGroupSourceCode, serviceGroupSourceCode, is(expectedSourceCode));
        }
        else
        {
            fail(compilationResult.getClass().getSimpleName());
        }
    }
}
