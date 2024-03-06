package cool.klass.model.meta.domain;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.Interface;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;

public final class InterfaceImpl
        extends AbstractClassifier
        implements Interface
{
    private InterfaceImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nullable SourceCode sourceCode,
            @Nonnull IdentifierContext nameContext,
            int ordinal,
            @Nonnull String packageName)
    {
        super(elementContext, macroElement, sourceCode, nameContext, ordinal, packageName);
    }

    public static final class InterfaceBuilder
            extends ClassifierBuilder<InterfaceImpl>
    {
        public InterfaceBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                @Nonnull IdentifierContext nameContext,
                int ordinal,
                @Nonnull String packageName)
        {
            super(elementContext, macroElement, sourceCode, nameContext, ordinal, packageName);
        }

        @Override
        @Nonnull
        protected InterfaceImpl buildUnsafe()
        {
            return new InterfaceImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.build(),
                    this.getNameContext(),
                    this.ordinal,
                    this.packageName);
        }

        @Override
        public InterfaceImpl getType()
        {
            return Objects.requireNonNull(this.element);
        }
    }
}
