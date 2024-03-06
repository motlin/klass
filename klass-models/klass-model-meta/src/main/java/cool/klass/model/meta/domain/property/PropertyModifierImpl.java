package cool.klass.model.meta.domain.property;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractNamedElement;
import cool.klass.model.meta.domain.api.property.PropertyModifier;
import org.antlr.v4.runtime.ParserRuleContext;

public final class PropertyModifierImpl extends AbstractNamedElement implements PropertyModifier
{
    private PropertyModifierImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal)
    {
        super(elementContext, inferred, nameContext, name, ordinal);
    }

    public static final class PropertyModifierBuilder extends NamedElementBuilder
    {
        public PropertyModifierBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal)
        {
            super(elementContext, inferred, nameContext, name, ordinal);
        }

        public PropertyModifierImpl build()
        {
            return new PropertyModifierImpl(this.elementContext, this.inferred, this.nameContext, this.name, this.ordinal);
        }
    }
}
