package cool.klass.model.meta.domain.projection;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.NamedElement;
import cool.klass.model.meta.domain.property.DataTypeProperty;
import cool.klass.model.meta.domain.property.DataTypeProperty.DataTypePropertyBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.impl.factory.Lists;

public final class ProjectionDataTypeProperty extends NamedElement implements ProjectionElement
{
    @Nonnull
    private final ParserRuleContext   headerContext;
    @Nonnull
    private final String              headerText;
    @Nonnull
    private final DataTypeProperty<?> property;

    private ProjectionDataTypeProperty(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            @Nonnull ParserRuleContext headerContext,
            @Nonnull String headerText,
            @Nonnull DataTypeProperty<?> property)
    {
        super(elementContext, nameContext, name);
        this.headerContext = Objects.requireNonNull(headerContext);
        this.headerText = Objects.requireNonNull(headerText);
        this.property = Objects.requireNonNull(property);
    }

    @Override
    public void enter(ProjectionListener listener)
    {
        listener.enterProjectionDataTypeProperty(this);
    }

    @Override
    public void exit(ProjectionListener listener)
    {
        listener.exitProjectionDataTypeProperty(this);
    }

    @Override
    public ImmutableList<ProjectionElement> getChildren()
    {
        return Lists.immutable.empty();
    }

    @Nonnull
    public String getHeaderText()
    {
        return this.headerText;
    }

    @Nonnull
    public DataTypeProperty<?> getProperty()
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
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                @Nonnull ParserRuleContext headerContext,
                @Nonnull String headerText,
                @Nonnull DataTypePropertyBuilder<?, ?> propertyBuilder)
        {
            super(elementContext, nameContext, name);
            this.headerContext = Objects.requireNonNull(headerContext);
            this.headerText = Objects.requireNonNull(headerText);
            this.propertyBuilder = Objects.requireNonNull(propertyBuilder);
        }

        @Override
        public ProjectionDataTypeProperty build()
        {
            return new ProjectionDataTypeProperty(
                    this.elementContext,
                    this.nameContext,
                    this.name,
                    this.headerContext,
                    this.headerText,
                    this.propertyBuilder.getProperty());
        }
    }
}
