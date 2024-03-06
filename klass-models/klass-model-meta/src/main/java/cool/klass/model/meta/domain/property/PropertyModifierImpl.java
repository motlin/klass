package cool.klass.model.meta.domain.property;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractNamedElement;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.property.PropertyModifier;
import org.antlr.v4.runtime.ParserRuleContext;

public final class PropertyModifierImpl extends AbstractNamedElement implements PropertyModifier
{
    private PropertyModifierImpl(
            @Nonnull ParserRuleContext elementContext,
            Optional<Element> macroElement,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal)
    {
        super(elementContext, macroElement, nameContext, name, ordinal);
    }

    public static final class PropertyModifierBuilder extends NamedElementBuilder<PropertyModifierImpl>
    {
        public PropertyModifierBuilder(
                @Nonnull ParserRuleContext elementContext,
                Optional<ElementBuilder<?>> macroElement,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal)
        {
            super(elementContext, macroElement, nameContext, name, ordinal);
        }

        @Override
        @Nonnull
        protected PropertyModifierImpl buildUnsafe()
        {
            return new PropertyModifierImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.nameContext,
                    this.name,
                    this.ordinal);
        }
    }
}
