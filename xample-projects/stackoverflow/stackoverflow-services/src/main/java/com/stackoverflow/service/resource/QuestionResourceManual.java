package com.stackoverflow.service.resource;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.ClientErrorException;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.SecurityContext;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gs.fw.common.mithra.MithraObject;
import com.gs.fw.common.mithra.finder.Operation;
import com.gs.fw.common.mithra.list.merge.TopLevelMergeOptions;
import cool.klass.deserializer.json.IncomingDataModelValidator;
import cool.klass.deserializer.json.IncomingDataProjectionValidator;
import cool.klass.model.meta.domain.api.DomainModel;
import cool.klass.model.meta.domain.api.projection.Projection;
import cool.klass.serializer.json.ReladomoJsonTree;
import com.stackoverflow.Question;
import com.stackoverflow.QuestionFinder;
import com.stackoverflow.QuestionList;
import com.stackoverflow.QuestionVersion;
import com.stackoverflow.QuestionVersionFinder;
import com.stackoverflow.meta.constants.StackOverflowDomainModel;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.primitive.LongSets;
import org.eclipse.collections.impl.set.mutable.SetAdapter;
import org.eclipse.collections.impl.utility.Iterate;

@Path("/manual")
public class QuestionResourceManual
{
    private final DomainModel domainModel;

    public QuestionResourceManual(DomainModel domainModel)
    {
        this.domainModel = domainModel;
    }

    @Nonnull
    @Timed
    @ExceptionMetered
    @GET
    @Path("/api/question/{id}") // ?{version}
    @Produces(MediaType.APPLICATION_JSON)
    public ReladomoJsonTree method0(
            @PathParam("id") Long id,
            @Nullable @QueryParam("version") Integer version)
    {
        // TODO: Optional criteria

        // Question

        // this.id == id
        Operation queryOperation = QuestionFinder.id().eq(id);
        // this.system equalsEdgePoint && this.version.number == version
        Operation versionOperation = version == null
                ? QuestionFinder.all()
                : QuestionFinder.system().equalsEdgePoint().and(QuestionFinder.version().number().eq(version));

        QuestionList result = QuestionFinder.findMany(queryOperation.and(versionOperation));
        // Deep fetch using projection QuestionReadProjection
        result.deepFetch(QuestionFinder.answers());
        result.deepFetch(QuestionFinder.version());

        if (result.isEmpty())
        {
            throw new ClientErrorException("Url valid, data not found.", Status.GONE);
        }
        MithraObject mithraObject = Iterate.getOnly(result);

        Projection projection = StackOverflowDomainModel.QuestionReadProjection;
        return new ReladomoJsonTree(mithraObject, projection.getChildren());
    }

    @Timed
    @ExceptionMetered
    @PUT
    @Path("/api/question/{id}") // ?{version}
    @Produces(MediaType.APPLICATION_JSON)
    public void method1(
            @PathParam("id") Long id,
            @Nonnull @QueryParam("version") Optional<Integer> optionalVersion,
            @Nonnull @NotNull ObjectNode objectNode)
    {
        MutableList<String> errors = Lists.mutable.empty();
        IncomingDataModelValidator.validate(objectNode, StackOverflowDomainModel.Question, errors);
        IncomingDataProjectionValidator.validate(objectNode, StackOverflowDomainModel.QuestionWriteProjection, errors);
        if (errors.notEmpty())
        {
            Response response = Response
                    .status(Status.BAD_REQUEST)
                    .entity(errors)
                    .build();
            throw new BadRequestException("Incoming data failed validation.", response);
        }

        // Question

        // this.id == id
        Operation queryOperation = QuestionFinder.id().eq(id);
        // this.version.number == version
        Operation conflictOperation = optionalVersion
                .map(version -> QuestionFinder.version().number().eq(version))
                .orElse(QuestionFinder.all());

        QuestionList result = QuestionFinder.findMany(queryOperation);
        // Deep fetch using projection QuestionReadProjection
        result.deepFetch(QuestionFinder.answers());
        result.deepFetch(QuestionFinder.version());

        if (result.isEmpty())
        {
            throw new ClientErrorException("Url valid, data not found.", Status.GONE);
        }

        boolean hasConflict = !result.asEcList().allSatisfy(conflictOperation::matches);
        if (hasConflict)
        {
            throw new ClientErrorException(Status.CONFLICT);
        }

        if (result.size() > 1)
        {
            throw new InternalServerErrorException("TODO");
        }

        Question databaseQuestion = result.get(0);

        // Validate incoming json
        Question incomingQuestion = new Question();
        incomingQuestion.setId(id);

        incomingQuestion.setTitle(objectNode.get("title").textValue());
        incomingQuestion.setBody(objectNode.get("body").textValue());
        incomingQuestion.setStatus(objectNode.get("status").textValue());
        incomingQuestion.setDeleted(objectNode.get("deleted").booleanValue());

        incomingQuestion.setCreatedById("TODO");
        incomingQuestion.setCreatedOn(Timestamp.from(Instant.now()));
        incomingQuestion.setLastUpdatedById("TODO");

        QuestionVersion questionVersion  = databaseQuestion.getVersion().getDetachedCopy();
        questionVersion.setNumber(questionVersion.getNumber() + 1);
        incomingQuestion.setVersion(questionVersion);

        // AnswerList answers = new AnswerList();
        // for (JsonNode answerJsonNode : body.get("answers"))
        // {
        //     answers.add(convertJsonNode(answerJsonNode));
        // }
        // question.setAnswers(answers);

        // TODO: Version number stuff
        TopLevelMergeOptions<Question> mergeOptions = new TopLevelMergeOptions<>(QuestionFinder.getFinderInstance());
        mergeOptions.navigateTo(QuestionFinder.version());
        // TODO: Test dependent relationships (Projito?)
        // mergeOptions.navigateTo(AnswerFinder.question());

        QuestionList questions = new QuestionList();
        questions.add(incomingQuestion);

        result.merge(questions, mergeOptions);
    }

    @Nonnull
    @Timed
    @ExceptionMetered
    @DELETE
    @Path("/api/question/{id}") // ?{version}
    @Produces(MediaType.APPLICATION_JSON)
    public ReladomoJsonTree method2(
            @PathParam("id") Long id,
            @QueryParam("version") Integer version,
            @Nonnull @Context SecurityContext securityContext)
    {
        // Question

        String userPrincipalName = securityContext.getUserPrincipal().getName();
        // this.id == id
        Operation queryOperation = QuestionFinder.id().eq(id);
        // this.createdById == user
        Operation authorizeOperation = QuestionFinder.createdById().eq(userPrincipalName);
        // this.version.number == version
        Operation conflictOperation = QuestionFinder.version().number().eq(version);

        QuestionList result = QuestionFinder.findMany(queryOperation);
        // Deep fetch using projection QuestionWriteProjection

        boolean isAuthorized = !result.asEcList().allSatisfy(authorizeOperation::matches);
        if (!isAuthorized)
        {
            throw new ForbiddenException();
        }
        boolean hasConflict = !result.asEcList().allSatisfy(conflictOperation::matches);
        if (!hasConflict)
        {
            throw new ClientErrorException(Status.CONFLICT);
        }
        if (result.isEmpty())
        {
            throw new ClientErrorException("Url valid, data not found.", Status.GONE);
        }
        MithraObject mithraObject = Iterate.getOnly(result);

        Projection projection = StackOverflowDomainModel.QuestionWriteProjection;
        return new ReladomoJsonTree(mithraObject, projection.getChildren());
    }

    @Nonnull
    @Timed
    @ExceptionMetered
    @GET
    @Path("/api/question/in") // ?{ids}
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReladomoJsonTree> method3(@Nonnull @QueryParam("ids") Set<Long> ids)
    {
        // Question

        // this.id in ids
        Operation queryOperation = QuestionFinder.id().in(SetAdapter.adapt(ids).collectLong(
                x -> x,
                LongSets.mutable.empty()));

        QuestionList result = QuestionFinder.findMany(queryOperation);
        // Deep fetch using projection QuestionReadProjection
        result.deepFetch(QuestionFinder.answers());
        result.deepFetch(QuestionFinder.version());

        return this.applyProjection(result.asEcList(), StackOverflowDomainModel.QuestionReadProjection);
    }

    @Nonnull
    @Timed
    @ExceptionMetered
    @GET
    @Path("/api/question/firstTwo")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReladomoJsonTree> method4()
    {
        // Question

        // this.id in (1, 2)
        Operation queryOperation = QuestionFinder.id().in(LongSets.immutable.with(1, 2));

        QuestionList result = QuestionFinder.findMany(queryOperation);
        // Deep fetch using projection QuestionReadProjection
        result.deepFetch(QuestionFinder.answers());
        result.deepFetch(QuestionFinder.version());

        return this.applyProjection(result.asEcList(), StackOverflowDomainModel.QuestionReadProjection);
    }

    @Nonnull
    @Timed
    @ExceptionMetered
    @GET
    @Path("/api/question/{id}/version/{version}")
    @Produces(MediaType.APPLICATION_JSON)
    public ReladomoJsonTree method3(
            @PathParam("id") Long id,
            @PathParam("version") Integer version)
    {
        Operation queryOperation = QuestionFinder.id().eq(id)
                .and(QuestionFinder.system().equalsEdgePoint())
                .and(QuestionFinder.version().number().eq(version));
        Operation authorizeOperation = QuestionFinder.all();
        Operation validateOperation  = QuestionFinder.all();
        Operation conflictOperation  = QuestionFinder.all();

        QuestionList result = QuestionFinder.findMany(queryOperation);
        result.deepFetch(QuestionFinder.answers());
        result.deepFetch(QuestionFinder.version());
        // TODO: Deep fetch using projection QuestionWriteProjection

        boolean isAuthorized = !result.asEcList().allSatisfy(authorizeOperation::matches);
        boolean isValidated  = !result.asEcList().allSatisfy(validateOperation::matches);
        boolean hasConflict  = !result.asEcList().allSatisfy(conflictOperation::matches);
        if (result.isEmpty())
        {
            throw new ClientErrorException("Url valid, data not found.", Status.GONE);
        }
        MithraObject mithraObject = Iterate.getOnly(result);

        Projection projection = StackOverflowDomainModel.QuestionReadProjection;
        return new ReladomoJsonTree(mithraObject, projection.getChildren());
    }

    @Nonnull
    @Timed
    @ExceptionMetered
    @DELETE
    @Path("/api/question/{id}?{version}")
    @Produces(MediaType.APPLICATION_JSON)
    public ReladomoJsonTree method4(
            @PathParam("id") Long id,
            @QueryParam("version") Integer version,
            @Nonnull @Context SecurityContext securityContext)
    {
        String    userPrincipalName  = securityContext.getUserPrincipal().getName();
        Operation queryOperation     = QuestionFinder.id().eq(id);
        Operation authorizeOperation = QuestionFinder.createdById().eq(userPrincipalName);
        Operation validateOperation = QuestionFinder.id().eq(QuestionVersionFinder.id()).and(QuestionVersionFinder.number().eq(
                version));
        Operation conflictOperation = QuestionFinder.all();

        QuestionList result = QuestionFinder.findMany(queryOperation);
        // TODO: Deep fetch using projection QuestionWriteProjection

        boolean isAuthorized = !result.asEcList().allSatisfy(authorizeOperation::matches);
        boolean isValidated  = !result.asEcList().allSatisfy(validateOperation::matches);
        boolean hasConflict  = !result.asEcList().allSatisfy(conflictOperation::matches);
        if (result.isEmpty())
        {
            throw new ClientErrorException("Url valid, data not found.", Status.GONE);
        }
        MithraObject mithraObject = Iterate.getOnly(result);

        Projection projection = StackOverflowDomainModel.QuestionWriteProjection;
        return new ReladomoJsonTree(mithraObject, projection.getChildren());
    }

    @Nonnull
    @Timed
    @ExceptionMetered
    @POST
    @Path("/api/question")
    @Produces(MediaType.APPLICATION_JSON)
    public ReladomoJsonTree method5(ObjectNode objectNode)
    {
        MutableList<String> errors = Lists.mutable.empty();
        IncomingDataModelValidator.validate(objectNode, StackOverflowDomainModel.Question, errors);
        IncomingDataProjectionValidator.validate(objectNode, StackOverflowDomainModel.QuestionWriteProjection, errors);
        if (errors.notEmpty())
        {
            Response response = Response
                    .status(Status.BAD_REQUEST)
                    .entity(errors)
                    .build();
            throw new BadRequestException("Incoming data failed validation.", response);
        }

        Operation queryOperation     = QuestionFinder.all();
        Operation authorizeOperation = QuestionFinder.all();
        Operation validateOperation  = QuestionFinder.all();
        Operation conflictOperation  = QuestionFinder.all();

        QuestionList result = QuestionFinder.findMany(queryOperation);
        // TODO: Deep fetch using projection QuestionWriteProjection

        boolean isAuthorized = !result.asEcList().allSatisfy(authorizeOperation::matches);
        boolean isValidated  = !result.asEcList().allSatisfy(validateOperation::matches);
        boolean hasConflict  = !result.asEcList().allSatisfy(conflictOperation::matches);
        if (result.isEmpty())
        {
            throw new ClientErrorException("Url valid, data not found.", Status.GONE);
        }
        MithraObject mithraObject = Iterate.getOnly(result);

        Projection projection = StackOverflowDomainModel.QuestionWriteProjection;
        return new ReladomoJsonTree(mithraObject, projection.getChildren());
    }

    @Timed
    @ExceptionMetered
    @GET
    @Path("/api/question")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReladomoJsonTree> method6()
    {
        // Question

        // this.title startsWith "Why do"
        Operation queryOperation = QuestionFinder.title().startsWith("Why do");

        QuestionList result = QuestionFinder.findMany(queryOperation);
        // Deep fetch using projection QuestionReadProjection
        result.deepFetch(QuestionFinder.answers());
        result.deepFetch(QuestionFinder.version());

        return this.applyProjection(result.asEcList(), StackOverflowDomainModel.QuestionReadProjection);
    }

    @Timed
    @ExceptionMetered
    @GET
    @Path("/api/user/{userId}/questions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReladomoJsonTree> method7(@PathParam("userId") String userId)
    {
        // Question

        // this.createdById == userId
        Operation queryOperation = QuestionFinder.createdById().eq(userId);

        QuestionList result = QuestionFinder.findMany(queryOperation);
        // Deep fetch using projection QuestionWriteProjection

        return this.applyProjection(result.asEcList(), StackOverflowDomainModel.QuestionWriteProjection);
    }

    private List<ReladomoJsonTree> applyProjection(
            MutableList<? extends MithraObject> mithraObjects,
            @Nonnull Projection projection)
    {
        return mithraObjects.<ReladomoJsonTree>collect(mithraObject -> new ReladomoJsonTree(
                mithraObject,
                projection.getChildren()));
    }
}
