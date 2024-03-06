package cool.klass.model.meta.domain.service;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractElement;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.service.ServiceProjectionDispatch;
import cool.klass.model.meta.domain.projection.ProjectionImpl;
import cool.klass.model.meta.domain.projection.ProjectionImpl.ProjectionBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class ServiceProjectionDispatchImpl extends AbstractElement implements ServiceProjectionDispatch
{
    @Nonnull
    private final ProjectionImpl projection;

    private ServiceProjectionDispatchImpl(
            @Nonnull ParserRuleContext elementContext,
            Optional<Element> macroElement,
            @Nonnull ProjectionImpl projection)
    {
        super(elementContext, macroElement);
        this.projection = Objects.requireNonNull(projection);
    }

    @Override
    @Nonnull
    public ProjectionImpl getProjection()
    {
        return this.projection;
    }

    public static final class ServiceProjectionDispatchBuilder extends ElementBuilder<ServiceProjectionDispatchImpl>
    {
        @Nonnull
        private final ProjectionBuilder projectionBuilder;

        public ServiceProjectionDispatchBuilder(
                @Nonnull ParserRuleContext elementContext,
                Optional<ElementBuilder<?>> macroElement,
                @Nonnull ProjectionBuilder projectionBuilder)
        {
            super(elementContext, macroElement);
            this.projectionBuilder = Objects.requireNonNull(projectionBuilder);
        }

        @Override
        @Nonnull
        protected ServiceProjectionDispatchImpl buildUnsafe()
        {
            return new ServiceProjectionDispatchImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.projectionBuilder.getElement());
        }
    }
}
