package cool.klass.model.meta.domain;

import java.util.Objects;

import org.antlr.v4.runtime.ParserRuleContext;

public abstract class Element
{
    public abstract static class ElementBuilder
    {
        protected final ParserRuleContext elementContext;

        protected ElementBuilder(ParserRuleContext elementContext)
        {
            this.elementContext = Objects.requireNonNull(elementContext);
        }

        public ParserRuleContext getElementContext()
        {
            return this.elementContext;
        }
    }
}
