package cool.klass.model.meta.domain.service;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.service.ServiceProjectionDispatch;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.projection.ProjectionImpl;
import cool.klass.model.meta.domain.projection.ProjectionImpl.ProjectionBuilder;
import cool.klass.model.meta.grammar.KlassParser.ServiceProjectionDispatchContext;

public final class ServiceProjectionDispatchImpl
        extends AbstractElement
        implements ServiceProjectionDispatch
{
    @Nonnull
    private final ProjectionImpl projection;

    private ServiceProjectionDispatchImpl(
            @Nonnull ServiceProjectionDispatchContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            @Nonnull ProjectionImpl projection)
    {
        super(elementContext, macroElement, sourceCode);
        this.projection = Objects.requireNonNull(projection);
    }

    @Nonnull
    @Override
    public ServiceProjectionDispatchContext getElementContext()
    {
        return (ServiceProjectionDispatchContext) super.getElementContext();
    }

    @Override
    @Nonnull
    public ProjectionImpl getProjection()
    {
        return this.projection;
    }

    public static final class ServiceProjectionDispatchBuilder
            extends ElementBuilder<ServiceProjectionDispatchImpl>
    {
        @Nonnull
        private final ProjectionBuilder projectionBuilder;

        public ServiceProjectionDispatchBuilder(
                @Nonnull ServiceProjectionDispatchContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                @Nonnull ProjectionBuilder projectionBuilder)
        {
            super(elementContext, macroElement, sourceCode);
            this.projectionBuilder = Objects.requireNonNull(projectionBuilder);
        }

        @Override
        @Nonnull
        protected ServiceProjectionDispatchImpl buildUnsafe()
        {
            return new ServiceProjectionDispatchImpl(
                    (ServiceProjectionDispatchContext) this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.projectionBuilder.getElement());
        }
    }
}
