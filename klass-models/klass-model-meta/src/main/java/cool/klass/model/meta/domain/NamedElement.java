package cool.klass.model.meta.domain;

import java.util.Objects;

import org.antlr.v4.runtime.ParserRuleContext;

public abstract class NamedElement extends Element
{
    private final ParserRuleContext nameContext;
    private final String            name;

    protected NamedElement(
            ParserRuleContext elementContext,
            ParserRuleContext nameContext,
            String name)
    {
        super(elementContext);
        this.nameContext = nameContext;
        this.name = name;
    }

    public ParserRuleContext getNameContext()
    {
        return this.nameContext;
    }

    public final String getName()
    {
        return this.name;
    }

    public abstract static class NamedElementBuilder extends ElementBuilder
    {
        protected final ParserRuleContext nameContext;
        protected final String            name;

        protected NamedElementBuilder(
                ParserRuleContext elementContext,
                ParserRuleContext nameContext,
                String name)
        {
            super(elementContext);
            this.nameContext = Objects.requireNonNull(nameContext);
            this.name = Objects.requireNonNull(name);
        }
    }
}
