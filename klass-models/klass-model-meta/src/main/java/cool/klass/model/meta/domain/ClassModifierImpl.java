package cool.klass.model.meta.domain;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.api.ClassModifier;
import org.antlr.v4.runtime.ParserRuleContext;

public final class ClassModifierImpl extends AbstractNamedElement implements ClassModifier
{
    private ClassModifierImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal)
    {
        super(elementContext, inferred, nameContext, name, ordinal);
    }

    public static final class ClassModifierBuilder extends NamedElementBuilder
    {
        public ClassModifierBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal)
        {
            super(elementContext, inferred, nameContext, name, ordinal);
        }

        public ClassModifierImpl build()
        {
            return new ClassModifierImpl(this.elementContext, this.inferred, this.nameContext, this.name, this.ordinal);
        }
    }
}
