package cool.klass.model.meta.domain.value;

import cool.klass.model.meta.domain.DataTypeProperty;
import cool.klass.model.meta.domain.DataTypeProperty.DataTypePropertyBuilder;
import cool.klass.model.meta.domain.Klass;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class ThisMemberExpressionValue extends MemberExpressionValue
{
    private ThisMemberExpressionValue(
            ParserRuleContext elementContext,
            Klass klass, DataTypeProperty<?> property)
    {
        super(elementContext, klass, property);
    }

    @Override
    public void visit(ExpressionValueVisitor visitor)
    {
        visitor.visitThisMember(this);
    }

    public static class ThisMemberExpressionValueBuilder extends MemberExpressionValueBuilder
    {
        public ThisMemberExpressionValueBuilder(
                ParserRuleContext elementContext,
                KlassBuilder klassBuilder,
                DataTypePropertyBuilder<?, ?> propertyBuilder)
        {
            super(elementContext, klassBuilder, propertyBuilder);
        }

        @Override
        public ThisMemberExpressionValue build()
        {
            return new ThisMemberExpressionValue(
                    this.elementContext,
                    this.klassBuilder.getKlass(),
                    this.propertyBuilder.getProperty());
        }
    }
}
