package cool.klass.model.meta.domain.property;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.DataType;
import cool.klass.model.meta.domain.DataType.DataTypeBuilder;
import cool.klass.model.meta.domain.Klass;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

// TODO: The generic type here is inconvenient. Replace it with a bunch of overrides of the getType method
public abstract class DataTypeProperty<T extends DataType> extends Property<T>
{
    private final boolean key;
    private final boolean optional;

    protected DataTypeProperty(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            @Nonnull T dataType,
            @Nonnull Klass owningKlass,
            boolean isKey,
            boolean isOptional)
    {
        super(elementContext, nameContext, name, dataType, owningKlass);
        this.key = isKey;
        this.optional = isOptional;
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
        protected final boolean isKey;
        protected final boolean isOptional;

        protected DataTypePropertyBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                @Nonnull TB typeBuilder,
                @Nonnull KlassBuilder owningKlassBuilder,
                boolean isKey,
                boolean isOptional)
        {
            super(elementContext, nameContext, name, typeBuilder, owningKlassBuilder);
            this.isKey = isKey;
            this.isOptional = isOptional;
        }

        @Override
        public abstract DataTypeProperty<T> build();

        public abstract DataTypeProperty<T> getProperty();
    }
}
