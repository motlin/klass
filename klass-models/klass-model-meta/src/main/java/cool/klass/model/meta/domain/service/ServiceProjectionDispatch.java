package cool.klass.model.meta.domain.service;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Element;
import cool.klass.model.meta.domain.projection.Projection;
import cool.klass.model.meta.domain.projection.Projection.ProjectionBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class ServiceProjectionDispatch extends Element
{
    @Nonnull
    private final Projection projection;

    private ServiceProjectionDispatch(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Projection projection)
    {
        super(elementContext);
        this.projection = Objects.requireNonNull(projection);
    }

    @Nonnull
    public Projection getProjection()
    {
        return this.projection;
    }

    public static final class ServiceProjectionDispatchBuilder extends ElementBuilder
    {
        @Nonnull
        private final ProjectionBuilder projectionBuilder;

        public ServiceProjectionDispatchBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull ProjectionBuilder projectionBuilder)
        {
            super(elementContext);
            this.projectionBuilder = Objects.requireNonNull(projectionBuilder);
        }

        public ServiceProjectionDispatch build()
        {
            return new ServiceProjectionDispatch(this.elementContext, this.projectionBuilder.getProjection());
        }
    }
}
