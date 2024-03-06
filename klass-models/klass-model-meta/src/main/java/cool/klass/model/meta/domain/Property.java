package cool.klass.model.meta.domain;

import org.antlr.v4.runtime.ParserRuleContext;

public abstract class Property extends TypedElement
{
    protected Property(String name, Type type)
    {
        super(name, type);
    }

    public abstract static class PropertyBuilder extends TypedElementBuilder
    {
        protected PropertyBuilder(
                ParserRuleContext elementContext,
                ParserRuleContext nameContext,
                ParserRuleContext typeContext)
        {
            super(elementContext, nameContext, typeContext);
        }
    }
}
