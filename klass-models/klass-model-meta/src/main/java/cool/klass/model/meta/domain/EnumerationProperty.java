package cool.klass.model.meta.domain;

import org.antlr.v4.runtime.ParserRuleContext;

public class EnumerationProperty extends DataTypeProperty
{
    protected EnumerationProperty(String name, Enumeration type)
    {
        super(name, type);
    }

    @Override
    public Enumeration getType()
    {
        return (Enumeration) super.getType();
    }

    public static class EnumerationPropertyBuilder extends DataTypePropertyBuilder
    {
        public EnumerationPropertyBuilder(
                ParserRuleContext elementContext,
                ParserRuleContext nameContext,
                ParserRuleContext typeContext,
                boolean isOptional)
        {
            super(elementContext, nameContext, typeContext, isOptional);
        }
    }
}
