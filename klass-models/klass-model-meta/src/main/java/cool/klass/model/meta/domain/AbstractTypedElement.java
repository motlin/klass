package cool.klass.model.meta.domain;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.Type;
import cool.klass.model.meta.domain.api.Type.TypeGetter;
import cool.klass.model.meta.domain.api.TypedElement;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AbstractTypedElement<T extends Type> extends AbstractNamedElement implements TypedElement
{
    @Nonnull
    protected final T type;

    protected AbstractTypedElement(
            @Nonnull ParserRuleContext elementContext,
            Optional<Element> macroElement,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull T type)
    {
        super(elementContext, macroElement, nameContext, name, ordinal);
        this.type = Objects.requireNonNull(type);
    }

    @Override
    @Nonnull
    public final T getType()
    {
        return this.type;
    }

    public abstract static class TypedElementBuilder<T extends Type, TG extends TypeGetter, BuiltElement extends AbstractTypedElement<T>> extends NamedElementBuilder<BuiltElement>
    {
        @Nonnull
        protected final TG typeBuilder;

        protected TypedElementBuilder(
                @Nonnull ParserRuleContext elementContext,
                Optional<ElementBuilder<?>> macroElement,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull TG typeBuilder)
        {
            super(elementContext, macroElement, nameContext, name, ordinal);
            this.typeBuilder = Objects.requireNonNull(typeBuilder);
        }
    }
}
