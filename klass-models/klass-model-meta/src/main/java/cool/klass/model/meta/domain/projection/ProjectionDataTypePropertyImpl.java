package cool.klass.model.meta.domain.projection;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractNamedElement;
import cool.klass.model.meta.domain.api.projection.ProjectionDataTypeProperty;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty.DataTypePropertyBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class ProjectionDataTypePropertyImpl extends AbstractNamedElement implements AbstractProjectionElement, ProjectionDataTypeProperty
{
    @Nonnull
    private final ParserRuleContext           headerContext;
    @Nonnull
    private final String                      headerText;
    @Nonnull
    private final AbstractDataTypeProperty<?> property;

    private ProjectionDataTypePropertyImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull ParserRuleContext headerContext,
            @Nonnull String headerText,
            @Nonnull AbstractDataTypeProperty<?> property)
    {
        super(elementContext, inferred, nameContext, name, ordinal);
        this.headerContext = Objects.requireNonNull(headerContext);
        this.headerText = Objects.requireNonNull(headerText);
        this.property = Objects.requireNonNull(property);
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

    public static final class ProjectionDataTypePropertyBuilder extends NamedElementBuilder implements ProjectionElementBuilder
    {
        @Nonnull
        private final ParserRuleContext             headerContext;
        @Nonnull
        private final String                        headerText;
        @Nonnull
        private final DataTypePropertyBuilder<?, ?> propertyBuilder;

        public ProjectionDataTypePropertyBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull ParserRuleContext headerContext,
                @Nonnull String headerText,
                @Nonnull DataTypePropertyBuilder<?, ?> propertyBuilder)
        {
            super(elementContext, inferred, nameContext, name, ordinal);
            this.headerContext = Objects.requireNonNull(headerContext);
            this.headerText = Objects.requireNonNull(headerText);
            this.propertyBuilder = Objects.requireNonNull(propertyBuilder);
        }

        @Override
        public ProjectionDataTypePropertyImpl build()
        {
            return new ProjectionDataTypePropertyImpl(
                    this.elementContext,
                    this.inferred,
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.headerContext,
                    this.headerText,
                    this.propertyBuilder.getProperty());
        }
    }
}
