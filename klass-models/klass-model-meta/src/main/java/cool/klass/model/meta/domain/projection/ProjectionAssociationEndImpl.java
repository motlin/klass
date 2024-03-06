package cool.klass.model.meta.domain.projection;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.projection.ProjectionAssociationEnd;
import cool.klass.model.meta.domain.api.projection.ProjectionElement;
import cool.klass.model.meta.domain.property.AssociationEndImpl;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class ProjectionAssociationEndImpl extends AbstractProjectionParent implements ProjectionAssociationEnd
{
    // TODO: This is redundant since it can be inferred from the associationEnd
    @Nonnull
    private final AssociationEndImpl associationEnd;

    private ProjectionAssociationEndImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AssociationEndImpl associationEnd)
    {
        super(elementContext, inferred, nameContext, name, ordinal);
        this.associationEnd = Objects.requireNonNull(associationEnd);
    }

    @Override
    @Nonnull
    public AssociationEndImpl getAssociationEnd()
    {
        return this.associationEnd;
    }

    public static final class ProjectionAssociationEndBuilder extends ProjectionParentBuilder implements ProjectionElementBuilder
    {
        @Nonnull
        private final AssociationEndBuilder associationEndBuilder;

        private ProjectionAssociationEndImpl projectionAssociationEnd;

        public ProjectionAssociationEndBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull AssociationEndBuilder associationEndBuilder)
        {
            super(elementContext, inferred, nameContext, name, ordinal);
            this.associationEndBuilder = Objects.requireNonNull(associationEndBuilder);
        }

        @Override
        public ProjectionAssociationEndImpl build()
        {
            if (this.projectionAssociationEnd != null)
            {
                throw new IllegalStateException();
            }
            this.projectionAssociationEnd = new ProjectionAssociationEndImpl(
                    this.elementContext,
                    this.inferred,
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.associationEndBuilder.getAssociationEnd());

            ImmutableList<ProjectionElement> children = this.childBuilders.collect(ProjectionElementBuilder::build);

            this.projectionAssociationEnd.setChildren(children);

            return this.projectionAssociationEnd;
        }
    }
}
