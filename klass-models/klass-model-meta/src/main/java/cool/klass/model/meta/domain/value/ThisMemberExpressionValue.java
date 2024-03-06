package cool.klass.model.meta.domain.value;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.DataTypeProperty;
import cool.klass.model.meta.domain.DataTypeProperty.DataTypePropertyBuilder;
import cool.klass.model.meta.domain.Klass;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import org.antlr.v4.runtime.ParserRuleContext;

public final class ThisMemberExpressionValue extends MemberExpressionValue
{
    private ThisMemberExpressionValue(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Klass klass, @Nonnull DataTypeProperty<?> property)
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
                @Nonnull ParserRuleContext elementContext,
                @Nonnull KlassBuilder klassBuilder,
                @Nonnull DataTypePropertyBuilder<?, ?> propertyBuilder)
        {
            super(elementContext, klassBuilder, propertyBuilder);
        }

        @Nonnull
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
