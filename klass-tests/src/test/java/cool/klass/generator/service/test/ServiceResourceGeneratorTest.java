package cool.klass.generator.service.test;

import cool.klass.generator.service.ServiceResourceGenerator;
import cool.klass.model.converter.compiler.CompilationUnit;
import cool.klass.model.converter.compiler.KlassCompiler;
import cool.klass.model.converter.compiler.error.CompilerError;
import cool.klass.model.converter.compiler.error.CompilerErrorHolder;
import cool.klass.model.meta.domain.DomainModel;
import cool.klass.model.meta.domain.service.ServiceGroup;
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
        //<editor-fold desc="source code">
        //language=Klass
        String sourceCodeText = "/*\n"
                + " * Simplified StackOverflow domain model. One question has many answers.\n"
                + " */\n"
                + "package com.stackoverflow\n"
                + "\n"
                + "// 'user' is just a special class that represents logged in users\n"
                + "// There must only be a single user class in the model, it must have a single key, and the key must be of type String. Other properties must be nullable\n"
                + "user User\n"
                + "    read\n"
                + "    systemTemporal\n"
                + "{\n"
                + "    // `read` keyword with no projection means create read services with the default read projection\n"
                + "    // The default read projection includes just this class and all its properties\n"
                + "    key userId        : String\n"
                + "\n"
                + "    // TODO: String max lengths\n"
                + "    firstName         : String?\n"
                + "    lastName          : String?\n"
                + "    email             : String?\n"
                + "}\n"
                + "\n"
                + "enumeration Status\n"
                + "{\n"
                + "    OPEN(\"Open\"),\n"
                + "    ON_HOLD(\"On hold\"),\n"
                + "    CLOSED(\"Closed\"),\n"
                + "}\n"
                + "\n"
                + "class Question\n"
                + "    read(QuestionReadProjection)\n"
                + "    create(QuestionWriteProjection)\n"
                + "    update(QuestionWriteProjection)\n"
                + "    systemTemporal\n"
                + "    versioned\n"
                + "    audited\n"
                + "    optimisticallyLocked\n"
                + "{\n"
                + "    key id            : ID\n"
                + "    title             : String\n"
                + "    body              : String\n"
                + "\n"
                + "    // TODO: Statemachine\n"
                + "    status            : Status\n"
                + "    deleted           : Boolean\n"
                + "\n"
                + "    // TODO: Consider type inference on paramterized property parameters based on usage in relationship\n"
                + "    // orderBy natural key ascending is the default\n"
                + "    answersWithSubstring(substring: String[1..1]): Answer[0..*]\n"
                + "        orderBy: this.id ascending\n"
                + "    {\n"
                + "        this.id == Answer.questionId\n"
                + "            && Answer.body contains substring\n"
                + "    }\n"
                + "\n"
                + "    activeAnswers(): Answer[0..*]\n"
                + "    {\n"
                + "        this.id == Answer.questionId\n"
                + "            && Answer.deleted == false\n"
                + "    }\n"
                + "\n"
                + "    // This isn't even a real property. It's referenced in criteria as a shorthand. For example Question.system == {time} is shorthand for Question.systemFrom <= {time} && Question.systemTo > {time}\n"
                + "    system            : TemporalRange\n"
                + "\n"
                + "    // These two properties are implied by systemTemporal, and are not supposed to actually be declared\n"
                + "    systemFrom        : TemporalInstant\n"
                + "    systemTo          : TemporalInstant\n"
                + "\n"
                + "    // These three properties and two parameterized properties are implied by audited, and are not supposed to actually be declared\n"
                + "    // They could automatically be part of read projections and not write projections\n"
                + "    createdById       : String\n"
                + "\n"
                + "    // createdOn, or createdDate?\n"
                + "    createdOn         : Instant\n"
                + "    lastUpdatedById   : String\n"
                + "\n"
                + "    createdBy(): User[1..1]\n"
                + "    {\n"
                + "        this.createdById == User.userId\n"
                + "    }\n"
                + "\n"
                + "    lastUpdatedBy(): User[1..1]\n"
                + "    {\n"
                + "        this.lastUpdatedById == User.userId\n"
                + "    }\n"
                + "}\n"
                + "\n"
                + "// The _Version class is implied by the versioned annotation, and not supposed to actually be declared\n"
                + "class QuestionVersion\n"
                + "    systemTemporal\n"
                + "{\n"
                + "    // Key properties copied from Question\n"
                + "    key id            : ID\n"
                + "\n"
                + "    // The version number\n"
                + "    // It gets incremented automatically when anything in the version project (QuestionWriteProjection) gets edited\n"
                + "    // TODO: Consider changing the primitive names to int32, int64, float32, float64, id32, id64\n"
                + "    number            : Integer\n"
                + "}\n"
                + "\n"
                + "association QuestionHasAnswer\n"
                + "{\n"
                + "    question: Question[1..1]\n"
                + "    answers: Answer[0..*]\n"
                + "        orderBy: this.id ascending\n"
                + "\n"
                + "    // ordering by primary key ascending is the default\n"
                + "    relationship this.id == Answer.questionId\n"
                + "}\n"
                + "\n"
                + "// The _HasVersion association is implied by the versioned annotation, and not supposed to actually be declared\n"
                + "// It could automatically be part of read projections and not write projections\n"
                + "association QuestionHasVersion\n"
                + "{\n"
                + "    question: Question[1..1]\n"
                + "    version: QuestionVersion[1..1]\n"
                + "\n"
                + "    // implied\n"
                + "    relationship this.id == QuestionVersion.id\n"
                + "            && this.system == QuestionVersion.system\n"
                + "}\n"
                + "\n"
                + "class Answer\n"
                + "    systemTemporal\n"
                + "    versioned\n"
                + "    audited\n"
                + "    optimisticallyLocked\n"
                + "{\n"
                + "    key id            : ID\n"
                + "    body              : String\n"
                + "    deleted           : Boolean\n"
                + "    private questionId: Long\n"
                + "}\n"
                + "\n"
                + "// TODO Model AnswerVote\n"
                + "// TODO Interface Vote\n"
                + "// TODO superclass AbstractVote\n"
                + "// TODO Aggregation for number of upvotes and downvotes\n"
                + "class QuestionVote\n"
                + "    systemTemporal\n"
                + "    audited\n"
                + "{\n"
                + "    key questionId    : Long\n"
                + "    key createdById   : String\n"
                + "    direction         : VoteDirection\n"
                + "}\n"
                + "\n"
                + "enumeration VoteDirection\n"
                + "{\n"
                + "    UP(\"up\"),\n"
                + "    DOWN(\"down\"),\n"
                + "}\n"
                + "\n"
                + "// TODO: Projection inheritance? QuestionReadProjection extends QuestionWriteProjection\n"
                + "projection QuestionReadProjection on Question\n"
                + "{\n"
                + "    // field names are \"includes\", field values are column headers when serializing to a tabular format\n"
                + "    title  : \"Question title\",\n"
                + "    body   : \"Question body\",\n"
                + "    answers:\n"
                + "    {\n"
                + "        body: \"Answer body\",\n"
                + "    },\n"
                + "}\n"
                + "\n"
                + "projection QuestionWriteProjection on Question\n"
                + "{\n"
                + "    title: \"Question title\",\n"
                + "    body : \"Question body\",\n"
                + "}\n"
                + "\n"
                + "// TODO: Type inference on projection paramters\n"
                + "/*\n"
                + "projection FilteredAnswersProjection(substring: String[1..1]) on Question\n"
                + "{\n"
                + "    title                          : \"Question title\",\n"
                + "    body                           : \"Question body\",\n"
                + "    answersWithSubstring(substring):\n"
                + "    {\n"
                + "        body: \"Answer body\",\n"
                + "    },\n"
                + "}\n"
                + "*/\n"
                + "\n"
                + "// Just embed inside the Question class?\n"
                + "service Question\n"
                + "{\n"
                + "    // TODO: matrix url params\n"
                + "    /*\n"
                + "    /api/question/{id: Long[1..1]}/{substring: String[1..1]}\n"
                + "        GET\n"
                + "        {\n"
                + "            multiplicity: many\n"
                + "            criteria    : this.id == id\n"
                + "            projection  : FilteredAnswersProjection(substring)\n"
                + "        }\n"
                + "    */\n"
                + "    /api/question/{titleSubstring: String[1..1]}\n"
                + "        GET\n"
                + "        {\n"
                + "            multiplicity: many\n"
                + "            criteria    : this.title startsWith titleSubstring\n"
                + "            projection  : QuestionReadProjection\n"
                + "        }\n"
                + "    /api/question/{id: Long[1..1]}\n"
                + "        GET\n"
                + "        {\n"
                + "            multiplicity: one\n"
                + "            criteria    : this.id == id\n"
                + "            projection  : QuestionReadProjection\n"
                + "        }\n"
                + "    /api/question/in/{id: Long[0..*]}\n"
                + "        GET\n"
                + "        {\n"
                + "            multiplicity: one\n"
                + "            criteria    : this.id in id\n"
                + "            projection  : QuestionReadProjection\n"
                + "        }\n"
                + "    /api/question/firstTwo\n"
                + "        GET\n"
                + "        {\n"
                + "            multiplicity: one\n"
                + "            criteria    : this.id in (1, 2)\n"
                + "            projection  : QuestionReadProjection\n"
                + "        }\n"
                + "    /api/question/{id: Long[1..1]}?{version: Integer[1..1]}\n"
                + "        PUT\n"
                + "        {\n"
                + "            // PUT, PATCH, and DELETE should implicitly get ?version={version} due to optimistic locking\n"
                + "            multiplicity: one\n"
                + "            criteria    : this.id == id\n"
                + "\n"
                + "            // TODO: Should differentiate between 'validate' 400 error and something like 'conflict' 409\n"
                + "            // Consider 412 Precondition Failed or 417 Expectation Failed instead of 400, and leave bad request for problems like invalid json, or mismatches with the meta model\n"
                + "            conflict    : this.id == QuestionVersion.id\n"
                + "                && QuestionVersion.number == version\n"
                + "            projection  : QuestionWriteProjection\n"
                + "        }\n"
                + "        DELETE\n"
                + "        {\n"
                + "            // PUT, PATCH, and DELETE should implicitly get ?version={version} due to optimistic locking\n"
                + "            multiplicity: one\n"
                + "            criteria    : this.id == id\n"
                + "\n"
                + "            // 'authorize' is like 'validate' but should give 401 Unauthorized or 403 forbidden instead of 400\n"
                + "            // TODO: conditions implemented in code, like `|| native(UserIsModeratorClass)`\n"
                + "            authorize   : this.createdById == user\n"
                + "\n"
                + "            // TODO: Should differentiate between 'validate' 400 error and something like 'conflict' 409\n"
                + "            validate    : this.id == QuestionVersion.id\n"
                + "                    && QuestionVersion.number == version\n"
                + "            projection  : QuestionWriteProjection\n"
                + "        }\n"
                + "    /api/question\n"
                + "        POST\n"
                + "        {\n"
                + "            multiplicity: one\n"
                + "            projection  : QuestionWriteProjection\n"
                + "        }\n"
                + "        GET\n"
                + "        {\n"
                + "            multiplicity: many\n"
                + "            criteria    : this.title startsWith \"Why do\"\n"
                + "            projection  : QuestionReadProjection\n"
                + "        }\n"
                + "\n"
                + "    // TODO: Type inference on url parameters\n"
                + "    /api/user/{userId: String[1..1]}/questions\n"
                + "        GET\n"
                + "        {\n"
                + "            //?page=1&pageSize=25\n"
                + "            multiplicity: many\n"
                + "            criteria    : this.createdById == userId\n"
                + "            projection  : QuestionWriteProjection\n"
                + "\n"
                + "            // Deliberately shallow\n"
                + "            orderBy     : this.createdOn\n"
                + "\n"
                + "            // pageSize: ???\n"
                + "            // page: ???\n"
                + "        }\n"
                + "}\n";
        //</editor-fold>

        CompilationUnit       compilationUnit = CompilationUnit.createFromText("example.klass", sourceCodeText);
        DomainModel           domainModel     = this.compiler.compile(compilationUnit);
        ImmutableList<String> compilerErrors  = this.compilerErrorHolder.getCompilerErrors().collect(CompilerError::toString);
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
                + "import com.stackoverflow.*;\n"
                + "import com.gs.fw.common.mithra.finder.Operation;\n"
                + "import com.gs.fw.common.mithra.util.serializer.*;\n"
                + "import org.eclipse.collections.api.set.primitive.*;\n"
                + "import org.eclipse.collections.impl.factory.primitive.*;\n"
                + "import org.eclipse.collections.impl.set.mutable.SetAdapter;\n"
                + "import org.eclipse.collections.impl.utility.Iterate;\n"
                + "\n"
                + "@Path(\"/\")\n"
                + "public class QuestionResource\n"
                + "{\n"
                + "    @GET\n"
                + "    @Path(\"/api/question/{titleSubstring}\")\n"
                + "    @Produces(MediaType.APPLICATION_JSON)\n"
                + "    public SerializedList<Question, QuestionList> method0(@PathParam(\"titleSubstring\") String titleSubstring)\n"
                + "    {\n"
                + "        Operation queryOperation     = QuestionFinder.title().startsWith(titleSubstring);\n"
                + "        Operation authorizeOperation = QuestionFinder.all();\n"
                + "        Operation validateOperation  = QuestionFinder.all();\n"
                + "        Operation conflictOperation  = QuestionFinder.all();\n"
                + "\n"
                + "        QuestionList result = QuestionFinder.findMany(queryOperation);\n"
                + "        // TODO: Deep fetch using projection QuestionReadProjection\n"
                + "\n"
                + "        boolean isAuthorized = !result.asEcList().allSatisfy(authorizeOperation::matches);\n"
                + "        boolean isValidated  = !result.asEcList().allSatisfy(validateOperation::matches);\n"
                + "        boolean hasConflict  = !result.asEcList().allSatisfy(conflictOperation::matches);\n"
                + "        SerializationConfig serializationConfig = SerializationConfig.withDeepFetchesFromList(\n"
                + "                QuestionFinder.getFinderInstance(),\n"
                + "                result);\n"
                + "        return new SerializedList<>(result, serializationConfig);\n"
                + "    }\n"
                + "\n"
                + "    @GET\n"
                + "    @Path(\"/api/question/{id}\")\n"
                + "    @Produces(MediaType.APPLICATION_JSON)\n"
                + "    public Serialized<Question> method1(@PathParam(\"id\") Long id)\n"
                + "    {\n"
                + "        Operation queryOperation     = QuestionFinder.id().eq(id);\n"
                + "        Operation authorizeOperation = QuestionFinder.all();\n"
                + "        Operation validateOperation  = QuestionFinder.all();\n"
                + "        Operation conflictOperation  = QuestionFinder.all();\n"
                + "\n"
                + "        QuestionList result = QuestionFinder.findMany(queryOperation);\n"
                + "        // TODO: Deep fetch using projection QuestionReadProjection\n"
                + "\n"
                + "        boolean isAuthorized = !result.asEcList().allSatisfy(authorizeOperation::matches);\n"
                + "        boolean isValidated  = !result.asEcList().allSatisfy(validateOperation::matches);\n"
                + "        boolean hasConflict  = !result.asEcList().allSatisfy(conflictOperation::matches);\n"
                + "        if (result.isEmpty())\n"
                + "        {\n"
                + "            throw new NotFoundException();\n"
                + "        }\n"
                + "        SerializationConfig serializationConfig = SerializationConfig.withDeepFetchesFromList(\n"
                + "                QuestionFinder.getFinderInstance(),\n"
                + "                result);\n"
                + "        return new Serialized<>(Iterate.getOnly(result), serializationConfig);\n"
                + "    }\n"
                + "\n"
                + "    @GET\n"
                + "    @Path(\"/api/question/in/{id}\")\n"
                + "    @Produces(MediaType.APPLICATION_JSON)\n"
                + "    public Serialized<Question> method2(@PathParam(\"id\") Set<Long> id)\n"
                + "    {\n"
                + "        Operation queryOperation     = QuestionFinder.id().in(SetAdapter.adapt(id).collectLong(x -> x, LongSets.mutable.empty()));\n"
                + "        Operation authorizeOperation = QuestionFinder.all();\n"
                + "        Operation validateOperation  = QuestionFinder.all();\n"
                + "        Operation conflictOperation  = QuestionFinder.all();\n"
                + "\n"
                + "        QuestionList result = QuestionFinder.findMany(queryOperation);\n"
                + "        // TODO: Deep fetch using projection QuestionReadProjection\n"
                + "\n"
                + "        boolean isAuthorized = !result.asEcList().allSatisfy(authorizeOperation::matches);\n"
                + "        boolean isValidated  = !result.asEcList().allSatisfy(validateOperation::matches);\n"
                + "        boolean hasConflict  = !result.asEcList().allSatisfy(conflictOperation::matches);\n"
                + "        if (result.isEmpty())\n"
                + "        {\n"
                + "            throw new NotFoundException();\n"
                + "        }\n"
                + "        SerializationConfig serializationConfig = SerializationConfig.withDeepFetchesFromList(\n"
                + "                QuestionFinder.getFinderInstance(),\n"
                + "                result);\n"
                + "        return new Serialized<>(Iterate.getOnly(result), serializationConfig);\n"
                + "    }\n"
                + "\n"
                + "    @GET\n"
                + "    @Path(\"/api/question/firstTwo\")\n"
                + "    @Produces(MediaType.APPLICATION_JSON)\n"
                + "    public Serialized<Question> method3()\n"
                + "    {\n"
                + "        Operation queryOperation     = QuestionFinder.id().in(LongSets.immutable.with(1, 2));\n"
                + "        Operation authorizeOperation = QuestionFinder.all();\n"
                + "        Operation validateOperation  = QuestionFinder.all();\n"
                + "        Operation conflictOperation  = QuestionFinder.all();\n"
                + "\n"
                + "        QuestionList result = QuestionFinder.findMany(queryOperation);\n"
                + "        // TODO: Deep fetch using projection QuestionReadProjection\n"
                + "\n"
                + "        boolean isAuthorized = !result.asEcList().allSatisfy(authorizeOperation::matches);\n"
                + "        boolean isValidated  = !result.asEcList().allSatisfy(validateOperation::matches);\n"
                + "        boolean hasConflict  = !result.asEcList().allSatisfy(conflictOperation::matches);\n"
                + "        if (result.isEmpty())\n"
                + "        {\n"
                + "            throw new NotFoundException();\n"
                + "        }\n"
                + "        SerializationConfig serializationConfig = SerializationConfig.withDeepFetchesFromList(\n"
                + "                QuestionFinder.getFinderInstance(),\n"
                + "                result);\n"
                + "        return new Serialized<>(Iterate.getOnly(result), serializationConfig);\n"
                + "    }\n"
                + "\n"
                + "    @PUT\n"
                + "    @Path(\"/api/question/{id}?{version}\")\n"
                + "    @Produces(MediaType.APPLICATION_JSON)\n"
                + "    public Serialized<Question> method4(\n"
                + "            @PathParam(\"id\") Long id,\n"
                + "            @QueryParam(\"version\") Integer version)\n"
                + "    {\n"
                + "        Operation queryOperation     = QuestionFinder.id().eq(id);\n"
                + "        Operation authorizeOperation = QuestionFinder.all();\n"
                + "        Operation validateOperation  = QuestionFinder.all();\n"
                + "        Operation conflictOperation  = QuestionFinder.id().eq(QuestionVersionFinder.id()).and(QuestionVersionFinder.number().eq(version));\n"
                + "\n"
                + "        QuestionList result = QuestionFinder.findMany(queryOperation);\n"
                + "        // TODO: Deep fetch using projection QuestionWriteProjection\n"
                + "\n"
                + "        boolean isAuthorized = !result.asEcList().allSatisfy(authorizeOperation::matches);\n"
                + "        boolean isValidated  = !result.asEcList().allSatisfy(validateOperation::matches);\n"
                + "        boolean hasConflict  = !result.asEcList().allSatisfy(conflictOperation::matches);\n"
                + "        if (result.isEmpty())\n"
                + "        {\n"
                + "            throw new NotFoundException();\n"
                + "        }\n"
                + "        SerializationConfig serializationConfig = SerializationConfig.withDeepFetchesFromList(\n"
                + "                QuestionFinder.getFinderInstance(),\n"
                + "                result);\n"
                + "        return new Serialized<>(Iterate.getOnly(result), serializationConfig);\n"
                + "    }\n"
                + "\n"
                + "    @DELETE\n"
                + "    @Path(\"/api/question/{id}?{version}\")\n"
                + "    @Produces(MediaType.APPLICATION_JSON)\n"
                + "    public Serialized<Question> method5(\n"
                + "            @PathParam(\"id\") Long id,\n"
                + "            @QueryParam(\"version\") Integer version,\n"
                + "            @Context SecurityContext securityContext)\n"
                + "    {\n"
                + "        String    userPrincipalName  = securityContext.getUserPrincipal().getName();\n"
                + "        Operation queryOperation     = QuestionFinder.id().eq(id);\n"
                + "        Operation authorizeOperation = QuestionFinder.createdById().eq(userPrincipalName);\n"
                + "        Operation validateOperation  = QuestionFinder.id().eq(QuestionVersionFinder.id()).and(QuestionVersionFinder.number().eq(version));\n"
                + "        Operation conflictOperation  = QuestionFinder.all();\n"
                + "\n"
                + "        QuestionList result = QuestionFinder.findMany(queryOperation);\n"
                + "        // TODO: Deep fetch using projection QuestionWriteProjection\n"
                + "\n"
                + "        boolean isAuthorized = !result.asEcList().allSatisfy(authorizeOperation::matches);\n"
                + "        boolean isValidated  = !result.asEcList().allSatisfy(validateOperation::matches);\n"
                + "        boolean hasConflict  = !result.asEcList().allSatisfy(conflictOperation::matches);\n"
                + "        if (result.isEmpty())\n"
                + "        {\n"
                + "            throw new NotFoundException();\n"
                + "        }\n"
                + "        SerializationConfig serializationConfig = SerializationConfig.withDeepFetchesFromList(\n"
                + "                QuestionFinder.getFinderInstance(),\n"
                + "                result);\n"
                + "        return new Serialized<>(Iterate.getOnly(result), serializationConfig);\n"
                + "    }\n"
                + "\n"
                + "    @POST\n"
                + "    @Path(\"/api/question\")\n"
                + "    @Produces(MediaType.APPLICATION_JSON)\n"
                + "    public Serialized<Question> method6()\n"
                + "    {\n"
                + "        Operation queryOperation     = QuestionFinder.all();\n"
                + "        Operation authorizeOperation = QuestionFinder.all();\n"
                + "        Operation validateOperation  = QuestionFinder.all();\n"
                + "        Operation conflictOperation  = QuestionFinder.all();\n"
                + "\n"
                + "        QuestionList result = QuestionFinder.findMany(queryOperation);\n"
                + "        // TODO: Deep fetch using projection QuestionWriteProjection\n"
                + "\n"
                + "        boolean isAuthorized = !result.asEcList().allSatisfy(authorizeOperation::matches);\n"
                + "        boolean isValidated  = !result.asEcList().allSatisfy(validateOperation::matches);\n"
                + "        boolean hasConflict  = !result.asEcList().allSatisfy(conflictOperation::matches);\n"
                + "        if (result.isEmpty())\n"
                + "        {\n"
                + "            throw new NotFoundException();\n"
                + "        }\n"
                + "        SerializationConfig serializationConfig = SerializationConfig.withDeepFetchesFromList(\n"
                + "                QuestionFinder.getFinderInstance(),\n"
                + "                result);\n"
                + "        return new Serialized<>(Iterate.getOnly(result), serializationConfig);\n"
                + "    }\n"
                + "\n"
                + "    @GET\n"
                + "    @Path(\"/api/question\")\n"
                + "    @Produces(MediaType.APPLICATION_JSON)\n"
                + "    public SerializedList<Question, QuestionList> method7()\n"
                + "    {\n"
                + "        Operation queryOperation     = QuestionFinder.title().startsWith(\"Why do\");\n"
                + "        Operation authorizeOperation = QuestionFinder.all();\n"
                + "        Operation validateOperation  = QuestionFinder.all();\n"
                + "        Operation conflictOperation  = QuestionFinder.all();\n"
                + "\n"
                + "        QuestionList result = QuestionFinder.findMany(queryOperation);\n"
                + "        // TODO: Deep fetch using projection QuestionReadProjection\n"
                + "\n"
                + "        boolean isAuthorized = !result.asEcList().allSatisfy(authorizeOperation::matches);\n"
                + "        boolean isValidated  = !result.asEcList().allSatisfy(validateOperation::matches);\n"
                + "        boolean hasConflict  = !result.asEcList().allSatisfy(conflictOperation::matches);\n"
                + "        SerializationConfig serializationConfig = SerializationConfig.withDeepFetchesFromList(\n"
                + "                QuestionFinder.getFinderInstance(),\n"
                + "                result);\n"
                + "        return new SerializedList<>(result, serializationConfig);\n"
                + "    }\n"
                + "\n"
                + "    @GET\n"
                + "    @Path(\"/api/user/{userId}/questions\")\n"
                + "    @Produces(MediaType.APPLICATION_JSON)\n"
                + "    public SerializedList<Question, QuestionList> method8(@PathParam(\"userId\") String userId)\n"
                + "    {\n"
                + "        Operation queryOperation     = QuestionFinder.createdById().eq(userId);\n"
                + "        Operation authorizeOperation = QuestionFinder.all();\n"
                + "        Operation validateOperation  = QuestionFinder.all();\n"
                + "        Operation conflictOperation  = QuestionFinder.all();\n"
                + "\n"
                + "        QuestionList result = QuestionFinder.findMany(queryOperation);\n"
                + "        // TODO: Deep fetch using projection QuestionWriteProjection\n"
                + "\n"
                + "        boolean isAuthorized = !result.asEcList().allSatisfy(authorizeOperation::matches);\n"
                + "        boolean isValidated  = !result.asEcList().allSatisfy(validateOperation::matches);\n"
                + "        boolean hasConflict  = !result.asEcList().allSatisfy(conflictOperation::matches);\n"
                + "        SerializationConfig serializationConfig = SerializationConfig.withDeepFetchesFromList(\n"
                + "                QuestionFinder.getFinderInstance(),\n"
                + "                result);\n"
                + "        return new SerializedList<>(result, serializationConfig);\n"
                + "    }\n"
                + "}\n";
        //</editor-fold>

        assertThat(serviceGroupSourceCode, serviceGroupSourceCode, is(expectedSourceCode));
    }
}
