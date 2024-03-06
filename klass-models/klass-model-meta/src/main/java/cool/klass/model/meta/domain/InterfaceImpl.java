package cool.klass.model.meta.domain;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.Interface;
import org.antlr.v4.runtime.ParserRuleContext;

public final class InterfaceImpl extends AbstractClassifier implements Interface
{
    private InterfaceImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull String packageName)
    {
        super(elementContext, macroElement, nameContext, name, ordinal, packageName);
    }

    public static final class InterfaceBuilder extends ClassifierBuilder<InterfaceImpl>
    {
        public InterfaceBuilder(
                @Nonnull ParserRuleContext elementContext,
                Optional<ElementBuilder<?>> macroElement,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull String packageName)
        {
            super(elementContext, macroElement, nameContext, name, ordinal, packageName);
        }

        @Override
        @Nonnull
        protected InterfaceImpl buildUnsafe()
        {
            return new InterfaceImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
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
