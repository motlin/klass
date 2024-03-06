package cool.klass.model.meta.domain.value;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.Klass;
import cool.klass.model.meta.domain.Klass.KlassBuilder;
import cool.klass.model.meta.domain.property.AssociationEnd;
import cool.klass.model.meta.domain.property.AssociationEnd.AssociationEndBuilder;
import cool.klass.model.meta.domain.property.DataTypeProperty;
import cool.klass.model.meta.domain.property.DataTypeProperty.DataTypePropertyBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class TypeMemberExpressionValue extends MemberExpressionValue
{
    private TypeMemberExpressionValue(
            @Nonnull ParserRuleContext elementContext,
            @Nonnull Klass klass,
            @Nonnull ImmutableList<AssociationEnd> associationEnds,
            @Nonnull DataTypeProperty<?> property)
    {
        super(elementContext, klass, associationEnds, property);
    }

    @Override
    public void visit(ExpressionValueVisitor visitor)
    {
        visitor.visitTypeMember(this);
    }

    public static class TypeMemberExpressionValueBuilder extends MemberExpressionValueBuilder
    {
        public TypeMemberExpressionValueBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull KlassBuilder klassBuilder,
                @Nonnull ImmutableList<AssociationEndBuilder> associationEndBuilders,
                @Nonnull DataTypePropertyBuilder<?, ?> propertyBuilder)
        {
            super(elementContext, klassBuilder, associationEndBuilders, propertyBuilder);
        }

        @Nonnull
        @Override
        public TypeMemberExpressionValue build()
        {
            return new TypeMemberExpressionValue(
                    this.elementContext,
                    this.klassBuilder.getKlass(),
                    this.associationEndBuilders.collect(AssociationEndBuilder::getAssociationEnd),
                    this.propertyBuilder.getProperty());
        }
    }
}
