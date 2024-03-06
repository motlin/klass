package cool.klass.model.meta.domain.projection;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.AbstractNamedElement;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.projection.Projection;
import cool.klass.model.meta.domain.api.projection.ProjectionParent;
import cool.klass.model.meta.domain.api.projection.ProjectionProjectionReference;
import cool.klass.model.meta.domain.api.property.ReferenceProperty;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.projection.AbstractProjectionElement.ProjectionChildBuilder;
import cool.klass.model.meta.domain.projection.AbstractProjectionParent.AbstractProjectionParentBuilder;
import cool.klass.model.meta.domain.projection.ProjectionImpl.ProjectionBuilder;
import cool.klass.model.meta.domain.property.ReferencePropertyImpl.ReferencePropertyBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class ProjectionProjectionReferenceImpl
        extends AbstractNamedElement
        implements ProjectionProjectionReference
{
    @Nonnull
    private final ProjectionParent  parent;
    @Nonnull
    private final ReferenceProperty referenceProperty;

    private ProjectionImpl referencedProjection;

    private ProjectionProjectionReferenceImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            @Nonnull ParserRuleContext nameContext,
            int ordinal,
            @Nonnull ProjectionParent parent,
            @Nonnull ReferenceProperty referenceProperty)
    {
        super(elementContext, macroElement, sourceCode, nameContext, ordinal);
        this.parent            = Objects.requireNonNull(parent);
        this.referenceProperty = Objects.requireNonNull(referenceProperty);
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
    public ReferenceProperty getProperty()
    {
        return this.referenceProperty;
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
        private final ReferencePropertyBuilder<?, ?, ?>  referencePropertyBuilder;
        private       ProjectionBuilder                  referencedProjectionBuilder;

        public ProjectionProjectionReferenceBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                @Nonnull ParserRuleContext nameContext,
                int ordinal,
                @Nonnull AbstractProjectionParentBuilder<?> parentBuilder,
                @Nonnull ReferencePropertyBuilder<?, ?, ?> referencePropertyBuilder)
        {
            super(elementContext, macroElement, sourceCode, nameContext, ordinal);
            this.parentBuilder            = Objects.requireNonNull(parentBuilder);
            this.referencePropertyBuilder = Objects.requireNonNull(referencePropertyBuilder);
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
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.nameContext,
                    this.ordinal,
                    this.parentBuilder.getElement(),
                    this.referencePropertyBuilder.getElement());
        }

        @Override
        public void build2()
        {
            ProjectionImpl projection = this.referencedProjectionBuilder.getElement();
            this.getElement().setReferencedProjection(projection);
        }
    }
}
