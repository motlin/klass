package cool.klass.model.meta.domain.projection;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractNamedElement;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.projection.ProjectionDataTypeProperty;
import cool.klass.model.meta.domain.api.projection.ProjectionParent;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.projection.AbstractProjectionParent.AbstractProjectionParentBuilder;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty.DataTypePropertyBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class ProjectionDataTypePropertyImpl
        extends AbstractNamedElement
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
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nonnull Optional<SourceCode> sourceCode,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull ParserRuleContext headerContext,
            @Nonnull String headerText,
            @Nonnull ProjectionParent parent,
            @Nonnull AbstractDataTypeProperty<?> property)
    {
        super(elementContext, macroElement, sourceCode, nameContext, name, ordinal);
        this.headerContext = Objects.requireNonNull(headerContext);
        this.headerText    = Objects.requireNonNull(headerText);
        this.parent        = Objects.requireNonNull(parent);
        this.property      = Objects.requireNonNull(property);
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
            extends NamedElementBuilder<ProjectionDataTypePropertyImpl>
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
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull Optional<SourceCodeBuilder> sourceCode,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull ParserRuleContext headerContext,
                @Nonnull String headerText,
                @Nonnull AbstractProjectionParentBuilder<?> parentBuilder,
                @Nonnull DataTypePropertyBuilder<?, ?, ?> propertyBuilder)
        {
            super(elementContext, macroElement, sourceCode, nameContext, name, ordinal);
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
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.map(SourceCodeBuilder::build),
                    this.nameContext,
                    this.name,
                    this.ordinal,
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
