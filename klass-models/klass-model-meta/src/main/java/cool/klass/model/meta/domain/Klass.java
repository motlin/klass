package cool.klass.model.meta.domain;

import cool.klass.model.meta.domain.EnumerationProperty.EnumerationPropertyBuilder;
import cool.klass.model.meta.domain.PrimitiveProperty.PrimitivePropertyBuilder;
import cool.klass.model.meta.domain.Property.PropertyBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;
import org.eclipse.collections.api.list.MutableList;
import org.eclipse.collections.impl.factory.Lists;

public class Klass extends Type
{
    private ImmutableList<Property<?>> properties;

    public Klass(
            ParserRuleContext elementContext,
            ParserRuleContext nameContext,
            String name,
            String packageName)
    {
        super(elementContext, nameContext, name, packageName);
    }

    public ImmutableList<Property<?>> getProperties()
    {
        return this.properties;
    }

    private void setProperties(ImmutableList<Property<?>> properties)
    {
        this.properties = properties;
    }

    public static final class KlassBuilder extends TypeBuilder
    {
        private final MutableList<PropertyBuilder<?>>         memberBuilders              = Lists.mutable.empty();
        private final MutableList<PrimitivePropertyBuilder>   primitivePropertyBuilders   = Lists.mutable.empty();
        private final MutableList<EnumerationPropertyBuilder> enumerationPropertyBuilders = Lists.mutable.empty();

        public KlassBuilder(
                ParserRuleContext elementContext,
                ParserRuleContext nameContext,
                String name,
                String packageName)
        {
            super(elementContext, nameContext, name, packageName);
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

        public Klass build()
        {
            Klass klass = new Klass(
                    this.elementContext,
                    this.nameContext,
                    this.name,
                    this.packageName);

            ImmutableList<Property<?>> properties = this.memberBuilders
                    .<Property<?>>collect(each -> each.build(this.elementContext, klass))
                    .toImmutable();

            klass.setProperties(properties);
            return klass;
        }
    }
}
