package cool.klass.model.meta.domain;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.KlassImpl.KlassBuilder;
import cool.klass.model.meta.domain.api.ClassModifier;
import org.antlr.v4.runtime.ParserRuleContext;

public final class ClassModifierImpl extends AbstractNamedElement implements ClassModifier
{
    private final KlassImpl owningKlass;

    private ClassModifierImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            KlassImpl owningKlass)
    {
        super(elementContext, inferred, nameContext, name, ordinal);
        this.owningKlass = owningKlass;
    }

    public static final class ClassModifierBuilder extends NamedElementBuilder<ClassModifierImpl>
    {
        @Nonnull
        private final KlassBuilder owningKlassBuilder;

        public ClassModifierBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull KlassBuilder owningKlassBuilder)
        {
            super(elementContext, inferred, nameContext, name, ordinal);
            this.owningKlassBuilder = owningKlassBuilder;
        }

        @Override
        @Nonnull
        protected ClassModifierImpl buildUnsafe()
        {
            return new ClassModifierImpl(
                    this.elementContext,
                    this.inferred,
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.owningKlassBuilder.getElement());
        }
    }
}
