package cool.klass.model.meta.domain.service.url;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.api.service.Service;
import cool.klass.model.meta.domain.api.service.url.Url;
import cool.klass.model.meta.domain.api.service.url.UrlParameter;
import cool.klass.model.meta.domain.api.service.url.UrlPathSegment;
import cool.klass.model.meta.domain.api.service.url.UrlQueryParameter;
import cool.klass.model.meta.domain.service.ServiceGroupImpl;
import cool.klass.model.meta.domain.service.ServiceGroupImpl.ServiceGroupBuilder;
import cool.klass.model.meta.domain.service.ServiceImpl.ServiceBuilder;
import cool.klass.model.meta.domain.service.url.AbstractUrlParameter.UrlParameterBuilder;
import cool.klass.model.meta.domain.service.url.AbstractUrlPathSegment.UrlPathSegmentBuilder;
import cool.klass.model.meta.domain.service.url.UrlQueryParameterImpl.UrlQueryParameterBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class UrlImpl extends AbstractElement implements Url
{
    @Nonnull
    private final ServiceGroupImpl                 serviceGroup;
    private       ImmutableList<UrlPathSegment>    urlPathSegments;
    private       ImmutableList<UrlQueryParameter> queryParameters;
    private       ImmutableList<UrlParameter>      urlParameters;
    private       ImmutableList<Service>           services;

    private UrlImpl(@Nonnull ParserRuleContext elementContext, boolean inferred, @Nonnull ServiceGroupImpl serviceGroup)
    {
        super(elementContext, inferred);
        this.serviceGroup = Objects.requireNonNull(serviceGroup);
    }

    @Override
    @Nonnull
    public ServiceGroupImpl getServiceGroup()
    {
        return Objects.requireNonNull(this.serviceGroup);
    }

    @Override
    public ImmutableList<UrlPathSegment> getUrlPathSegments()
    {
        return Objects.requireNonNull(this.urlPathSegments);
    }

    private void setUrlPathSegments(@Nonnull ImmutableList<UrlPathSegment> urlPathSegments)
    {
        if (this.urlPathSegments != null)
        {
            throw new IllegalStateException();
        }
        this.urlPathSegments = Objects.requireNonNull(urlPathSegments);
    }

    @Override
    public ImmutableList<UrlQueryParameter> getQueryParameters()
    {
        return Objects.requireNonNull(this.queryParameters);
    }

    private void setQueryParameters(@Nonnull ImmutableList<UrlQueryParameter> queryParameters)
    {
        if (this.queryParameters != null)
        {
            throw new IllegalStateException();
        }
        this.queryParameters = Objects.requireNonNull(queryParameters);
    }

    @Override
    public ImmutableList<UrlParameter> getUrlParameters()
    {
        return Objects.requireNonNull(this.urlParameters);
    }

    private void setUrlParameters(@Nonnull ImmutableList<UrlParameter> urlParameters)
    {
        if (this.urlParameters != null)
        {
            throw new IllegalStateException();
        }
        this.urlParameters = Objects.requireNonNull(urlParameters);
    }

    @Override
    public ImmutableList<Service> getServices()
    {
        return Objects.requireNonNull(this.services);
    }

    private void setServices(@Nonnull ImmutableList<Service> services)
    {
        if (this.services != null)
        {
            throw new IllegalStateException();
        }
        this.services = Objects.requireNonNull(services);
    }

    public static final class UrlBuilder extends ElementBuilder
    {
        @Nonnull
        private final ServiceGroupBuilder serviceGroupBuilder;

        private ImmutableList<UrlPathSegmentBuilder>    pathSegmentBuilders;
        private ImmutableList<UrlQueryParameterBuilder> queryParameters;
        private ImmutableList<ServiceBuilder>           services;
        private UrlImpl                                 url;
        private ImmutableList<UrlParameterBuilder>      urlParameters;

        public UrlBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ServiceGroupBuilder serviceGroupBuilder)
        {
            super(elementContext, inferred);
            this.serviceGroupBuilder = Objects.requireNonNull(serviceGroupBuilder);
        }

        public void setPathSegments(@Nonnull ImmutableList<UrlPathSegmentBuilder> pathSegmentBuilders)
        {
            this.pathSegmentBuilders = Objects.requireNonNull(pathSegmentBuilders);
        }

        public void setQueryParameters(@Nonnull ImmutableList<UrlQueryParameterBuilder> queryParameters)
        {
            this.queryParameters = Objects.requireNonNull(queryParameters);
        }

        public void setUrlParameters(@Nonnull ImmutableList<UrlParameterBuilder> urlParameters)
        {
            this.urlParameters = Objects.requireNonNull(urlParameters);
        }

        public void setServices(@Nonnull ImmutableList<ServiceBuilder> services)
        {
            this.services = Objects.requireNonNull(services);
        }

        public UrlImpl build()
        {
            if (this.url != null)
            {
                throw new IllegalStateException();
            }
            this.url = new UrlImpl(this.elementContext, this.inferred, this.serviceGroupBuilder.getElement());

            ImmutableList<UrlPathSegment> urlPathSegments = this.pathSegmentBuilders.collect(UrlPathSegmentBuilder::build);
            this.url.setUrlPathSegments(urlPathSegments);

            ImmutableList<UrlQueryParameter> queryParameters = this.queryParameters.collect(UrlQueryParameterBuilder::build);
            this.url.setQueryParameters(queryParameters);

            ImmutableList<UrlParameter> urlParameters = this.urlParameters.collect(UrlParameterBuilder::getUrlParameter);
            this.url.setUrlParameters(urlParameters);

            ImmutableList<Service> services = this.services.collect(ServiceBuilder::build);
            this.url.setServices(services);

            return this.url;
        }

        public UrlImpl getUrl()
        {
            return Objects.requireNonNull(this.url);
        }
    }
}
