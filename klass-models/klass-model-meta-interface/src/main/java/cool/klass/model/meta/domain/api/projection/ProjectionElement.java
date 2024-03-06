package cool.klass.model.meta.domain.api.projection;

import java.util.Optional;

import cool.klass.model.meta.domain.api.NamedElement;
import org.eclipse.collections.api.list.ImmutableList;

public interface ProjectionElement extends NamedElement
{
    Optional<ProjectionParent> getParent();

    ImmutableList<? extends ProjectionChild> getChildren();

    default int getDepth()
    {
        return 1 + this.getParent().map(ProjectionElement::getDepth).orElse(0);
    }

    void enter(ProjectionListener listener);

    void exit(ProjectionListener listener);

    void visit(ProjectionVisitor visitor);

    default void visit(ProjectionListener listener)
    {
        try
        {
            this.enter(listener);
        }
        finally
        {
            this.exit(listener);
        }
    }
}
