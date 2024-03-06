package cool.klass.model.meta.domain.projection;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.projection.ProjectionParent;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.api.source.projection.ProjectionReferencePropertyWithSourceCode;
import cool.klass.model.meta.domain.api.source.property.ReferencePropertyWithSourceCode;
import cool.klass.model.meta.domain.property.ReferencePropertyImpl.ReferencePropertyBuilder;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionReferencePropertyContext;

public final class ProjectionReferencePropertyImpl
        extends AbstractProjectionParent
        implements ProjectionReferencePropertyWithSourceCode
{
    @Nonnull
    private final ProjectionParent                parent;
    @Nonnull
    private final ReferencePropertyWithSourceCode referenceProperty;

    private ProjectionReferencePropertyImpl(
            @Nonnull ProjectionReferencePropertyContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            int ordinal,
            @Nonnull IdentifierContext nameContext,
            @Nonnull ProjectionParent parent,
            @Nonnull ReferencePropertyWithSourceCode referenceProperty)
    {
        super(elementContext, macroElement, sourceCode, ordinal, nameContext);
        this.parent            = Objects.requireNonNull(parent);
        this.referenceProperty = Objects.requireNonNull(referenceProperty);
    }

    @Nonnull
    @Override
    public ProjectionReferencePropertyContext getElementContext()
    {
        return (ProjectionReferencePropertyContext) super.getElementContext();
    }

    @Override
    @Nonnull
    public Optional<ProjectionParent> getParent()
    {
        return Optional.of(this.parent);
    }

    @Override
    @Nonnull
    public ReferencePropertyWithSourceCode getProperty()
    {
        return this.referenceProperty;
    }

    public static final class ProjectionReferencePropertyBuilder
            extends AbstractProjectionParentBuilder<ProjectionReferencePropertyImpl>
            implements ProjectionChildBuilder
    {
        @Nonnull
        private final AbstractProjectionParentBuilder<?> parentBuilder;
        @Nonnull
        private final ReferencePropertyBuilder<?, ?, ?>  referencePropertyBuilder;

        public ProjectionReferencePropertyBuilder(
                @Nonnull ProjectionReferencePropertyContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                int ordinal,
                @Nonnull IdentifierContext nameContext,
                @Nonnull AbstractProjectionParentBuilder<?> parentBuilder,
                @Nonnull ReferencePropertyBuilder<?, ?, ?> referencePropertyBuilder)
        {
            super(elementContext, macroElement, sourceCode, ordinal, nameContext);
            this.parentBuilder            = Objects.requireNonNull(parentBuilder);
            this.referencePropertyBuilder = Objects.requireNonNull(referencePropertyBuilder);
        }

        @Override
        @Nonnull
        protected ProjectionReferencePropertyImpl buildUnsafe()
        {
            return new ProjectionReferencePropertyImpl(
                    (ProjectionReferencePropertyContext) this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.ordinal,
                    this.getNameContext(),
                    this.parentBuilder.getElement(),
                    this.referencePropertyBuilder.getElement());
        }
    }
}
