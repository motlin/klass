package cool.klass.model.meta.domain.api.projection;

import javax.annotation.Nonnull;

import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.factory.Sets;

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
        ProjectionWalker.recursiveWalk(projectionElement, listener, Sets.mutable.empty());
    }

    private static void recursiveWalk(
            @Nonnull ProjectionElement projectionElement,
            @Nonnull ProjectionListener listener,
            @Nonnull MutableSet<ProjectionElement> visited)
    {
        projectionElement.enter(listener);
        projectionElement.getChildren().forEach(eachChild ->
        {
            boolean notYetVisited = visited.add(eachChild);
            if (notYetVisited)
            {
                ProjectionWalker.recursiveWalk(eachChild, listener, visited);
            }
        });
        projectionElement.exit(listener);
    }
}
