package cool.klass.model.meta.domain;

import java.util.Objects;

import cool.klass.model.meta.domain.Klass.KlassBuilder;
import cool.klass.model.meta.domain.Type.TypeBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class Property<T extends Type> extends TypedElement<T>
{
    private final Klass owningKlass;

    protected Property(
            ParserRuleContext elementContext,
            ParserRuleContext nameContext,
            String name,
            T type,
            Klass owningKlass)
    {
        super(elementContext, nameContext, name, type);
        this.owningKlass = Objects.requireNonNull(owningKlass);
    }

    public Klass getOwningKlass()
    {
        return this.owningKlass;
    }

    public abstract static class PropertyBuilder<T extends Type, TB extends TypeBuilder<T>> extends TypedElementBuilder<T, TB>
    {
        protected final KlassBuilder owningKlassBuilder;

        protected PropertyBuilder(
                ParserRuleContext elementContext,
                ParserRuleContext nameContext,
                String name,
                TB typeBuilder,
                KlassBuilder owningKlassBuilder)
        {
            super(elementContext, nameContext, name, typeBuilder);
            this.owningKlassBuilder = Objects.requireNonNull(owningKlassBuilder);
        }

        public abstract Property<T> build();
    }
}
