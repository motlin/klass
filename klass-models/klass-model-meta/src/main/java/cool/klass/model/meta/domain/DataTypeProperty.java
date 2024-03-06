package cool.klass.model.meta.domain;

import java.util.LinkedHashSet;

import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.impl.set.mutable.SetAdapter;

public class DataTypeProperty extends Property
{
    public DataTypeProperty(String name, Type type)
    {
        super(name, type);
    }

    @Override
    public DataType getType()
    {
        return (DataType) super.getType();
    }

    public abstract static class DataTypePropertyBuilder extends PropertyBuilder
    {
        protected final boolean isOptional;
        private final MutableSet<String> modifiers = SetAdapter.adapt(new LinkedHashSet<>());

        protected DataTypePropertyBuilder(
                ParserRuleContext elementContext,
                ParserRuleContext nameContext,
                ParserRuleContext typeContext,
                boolean isOptional)
        {
            super(elementContext, nameContext, typeContext);
            this.isOptional = isOptional;
        }

        public boolean addModifier(String modifier)
        {
            return this.modifiers.add(modifier);
        }
    }
}
