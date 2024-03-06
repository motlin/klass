package cool.klass.model.meta.domain.value;

import java.util.Objects;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Klass;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import cool.klass.model.meta.domain.property.DataTypeProperty;
import cool.klass.model.meta.domain.property.DataTypeProperty.DataTypePropertyBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class MemberExpressionValue extends ExpressionValue
{
    @Nonnull
    private final Klass               klass;
    @Nonnull
    private final DataTypeProperty<?> property;

    protected MemberExpressionValue(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Klass klass,
            @Nonnull DataTypeProperty<?> property)
    {
        super(elementContext);
        this.klass = Objects.requireNonNull(klass);
        this.property = Objects.requireNonNull(property);
    }

    @Nonnull
    public Klass getKlass()
    {
        return this.klass;
    }

    @Nonnull
    public DataTypeProperty<?> getProperty()
    {
        return this.property;
    }

    public abstract static class MemberExpressionValueBuilder extends ExpressionValueBuilder
    {
        @Nonnull
        protected final KlassBuilder                  klassBuilder;
        @Nonnull
        protected final DataTypePropertyBuilder<?, ?> propertyBuilder;

        protected MemberExpressionValueBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull KlassBuilder klassBuilder,
                @Nonnull DataTypePropertyBuilder<?, ?> propertyBuilder)
        {
            super(elementContext);
            this.klassBuilder = Objects.requireNonNull(klassBuilder);
            this.propertyBuilder = Objects.requireNonNull(propertyBuilder);
        }
    }
}
