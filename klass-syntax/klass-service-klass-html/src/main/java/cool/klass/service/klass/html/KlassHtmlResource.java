package cool.klass.service.klass.html;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.codahale.metrics.annotation.ExceptionMetered;
import com.codahale.metrics.annotation.Timed;
import cool.klass.generator.klass.html.KlassSourceCodeHtmlGenerator;
import cool.klass.model.meta.domain.api.TopLevelElement;
import cool.klass.model.meta.domain.api.property.Property;
import cool.klass.model.meta.domain.api.service.ServiceGroup;
import cool.klass.model.meta.domain.api.source.DomainModelWithSourceCode;
import cool.klass.model.meta.domain.api.source.ElementWithSourceCode;
import cool.klass.model.meta.domain.api.source.KlassWithSourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.TopLevelElementWithSourceCode;
import org.eclipse.collections.api.list.ImmutableList;

@Path("/")
public class KlassHtmlResource
{
    @Nonnull
    private final DomainModelWithSourceCode domainModel;

    public KlassHtmlResource(@Nonnull DomainModelWithSourceCode domainModel)
    {
        this.domainModel = Objects.requireNonNull(domainModel);
    }

    @Timed
    @ExceptionMetered
    @GET
    @Path("/meta/code/element/{topLevelElementName}")
    @Produces(MediaType.TEXT_HTML)
    public Response topLevelElementNameSourceCode(@PathParam("topLevelElementName") String topLevelElementName)
    {
        // TODO: Implement DomainModel.getTopLevelElementByName(name)
        ImmutableList<TopLevelElement> topLevelElements = this.domainModel
                .getTopLevelElements()
                .reject(ServiceGroup.class::isInstance)
                .select(each -> each.getName().equals(topLevelElementName));

        if (topLevelElements.size() != 1)
        {
            throw new BadRequestException();
        }
        TopLevelElementWithSourceCode topLevelElement   = (TopLevelElementWithSourceCode) topLevelElements.getOnly();
        SourceCode                    sourceCode        = topLevelElement.getSourceCodeObject();
        String                        sourceName        = sourceCode.getSourceName();
        String                        encodedSourceName = URLEncoder.encode(sourceName, StandardCharsets.UTF_8);
        String uriString = String.format(
                "/api/meta/code/file/%s.html#%s",
                encodedSourceName,
                topLevelElementName);
        URI location = URI.create(uriString);
        return Response.temporaryRedirect(location).build();
    }

    @Timed
    @ExceptionMetered
    @GET
    @Path("/meta/code/element/{topLevelElementName}/{memberName}")
    @Produces(MediaType.TEXT_HTML)
    public String topLevelElementNameSourceCode(
            @PathParam("topLevelElementName") String topLevelElementName,
            @PathParam("memberName") String memberName)
    {
        TopLevelElementWithSourceCode topLevelElement = this.domainModel
                .getTopLevelElementByName(topLevelElementName);

        Optional<SourceCode> sourceCode = getSourceCodeObject(topLevelElement, memberName);
        if (sourceCode.isEmpty())
        {
            throw new BadRequestException();
        }
        return KlassSourceCodeHtmlGenerator.getSourceCode(
                this.domainModel,
                sourceCode.get(),
                Optional.of(topLevelElement),
                Optional.of(memberName));
    }

    @Nonnull
    private static Optional<SourceCode> getSourceCodeObject(TopLevelElementWithSourceCode topLevelElement, String memberName)
    {
        if (memberName == null)
        {
            return Optional.of(topLevelElement.getSourceCodeObject());
        }

        if (topLevelElement instanceof KlassWithSourceCode klass)
        {
            Optional<Property> property = klass.getPropertyByName(memberName);
            if (property.isEmpty())
            {
                return Optional.empty();
            }
            if (property.get() instanceof ElementWithSourceCode elementWithSourceCode)
            {
                return Optional.of(elementWithSourceCode.getSourceCodeObject());
            }
        }

        throw new AssertionError(topLevelElement);
    }

    @Timed
    @ExceptionMetered
    @GET
    @Path("/meta/code/file/{fileName}.html")
    @Produces(MediaType.TEXT_HTML)
    public String fileSourceCode(@PathParam("fileName") String fileName)
    {
        ImmutableList<SourceCode> sourceCodes = this.domainModel
                .getSourceCodes()
                .select(each -> each.getSourceName().equals(fileName));

        if (sourceCodes.size() != 1)
        {
            throw new NotFoundException(this.domainModel
                    .getSourceCodes()
                    .collect(SourceCode::getSourceName)
                    .toString());
        }

        SourceCode sourceCode = sourceCodes.getOnly();
        return KlassSourceCodeHtmlGenerator.getSourceCode(
                this.domainModel,
                sourceCode,
                Optional.empty(),
                Optional.empty());
    }
}
