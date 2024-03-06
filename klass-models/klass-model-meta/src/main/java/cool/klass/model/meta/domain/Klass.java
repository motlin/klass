package cool.klass.model.meta.domain;

import cool.klass.model.meta.domain.EnumerationProperty.EnumerationPropertyBuilder;
import cool.klass.model.meta.domain.PrimitiveProperty.PrimitivePropertyBuilder;
import cool.klass.model.meta.domain.Property.PropertyBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

public class Klass extends Type
{
    public Klass(String name, String packageName)
    {
        super(name, packageName);
    }

    public static final class KlassBuilder extends TypeBuilder
    {
        private final MutableList<PropertyBuilder> memberBuilders = Lists.mutable.empty();
        private final MutableList<PrimitivePropertyBuilder> primitivePropertyBuilders = Lists.mutable.empty();
        private final MutableList<EnumerationPropertyBuilder> enumerationPropertyBuilders = Lists.mutable.empty();

        public KlassBuilder(
                ParserRuleContext elementContext,
                ParserRuleContext nameContext,
                String packageName)
        {
            super(elementContext, nameContext, packageName);
        }

        public KlassBuilder primitiveProperty(PrimitivePropertyBuilder primitivePropertyBuilder)
        {
            this.memberBuilders.add(primitivePropertyBuilder);
            this.primitivePropertyBuilders.add(primitivePropertyBuilder);
            return this;
        }

        public KlassBuilder enumerationProperty(EnumerationPropertyBuilder enumerationPropertyBuilder)
        {
            this.memberBuilders.add(enumerationPropertyBuilder);
            this.enumerationPropertyBuilders.add(enumerationPropertyBuilder);
            return this;
        }
    }
}
