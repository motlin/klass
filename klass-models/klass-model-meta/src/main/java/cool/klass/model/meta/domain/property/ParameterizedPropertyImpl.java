package cool.klass.model.meta.domain.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.KlassImpl;
import cool.klass.model.meta.domain.KlassImpl.KlassBuilder;
import cool.klass.model.meta.domain.api.Klass;
import cool.klass.model.meta.domain.api.Multiplicity;
import cool.klass.model.meta.domain.api.order.OrderBy;
import cool.klass.model.meta.domain.api.property.ParameterizedProperty;
import cool.klass.model.meta.domain.order.OrderByImpl.OrderByBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

// TODO: Super class for reference-type-property?
public final class ParameterizedPropertyImpl extends AbstractProperty<KlassImpl> implements ParameterizedProperty
{
    @Nonnull
    private final Multiplicity multiplicity;
    // @Nonnull
    // private final ImmutableList<ParameterizedPropertyModifier> parameterizedPropertyModifiers;

    @Nonnull
    private Optional<OrderBy> orderBy = Optional.empty();

    private ParameterizedPropertyImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull KlassImpl type,
            @Nonnull KlassImpl owningKlass,
            @Nonnull Multiplicity multiplicity)
    {
        super(elementContext, inferred, nameContext, name, ordinal, type, owningKlass);
        this.multiplicity = Objects.requireNonNull(multiplicity);
    }

    @Nonnull
    @Override
    public Klass getOwningClassifier()
    {
        return (Klass) super.getOwningClassifier();
    }

    @Override
    @Nonnull
    public Multiplicity getMultiplicity()
    {
        return this.multiplicity;
    }

    @Override
    @Nonnull
    public Optional<OrderBy> getOrderBy()
    {
        return Objects.requireNonNull(this.orderBy);
    }

    private void setOrderBy(@Nonnull Optional<OrderBy> orderBy)
    {
        this.orderBy = Objects.requireNonNull(orderBy);
    }

    public static final class ParameterizedPropertyBuilder extends PropertyBuilder<KlassImpl, KlassBuilder, ParameterizedPropertyImpl>
    {
        @Nonnull
        private final Multiplicity multiplicity;

        @Nonnull
        private Optional<OrderByBuilder> orderByBuilder = Optional.empty();

        public ParameterizedPropertyBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull ParserRuleContext nameContext,
                @Nonnull String name,
                int ordinal,
                @Nonnull KlassBuilder type,
                @Nonnull KlassBuilder owningKlassBuilder,
                @Nonnull Multiplicity multiplicity)
        {
            super(elementContext, inferred, nameContext, name, ordinal, type, owningKlassBuilder);
            this.multiplicity = Objects.requireNonNull(multiplicity);
        }

        public void setOrderByBuilder(@Nonnull Optional<OrderByBuilder> orderByBuilder)
        {
            this.orderByBuilder = Objects.requireNonNull(orderByBuilder);
        }

        @Override
        @Nonnull
        protected ParameterizedPropertyImpl buildUnsafe()
        {
            ParameterizedPropertyImpl parameterizedProperty = new ParameterizedPropertyImpl(
                    this.elementContext,
                    this.inferred,
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.typeBuilder.getElement(),
                    (KlassImpl) this.owningClassifierBuilder.getElement(),
                    this.multiplicity);

            Optional<OrderBy> orderBy = this.orderByBuilder.map(OrderByBuilder::build);
            parameterizedProperty.setOrderBy(orderBy);

            /*
            ImmutableList<ParameterizedPropertyModifier> parameterizedPropertyModifiers =
                    this.parameterizedPropertyModifierBuilders.collect(ParameterizedPropertyModifierBuilder::build);
            */
            return parameterizedProperty;
        }
    }
}
