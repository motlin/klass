package cool.klass.model.meta.domain.projection;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.NamedElement;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public abstract class ProjectionParent extends NamedElement implements ProjectionElement
{
    private ImmutableList<ProjectionElement> children;

    protected ProjectionParent(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name)
    {
        super(elementContext, nameContext, name);
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
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name)
        {
            super(elementContext, nameContext, name);
        }

        public void setChildBuilders(@Nonnull ImmutableList<ProjectionElementBuilder> projectionChildrenBuilders)
        {
            this.childBuilders = Objects.requireNonNull(projectionChildrenBuilders);
        }
    }
}
