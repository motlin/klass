package cool.klass.model.meta.domain.property;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractTypedElement;
import cool.klass.model.meta.domain.KlassImpl;
import cool.klass.model.meta.domain.KlassImpl.KlassBuilder;
import cool.klass.model.meta.domain.api.Type;
import cool.klass.model.meta.domain.api.Type.TypeGetter;
import cool.klass.model.meta.domain.api.property.Property;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AbstractProperty<T extends Type> extends AbstractTypedElement<T> implements Property
{
    @Nonnull
    private final KlassImpl owningKlass;

    protected AbstractProperty(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull T type,
            @Nonnull KlassImpl owningKlass)
    {
        super(elementContext, inferred, nameContext, name, ordinal, type);
        this.owningKlass = Objects.requireNonNull(owningKlass);
    }

    @Override
    @Nonnull
    public KlassImpl getOwningKlass()
    {
        return this.owningKlass;
    }

    public abstract static class PropertyBuilder<T extends Type, TG extends TypeGetter, BuiltElement extends AbstractProperty<T>> extends TypedElementBuilder<T, TG, BuiltElement>
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
    }
}
