package cool.klass.model.meta.domain.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.AbstractClassifier;
import cool.klass.model.meta.domain.AbstractClassifier.ClassifierBuilder;
import cool.klass.model.meta.domain.api.Classifier;
import cool.klass.model.meta.domain.api.Element;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.Type.TypeGetter;
import cool.klass.model.meta.domain.api.modifier.AssociationEndModifier;
import cool.klass.model.meta.domain.api.order.OrderBy;
import cool.klass.model.meta.domain.api.property.ReferenceProperty;
import cool.klass.model.meta.domain.api.source.SourceCode;
import cool.klass.model.meta.domain.api.source.SourceCode.SourceCodeBuilder;
import cool.klass.model.meta.domain.order.OrderByImpl.OrderByBuilder;
import cool.klass.model.meta.domain.property.AssociationEndModifierImpl.AssociationEndModifierBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public abstract class ReferencePropertyImpl<T extends Classifier>
        extends AbstractProperty<T>
        implements ReferenceProperty
{
    @Nonnull
    protected final Multiplicity                          multiplicity;
    protected final boolean                               owned;
    @Nonnull
    private         Optional<OrderBy>                     orderBy = Optional.empty();
    private         ImmutableList<AssociationEndModifier> associationEndModifiers;

    protected ReferencePropertyImpl(
            ParserRuleContext elementContext,
            Optional<Element> macroElement,
            Optional<SourceCode> sourceCode,
            ParserRuleContext nameContext,
            String name,
            int ordinal,
            T type,
            AbstractClassifier owningClassifier,
            @Nonnull Multiplicity multiplicity, boolean owned)
    {
        super(elementContext, macroElement, sourceCode, nameContext, name, ordinal, type, owningClassifier);
        this.multiplicity = Objects.requireNonNull(multiplicity);
        this.owned        = owned;
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

    @Nonnull
    public final ImmutableList<AssociationEndModifier> getAssociationEndModifiers()
    {
        return this.associationEndModifiers;
    }

    public final boolean isOwned()
    {
        return this.owned;
    }

    protected final void setAssociationEndModifiers(ImmutableList<AssociationEndModifier> associationEndModifiers)
    {
        this.associationEndModifiers = associationEndModifiers;
    }

    public abstract static class ReferencePropertyBuilder<T extends Classifier, TG extends TypeGetter, BuiltElement extends ReferencePropertyImpl<T>>
            extends PropertyBuilder<T, TG, BuiltElement>
    {
        @Nonnull
        protected final Multiplicity multiplicity;
        protected final boolean      isOwned;

        @Nonnull
        private Optional<OrderByBuilder> orderByBuilder = Optional.empty();

        private ImmutableList<AssociationEndModifierBuilder> associationEndModifierBuilders;

        protected ReferencePropertyBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull Optional<SourceCodeBuilder> sourceCode,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull TG type,
                @Nonnull ClassifierBuilder<?> owningClassifierBuilder,
                @Nonnull Multiplicity multiplicity,
                boolean isOwned)
        {
            super(elementContext, macroElement, sourceCode, nameContext, name, ordinal, type, owningClassifierBuilder);
            this.multiplicity = Objects.requireNonNull(multiplicity);
            this.isOwned      = isOwned;
        }

        public void setOrderByBuilder(@Nonnull Optional<OrderByBuilder> orderByBuilder)
        {
            this.orderByBuilder = Objects.requireNonNull(orderByBuilder);
        }

        public void setAssociationEndModifierBuilders(ImmutableList<AssociationEndModifierBuilder> associationEndModifierBuilders)
        {
            this.associationEndModifierBuilders = associationEndModifierBuilders;
        }

        @Override
        protected final void buildChildren()
        {
            ImmutableList<AssociationEndModifier> associationEndModifiers =
                    this.associationEndModifierBuilders.collect(AssociationEndModifierBuilder::build);
            this.element.setAssociationEndModifiers(associationEndModifiers);

            Optional<OrderBy> orderBy = this.orderByBuilder.map(OrderByBuilder::build);
            this.element.setOrderBy(orderBy);
        }
    }
}
