package cool.klass.model.meta.domain.projection;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractNamedElement;
import cool.klass.model.meta.domain.api.projection.Projection;
import cool.klass.model.meta.domain.api.projection.ProjectionParent;
import cool.klass.model.meta.domain.api.projection.ProjectionProjectionReference;
import cool.klass.model.meta.domain.projection.AbstractProjectionElement.ProjectionChildBuilder;
import cool.klass.model.meta.domain.projection.AbstractProjectionParent.AbstractProjectionParentBuilder;
import cool.klass.model.meta.domain.projection.ProjectionImpl.ProjectionBuilder;
import cool.klass.model.meta.domain.property.AssociationEndImpl;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class ProjectionProjectionReferenceImpl
        extends AbstractNamedElement
        implements ProjectionProjectionReference
{
    @Nonnull
    private final ProjectionParent   parent;
    @Nonnull
    private final AssociationEndImpl associationEnd;

    private ProjectionImpl referencedProjection;

    private ProjectionProjectionReferenceImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull ProjectionParent parent,
            @Nonnull AssociationEndImpl associationEnd)
    {
        super(elementContext, inferred, nameContext, name, ordinal);
        this.parent = Objects.requireNonNull(parent);
        this.associationEnd = Objects.requireNonNull(associationEnd);
    }

    @Override
    public Projection getProjection()
    {
        return this.referencedProjection;
    }

    @Override
    @Nonnull
    public Optional<ProjectionParent> getParent()
    {
        return Optional.of(this.parent);
    }

    @Override
    @Nonnull
    public AssociationEndImpl getProperty()
    {
        return this.associationEnd;
    }

    private void setReferencedProjection(ProjectionImpl referencedProjection)
    {
        if (this.referencedProjection != null)
        {
            throw new IllegalStateException();
        }
        this.referencedProjection = Objects.requireNonNull(referencedProjection);
    }

    public static final class ProjectionProjectionReferenceBuilder
            extends NamedElementBuilder<ProjectionProjectionReferenceImpl>
            implements ProjectionChildBuilder
    {
        @Nonnull
        private final AbstractProjectionParentBuilder<?> parentBuilder;
        @Nonnull
        private final AssociationEndBuilder              associationEndBuilder;
        private       ProjectionBuilder                  referencedProjectionBuilder;

        public ProjectionProjectionReferenceBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull AbstractProjectionParentBuilder<?> parentBuilder,
                @Nonnull AssociationEndBuilder associationEndBuilder)
        {
            super(elementContext, inferred, nameContext, name, ordinal);
            this.parentBuilder = Objects.requireNonNull(parentBuilder);
            this.associationEndBuilder = Objects.requireNonNull(associationEndBuilder);
        }

        public void setReferencedProjectionBuilder(ProjectionBuilder referencedProjectionBuilder)
        {
            if (this.referencedProjectionBuilder != null)
            {
                throw new IllegalStateException();
            }

            this.referencedProjectionBuilder = Objects.requireNonNull(referencedProjectionBuilder);
        }

        @Override
        @Nonnull
        protected ProjectionProjectionReferenceImpl buildUnsafe()
        {
            return new ProjectionProjectionReferenceImpl(
                    this.elementContext,
                    this.inferred,
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.parentBuilder.getElement(),
                    this.associationEndBuilder.getElement());
        }

        @Override
        public void build2()
        {
            ProjectionImpl projection = this.referencedProjectionBuilder.getElement();
            this.getElement().setReferencedProjection(projection);
        }
    }
}
