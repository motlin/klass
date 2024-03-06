package cool.klass.model.meta.domain.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.modifier.DataTypePropertyModifier;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty.DataTypePropertyBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class DataTypePropertyModifierImpl
        extends AbstractModifier
        implements DataTypePropertyModifier
{
    private DataTypePropertyModifierImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AbstractDataTypeProperty<?> owningProperty)
    {
        super(elementContext, macroElement, nameContext, name, ordinal, owningProperty);
    }

    @Override
    public DataTypeProperty getModifierOwner()
    {
        return (DataTypeProperty) super.getModifierOwner();
    }

    public static final class DataTypePropertyModifierBuilder
            extends ModifierBuilder<DataTypePropertyModifierImpl>
    {
        @Nonnull
        private final DataTypePropertyBuilder<?, ?, ?> owningPropertyBuilder;

        public DataTypePropertyModifierBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull DataTypePropertyBuilder<?, ?, ?> owningPropertyBuilder)
        {
            super(elementContext, macroElement, nameContext, name, ordinal);
            this.owningPropertyBuilder = Objects.requireNonNull(owningPropertyBuilder);
        }

        @Override
        @Nonnull
        protected DataTypePropertyModifierImpl buildUnsafe()
        {
            return new DataTypePropertyModifierImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.owningPropertyBuilder.getElement());
        }
    }
}
