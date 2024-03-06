package cool.klass.model.meta.domain.property;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Klass;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import cool.klass.model.meta.domain.Multiplicity;
import cool.klass.model.meta.domain.order.OrderBy;
import cool.klass.model.meta.domain.order.OrderBy.OrderByBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

// TODO: Super class for reference-type-property?
public final class ParameterizedProperty extends Property<Klass>
{
    @Nonnull
    private final Multiplicity multiplicity;
    // @Nonnull
    // private final ImmutableList<ParameterizedPropertyModifier> parameterizedPropertyModifiers;

    @Nonnull
    private Optional<OrderBy> orderBy = Optional.empty();

    private ParameterizedProperty(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull ParserRuleContext nameContext,
            @Nonnull String name,
            int ordinal,
            @Nonnull Klass type,
            @Nonnull Klass owningKlass,
            @Nonnull Multiplicity multiplicity)
    {
        super(elementContext, inferred, nameContext, name, ordinal, type, owningKlass);
        this.multiplicity = Objects.requireNonNull(multiplicity);
    }

    @Nonnull
    public Multiplicity getMultiplicity()
    {
        return this.multiplicity;
    }

    @Nonnull
    public Optional<OrderBy> getOrderBy()
    {
        return Objects.requireNonNull(this.orderBy);
    }

    private void setOrderBy(@Nonnull Optional<OrderBy> orderBy)
    {
        this.orderBy = Objects.requireNonNull(orderBy);
    }

    public static class ParameterizedPropertyBuilder extends PropertyBuilder<Klass, KlassBuilder>
    {
        @Nonnull
        private final Multiplicity multiplicity;

        private ParameterizedProperty    parameterizedProperty;
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
        public ParameterizedProperty build()
        {
            if (this.parameterizedProperty != null)
            {
                throw new IllegalStateException();
            }

            /*
            ImmutableList<ParameterizedPropertyModifier> parameterizedPropertyModifiers =
                    this.parameterizedPropertyModifierBuilders.collect(ParameterizedPropertyModifierBuilder::build);
            */

            this.parameterizedProperty = new ParameterizedProperty(
                    this.elementContext,
                    this.inferred,
                    this.nameContext,
                    this.name,
                    this.ordinal,
                    this.typeBuilder.getKlass(),
                    this.owningKlassBuilder.getKlass(),
                    this.multiplicity);

            Optional<OrderBy> orderBy = this.orderByBuilder.map(OrderByBuilder::build);
            this.parameterizedProperty.setOrderBy(orderBy);

            return this.parameterizedProperty;
        }

        public ParameterizedProperty getParameterizedProperty()
        {
            return this.parameterizedProperty;
        }
    }
}
