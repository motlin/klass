package cool.klass.model.meta.domain;

import cool.klass.model.meta.domain.Klass.KlassBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class Property<T extends Type> extends TypedElement<T>
{
    private final ParserRuleContext owningKlassContext;
    private final Klass             owningKlass;

    protected Property(
            ParserRuleContext elementContext,
            ParserRuleContext nameContext,
            String name,
            ParserRuleContext typeContext,
            T type,
            ParserRuleContext owningKlassContext,
            Klass owningKlass)
    {
        super(elementContext, nameContext, name, typeContext, type);
        this.owningKlassContext = owningKlassContext;
        this.owningKlass = owningKlass;
    }

    public ParserRuleContext getOwningKlassContext()
    {
        return this.owningKlassContext;
    }

    public Klass getOwningKlass()
    {
        return this.owningKlass;
    }

    public abstract static class PropertyBuilder<T extends Type> extends TypedElementBuilder<T>
    {
        private final KlassBuilder owningKlassBuilder;

        protected PropertyBuilder(
                ParserRuleContext elementContext,
                ParserRuleContext nameContext,
                String name,
                ParserRuleContext typeContext,
                T type,
                KlassBuilder owningKlassBuilder)
        {
            super(elementContext, nameContext, name, typeContext, type);
            this.owningKlassBuilder = owningKlassBuilder;
        }

        public abstract Property<T> build(ParserRuleContext owningKlassContext, Klass owningKlass);
    }
}
