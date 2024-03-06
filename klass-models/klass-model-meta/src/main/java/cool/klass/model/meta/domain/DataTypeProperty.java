package cool.klass.model.meta.domain;

import java.util.LinkedHashSet;

import cool.klass.model.meta.domain.Klass.KlassBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.set.mutable.SetAdapter;

public class DataTypeProperty<T extends DataType> extends Property<T>
{
    private final boolean key;
    private final boolean optional;

    public DataTypeProperty(
            ParserRuleContext elementContext,
            ParserRuleContext nameContext,
            String name,
            ParserRuleContext dataTypeContext,
            T dataType,
            ParserRuleContext owningKlassContext,
            Klass owningKlass,
            boolean isKey,
            boolean isOptional)
    {
        super(elementContext, nameContext, name, dataTypeContext, dataType, owningKlassContext, owningKlass);
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

    public abstract static class DataTypePropertyBuilder<T extends DataType> extends PropertyBuilder<T>
    {
        protected final boolean            isOptional;
        protected final MutableSet<String> modifiers = SetAdapter.adapt(new LinkedHashSet<>());

        protected DataTypePropertyBuilder(
                ParserRuleContext elementContext,
                ParserRuleContext nameContext,
                String name,
                ParserRuleContext typeContext,
                T type,
                KlassBuilder owningKlassBuilder,
                boolean isOptional)
        {
            super(elementContext, nameContext, name, typeContext, type, owningKlassBuilder);
            this.isOptional = isOptional;
        }

        public boolean addModifier(String modifier)
        {
            return this.modifiers.add(modifier);
        }

        protected boolean isKey()
        {
            return this.modifiers.contains("key");
        }
    }
}
