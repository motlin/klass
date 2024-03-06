package cool.klass.model.meta.domain.property;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.KlassImpl;
import cool.klass.model.meta.domain.KlassImpl.KlassBuilder;
import cool.klass.model.meta.domain.api.DataType;
import cool.klass.model.meta.domain.api.DataType.DataTypeGetter;
import cool.klass.model.meta.domain.api.property.DataTypeProperty;
import cool.klass.model.meta.domain.api.property.PropertyModifier;
import cool.klass.model.meta.domain.property.PropertyModifierImpl.PropertyModifierBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

// TODO: The generic type here is inconvenient. Replace it with a bunch of overrides of the getType method
public abstract class AbstractDataTypeProperty<T extends DataType> extends AbstractProperty<T> implements DataTypeProperty
{
    @Nonnull
    private final ImmutableList<PropertyModifier> propertyModifiers;
    private final boolean                         key;
    private final boolean                         optional;

    protected AbstractDataTypeProperty(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull T dataType,
            @Nonnull KlassImpl owningKlass,
            @Nonnull ImmutableList<PropertyModifier> propertyModifiers,
            boolean isKey,
            boolean isOptional)
    {
        super(elementContext, inferred, nameContext, name, ordinal, dataType, owningKlass);
        this.propertyModifiers = Objects.requireNonNull(propertyModifiers);
        this.key = isKey;
        this.optional = isOptional;
    }

    @Override
    @Nonnull
    public ImmutableList<PropertyModifier> getPropertyModifiers()
    {
        return this.propertyModifiers;
    }

    @Override
    public boolean isKey()
    {
        return this.key;
    }

    @Override
    public boolean isOptional()
    {
        return this.optional;
    }

    public abstract static class DataTypePropertyBuilder<T extends DataType, TG extends DataTypeGetter> extends PropertyBuilder<T, TG>
    {
        protected final ImmutableList<PropertyModifierBuilder> propertyModifierBuilders;
        protected final boolean                                isKey;
        protected final boolean                                isOptional;

        protected DataTypePropertyBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull TG typeBuilder,
                @Nonnull KlassBuilder owningKlassBuilder,
                ImmutableList<PropertyModifierBuilder> propertyModifierBuilders,
                boolean isKey,
                boolean isOptional)
        {
            super(elementContext, inferred, nameContext, name, ordinal, typeBuilder, owningKlassBuilder);
            this.propertyModifierBuilders = Objects.requireNonNull(propertyModifierBuilders);
            this.isKey = isKey;
            this.isOptional = isOptional;
        }

        @Override
        public abstract AbstractDataTypeProperty<T> build();

        public abstract AbstractDataTypeProperty<T> getProperty();
    }
}
