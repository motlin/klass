package cool.klass.model.meta.domain;

import org.antlr.v4.runtime.ParserRuleContext;

public abstract class TypedElement<T extends Type> extends NamedElement
{
    private final ParserRuleContext typeContext;
    private final T                 type;

    protected TypedElement(
            ParserRuleContext elementContext,
            ParserRuleContext nameContext,
            String name,
            ParserRuleContext typeContext,
            T type)
    {
        super(elementContext, nameContext, name);
        this.typeContext = typeContext;
        this.type = type;
    }

    public ParserRuleContext getTypeContext()
    {
        return this.typeContext;
    }

    public T getType()
    {
        return this.type;
    }

    public abstract static class TypedElementBuilder<T extends Type> extends NamedElementBuilder
    {
        protected final ParserRuleContext typeContext;
        protected final T                 type;

        protected TypedElementBuilder(
                ParserRuleContext elementContext,
                ParserRuleContext nameContext,
                String name,
                ParserRuleContext typeContext,
                T type)
        {
            super(elementContext, nameContext, name);
            this.typeContext = typeContext;
            this.type = type;
        }
    }
}
