package cool.klass.model.meta.domain.projection;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.projection.ProjectionParent;
import cool.klass.model.meta.domain.api.projection.ProjectionReferenceProperty;
import cool.klass.model.meta.domain.api.property.ReferenceProperty;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.property.ReferencePropertyImpl.ReferencePropertyBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class ProjectionReferencePropertyImpl
        extends AbstractProjectionParent
        implements ProjectionReferenceProperty
{
    @Nonnull
    private final ProjectionParent  parent;
    @Nonnull
    private final ReferenceProperty referenceProperty;

    private ProjectionReferencePropertyImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull ProjectionParent parent,
            @Nonnull ReferenceProperty referenceProperty)
    {
        super(elementContext, macroElement, sourceCode, nameContext, name, ordinal);
        this.parent            = Objects.requireNonNull(parent);
        this.referenceProperty = Objects.requireNonNull(referenceProperty);
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

    public static final class ProjectionReferencePropertyBuilder
            extends AbstractProjectionParentBuilder<ProjectionReferencePropertyImpl>
            implements ProjectionChildBuilder
    {
        @Nonnull
        private final AbstractProjectionParentBuilder<?> parentBuilder;
        @Nonnull
        private final ReferencePropertyBuilder<?, ?, ?>  referencePropertyBuilder;

        public ProjectionReferencePropertyBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull AbstractProjectionParentBuilder<?> parentBuilder,
                @Nonnull ReferencePropertyBuilder<?, ?, ?> referencePropertyBuilder)
        {
            super(elementContext, macroElement, sourceCode, nameContext, name, ordinal);
            this.parentBuilder            = Objects.requireNonNull(parentBuilder);
            this.referencePropertyBuilder = Objects.requireNonNull(referencePropertyBuilder);
        }

        @Override
        @Nonnull
        protected ProjectionReferencePropertyImpl buildUnsafe()
        {
            return new ProjectionReferencePropertyImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.parentBuilder.getElement(),
                    this.referencePropertyBuilder.getElement());
        }
    }
}
