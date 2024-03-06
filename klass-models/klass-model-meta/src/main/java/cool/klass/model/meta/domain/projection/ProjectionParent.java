package cool.klass.model.meta.domain.projection;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.NamedElement;
import cool.klass.model.meta.domain.projection.ProjectionMember.ProjectionMemberBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public abstract class ProjectionParent extends NamedElement
{
    private ImmutableList<ProjectionMember> projectionMembers;

    protected ProjectionParent(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name)
    {
        super(elementContext, nameContext, name);
    }

    public ImmutableList<ProjectionMember> getProjectionMembers()
    {
        return Objects.requireNonNull(this.projectionMembers);
    }

    protected void setProjectionMembers(@Nonnull ImmutableList<ProjectionMember> projectionMembers)
    {
        this.projectionMembers = Objects.requireNonNull(projectionMembers);
    }

    public abstract static class ProjectionParentBuilder extends NamedElementBuilder
    {
        protected ImmutableList<ProjectionMemberBuilder> projectionMemberBuilders;

        protected ProjectionParentBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name)
        {
            super(elementContext, nameContext, name);
        }

        public void setProjectionMemberBuilders(@Nonnull ImmutableList<ProjectionMemberBuilder> projectionMemberBuilders)
        {
            this.projectionMemberBuilders = Objects.requireNonNull(projectionMemberBuilders);
        }
    }
}
