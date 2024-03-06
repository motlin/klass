package cool.klass.model.meta.domain;

import org.antlr.v4.runtime.ParserRuleContext;

public class Enumeration extends DataType
{
    public Enumeration(String name, String packageName)
    {
        super(name, packageName);
    }

    public static class EnumerationBuilder extends DataTypeBuilder
    {
        public EnumerationBuilder(
                ParserRuleContext elementContext,
                ParserRuleContext nameContext,
                String packageName)
        {
            super(elementContext, nameContext, packageName);
        }
    }
}
