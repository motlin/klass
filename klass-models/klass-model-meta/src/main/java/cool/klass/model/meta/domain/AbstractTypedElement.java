package cool.klass.model.meta.domain;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.Type;
import cool.klass.model.meta.domain.api.Type.TypeGetter;
import cool.klass.model.meta.domain.api.TypedElement;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AbstractTypedElement<T extends Type>
        extends AbstractNamedElement
        implements TypedElement
{
    @Nonnull
    protected final T type;

    protected AbstractTypedElement(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            @Nonnull ParserRuleContext nameContext,
            int ordinal,
            @Nonnull T type)
    {
        super(elementContext, macroElement, sourceCode, nameContext, ordinal);
        this.type = Objects.requireNonNull(type);
    }

    @Override
    @Nonnull
    public final T getType()
    {
        return this.type;
    }

    public abstract static class TypedElementBuilder<T extends Type, TG extends TypeGetter, BuiltElement extends AbstractTypedElement<T>>
            extends NamedElementBuilder<BuiltElement>
    {
        @Nonnull
        protected final TG typeBuilder;

        protected TypedElementBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                @Nonnull ParserRuleContext nameContext,
                int ordinal,
                @Nonnull TG typeBuilder)
        {
            super(elementContext, macroElement, sourceCode, nameContext, ordinal);
            this.typeBuilder = Objects.requireNonNull(typeBuilder);
        }
    }
}
