package com.stackoverflow.service.resource;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Objects;
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
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.gs.fw.common.mithra.MithraManagerProvider;
import com.gs.fw.common.mithra.finder.Operation;
import cool.klass.data.store.DataStore;
import cool.klass.deserializer.json.JsonTypeCheckingValidator;
import cool.klass.deserializer.json.OperationMode;
import cool.klass.deserializer.json.RequiredPropertiesValidator;
import cool.klass.reladomo.persistent.writer.IncomingUpdateDataModelValidator;
import cool.klass.reladomo.persistent.writer.MutationContext;
import cool.klass.reladomo.persistent.writer.PersistentCreator;
import cool.klass.reladomo.persistent.writer.PersistentReplacer;
import com.stackoverflow.Question;
import com.stackoverflow.QuestionFinder;
import com.stackoverflow.QuestionList;
import com.stackoverflow.QuestionVersionFinder;
import com.stackoverflow.json.view.QuestionReadProjection_JsonView;
import com.stackoverflow.json.view.QuestionWriteProjection_JsonView;
import com.stackoverflow.meta.constants.StackOverflowDomainModel;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;
import org.eclipse.collections.impl.factory.primitive.LongSets;
import org.eclipse.collections.impl.set.mutable.SetAdapter;
import org.eclipse.collections.impl.utility.Iterate;

@Path("/manual")
public class QuestionResourceManual
{
    @Nonnull
    private final DataStore dataStore;
    @Nonnull
    private final Clock     clock;

    public QuestionResourceManual(@Nonnull DataStore dataStore, @Nonnull Clock clock)
    {
        this.dataStore = Objects.requireNonNull(dataStore);
        this.clock     = Objects.requireNonNull(clock);
    }

    @Nonnull
    @Timed
    @ExceptionMetered
    @GET
    @Path("/question/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(QuestionReadProjection_JsonView.class)
    public Question method0(
            @PathParam("id") Long id,
            @Nullable @QueryParam("version") Integer version)
    {
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
        result.deepFetch(QuestionFinder.tags().tag());
        result.deepFetch(QuestionFinder.version());

        if (result.isEmpty())
        {
            throw new ClientErrorException("Url valid, data not found.", Status.GONE);
        }
        return Iterate.getOnly(result);
    }

    @Timed
    @ExceptionMetered
    @PUT
    @Path("/question/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void method1(
            @PathParam("id") Long id,
            @Nonnull @QueryParam("version") Optional<Integer> optionalVersion,
            @Nonnull @NotNull ObjectNode incomingInstance)
    {
        MutableList<String> errors   = Lists.mutable.empty();
        MutableList<String> warnings = Lists.mutable.empty();
        JsonTypeCheckingValidator.validate(incomingInstance, StackOverflowDomainModel.Question, errors);
        RequiredPropertiesValidator.validate(
                StackOverflowDomainModel.Question,
                incomingInstance,
                OperationMode.REPLACE,
                errors,
                warnings);

        if (errors.notEmpty())
        {
            Response response = Response
                    .status(Status.BAD_REQUEST)
                    .entity(errors)
                    .build();
            throw new BadRequestException("Incoming data failed validation.", response);
        }

        // this.id == id
        Operation    queryOperation = QuestionFinder.id().eq(id);
        QuestionList result         = QuestionFinder.findMany(queryOperation);
        result.deepFetch(QuestionFinder.tags().tag());
        result.deepFetch(QuestionFinder.version());

        if (result.isEmpty())
        {
            throw new ClientErrorException("Url valid, data not found.", Status.GONE);
        }

        // this.version.number == version
        Operation conflictOperation = QuestionFinder.version().number().eq(optionalVersion.get());
        boolean   hasConflict       = !result.asEcList().allSatisfy(conflictOperation::matches);
        if (hasConflict)
        {
            throw new ClientErrorException(Status.CONFLICT);
        }

        if (result.size() > 1)
        {
            throw new InternalServerErrorException("TODO");
        }
        Object persistentInstance = result.get(0);

        IncomingUpdateDataModelValidator.validate(
                this.dataStore,
                StackOverflowDomainModel.Question,
                persistentInstance,
                incomingInstance,
                errors,
                warnings);
        if (errors.notEmpty())
        {
            Response response = Response
                    .status(Status.BAD_REQUEST)
                    .entity(errors)
                    .build();
            throw new BadRequestException("Incoming data failed validation.", response);
        }

        Instant            transactionInstant = Instant.now(this.clock);
        MutationContext    mutationContext    = new MutationContext(Optional.empty(), transactionInstant);
        PersistentReplacer replacer           = new PersistentReplacer(mutationContext, this.dataStore);
        replacer.synchronize(StackOverflowDomainModel.Question, persistentInstance, incomingInstance);
    }

    @Nonnull
    @Timed
    @ExceptionMetered
    @DELETE
    @Path("/question/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(QuestionWriteProjection_JsonView.class)
    public Question method2(
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

        return Iterate.getOnly(result);
    }

    @Nonnull
    @Timed
    @ExceptionMetered
    @GET
    @Path("/question/in")
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(QuestionReadProjection_JsonView.class)
    public QuestionList getQuestionsById(@Nonnull @QueryParam("ids") Set<Long> ids)
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

        return result;
    }

    @Nonnull
    @Timed
    @ExceptionMetered
    @GET
    @Path("/question/firstTwo")
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(QuestionReadProjection_JsonView.class)
    public QuestionList getFirstTwoQuestions()
    {
        // Question

        // this.id in (1, 2)
        Operation queryOperation = QuestionFinder.id().in(LongSets.immutable.with(1, 2));

        QuestionList result = QuestionFinder.findMany(queryOperation);
        // Deep fetch using projection QuestionReadProjection
        result.deepFetch(QuestionFinder.answers());
        result.deepFetch(QuestionFinder.version());
        return result;
    }

    @Nonnull
    @Timed
    @ExceptionMetered
    @GET
    @Path("/question/{id}/version/{version}")
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(QuestionReadProjection_JsonView.class)
    public Question getQuestionByIdAndVersion(
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
        return Iterate.getOnly(result);
    }

    @Nonnull
    @Timed
    @ExceptionMetered
    @DELETE
    @Path("/question/{id}?{version}")
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(QuestionWriteProjection_JsonView.class)
    public Question deleteQuestionById(
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
        return Iterate.getOnly(result);
    }

    @Timed
    @ExceptionMetered
    @POST
    @Path("/question")
    @Produces(MediaType.APPLICATION_JSON)
    public Response method5(@Nonnull ObjectNode incomingInstance, @Nonnull @Context UriInfo uriInfo)
    {
        MutableList<String> errors   = Lists.mutable.empty();
        MutableList<String> warnings = Lists.mutable.empty();
        JsonTypeCheckingValidator.validate(incomingInstance, StackOverflowDomainModel.Question, errors);
        RequiredPropertiesValidator.validate(
                StackOverflowDomainModel.Question,
                incomingInstance,
                OperationMode.CREATE,
                errors,
                warnings);
        if (errors.notEmpty())
        {
            Response response = Response
                    .status(Status.BAD_REQUEST)
                    .entity(errors)
                    .build();
            throw new BadRequestException("Incoming data failed validation.", response);
        }

        Question persistentInstance = MithraManagerProvider.getMithraManager().executeTransactionalCommand(tx ->
        {
            Instant  now      = Instant.ofEpochMilli(tx.getProcessingStartTime());
            Question question = new Question();
            question.setCreatedById("TODO");
            question.setLastUpdatedById("TODO");
            question.setCreatedOn(Timestamp.valueOf(LocalDateTime.ofInstant(now, ZoneOffset.UTC)));
            question.generateAndSetId();

            Instant           transactionInstant = Instant.now(this.clock);
            MutationContext   mutationContext    = new MutationContext(Optional.empty(), transactionInstant);
            PersistentCreator creator            = new PersistentCreator(mutationContext, this.dataStore);
            creator.synchronize(StackOverflowDomainModel.Question, question, incomingInstance);
            question.insert();
            return question;
        });

        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(Long.toString(persistentInstance.getId()));
        return Response.created(builder.build()).build();
    }

    @Timed
    @ExceptionMetered
    @GET
    @Path("/question")
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(QuestionReadProjection_JsonView.class)
    public QuestionList method6()
    {
        // Question

        // this.title startsWith "Why do"
        Operation queryOperation = QuestionFinder.title().startsWith("Why do");

        QuestionList result = QuestionFinder.findMany(queryOperation);
        // Deep fetch using projection QuestionReadProjection
        result.deepFetch(QuestionFinder.answers());
        result.deepFetch(QuestionFinder.version());
        return result;
    }

    @Timed
    @ExceptionMetered
    @GET
    @Path("/user/{userId}/questions")
    @Produces(MediaType.APPLICATION_JSON)
    @JsonView(QuestionWriteProjection_JsonView.class)
    public QuestionList method7(@PathParam("userId") String userId)
    {
        // Question

        // this.createdById == userId
        Operation queryOperation = QuestionFinder.createdById().eq(userId);

        return QuestionFinder.findMany(queryOperation);
    }
}
