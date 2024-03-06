package cool.klass.model.meta.domain;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractClassifier.ClassifierBuilder;
import cool.klass.model.meta.domain.api.ClassModifier;
import org.antlr.v4.runtime.ParserRuleContext;

public final class ClassModifierImpl extends AbstractNamedElement implements ClassModifier
{
    private final AbstractClassifier owningClassifier;

    private ClassModifierImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            AbstractClassifier owningClassifier)
    {
        super(elementContext, inferred, nameContext, name, ordinal);
        this.owningClassifier = owningClassifier;
    }

    public static final class ClassModifierBuilder extends NamedElementBuilder<ClassModifierImpl>
    {
        @Nonnull
        private final ClassifierBuilder<?> owningClassifierBuilder;

        public ClassModifierBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull ClassifierBuilder<?> owningClassifierBuilder)
        {
            super(elementContext, inferred, nameContext, name, ordinal);
            this.owningClassifierBuilder = owningClassifierBuilder;
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
                    this.owningClassifierBuilder.getElement());
        }
    }
}
