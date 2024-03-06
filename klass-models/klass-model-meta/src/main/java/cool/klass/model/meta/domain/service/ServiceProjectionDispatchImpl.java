package cool.klass.model.meta.domain.service;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractElement;
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
            boolean inferred,
            @Nonnull ProjectionImpl projection)
    {
        super(elementContext, inferred);
        this.projection = Objects.requireNonNull(projection);
    }

    @Override
    @Nonnull
    public ProjectionImpl getProjection()
    {
        return this.projection;
    }

    public static final class ServiceProjectionDispatchBuilder extends ElementBuilder
    {
        @Nonnull
        private final ProjectionBuilder projectionBuilder;

        public ServiceProjectionDispatchBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ProjectionBuilder projectionBuilder)
        {
            super(elementContext, inferred);
            this.projectionBuilder = Objects.requireNonNull(projectionBuilder);
        }

        public ServiceProjectionDispatchImpl build()
        {
            return new ServiceProjectionDispatchImpl(
                    this.elementContext,
                    this.inferred,
                    this.projectionBuilder.getElement());
        }
    }
}
