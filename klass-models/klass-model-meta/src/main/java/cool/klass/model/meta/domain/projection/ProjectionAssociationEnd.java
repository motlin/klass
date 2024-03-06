package cool.klass.model.meta.domain.projection;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.property.AssociationEnd;
import cool.klass.model.meta.domain.property.AssociationEnd.AssociationEndBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class ProjectionAssociationEnd extends ProjectionParent implements ProjectionElement
{
    // TODO: This is redundant since it can be inferred from the associationEnd
    @Nonnull
    private final AssociationEnd associationEnd;

    private ProjectionAssociationEnd(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            @Nonnull AssociationEnd associationEnd)
    {
        super(elementContext, nameContext, name);
        this.associationEnd = Objects.requireNonNull(associationEnd);
    }

    @Override
    public void enter(ProjectionListener listener)
    {
        listener.enterProjectionAssociationEnd(this);
    }

    @Override
    public void exit(ProjectionListener listener)
    {
        listener.exitProjectionAssociationEnd(this);
    }

    @Nonnull
    public AssociationEnd getAssociationEnd()
    {
        return this.associationEnd;
    }

    public static final class ProjectionAssociationEndBuilder extends ProjectionParentBuilder implements ProjectionElementBuilder
    {
        @Nonnull
        private final AssociationEndBuilder associationEndBuilder;

        private ProjectionAssociationEnd projectionAssociationEnd;

        public ProjectionAssociationEndBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                @Nonnull AssociationEndBuilder associationEndBuilder)
        {
            super(elementContext, nameContext, name);
            this.associationEndBuilder = Objects.requireNonNull(associationEndBuilder);
        }

        @Override
        public ProjectionAssociationEnd build()
        {
            if (this.projectionAssociationEnd != null)
            {
                throw new IllegalStateException();
            }
            this.projectionAssociationEnd = new ProjectionAssociationEnd(
                    this.elementContext,
                    this.nameContext,
                    this.name,
                    this.associationEndBuilder.getAssociationEnd());

            ImmutableList<ProjectionElement> children = this.childBuilders.collect(ProjectionElementBuilder::build);

            this.projectionAssociationEnd.setChildren(children);

            return this.projectionAssociationEnd;
        }
    }
}
