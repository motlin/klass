package cool.klass.model.meta.domain.value;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.KlassImpl;
import cool.klass.model.meta.domain.KlassImpl.KlassBuilder;
import cool.klass.model.meta.domain.api.property.AssociationEnd;
import cool.klass.model.meta.domain.api.value.TypeMemberReferencePath;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty;
import cool.klass.model.meta.domain.property.AbstractDataTypeProperty.DataTypePropertyBuilder;
import cool.klass.model.meta.domain.property.AssociationEndImpl.AssociationEndBuilder;
import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.collections.api.list.ImmutableList;

public final class TypeMemberReferencePathImpl extends AbstractMemberReferencePath implements TypeMemberReferencePath
{
    private TypeMemberReferencePathImpl(
            @Nonnull ParserRuleContext elementContext,
            boolean inferred,
            @Nonnull KlassImpl klass,
            @Nonnull ImmutableList<AssociationEnd> associationEnds,
            @Nonnull AbstractDataTypeProperty<?> property)
    {
        super(elementContext, inferred, klass, associationEnds, property);
    }

    public static class TypeMemberReferencePathBuilder extends MemberReferencePathBuilder
    {
        public TypeMemberReferencePathBuilder(
                @Nonnull ParserRuleContext elementContext,
                boolean inferred,
                @Nonnull KlassBuilder klassBuilder,
                @Nonnull ImmutableList<AssociationEndBuilder> associationEndBuilders,
                @Nonnull DataTypePropertyBuilder<?, ?> propertyBuilder)
        {
            super(elementContext, inferred, klassBuilder, associationEndBuilders, propertyBuilder);
        }

        @Nonnull
        @Override
        public TypeMemberReferencePathImpl build()
        {
            return new TypeMemberReferencePathImpl(
                    this.elementContext,
                    this.inferred,
                    this.klassBuilder.getElement(),
                    this.associationEndBuilders.collect(AssociationEndBuilder::getElement),
                    this.propertyBuilder.getProperty());
        }
    }
}
