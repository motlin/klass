package cool.klass.model.meta.domain.projection;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.projection.ProjectionAssociationEnd;
import cool.klass.model.meta.domain.property.AssociationEndImpl;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

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

    public static final class ProjectionAssociationEndBuilder
            extends AbstractProjectionParentBuilder<ProjectionAssociationEndImpl>
            implements ProjectionElementBuilder
    {
        @Nonnull
        private final AssociationEndBuilder associationEndBuilder;

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
        @Nonnull
        protected ProjectionAssociationEndImpl buildUnsafe()
        {
            ProjectionAssociationEndImpl projectionAssociationEnd = new ProjectionAssociationEndImpl(
                    this.elementContext,
                    this.inferred,
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.associationEndBuilder.getElement());

            this.buildChildren(projectionAssociationEnd);

            return projectionAssociationEnd;
        }
    }
}
