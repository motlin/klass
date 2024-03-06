package cool.klass.model.meta.domain.value;

import java.util.Optional;

import javax.annotation.Nonnull;

import cool.klass.model.meta.domain.KlassImpl;
import cool.klass.model.meta.domain.KlassImpl.KlassBuilder;
import cool.klass.model.meta.domain.api.Element;
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
            @Nonnull Optional<Element> macroElement,
            @Nonnull KlassImpl klass,
            @Nonnull ImmutableList<AssociationEnd> associationEnds,
            @Nonnull AbstractDataTypeProperty<?> property)
    {
        super(elementContext, macroElement, klass, associationEnds, property);
    }

    public static final class TypeMemberReferencePathBuilder extends AbstractMemberReferencePathBuilder<TypeMemberReferencePathImpl>
    {
        public TypeMemberReferencePathBuilder(
                @Nonnull ParserRuleContext elementContext,
                @Nonnull Optional<ElementBuilder<?>> macroElement,
                @Nonnull KlassBuilder klassBuilder,
                @Nonnull ImmutableList<AssociationEndBuilder> associationEndBuilders,
                @Nonnull DataTypePropertyBuilder<?, ?, ?> propertyBuilder)
        {
            super(elementContext, macroElement, klassBuilder, associationEndBuilders, propertyBuilder);
        }

        @Override
        @Nonnull
        protected TypeMemberReferencePathImpl buildUnsafe()
        {
            return new TypeMemberReferencePathImpl(
                    this.elementContext,
                    this.macroElement.map(ElementBuilder::getElement),
                    this.klassBuilder.getElement(),
                    this.associationEndBuilders.collect(AssociationEndBuilder::getElement),
                    this.propertyBuilder.getElement());
        }
    }
}
