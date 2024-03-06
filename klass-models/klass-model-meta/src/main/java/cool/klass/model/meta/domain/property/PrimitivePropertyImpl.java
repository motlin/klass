package cool.klass.model.meta.domain.property;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.KlassImpl;
import cool.klass.model.meta.domain.KlassImpl.KlassBuilder;
import cool.klass.model.meta.domain.api.NamedElement;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.domain.api.property.PrimitiveProperty;
import cool.klass.model.meta.domain.api.property.PropertyModifier;
import cool.klass.model.meta.domain.property.PropertyModifierImpl.PropertyModifierBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class PrimitivePropertyImpl
        extends AbstractDataTypeProperty<PrimitiveType>
        implements PrimitiveProperty
{
    private PrimitivePropertyImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull PrimitiveType primitiveType,
            @Nonnull KlassImpl owningKlass,
            boolean isOptional)
    {
        super(
                elementContext,
                inferred,
                nameContext,
                name,
                ordinal,
                primitiveType,
                owningKlass,
                isOptional);
    }

    @Override
    public String toString()
    {
        String propertyModifiersString = this.getPropertyModifiers().isEmpty()
                ? ""
                : this.getPropertyModifiers().collect(NamedElement::getName).makeString(" ", " ", "");
        return String.format(
                "%s: %s%s",
                this.getName(),
                this.getType().toString(),
                propertyModifiersString);
    }

    public static final class PrimitivePropertyBuilder extends DataTypePropertyBuilder<PrimitiveType, PrimitiveType, PrimitivePropertyImpl>
    {
        public PrimitivePropertyBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull PrimitiveType primitiveType,
                @Nonnull KlassBuilder owningKlassBuilder,
                ImmutableList<PropertyModifierBuilder> propertyModifierBuilders,
                boolean isOptional)
        {
            super(
                    elementContext,
                    inferred,
                    nameContext,
                    name,
                    ordinal,
                    primitiveType,
                    owningKlassBuilder,
                    propertyModifierBuilders,
                    isOptional);
        }

        @Override
        @Nonnull
        protected PrimitivePropertyImpl buildUnsafe()
        {
            return new PrimitivePropertyImpl(
                    this.elementContext,
                    this.inferred,
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.typeBuilder,
                    this.owningKlassBuilder.getElement(),
                    this.isOptional);
        }

        @Override
        protected void buildChildren()
        {
            ImmutableList<PropertyModifier> propertyModifiers =
                    this.propertyModifierBuilders.collect(PropertyModifierBuilder::build);
            this.element.setPropertyModifiers(propertyModifiers);
        }
    }
}
