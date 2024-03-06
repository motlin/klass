package cool.klass.model.meta.domain.value;

import java.util.Objects;

import cool.klass.model.meta.domain.DataTypeProperty;
import cool.klass.model.meta.domain.DataTypeProperty.DataTypePropertyBuilder;
import cool.klass.model.meta.domain.Klass;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public class MemberExpressionValue extends ExpressionValue
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

    public static final class MemberExpressionValueBuilder extends ExpressionValueBuilder
    {
        private final KlassBuilder                  klassBuilder;
        private final DataTypePropertyBuilder<?, ?> propertyBuilder;

        public MemberExpressionValueBuilder(
                ParserRuleContext elementContext,
                KlassBuilder klassBuilder,
                DataTypePropertyBuilder<?, ?> propertyBuilder)
        {
            super(elementContext);
            this.klassBuilder = Objects.requireNonNull(klassBuilder);
            this.propertyBuilder = Objects.requireNonNull(propertyBuilder);
        }

        @Override
        public MemberExpressionValue build()
        {
            return new MemberExpressionValue(
                    this.elementContext,
                    this.klassBuilder.getKlass(),
                    this.propertyBuilder.getProperty());
        }
    }
}
