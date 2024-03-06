package cool.klass.model.meta.domain;

import javax.annotation.Nonnull;

import org.antlr.v4.runtime.ParserRuleContext;

public final class ClassModifier extends NamedElement
{
    private ClassModifier(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal)
    {
        super(elementContext, nameContext, name, ordinal);
    }

    public static final class ClassModifierBuilder extends NamedElementBuilder
    {
        public ClassModifierBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal)
        {
            super(elementContext, nameContext, name, ordinal);
        }

        public ClassModifier build()
        {
            return new ClassModifier(this.elementContext, this.nameContext, this.name, this.ordinal);
        }
    }
}
