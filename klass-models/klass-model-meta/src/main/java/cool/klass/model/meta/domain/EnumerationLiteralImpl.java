package cool.klass.model.meta.domain;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.EnumerationImpl.EnumerationBuilder;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.EnumerationLiteral;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class EnumerationLiteralImpl
        extends AbstractTypedElement<EnumerationImpl>
        implements EnumerationLiteral
{
    @Nonnull
    private final Optional<String> prettyName;

    private EnumerationLiteralImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nonnull Optional<SourceCode> sourceCode,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull EnumerationImpl enumeration,
            @Nonnull Optional<String> prettyName)
    {
        super(elementContext, macroElement, sourceCode, nameContext, name, ordinal, enumeration);
        this.prettyName = Objects.requireNonNull(prettyName);
    }

    @Nonnull
    @Override
    public Optional<String> getDeclaredPrettyName()
    {
        return this.prettyName;
    }

    public static final class EnumerationLiteralBuilder
            extends NamedElementBuilder<EnumerationLiteralImpl>
    {
        @Nonnull
        private final Optional<String>   prettyName;
        @Nonnull
        private final EnumerationBuilder enumerationBuilder;

        public EnumerationLiteralBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull Optional<SourceCodeBuilder> sourceCode,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull Optional<String> prettyName,
                @Nonnull EnumerationBuilder enumerationBuilder)
        {
            super(elementContext, macroElement, sourceCode, nameContext, name, ordinal);
            this.prettyName         = Objects.requireNonNull(prettyName);
            this.enumerationBuilder = Objects.requireNonNull(enumerationBuilder);
        }

        @Override
        @Nonnull
        protected EnumerationLiteralImpl buildUnsafe()
        {
            return new EnumerationLiteralImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.map(SourceCodeBuilder::build),
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.enumerationBuilder.getElement(),
                    this.prettyName);
        }
    }
}
