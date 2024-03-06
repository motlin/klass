package cool.klass.model.meta.domain.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import cool.klass.model.meta.domain.AbstractClassifier;
import cool.klass.model.meta.domain.AbstractClassifier.ClassifierBuilder;
import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.Type.TypeGetter;
import cool.klass.model.meta.domain.api.modifier.Modifier;
import cool.klass.model.meta.domain.api.order.OrderBy;
import cool.klass.model.meta.domain.api.property.ReferenceProperty;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.order.OrderByImpl.OrderByBuilder;
import cool.klass.model.meta.domain.property.ModifierImpl.ModifierBuilder;
import cool.klass.model.meta.grammar.KlassParser.IdentifierContext;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public abstract class ReferencePropertyImpl<T extends Classifier>
        extends AbstractProperty<T>
        implements ReferenceProperty
{
    @Nonnull
    protected final Multiplicity            multiplicity;
    @Nonnull
    private         Optional<OrderBy>       orderBy = Optional.empty();
    private         ImmutableList<Modifier> modifiers;

    protected ReferencePropertyImpl(
            ParserRuleContext elementContext,
            Optional<Element> macroElement,
            SourceCode sourceCode,
            IdentifierContext nameContext,
            int ordinal,
            T type,
            AbstractClassifier owningClassifier,
            @Nonnull Multiplicity multiplicity)
    {
        super(elementContext, macroElement, sourceCode, nameContext, ordinal, type, owningClassifier);
        this.multiplicity = Objects.requireNonNull(multiplicity);
    }

    @Override
    @Nonnull
    public final Multiplicity getMultiplicity()
    {
        return this.multiplicity;
    }

    @Override
    @Nonnull
    public final Optional<OrderBy> getOrderBy()
    {
        return Objects.requireNonNull(this.orderBy);
    }

    protected final void setOrderBy(@Nonnull Optional<OrderBy> orderBy)
    {
        this.orderBy = Objects.requireNonNull(orderBy);
    }

    @Override
    @Nonnull
    public final ImmutableList<Modifier> getModifiers()
    {
        return this.modifiers;
    }

    protected final void setModifiers(ImmutableList<Modifier> modifiers)
    {
        this.modifiers = modifiers;
    }

    public abstract static class ReferencePropertyBuilder<T extends Classifier, TG extends TypeGetter, BuiltElement extends ReferencePropertyImpl<T>>
            extends PropertyBuilder<T, TG, BuiltElement>
    {
        @Nonnull
        protected final Multiplicity multiplicity;

        @Nonnull
        private Optional<OrderByBuilder> orderByBuilder = Optional.empty();

        private ImmutableList<ModifierBuilder> modifierBuilders;

        protected ReferencePropertyBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nullable SourceCodeBuilder sourceCode,
                @Nonnull IdentifierContext nameContext,
                int ordinal,
                @Nonnull TG type,
                @Nonnull ClassifierBuilder<?> owningClassifierBuilder,
                @Nonnull Multiplicity multiplicity)
        {
            super(elementContext, macroElement, sourceCode, nameContext, ordinal, type, owningClassifierBuilder);
            this.multiplicity = Objects.requireNonNull(multiplicity);
        }

        public void setOrderByBuilder(@Nonnull Optional<OrderByBuilder> orderByBuilder)
        {
            this.orderByBuilder = Objects.requireNonNull(orderByBuilder);
        }

        public void setModifierBuilders(ImmutableList<ModifierBuilder> modifierBuilders)
        {
            this.modifierBuilders = modifierBuilders;
        }

        @Override
        protected final void buildChildren()
        {
            ImmutableList<Modifier> modifiers =
                    this.modifierBuilders.collect(ModifierBuilder::build);
            this.element.setModifiers(modifiers);

            Optional<OrderBy> orderBy = this.orderByBuilder.map(OrderByBuilder::build);
            this.element.setOrderBy(orderBy);
        }
    }
}
