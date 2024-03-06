package cool.klass.model.meta.domain.service.url;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.parameter.Parameter;
import cool.klass.model.meta.domain.api.service.Service;
import cool.klass.model.meta.domain.api.service.url.Url;
import cool.klass.model.meta.domain.parameter.ParameterImpl.ParameterBuilder;
import cool.klass.model.meta.domain.service.ServiceGroupImpl;
import cool.klass.model.meta.domain.service.ServiceGroupImpl.ServiceGroupBuilder;
import cool.klass.model.meta.domain.service.ServiceImpl.ServiceBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class UrlImpl extends AbstractElement implements Url
{
    @Nonnull
    private final ServiceGroupImpl         serviceGroup;
    private       ImmutableList<Element>   urlPathSegments;
    private       ImmutableList<Parameter> parameters;
    private       ImmutableList<Parameter> queryParameters;
    private       ImmutableList<Parameter> pathParameters;
    private       ImmutableList<Service>   services;

    private UrlImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nonnull ServiceGroupImpl serviceGroup)
    {
        super(elementContext, macroElement);
        this.serviceGroup = Objects.requireNonNull(serviceGroup);
    }

    @Override
    @Nonnull
    public ServiceGroupImpl getServiceGroup()
    {
        return Objects.requireNonNull(this.serviceGroup);
    }

    @Override
    public ImmutableList<Element> getUrlPathSegments()
    {
        return Objects.requireNonNull(this.urlPathSegments);
    }

    private void setUrlPathSegments(@Nonnull ImmutableList<Element> urlPathSegments)
    {
        if (this.urlPathSegments != null)
        {
            throw new IllegalStateException();
        }
        this.urlPathSegments = Objects.requireNonNull(urlPathSegments);
    }

    @Override
    public ImmutableList<Parameter> getParameters()
    {
        return Objects.requireNonNull(this.parameters);
    }

    private void setParameters(@Nonnull ImmutableList<Parameter> urlParameters)
    {
        if (this.parameters != null)
        {
            throw new IllegalStateException();
        }
        this.parameters = Objects.requireNonNull(urlParameters);
    }

    @Override
    public ImmutableList<Parameter> getQueryParameters()
    {
        return Objects.requireNonNull(this.queryParameters);
    }

    private void setQueryParameters(@Nonnull ImmutableList<Parameter> queryParameters)
    {
        if (this.queryParameters != null)
        {
            throw new IllegalStateException();
        }
        this.queryParameters = Objects.requireNonNull(queryParameters);
    }

    @Override
    public ImmutableList<Parameter> getPathParameters()
    {
        return Objects.requireNonNull(this.pathParameters);
    }

    private void setPathParameters(@Nonnull ImmutableList<Parameter> pathParameters)
    {
        if (this.pathParameters != null)
        {
            throw new IllegalStateException();
        }
        this.pathParameters = Objects.requireNonNull(pathParameters);
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

    public static final class UrlBuilder extends ElementBuilder<UrlImpl>
    {
        @Nonnull
        private final ServiceGroupBuilder serviceGroupBuilder;

        private ImmutableList<ElementBuilder<?>> pathSegmentBuilders;
        private ImmutableList<ParameterBuilder>  parameterBuilders;
        private ImmutableList<ParameterBuilder>  queryParameterBuilders;
        private ImmutableList<ParameterBuilder>  pathParameterBuilders;
        private ImmutableList<ServiceBuilder>    serviceBuilders;

        public UrlBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull ServiceGroupBuilder serviceGroupBuilder)
        {
            super(elementContext, macroElement);
            this.serviceGroupBuilder = Objects.requireNonNull(serviceGroupBuilder);
        }

        public void setPathSegments(@Nonnull ImmutableList<ElementBuilder<?>> pathSegmentBuilders)
        {
            this.pathSegmentBuilders = Objects.requireNonNull(pathSegmentBuilders);
        }

        public void setParameterBuilders(@Nonnull ImmutableList<ParameterBuilder> parameterBuilders)
        {
            this.parameterBuilders = Objects.requireNonNull(parameterBuilders);
        }

        public void setQueryParameterBuilders(@Nonnull ImmutableList<ParameterBuilder> queryParameterBuilders)
        {
            this.queryParameterBuilders = Objects.requireNonNull(queryParameterBuilders);
        }

        public void setPathParameterBuilders(@Nonnull ImmutableList<ParameterBuilder> pathParameterBuilders)
        {
            this.pathParameterBuilders = Objects.requireNonNull(pathParameterBuilders);
        }

        public void setServiceBuilders(@Nonnull ImmutableList<ServiceBuilder> serviceBuilders)
        {
            this.serviceBuilders = Objects.requireNonNull(serviceBuilders);
        }

        @Override
        @Nonnull
        protected UrlImpl buildUnsafe()
        {
            return new UrlImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.serviceGroupBuilder.getElement());
        }

        @Override
        protected void buildChildren()
        {
            this.element.setUrlPathSegments(this.pathSegmentBuilders.collect(ElementBuilder::build));
            this.element.setQueryParameters(this.queryParameterBuilders.collect(ParameterBuilder::build));
            this.element.setPathParameters(this.pathParameterBuilders.collect(ParameterBuilder::getElement));
            this.element.setParameters(this.parameterBuilders.collect(ParameterBuilder::getElement));

            ImmutableList<Service> services = this.serviceBuilders.collect(ServiceBuilder::build);
            this.element.setServices(services);
        }
    }
}
