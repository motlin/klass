package cool.klass.model.meta.domain;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.Interface;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class InterfaceImpl extends AbstractClassifier implements Interface
{
    private InterfaceImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nonnull Optional<SourceCode> sourceCode,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull String packageName)
    {
        super(elementContext, macroElement, sourceCode, nameContext, name, ordinal, packageName);
    }

    public static final class InterfaceBuilder extends ClassifierBuilder<InterfaceImpl>
    {
        public InterfaceBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull Optional<SourceCodeBuilder> sourceCode,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull String packageName)
        {
            super(elementContext, macroElement, sourceCode, nameContext, name, ordinal, packageName);
        }

        @Override
        @Nonnull
        protected InterfaceImpl buildUnsafe()
        {
            return new InterfaceImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.map(SourceCodeBuilder::build),
                    this.nameContext,
                    this.name,
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
