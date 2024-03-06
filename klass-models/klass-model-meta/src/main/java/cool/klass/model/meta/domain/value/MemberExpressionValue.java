package cool.klass.model.meta.domain.value;

import java.util.Objects;

import cool.klass.model.meta.domain.DataTypeProperty;
import cool.klass.model.meta.domain.DataTypeProperty.DataTypePropertyBuilder;
import cool.klass.model.meta.domain.Klass;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class MemberExpressionValue extends ExpressionValue
{
    private final Klass               klass;
    private final DataTypeProperty<?> property;

    protected MemberExpressionValue(
            ParserRuleContext elementContext,
            Klass klass,
            DataTypeProperty<?> property)
    {
        super(elementContext);
        this.klass = Objects.requireNonNull(klass);
        this.property = Objects.requireNonNull(property);
    }

    public Klass getKlass()
    {
        return this.klass;
    }

    public DataTypeProperty<?> getProperty()
    {
        return this.property;
    }

    public abstract static class MemberExpressionValueBuilder extends ExpressionValueBuilder
    {
        protected final KlassBuilder                  klassBuilder;
        protected final DataTypePropertyBuilder<?, ?> propertyBuilder;

        protected MemberExpressionValueBuilder(
                ParserRuleContext elementContext,
                KlassBuilder klassBuilder,
                DataTypePropertyBuilder<?, ?> propertyBuilder)
        {
            super(elementContext);
            this.klassBuilder = Objects.requireNonNull(klassBuilder);
            this.propertyBuilder = Objects.requireNonNull(propertyBuilder);
        }
    }
}
