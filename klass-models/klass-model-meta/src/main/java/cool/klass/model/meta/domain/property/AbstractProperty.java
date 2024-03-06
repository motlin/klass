package cool.klass.model.meta.domain.property;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractClassifier;
import cool.klass.model.meta.domain.AbstractClassifier.ClassifierBuilder;
import cool.klass.model.meta.domain.AbstractTypedElement;
import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.Type;
import cool.klass.model.meta.domain.api.Type.TypeGetter;
import cool.klass.model.meta.domain.api.property.Property;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class AbstractProperty<T extends Type> extends AbstractTypedElement<T> implements Property
{
    @Nonnull
    private final AbstractClassifier owningClassifier;

    protected AbstractProperty(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull T type,
            @Nonnull AbstractClassifier owningClassifier)
    {
        super(elementContext, inferred, nameContext, name, ordinal, type);
        this.owningClassifier = Objects.requireNonNull(owningClassifier);
    }

    @Override
    @Nonnull
    public Classifier getOwningClassifier()
    {
        return this.owningClassifier;
    }

    public abstract static class PropertyBuilder<T extends Type, TG extends TypeGetter, BuiltElement extends AbstractProperty<T>> extends TypedElementBuilder<T, TG, BuiltElement>
    {
        @Nonnull
        protected final ClassifierBuilder<?> owningClassifierBuilder;

        protected PropertyBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull TG typeBuilder,
                @Nonnull ClassifierBuilder<?> owningClassifierBuilder)
        {
            super(elementContext, inferred, nameContext, name, ordinal, typeBuilder);
            this.owningClassifierBuilder = Objects.requireNonNull(owningClassifierBuilder);
        }
    }
}
