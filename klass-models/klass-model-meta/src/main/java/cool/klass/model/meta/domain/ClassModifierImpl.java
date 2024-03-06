package cool.klass.model.meta.domain;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractClassifier.ClassifierBuilder;
import cool.klass.model.meta.domain.api.ClassModifier;
import cool.klass.model.meta.domain.api.Element;
import org.antlr.v4.runtime.ParserRuleContext;

public final class ClassModifierImpl extends AbstractNamedElement implements ClassModifier
{
    private final AbstractClassifier owningClassifier;

    private ClassModifierImpl(
            @Nonnull ParserRuleContext elementContext,
            Optional<Element> macroElement,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            AbstractClassifier owningClassifier)
    {
        super(elementContext, macroElement, nameContext, name, ordinal);
        this.owningClassifier = owningClassifier;
    }

    public static final class ClassModifierBuilder extends NamedElementBuilder<ClassModifierImpl>
    {
        @Nonnull
        private final ClassifierBuilder<?> owningClassifierBuilder;

        public ClassModifierBuilder(
                @Nonnull ParserRuleContext elementContext,
                Optional<ElementBuilder<?>> macroElement,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull ClassifierBuilder<?> owningClassifierBuilder)
        {
            super(elementContext, macroElement, nameContext, name, ordinal);
            this.owningClassifierBuilder = owningClassifierBuilder;
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
