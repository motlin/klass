package cool.klass.model.meta.domain.projection;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.projection.ProjectionAssociationEnd;
import cool.klass.model.meta.domain.api.projection.ProjectionParent;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.property.AssociationEndImpl;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class ProjectionAssociationEndImpl
        extends AbstractProjectionParent
        implements ProjectionAssociationEnd
{
    @Nonnull
    private final ProjectionParent   parent;
    @Nonnull
    private final AssociationEndImpl associationEnd;

    private ProjectionAssociationEndImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nonnull Optional<SourceCode> sourceCode,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull ProjectionParent parent,
            @Nonnull AssociationEndImpl associationEnd)
    {
        super(elementContext, macroElement, sourceCode, nameContext, name, ordinal);
        this.parent         = Objects.requireNonNull(parent);
        this.associationEnd = Objects.requireNonNull(associationEnd);
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

    public static final class ProjectionAssociationEndBuilder
            extends AbstractProjectionParentBuilder<ProjectionAssociationEndImpl>
            implements ProjectionChildBuilder
    {
        @Nonnull
        private final AbstractProjectionParentBuilder<?> parentBuilder;
        @Nonnull
        private final AssociationEndBuilder              associationEndBuilder;

        public ProjectionAssociationEndBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull Optional<SourceCodeBuilder> sourceCode,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull AbstractProjectionParentBuilder<?> parentBuilder,
                @Nonnull AssociationEndBuilder associationEndBuilder)
        {
            super(elementContext, macroElement, sourceCode, nameContext, name, ordinal);
            this.parentBuilder         = Objects.requireNonNull(parentBuilder);
            this.associationEndBuilder = Objects.requireNonNull(associationEndBuilder);
        }

        @Override
        @Nonnull
        protected ProjectionAssociationEndImpl buildUnsafe()
        {
            return new ProjectionAssociationEndImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.map(SourceCodeBuilder::build),
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.parentBuilder.getElement(),
                    this.associationEndBuilder.getElement());
        }
    }
}
