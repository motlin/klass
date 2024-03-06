package cool.klass.model.meta.domain.projection;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.NamedElement;
import cool.klass.model.meta.domain.property.DataTypeProperty;
import cool.klass.model.meta.domain.property.DataTypeProperty.DataTypePropertyBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class ProjectionPrimitiveMember extends NamedElement implements ProjectionMember
{
    @Nonnull
    private final ParserRuleContext   headerContext;
    @Nonnull
    private final String              headerText;
    @Nonnull
    private final DataTypeProperty<?> property;

    private ProjectionPrimitiveMember(
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

    public static final class ProjectionPrimitiveMemberBuilder extends NamedElementBuilder implements ProjectionMemberBuilder
    {
        @Nonnull
        private final ParserRuleContext             headerContext;
        @Nonnull
        private final String                        headerText;
        @Nonnull
        private final DataTypePropertyBuilder<?, ?> propertyBuilder;

        public ProjectionPrimitiveMemberBuilder(
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
        public ProjectionPrimitiveMember build()
        {
            return new ProjectionPrimitiveMember(
                    this.elementContext,
                    this.nameContext,
                    this.name,
                    this.headerContext,
                    this.headerText,
                    this.propertyBuilder.getProperty());
        }
    }
}
