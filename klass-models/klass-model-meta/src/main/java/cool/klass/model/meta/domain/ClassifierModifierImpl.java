package cool.klass.model.meta.domain;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractClassifier.ClassifierBuilder;
import cool.klass.model.meta.domain.api.ClassifierModifier;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.property.AbstractModifier;
import org.antlr.v4.runtime.ParserRuleContext;

public final class ClassifierModifierImpl
        extends AbstractModifier
        implements ClassifierModifier
{
    private ClassifierModifierImpl(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Optional<Element> macroElement,
            @Nonnull Optional<SourceCode> sourceCode,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull AbstractClassifier owningClassifier)
    {
        super(elementContext, macroElement, sourceCode, nameContext, name, ordinal, owningClassifier);
    }

    @Override
    public AbstractClassifier getModifierOwner()
    {
        return (AbstractClassifier) super.getModifierOwner();
    }

    public static final class ClassifierModifierBuilder
            extends ModifierBuilder<ClassifierModifierImpl>
    {
        @Nonnull
        private final ClassifierBuilder<?> owningClassifierBuilder;

        public ClassifierModifierBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull Optional<SourceCodeBuilder> sourceCode,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull ClassifierBuilder<?> owningClassifierBuilder)
        {
            super(elementContext, macroElement, sourceCode, nameContext, name, ordinal);
            this.owningClassifierBuilder = Objects.requireNonNull(owningClassifierBuilder);
        }

        @Override
        @Nonnull
        protected ClassifierModifierImpl buildUnsafe()
        {
            return new ClassifierModifierImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.sourceCode.map(SourceCodeBuilder::build),
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.owningClassifierBuilder.getElement());
        }
    }
}
