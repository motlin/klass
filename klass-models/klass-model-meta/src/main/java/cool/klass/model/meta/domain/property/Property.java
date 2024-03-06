package cool.klass.model.meta.domain.property;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Klass;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import cool.klass.model.meta.domain.Type;
import cool.klass.model.meta.domain.Type.TypeBuilder;
import cool.klass.model.meta.domain.TypedElement;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class Property<T extends Type> extends TypedElement<T>
{
    @Nonnull
    private final Klass owningKlass;

    protected Property(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            @Nonnull T type,
            @Nonnull Klass owningKlass)
    {
        super(elementContext, nameContext, name, type);
        this.owningKlass = Objects.requireNonNull(owningKlass);
    }

    @Nonnull
    public Klass getOwningKlass()
    {
        return this.owningKlass;
    }

    public abstract static class PropertyBuilder<T extends Type, TB extends TypeBuilder<T>> extends TypedElementBuilder<T, TB>
    {
        @Nonnull
        protected final KlassBuilder owningKlassBuilder;

        protected PropertyBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                @Nonnull TB typeBuilder,
                @Nonnull KlassBuilder owningKlassBuilder)
        {
            super(elementContext, nameContext, name, typeBuilder);
            this.owningKlassBuilder = Objects.requireNonNull(owningKlassBuilder);
        }

        public abstract Property<T> build();
    }
}
