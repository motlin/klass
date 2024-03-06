package cool.klass.model.meta.domain.property;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.KlassImpl;
import cool.klass.model.meta.domain.KlassImpl.KlassBuilder;
import cool.klass.model.meta.domain.api.PrimitiveType;
import cool.klass.model.meta.domain.api.property.PrimitiveProperty;
import cool.klass.model.meta.domain.api.property.PropertyModifier;
import cool.klass.model.meta.domain.property.PropertyModifierImpl.PropertyModifierBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class PrimitivePropertyImpl extends AbstractDataTypeProperty<PrimitiveType> implements PrimitiveProperty
{
    private final boolean isID;

    private PrimitivePropertyImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull PrimitiveType primitiveType,
            @Nonnull KlassImpl owningKlass,
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

    @Override
    public boolean isID()
    {
        return this.isID;
    }

    public static class PrimitivePropertyBuilder extends DataTypePropertyBuilder<PrimitiveType, PrimitiveType>
    {
        private final boolean isID;

        private PrimitivePropertyImpl primitiveProperty;

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
        public PrimitivePropertyImpl build()
        {
            if (this.primitiveProperty != null)
            {
                throw new IllegalStateException();
            }
            ImmutableList<PropertyModifier> propertyModifiers =
                    this.propertyModifierBuilders.collect(PropertyModifierBuilder::build);

            this.primitiveProperty = new PrimitivePropertyImpl(
                    this.elementContext,
                    this.inferred,
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.typeBuilder,
                    this.owningKlassBuilder.getElement(),
                    propertyModifiers,
                    this.isKey,
                    this.isOptional,
                    this.isID);
            return this.primitiveProperty;
        }

        @Override
        public PrimitivePropertyImpl getProperty()
        {
            return this.primitiveProperty;
        }
    }
}
