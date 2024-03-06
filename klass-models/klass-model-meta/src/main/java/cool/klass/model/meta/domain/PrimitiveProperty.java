package cool.klass.model.meta.domain;

import org.antlr.v4.runtime.ParserRuleContext;

public class PrimitiveProperty extends DataTypeProperty
{
    protected PrimitiveProperty(String name, PrimitiveType type)
    {
        super(name, type);
    }

    @Override
    public PrimitiveType getType()
    {
        return (PrimitiveType) super.getType();
    }

    public static class PrimitivePropertyBuilder extends DataTypePropertyBuilder
    {
        public PrimitivePropertyBuilder(
                ParserRuleContext elementContext,
                ParserRuleContext nameContext,
                ParserRuleContext typeContext,
                boolean isOptional)
        {
            super(elementContext, nameContext, typeContext, isOptional);
        }
    }
}
