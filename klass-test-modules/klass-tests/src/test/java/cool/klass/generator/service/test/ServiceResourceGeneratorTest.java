package cool.klass.generator.service.test;

import cool.klass.generator.service.ServiceResourceGenerator;
import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.converter.compiler.error.CompilerError;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.meta.domain.DomainModel;
import cool.klass.model.meta.domain.service.ServiceGroup;
import cool.klass.test.constants.KlassTestConstants;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ServiceResourceGeneratorTest
{
    private final CompilerErrorHolder compilerErrorHolder = new CompilerErrorHolder();
    private final KlassCompiler       compiler            = new KlassCompiler(this.compilerErrorHolder);

    @Test
    public void stackOverflow()
    {
        CompilationUnit compilationUnit = CompilationUnit.createFromText("example.klass",KlassTestConstants.STACK_OVERFLOW_SOURCE_CODE_TEXT);
        DomainModel           domainModel    = this.compiler.compile(compilationUnit);
        ImmutableList<String> compilerErrors = this.compilerErrorHolder.getCompilerErrors().collect(CompilerError::toString);
        assertThat(compilerErrors, is(Lists.immutable.empty()));
        assertThat(this.compilerErrorHolder.hasCompilerErrors(), is(false));
        assertThat(domainModel, notNullValue());

        ServiceResourceGenerator serviceResourceGenerator = new ServiceResourceGenerator(domainModel);
        ServiceGroup             serviceGroup             = domainModel.getServiceGroups().getOnly();

        String serviceGroupSourceCode = serviceResourceGenerator.getServiceGroupSourceCode(serviceGroup);

        //<editor-fold desc="expected java code">
        //language=JAVA
        String expectedSourceCode = ""
                + "package com.stackoverflow.service.resource;\n"
                + "\n"
                + "import java.util.List;\n"
                + "import java.util.Set;\n"
                + "\n"
                + "import javax.ws.rs.*;\n"
                + "import javax.ws.rs.core.*;\n"
                + "\n"
                + "import com.codahale.metrics.annotation.*;\n"
                + "import com.gs.fw.common.mithra.MithraObject;\n"
                + "import com.gs.fw.common.mithra.finder.Operation;\n"
                + "import cool.klass.model.meta.domain.DomainModel;\n"
                + "import cool.klass.model.meta.domain.projection.Projection;\n"
                + "import cool.klass.serializer.json.ReladomoJsonTree;\n"
                + "import com.stackoverflow.*;\n"
                + "import org.eclipse.collections.impl.factory.primitive.*;\n"
                + "import org.eclipse.collections.impl.set.mutable.SetAdapter;\n"
                + "import org.eclipse.collections.impl.utility.Iterate;\n"
                + "\n"
                + "/**\n"
                + " * Auto-generated by {@link cool.klass.generator.service.ServiceResourceGenerator}\n"
                + " */\n"
                + "@Path(\"/\")\n"
                + "public class QuestionResource\n"
                + "{\n"
                + "    private final DomainModel domainModel;\n"
                + "\n"
                + "    public QuestionResource(DomainModel domainModel)\n"
                + "    {\n"
                + "        this.domainModel = domainModel;\n"
                + "    }\n"
                + "\n"
                + "    @Timed\n"
                + "    @ExceptionMetered\n"
                + "    @GET\n"
                + "    @Path(\"/api/question/{titleSubstring}\")\n"
                + "    @Produces(MediaType.APPLICATION_JSON)\n"
                + "    public List<ReladomoJsonTree> method0(@PathParam(\"titleSubstring\") String titleSubstring)\n"
                + "    {\n"
                + "        // Question\n"
                + "\n"
                + "        // this.title startsWith titleSubstring\n"
                + "        Operation queryOperation     = QuestionFinder.title().startsWith(titleSubstring);\n"
                + "\n"
                + "        QuestionList result = QuestionFinder.findMany(queryOperation);\n"
                + "        // TODO: Deep fetch using projection QuestionReadProjection\n"
                + "\n"
                + "        Projection projection = this.domainModel.getProjectionByName(\"QuestionReadProjection\");\n"
                + "        return result.asEcList().<ReladomoJsonTree>collect(mithraObject -> new ReladomoJsonTree(\n"
                + "                mithraObject,\n"
                + "                projection.getProjectionMembers()));\n"
                + "    }\n"
                + "\n"
                + "    @Timed\n"
                + "    @ExceptionMetered\n"
                + "    @GET\n"
                + "    @Path(\"/api/question/{id}\")\n"
                + "    @Produces(MediaType.APPLICATION_JSON)\n"
                + "    public ReladomoJsonTree method1(@PathParam(\"id\") Long id)\n"
                + "    {\n"
                + "        // Question\n"
                + "\n"
                + "        // this.id == id\n"
                + "        Operation queryOperation     = QuestionFinder.id().eq(id);\n"
                + "\n"
                + "        QuestionList result = QuestionFinder.findMany(queryOperation);\n"
                + "        // TODO: Deep fetch using projection QuestionReadProjection\n"
                + "\n"
                + "        if (result.isEmpty())\n"
                + "        {\n"
                + "            throw new NotFoundException();\n"
                + "        }\n"
                + "        MithraObject mithraObject = Iterate.getOnly(result);\n"
                + "\n"
                + "        Projection projection = this.domainModel.getProjectionByName(\"QuestionReadProjection\");\n"
                + "        return new ReladomoJsonTree(mithraObject, projection.getProjectionMembers());\n"
                + "    }\n"
                + "\n"
                + "    @Timed\n"
                + "    @ExceptionMetered\n"
                + "    @GET\n"
                + "    @Path(\"/api/question/in/{id}\")\n"
                + "    @Produces(MediaType.APPLICATION_JSON)\n"
                + "    public ReladomoJsonTree method2(@PathParam(\"id\") Set<Long> id)\n"
                + "    {\n"
                + "        // Question\n"
                + "\n"
                + "        // this.id in id\n"
                + "        Operation queryOperation     = QuestionFinder.id().in(SetAdapter.adapt(id).collectLong(x -> x, LongSets.mutable.empty()));\n"
                + "\n"
                + "        QuestionList result = QuestionFinder.findMany(queryOperation);\n"
                + "        // TODO: Deep fetch using projection QuestionReadProjection\n"
                + "\n"
                + "        if (result.isEmpty())\n"
                + "        {\n"
                + "            throw new NotFoundException();\n"
                + "        }\n"
                + "        MithraObject mithraObject = Iterate.getOnly(result);\n"
                + "\n"
                + "        Projection projection = this.domainModel.getProjectionByName(\"QuestionReadProjection\");\n"
                + "        return new ReladomoJsonTree(mithraObject, projection.getProjectionMembers());\n"
                + "    }\n"
                + "\n"
                + "    @Timed\n"
                + "    @ExceptionMetered\n"
                + "    @GET\n"
                + "    @Path(\"/api/question/firstTwo\")\n"
                + "    @Produces(MediaType.APPLICATION_JSON)\n"
                + "    public ReladomoJsonTree method3()\n"
                + "    {\n"
                + "        // Question\n"
                + "\n"
                + "        // this.id in (1, 2)\n"
                + "        Operation queryOperation     = QuestionFinder.id().in(LongSets.immutable.with(1, 2));\n"
                + "\n"
                + "        QuestionList result = QuestionFinder.findMany(queryOperation);\n"
                + "        // TODO: Deep fetch using projection QuestionReadProjection\n"
                + "\n"
                + "        if (result.isEmpty())\n"
                + "        {\n"
                + "            throw new NotFoundException();\n"
                + "        }\n"
                + "        MithraObject mithraObject = Iterate.getOnly(result);\n"
                + "\n"
                + "        Projection projection = this.domainModel.getProjectionByName(\"QuestionReadProjection\");\n"
                + "        return new ReladomoJsonTree(mithraObject, projection.getProjectionMembers());\n"
                + "    }\n"
                + "\n"
                + "    @Timed\n"
                + "    @ExceptionMetered\n"
                + "    @PUT\n"
                + "    @Path(\"/api/question/{id}\")// ?{version}\n"
                + "    @Produces(MediaType.APPLICATION_JSON)\n"
                + "    public ReladomoJsonTree method4(\n"
                + "            @PathParam(\"id\") Long id,\n"
                + "            @QueryParam(\"version\") Integer version)\n"
                + "    {\n"
                + "        // Question\n"
                + "\n"
                + "        // this.id == id\n"
                + "        Operation queryOperation     = QuestionFinder.id().eq(id);\n"
                + "        // this.id == QuestionVersion.id && QuestionVersion.number == version\n"
                + "        Operation conflictOperation  = QuestionFinder.id().eq(QuestionVersionFinder.id()).and(QuestionVersionFinder.number().eq(version));\n"
                + "\n"
                + "        QuestionList result = QuestionFinder.findMany(queryOperation);\n"
                + "        // TODO: Deep fetch using projection QuestionWriteProjection\n"
                + "\n"
                + "        boolean ishasConflict = !result.asEcList().allSatisfy(conflictOperation::matches);\n"
                + "        if (!ishasConflict)\n"
                + "        {\n"
                + "            throw new ClientErrorException(Response.Status.CONFLICT);\n"
                + "        }\n"
                + "        if (result.isEmpty())\n"
                + "        {\n"
                + "            throw new NotFoundException();\n"
                + "        }\n"
                + "        MithraObject mithraObject = Iterate.getOnly(result);\n"
                + "\n"
                + "        Projection projection = this.domainModel.getProjectionByName(\"QuestionWriteProjection\");\n"
                + "        return new ReladomoJsonTree(mithraObject, projection.getProjectionMembers());\n"
                + "    }\n"
                + "\n"
                + "    @Timed\n"
                + "    @ExceptionMetered\n"
                + "    @DELETE\n"
                + "    @Path(\"/api/question/{id}\")// ?{version}\n"
                + "    @Produces(MediaType.APPLICATION_JSON)\n"
                + "    public ReladomoJsonTree method5(\n"
                + "            @PathParam(\"id\") Long id,\n"
                + "            @QueryParam(\"version\") Integer version,\n"
                + "            @Context SecurityContext securityContext)\n"
                + "    {\n"
                + "        // Question\n"
                + "\n"
                + "        String    userPrincipalName  = securityContext.getUserPrincipal().getName();\n"
                + "        // this.id == id\n"
                + "        Operation queryOperation     = QuestionFinder.id().eq(id);\n"
                + "        // this.createdById == user\n"
                + "        Operation authorizeOperation = QuestionFinder.createdById().eq(userPrincipalName);\n"
                + "        // this.id == QuestionVersion.id && QuestionVersion.number == version\n"
                + "        Operation conflictOperation  = QuestionFinder.id().eq(QuestionVersionFinder.id()).and(QuestionVersionFinder.number().eq(version));\n"
                + "\n"
                + "        QuestionList result = QuestionFinder.findMany(queryOperation);\n"
                + "        // TODO: Deep fetch using projection QuestionWriteProjection\n"
                + "\n"
                + "        boolean isisAuthorized = !result.asEcList().allSatisfy(authorizeOperation::matches);\n"
                + "        if (!isisAuthorized)\n"
                + "        {\n"
                + "            throw new ForbiddenException();\n"
                + "        }\n"
                + "        boolean ishasConflict = !result.asEcList().allSatisfy(conflictOperation::matches);\n"
                + "        if (!ishasConflict)\n"
                + "        {\n"
                + "            throw new ClientErrorException(Response.Status.CONFLICT);\n"
                + "        }\n"
                + "        if (result.isEmpty())\n"
                + "        {\n"
                + "            throw new NotFoundException();\n"
                + "        }\n"
                + "        MithraObject mithraObject = Iterate.getOnly(result);\n"
                + "\n"
                + "        Projection projection = this.domainModel.getProjectionByName(\"QuestionWriteProjection\");\n"
                + "        return new ReladomoJsonTree(mithraObject, projection.getProjectionMembers());\n"
                + "    }\n"
                + "\n"
                + "\n"
                + "    @Timed\n"
                + "    @ExceptionMetered\n"
                + "    @GET\n"
                + "    @Path(\"/api/question\")\n"
                + "    @Produces(MediaType.APPLICATION_JSON)\n"
                + "    public List<ReladomoJsonTree> method7()\n"
                + "    {\n"
                + "        // Question\n"
                + "\n"
                + "        // this.title startsWith \"Why do\"\n"
                + "        Operation queryOperation     = QuestionFinder.title().startsWith(\"Why do\");\n"
                + "\n"
                + "        QuestionList result = QuestionFinder.findMany(queryOperation);\n"
                + "        // TODO: Deep fetch using projection QuestionReadProjection\n"
                + "\n"
                + "        Projection projection = this.domainModel.getProjectionByName(\"QuestionReadProjection\");\n"
                + "        return result.asEcList().<ReladomoJsonTree>collect(mithraObject -> new ReladomoJsonTree(\n"
                + "                mithraObject,\n"
                + "                projection.getProjectionMembers()));\n"
                + "    }\n"
                + "\n"
                + "    @Timed\n"
                + "    @ExceptionMetered\n"
                + "    @GET\n"
                + "    @Path(\"/api/user/{userId}/questions\")\n"
                + "    @Produces(MediaType.APPLICATION_JSON)\n"
                + "    public List<ReladomoJsonTree> method8(@PathParam(\"userId\") String userId)\n"
                + "    {\n"
                + "        // Question\n"
                + "\n"
                + "        // this.createdById == userId\n"
                + "        Operation queryOperation     = QuestionFinder.createdById().eq(userId);\n"
                + "\n"
                + "        QuestionList result = QuestionFinder.findMany(queryOperation);\n"
                + "        // TODO: Deep fetch using projection QuestionWriteProjection\n"
                + "\n"
                + "        Projection projection = this.domainModel.getProjectionByName(\"QuestionWriteProjection\");\n"
                + "        return result.asEcList().<ReladomoJsonTree>collect(mithraObject -> new ReladomoJsonTree(\n"
                + "                mithraObject,\n"
                + "                projection.getProjectionMembers()));\n"
                + "    }\n"
                + "}\n";
        //</editor-fold>

        assertThat(serviceGroupSourceCode, serviceGroupSourceCode, is(expectedSourceCode));
    }
}
