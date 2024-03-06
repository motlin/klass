package cool.klass.model.meta.domain;

import cool.klass.model.meta.domain.DataType.DataTypeBuilder;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

// TODO: The generic type here is inconvenient. Replace it with a bunch of overrides of the getType method
public abstract class DataTypeProperty<T extends DataType> extends Property<T>
{
    private final boolean key;
    private final boolean optional;

    protected DataTypeProperty(
            ParserRuleContext elementContext,
            ParserRuleContext nameContext,
            String name,
            T dataType,
            Klass owningKlass,
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

    public abstract static class DataTypePropertyBuilder<T extends DataType, TB extends DataTypeBuilder<T>> extends PropertyBuilder<T, TB>
    {
        protected final boolean isKey;
        protected final boolean isOptional;

        protected DataTypePropertyBuilder(
                ParserRuleContext elementContext,
                ParserRuleContext nameContext,
                String name,
                TB typeBuilder,
                KlassBuilder owningKlassBuilder,
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
