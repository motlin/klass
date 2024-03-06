package cool.klass.model.meta.domain.property;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Klass;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import cool.klass.model.meta.domain.Type;
import cool.klass.model.meta.domain.Type.TypeGetter;
import cool.klass.model.meta.domain.TypedElement;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class Property<T extends Type> extends TypedElement<T>
{
    @Nonnull
    private final Klass owningKlass;

    protected Property(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull T type,
            @Nonnull Klass owningKlass)
    {
        super(elementContext, inferred, nameContext, name, ordinal, type);
        this.owningKlass = Objects.requireNonNull(owningKlass);
    }

    @Nonnull
    public Klass getOwningKlass()
    {
        return this.owningKlass;
    }

    public abstract static class PropertyBuilder<T extends Type, TG extends TypeGetter> extends TypedElementBuilder<TG>
    {
        @Nonnull
        protected final KlassBuilder owningKlassBuilder;

        protected PropertyBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull TG typeBuilder,
                @Nonnull KlassBuilder owningKlassBuilder)
        {
            super(elementContext, inferred, nameContext, name, ordinal, typeBuilder);
            this.owningKlassBuilder = Objects.requireNonNull(owningKlassBuilder);
        }

        public abstract Property<T> build();
    }
}
