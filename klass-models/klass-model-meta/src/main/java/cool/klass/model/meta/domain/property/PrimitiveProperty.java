package cool.klass.model.meta.domain.property;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Klass;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import cool.klass.model.meta.domain.property.PropertyModifier.PropertyModifierBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class PrimitiveProperty extends DataTypeProperty<PrimitiveType>
{
    private final boolean isID;

    private PrimitiveProperty(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull PrimitiveType primitiveType,
            @Nonnull Klass owningKlass,
            @Nonnull ImmutableList<PropertyModifier> propertyModifiers,
            boolean isKey,
            boolean isOptional,
            boolean isID)
    {
        super(
                elementContext,
                inferred,
                nameContext,
                name,
                ordinal,
                primitiveType,
                owningKlass,
                propertyModifiers,
                isKey,
                isOptional);
        this.isID = isID;
    }

    @Override
    public boolean isTemporalRange()
    {
        return this.type.isTemporalRange();
    }

    @Override
    public boolean isTemporalInstant()
    {
        return this.type.isTemporalInstant();
    }

    @Override
    public boolean isTemporal()
    {
        return this.type.isTemporal();
    }

    public boolean isID()
    {
        return this.isID;
    }

    public static class PrimitivePropertyBuilder extends DataTypePropertyBuilder<PrimitiveType, PrimitiveType>
    {
        private final boolean isID;

        private PrimitiveProperty primitiveProperty;

        public PrimitivePropertyBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull PrimitiveType primitiveType,
                @Nonnull KlassBuilder owningKlassBuilder,
                ImmutableList<PropertyModifierBuilder> propertyModifierBuilders,
                boolean isKey,
                boolean isOptional,
                boolean isID)
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
                    isKey,
                    isOptional);
            this.isID = isID;
        }

        @Override
        public PrimitiveProperty build()
        {
            if (this.primitiveProperty != null)
            {
                throw new IllegalStateException();
            }
            ImmutableList<PropertyModifier> propertyModifiers =
                    this.propertyModifierBuilders.collect(PropertyModifierBuilder::build);

            this.primitiveProperty = new PrimitiveProperty(
                    this.elementContext,
                    this.inferred,
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.typeBuilder,
                    this.owningKlassBuilder.getKlass(),
                    propertyModifiers,
                    this.isKey,
                    this.isOptional,
                    this.isID);
            return this.primitiveProperty;
        }

        @Override
        public PrimitiveProperty getProperty()
        {
            return this.primitiveProperty;
        }
    }
}
