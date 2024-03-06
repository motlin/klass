package cool.klass.model.meta.domain.api.projection;

import javax.annotation.Nonnull;

public final class ProjectionWalker
{
    private ProjectionWalker()
    {
        throw new AssertionError("Suppress default constructor for noninstantiability");
    }

    public static void walk(
            @Nonnull ProjectionElement projectionElement,
            @Nonnull ProjectionListener listener)
    {
        projectionElement.enter(listener);
        projectionElement.getChildren().forEachWith(ProjectionWalker::walk, listener);
        projectionElement.exit(listener);
    }
}
