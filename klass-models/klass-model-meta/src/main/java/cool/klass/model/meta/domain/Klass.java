package cool.klass.model.meta.domain;

import cool.klass.model.meta.domain.Property.PropertyBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public class Klass extends Type
{
    private ImmutableList<Property<?>>         properties;
    private ImmutableList<DataTypeProperty<?>> dataTypeProperties;

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
        this.dataTypeProperties = (ImmutableList<DataTypeProperty<?>>) (ImmutableList<?>) properties
                .selectInstancesOf(DataTypeProperty.class);
    }

    public ImmutableList<DataTypeProperty<?>> getDataTypeProperties()
    {
        return this.dataTypeProperties;
    }

    public static final class KlassBuilder extends TypeBuilder
    {
        private ImmutableList<PropertyBuilder<?>> propertyBuilders;

        public KlassBuilder(
                ParserRuleContext elementContext,
                ParserRuleContext nameContext,
                String name,
                String packageName)
        {
            super(elementContext, nameContext, name, packageName);
        }

        public void setProperties(ImmutableList<PropertyBuilder<?>> propertyBuilders)
        {
            this.propertyBuilders = propertyBuilders;
        }

        public Klass build()
        {
            Klass klass = new Klass(
                    this.elementContext,
                    this.nameContext,
                    this.name,
                    this.packageName);

            ImmutableList<Property<?>> properties = this.propertyBuilders
                    .<Property<?>>collect(each -> each.build(this.elementContext, klass))
                    .toImmutable();

            klass.setProperties(properties);
            return klass;
        }
    }
}
