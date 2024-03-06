package cool.klass.model.meta.domain;

import org.antlr.v4.runtime.ParserRuleContext;

public abstract class TypedElement extends NamedElement
{
    private final Type type;

    protected TypedElement(String name, Type type)
    {
        super(name);
        this.type = type;
    }

    public Type getType()
    {
        return this.type;
    }

    public abstract static class TypedElementBuilder extends NamedElementBuilder
    {
        private final ParserRuleContext typeContext;

        protected TypedElementBuilder(
                ParserRuleContext elementContext,
                ParserRuleContext nameContext,
                ParserRuleContext typeContext)
        {
            super(elementContext, nameContext);
            this.typeContext = typeContext;
        }
    }
}
