package cool.klass.model.meta.domain.projection;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractNamedElement;
import cool.klass.model.meta.domain.api.projection.ProjectionElement;
import cool.klass.model.meta.domain.api.projection.ProjectionParent;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public abstract class AbstractProjectionParent extends AbstractNamedElement implements AbstractProjectionElement, ProjectionParent
{
    private ImmutableList<ProjectionElement> children;

    protected AbstractProjectionParent(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal)
    {
        super(elementContext, inferred, nameContext, name, ordinal);
    }

    @Override
    public ImmutableList<ProjectionElement> getChildren()
    {
        return Objects.requireNonNull(this.children);
    }

    protected void setChildren(@Nonnull ImmutableList<ProjectionElement> children)
    {
        this.children = Objects.requireNonNull(children);
    }

    public abstract static class ProjectionParentBuilder extends NamedElementBuilder
    {
        protected ImmutableList<ProjectionElementBuilder> childBuilders;

        protected ProjectionParentBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal)
        {
            super(elementContext, inferred, nameContext, name, ordinal);
        }

        public void setChildBuilders(@Nonnull ImmutableList<ProjectionElementBuilder> projectionChildrenBuilders)
        {
            this.childBuilders = Objects.requireNonNull(projectionChildrenBuilders);
        }
    }
}
