package cool.klass.model.meta.domain;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Type.TypeGetter;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class TypedElement<T extends Type> extends NamedElement
{
    @Nonnull
    protected final T type;

    protected TypedElement(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull T type)
    {
        super(elementContext, inferred, nameContext, name, ordinal);
        this.type = Objects.requireNonNull(type);
    }

    @Nonnull
    public T getType()
    {
        return this.type;
    }

    public abstract static class TypedElementBuilder<TG extends TypeGetter> extends NamedElementBuilder
    {
        @Nonnull
        protected final TG typeBuilder;

        protected TypedElementBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull TG typeBuilder)
        {
            super(elementContext, inferred, nameContext, name, ordinal);
            this.typeBuilder = Objects.requireNonNull(typeBuilder);
        }
    }
}
