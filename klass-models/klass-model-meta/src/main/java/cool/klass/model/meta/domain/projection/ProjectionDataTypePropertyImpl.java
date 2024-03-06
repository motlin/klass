package cool.klass.model.meta.domain.projection;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.AbstractIdentifierElement;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.projection.ProjectionDataTypeProperty;
import cool.klass.model.meta.domain.api.projection.ProjectionParent;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.projection.AbstractProjectionParent.AbstractProjectionParentBuilder;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty.DataTypePropertyBuilder;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import cool.klass.model.meta.grammar.KlassParser.ProjectionPrimitiveMemberContext;
import org.antlr.v4.runtime.ParserRuleContext;

public final class ProjectionDataTypePropertyImpl
        extends AbstractIdentifierElement
        implements AbstractProjectionElement, ProjectionDataTypeProperty
{
    @Nonnull
    private final ParserRuleContext           headerContext;
    @Nonnull
    private final String                      headerText;
    @Nonnull
    private final ProjectionParent            parent;
    @Nonnull
    private final AbstractDataTypeProperty<?> property;

    private ProjectionDataTypePropertyImpl(
            @Nonnull ProjectionPrimitiveMemberContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            int ordinal,
            @Nonnull IdentifierContext nameContext,
            @Nonnull ParserRuleContext headerContext,
            @Nonnull String headerText,
            @Nonnull ProjectionParent parent,
            @Nonnull AbstractDataTypeProperty<?> property)
    {
        super(elementContext, macroElement, sourceCode, ordinal, nameContext);
        this.headerContext = Objects.requireNonNull(headerContext);
        this.headerText    = Objects.requireNonNull(headerText);
        this.parent        = Objects.requireNonNull(parent);
        this.property      = Objects.requireNonNull(property);
    }

    @Nonnull
    @Override
    public ProjectionPrimitiveMemberContext getElementContext()
    {
        return (ProjectionPrimitiveMemberContext) super.getElementContext();
    }

    @Nonnull
    @Override
    public Optional<ProjectionParent> getParent()
    {
        return Optional.of(this.parent);
    }

    @Override
    @Nonnull
    public String getHeaderText()
    {
        return this.headerText;
    }

    @Override
    @Nonnull
    public AbstractDataTypeProperty<?> getProperty()
    {
        return this.property;
    }

    public static final class ProjectionDataTypePropertyBuilder
            extends IdentifierElementBuilder<ProjectionDataTypePropertyImpl>
            implements ProjectionChildBuilder
    {
        @Nonnull
        private final ParserRuleContext                  headerContext;
        @Nonnull
        private final String                             headerText;
        @Nonnull
        private final AbstractProjectionParentBuilder<?> parentBuilder;
        @Nonnull
        private final DataTypePropertyBuilder<?, ?, ?>   propertyBuilder;

        public ProjectionDataTypePropertyBuilder(
                @Nonnull ProjectionPrimitiveMemberContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                int ordinal,
                @Nonnull IdentifierContext nameContext,
                @Nonnull ParserRuleContext headerContext,
                @Nonnull String headerText,
                @Nonnull AbstractProjectionParentBuilder<?> parentBuilder,
                @Nonnull DataTypePropertyBuilder<?, ?, ?> propertyBuilder)
        {
            super(elementContext, macroElement, sourceCode, ordinal, nameContext);
            this.headerContext   = Objects.requireNonNull(headerContext);
            this.headerText      = Objects.requireNonNull(headerText);
            this.parentBuilder   = Objects.requireNonNull(parentBuilder);
            this.propertyBuilder = Objects.requireNonNull(propertyBuilder);
        }

        @Override
        @Nonnull
        protected ProjectionDataTypePropertyImpl buildUnsafe()
        {
            return new ProjectionDataTypePropertyImpl(
                    (ProjectionPrimitiveMemberContext) this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.ordinal,
                    this.getNameContext(),
                    this.headerContext,
                    this.headerText,
                    this.parentBuilder.getElement(),
                    this.propertyBuilder.getElement());
        }

        @Override
        public void build2()
        {
            // Deliberately empty
        }
    }
}
