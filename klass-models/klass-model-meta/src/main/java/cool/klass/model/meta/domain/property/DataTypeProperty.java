package cool.klass.model.meta.domain.property;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.DataType;
import cool.klass.model.meta.domain.DataType.DataTypeBuilder;
import cool.klass.model.meta.domain.Klass;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import cool.klass.model.meta.domain.property.PropertyModifier.PropertyModifierBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

// TODO: The generic type here is inconvenient. Replace it with a bunch of overrides of the getType method
public abstract class DataTypeProperty<T extends DataType> extends Property<T>
{
    @Nonnull
    private final ImmutableList<PropertyModifier> propertyModifiers;
    private final boolean                         key;
    private final boolean                         optional;

    protected DataTypeProperty(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull T dataType,
            @Nonnull Klass owningKlass,
            @Nonnull ImmutableList<PropertyModifier> propertyModifiers,
            boolean isKey,
            boolean isOptional)
    {
        super(elementContext, nameContext, name, ordinal, dataType, owningKlass);
        this.propertyModifiers = Objects.requireNonNull(propertyModifiers);
        this.key = isKey;
        this.optional = isOptional;
    }

    @Nonnull
    public ImmutableList<PropertyModifier> getPropertyModifiers()
    {
        return this.propertyModifiers;
    }

    public boolean isKey()
    {
        return this.key;
    }

    public boolean isOptional()
    {
        return this.optional;
    }

    public abstract boolean isTemporalRange();

    public abstract boolean isTemporalInstant();

    public abstract boolean isTemporal();

    public abstract static class DataTypePropertyBuilder<T extends DataType, TB extends DataTypeBuilder> extends PropertyBuilder<T, TB>
    {
        protected final ImmutableList<PropertyModifierBuilder> propertyModifierBuilders;
        protected final boolean                                isKey;
        protected final boolean                                isOptional;

        protected DataTypePropertyBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull TB typeBuilder,
                @Nonnull KlassBuilder owningKlassBuilder,
                ImmutableList<PropertyModifierBuilder> propertyModifierBuilders,
                boolean isKey,
                boolean isOptional)
        {
            super(elementContext, nameContext, name, ordinal, typeBuilder, owningKlassBuilder);
            this.propertyModifierBuilders = Objects.requireNonNull(propertyModifierBuilders);
            this.isKey = isKey;
            this.isOptional = isOptional;
        }

        @Override
        public abstract DataTypeProperty<T> build();

        public abstract DataTypeProperty<T> getProperty();
    }
}
