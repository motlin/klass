package cool.klass.model.meta.domain;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.EnumerationImpl.EnumerationBuilder;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.source.EnumerationLiteralWithSourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.grammar.KlassParser.EnumerationLiteralContext;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;

public final class EnumerationLiteralImpl
        extends AbstractTypedElement<EnumerationImpl>
        implements EnumerationLiteralWithSourceCode
{
    @Nonnull
    private final Optional<String> prettyName;

    private EnumerationLiteralImpl(
            @Nonnull EnumerationLiteralContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            int ordinal,
            @Nonnull IdentifierContext nameContext,
            @Nonnull EnumerationImpl enumeration,
            @Nonnull Optional<String> prettyName)
    {
        super(elementContext, macroElement, sourceCode, ordinal, nameContext, enumeration);
        this.prettyName = Objects.requireNonNull(prettyName);
    }

    @Nonnull
    @Override
    public EnumerationLiteralContext getElementContext()
    {
        return (EnumerationLiteralContext) super.getElementContext();
    }

    @Nonnull
    @Override
    public Optional<String> getDeclaredPrettyName()
    {
        return this.prettyName;
    }

    public static final class EnumerationLiteralBuilder
            extends IdentifierElementBuilder<EnumerationLiteralImpl>
    {
        @Nonnull
        private final Optional<String>   prettyName;
        @Nonnull
        private final EnumerationBuilder enumerationBuilder;

        public EnumerationLiteralBuilder(
                @Nonnull EnumerationLiteralContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                int ordinal,
                @Nonnull IdentifierContext nameContext,
                @Nonnull Optional<String> prettyName,
                @Nonnull EnumerationBuilder enumerationBuilder)
        {
            super(elementContext, macroElement, sourceCode, ordinal, nameContext);
            this.prettyName         = Objects.requireNonNull(prettyName);
            this.enumerationBuilder = Objects.requireNonNull(enumerationBuilder);
        }

        @Override
        @Nonnull
        protected EnumerationLiteralImpl buildUnsafe()
        {
            return new EnumerationLiteralImpl(
                    (EnumerationLiteralContext) this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.ordinal,
                    this.getNameContext(),
                    this.enumerationBuilder.getElement(),
                    this.prettyName);
        }
    }
}
