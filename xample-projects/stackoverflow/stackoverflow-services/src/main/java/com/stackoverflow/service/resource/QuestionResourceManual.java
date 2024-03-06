package com.stackoverflow.service.resource;

import java.util.List;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.SecurityContext;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import com.gs.fw.common.mithra.MithraObject;
import com.gs.fw.common.mithra.finder.Operation;
import cool.klass.model.meta.domain.DomainModel;
import cool.klass.model.meta.domain.projection.Projection;
import cool.klass.serializer.json.ReladomoJsonTree;
import com.stackoverflow.QuestionFinder;
import com.stackoverflow.QuestionList;
import com.stackoverflow.QuestionVersionFinder;
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
        result.deepFetch(QuestionFinder.answers());
        // TODO: Deep fetch using projection QuestionReadProjection

        if (result.isEmpty())
        {
            throw new NotFoundException();
        }
        MithraObject mithraObject = Iterate.getOnly(result);

        Projection projection = this.domainModel.getProjectionByName("QuestionReadProjection");
        return new ReladomoJsonTree(mithraObject, projection.getChildren());
    }

    @Nonnull
    @Timed
    @ExceptionMetered
    @GET
    @Path("/api/question/in?{ids}")
    @Produces(MediaType.APPLICATION_JSON)
    public ReladomoJsonTree method1(@Nonnull @QueryParam("ids") Set<Long> ids)
    {
        Operation queryOperation = QuestionFinder.id().in(SetAdapter.adapt(ids).collectLong(
                x -> x,
                LongSets.mutable.empty()));
        Operation authorizeOperation = QuestionFinder.all();
        Operation validateOperation  = QuestionFinder.all();
        Operation conflictOperation  = QuestionFinder.all();

        QuestionList result = QuestionFinder.findMany(queryOperation);
        // TODO: Deep fetch using projection QuestionReadProjection

        boolean isAuthorized = !result.asEcList().allSatisfy(authorizeOperation::matches);
        boolean isValidated  = !result.asEcList().allSatisfy(validateOperation::matches);
        boolean hasConflict  = !result.asEcList().allSatisfy(conflictOperation::matches);
        if (result.isEmpty())
        {
            throw new NotFoundException();
        }
        MithraObject mithraObject = Iterate.getOnly(result);

        Projection projection = this.domainModel.getProjectionByName("QuestionReadProjection");
        return new ReladomoJsonTree(mithraObject, projection.getChildren());
    }

    @Nonnull
    @Timed
    @ExceptionMetered
    @GET
    @Path("/api/question/firstTwo")
    @Produces(MediaType.APPLICATION_JSON)
    public ReladomoJsonTree method2()
    {
        Operation queryOperation     = QuestionFinder.id().in(LongSets.immutable.with(1, 2));
        Operation authorizeOperation = QuestionFinder.all();
        Operation validateOperation  = QuestionFinder.all();
        Operation conflictOperation  = QuestionFinder.all();

        QuestionList result = QuestionFinder.findMany(queryOperation);
        // TODO: Deep fetch using projection QuestionReadProjection

        boolean isAuthorized = !result.asEcList().allSatisfy(authorizeOperation::matches);
        boolean isValidated  = !result.asEcList().allSatisfy(validateOperation::matches);
        boolean hasConflict  = !result.asEcList().allSatisfy(conflictOperation::matches);
        if (result.isEmpty())
        {
            throw new NotFoundException();
        }
        MithraObject mithraObject = Iterate.getOnly(result);

        Projection projection = this.domainModel.getProjectionByName("QuestionReadProjection");
        return new ReladomoJsonTree(mithraObject, projection.getChildren());
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
            throw new NotFoundException();
        }
        MithraObject mithraObject = Iterate.getOnly(result);

        Projection projection = this.domainModel.getProjectionByName("QuestionReadProjection");
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
            throw new NotFoundException();
        }
        MithraObject mithraObject = Iterate.getOnly(result);

        Projection projection = this.domainModel.getProjectionByName("QuestionWriteProjection");
        return new ReladomoJsonTree(mithraObject, projection.getChildren());
    }

    @Nonnull
    @Timed
    @ExceptionMetered
    @POST
    @Path("/api/question")
    @Produces(MediaType.APPLICATION_JSON)
    public ReladomoJsonTree method5()
    {
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
            throw new NotFoundException();
        }
        MithraObject mithraObject = Iterate.getOnly(result);

        Projection projection = this.domainModel.getProjectionByName("QuestionWriteProjection");
        return new ReladomoJsonTree(mithraObject, projection.getChildren());
    }

    @Timed
    @ExceptionMetered
    @GET
    @Path("/api/question")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReladomoJsonTree> method6()
    {
        Operation queryOperation     = QuestionFinder.title().startsWith("Why do");
        Operation authorizeOperation = QuestionFinder.all();
        Operation validateOperation  = QuestionFinder.all();
        Operation conflictOperation  = QuestionFinder.all();

        QuestionList result = QuestionFinder.findMany(queryOperation);
        // TODO: Deep fetch using projection QuestionReadProjection

        boolean    isAuthorized = !result.asEcList().allSatisfy(authorizeOperation::matches);
        boolean    isValidated  = !result.asEcList().allSatisfy(validateOperation::matches);
        boolean    hasConflict  = !result.asEcList().allSatisfy(conflictOperation::matches);
        Projection projection   = this.domainModel.getProjectionByName("QuestionReadProjection");
        return result.asEcList().collect(mithraObject -> new ReladomoJsonTree(
                mithraObject,
                projection.getChildren()));
    }

    @Timed
    @ExceptionMetered
    @GET
    @Path("/api/user/{userId}/questions")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ReladomoJsonTree> method7(@PathParam("userId") String userId)
    {
        Operation queryOperation     = QuestionFinder.createdById().eq(userId);
        Operation authorizeOperation = QuestionFinder.all();
        Operation validateOperation  = QuestionFinder.all();
        Operation conflictOperation  = QuestionFinder.all();

        QuestionList result = QuestionFinder.findMany(queryOperation);
        // TODO: Deep fetch using projection QuestionWriteProjection

        boolean    isAuthorized = !result.asEcList().allSatisfy(authorizeOperation::matches);
        boolean    isValidated  = !result.asEcList().allSatisfy(validateOperation::matches);
        boolean    hasConflict  = !result.asEcList().allSatisfy(conflictOperation::matches);
        Projection projection   = this.domainModel.getProjectionByName("QuestionWriteProjection");
        return result.asEcList().collect(mithraObject -> new ReladomoJsonTree(
                mithraObject,
                projection.getChildren()));
    }
}
