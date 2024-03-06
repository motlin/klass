package cool.klass.model.meta.domain;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractClassifier.ClassifierBuilder;
import cool.klass.model.meta.domain.api.ClassModifier;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.property.AbstractModifier;
import org.antlr.v4.runtime.ParserRuleContext;

public final class ClassModifierImpl
        extends AbstractModifier
        implements ClassModifier
{
    private ClassModifierImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AbstractClassifier owningClassifier)
    {
        super(elementContext, macroElement, nameContext, name, ordinal, owningClassifier);
    }

    @Override
    public AbstractClassifier getModifierOwner()
    {
        return (AbstractClassifier) super.getModifierOwner();
    }

    public static final class ClassModifierBuilder
            extends ModifierBuilder<ClassModifierImpl>
    {
        @Nonnull
        private final ClassifierBuilder<?> owningClassifierBuilder;

        public ClassModifierBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull ClassifierBuilder<?> owningClassifierBuilder)
        {
            super(elementContext, macroElement, nameContext, name, ordinal);
            this.owningClassifierBuilder = Objects.requireNonNull(owningClassifierBuilder);
        }

        @Override
        @Nonnull
        protected ClassModifierImpl buildUnsafe()
        {
            return new ClassModifierImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.owningClassifierBuilder.getElement());
        }
    }
}
