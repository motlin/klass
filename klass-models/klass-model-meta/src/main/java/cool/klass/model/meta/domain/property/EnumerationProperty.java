package cool.klass.model.meta.domain.property;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Enumeration;
import cool.klass.model.meta.domain.Enumeration.EnumerationBuilder;
import cool.klass.model.meta.domain.Klass;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import cool.klass.model.meta.domain.property.PropertyModifier.PropertyModifierBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class EnumerationProperty extends DataTypeProperty<Enumeration>
{
    private EnumerationProperty(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull Enumeration enumeration,
            @Nonnull Klass owningKlass,
            @Nonnull ImmutableList<PropertyModifier> propertyModifiers,
            boolean isKey,
            boolean isOptional)
    {
        super(
                elementContext,
                nameContext,
                name,
                ordinal,
                enumeration,
                owningKlass,
                propertyModifiers,
                isKey,
                isOptional);
    }

    @Override
    public boolean isTemporalRange()
    {
        return false;
    }

    @Override
    public boolean isTemporalInstant()
    {
        return false;
    }

    @Override
    public boolean isTemporal()
    {
        return false;
    }

    public static class EnumerationPropertyBuilder extends DataTypePropertyBuilder<Enumeration, EnumerationBuilder>
    {
        private EnumerationProperty enumerationProperty;

        public EnumerationPropertyBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull EnumerationBuilder enumerationBuilder,
                @Nonnull KlassBuilder owningKlassBuilder,
                @Nonnull ImmutableList<PropertyModifierBuilder> propertyModifierBuilders,
                boolean isKey,
                boolean isOptional)
        {
            super(
                    elementContext,
                    nameContext,
                    name,
                    ordinal,
                    enumerationBuilder,
                    owningKlassBuilder,
                    propertyModifierBuilders,
                    isKey,
                    isOptional);
        }

        @Override
        public EnumerationProperty build()
        {
            if (this.enumerationProperty != null)
            {
                throw new IllegalStateException();
            }

            ImmutableList<PropertyModifier> propertyModifiers =
                    this.propertyModifierBuilders.collect(PropertyModifierBuilder::build);

            this.enumerationProperty = new EnumerationProperty(
                    this.elementContext,
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.typeBuilder.getEnumeration(),
                    this.owningKlassBuilder.getKlass(),
                    propertyModifiers,
                    this.isKey,
                    this.isOptional);
            return this.enumerationProperty;
        }

        @Override
        public EnumerationProperty getProperty()
        {
            return this.enumerationProperty;
        }
    }
}
