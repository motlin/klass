package cool.klass.model.meta.domain.value;

import cool.klass.model.meta.domain.DataTypeProperty;
import cool.klass.model.meta.domain.DataTypeProperty.DataTypePropertyBuilder;
import cool.klass.model.meta.domain.Klass;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class TypeMemberExpressionValue extends MemberExpressionValue
{
    private TypeMemberExpressionValue(
            ParserRuleContext elementContext,
            Klass klass, DataTypeProperty<?> property)
    {
        super(elementContext, klass, property);
    }

    @Override
    public void visit(ExpressionValueVisitor visitor)
    {
        visitor.visitTypeMember(this);
    }

    public static class TypeMemberExpressionValueBuilder extends MemberExpressionValueBuilder
    {
        public TypeMemberExpressionValueBuilder(
                ParserRuleContext elementContext,
                KlassBuilder klassBuilder,
                DataTypePropertyBuilder<?, ?> propertyBuilder)
        {
            super(elementContext, klassBuilder, propertyBuilder);
        }

        @Override
        public TypeMemberExpressionValue build()
        {
            return new TypeMemberExpressionValue(
                    this.elementContext,
                    this.klassBuilder.getKlass(),
                    this.propertyBuilder.getProperty());
        }
    }
}
